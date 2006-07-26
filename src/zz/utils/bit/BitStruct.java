/*
 * Created on Jul 21, 2006
 */
package zz.utils.bit;

/**
 * A structure that contains fields of variable length, where the lengths
 * are expressed in bits.
 * To facilitate operation, a pointer to a current position is maintained and
 * updated by certain methods.
 * @author gpothier
 */
public class BitStruct
{
	private byte[] itsBytes;
	
	private int itsPos;
	
	/**
	 * Offset (in bytes) of the first used byte in the array.
	 */
	private int itsOffset;
	
	public BitStruct(byte[] aBytes, int aOffset)
	{
		if (aBytes != null) setBytes(aBytes);
		itsOffset = aOffset;
	}
	
	public BitStruct(byte[] aBytes)
	{
		this(aBytes, 0);
	}
	
	public BitStruct(int aBitCount)
	{
		this(new byte[(aBitCount+7)/8]);
	}
	
	/**
	 * Construct a struct with an initial size of 64 bits.
	 */
	public BitStruct()
	{
		this(64);
	}

	protected void setBytes(byte[] bytes)
	{
		itsBytes = bytes;
	}

	protected byte[] getBytes()
	{
		return itsBytes;
	}
	
	/**
	 * Returns the offset of the first used byte in this struct's backing array.
	 */
	public int getOffset()
	{
		return itsOffset;
	}

	/**
	 * Sets the offset of the firsy used byte in this struct's backing array.
	 */
	public void setOffset(int aOffset)
	{
		itsOffset = aOffset;
	}

	/**
	 * Returns the number of bits that can be stored withou
	 * growing the backing array, according to the current
	 * position.
	 */
	public int getRemainingBits()
	{
		return (getBytes().length - itsOffset)*8 - getPos();
	}

	
	/**
	 * Returns the position of the next bit read or written.
	 */
	public int getPos()
	{
		return itsPos;
	}

	/**
	 * Sets the position of the next bit read or written.
	 */
	public void setPos(int aPos)
	{
		itsPos = aPos;
	}
	
	/**
	 * Skips a number of bits.
	 */
	public void skip(int aBits)
	{
		itsPos += aBits;
	}
	
	/**
	 * Resets the current bit pointer.
	 */
	public void reset()
	{
		setPos(0);
	}
	
	/**
	 * Returns the bytes of this struct that are actually used,
	 * according to the current position.
	 */
	public byte[] packedBytes()
	{
		byte[] theResult = new byte[itsPos+7/8];
		System.arraycopy(getBytes(), itsOffset, theResult, 0, theResult.length);
		return theResult;
	}
	
	/**
	 * Grows the storage space so that it allows for at least for 
	 * the given size (in bits).
	 */
	protected void grow(int aMinSize)
	{
		if (itsOffset != 0) throw new UnsupportedOperationException("Cannot grow a struct when offset is not 0");
		
		int theNewSize = Math.max(getBytes().length*2, (aMinSize+7)/8);
		byte[] theNewBytes = new byte[theNewSize];
		System.arraycopy(getBytes(), 0, theNewBytes, 0, getBytes().length);
		setBytes(theNewBytes);
	}
	
	private void ensureCapacity(int aMinCapacity)
	{
		if ((getBytes().length - itsOffset) * 8 < aMinCapacity) grow(aMinCapacity);
	}
	
	public void writeLong(long aValue, int aBitCount)
	{
		ensureCapacity(itsPos+aBitCount);
		
		BitUtils.writeLong(getBytes(), itsOffset, aValue, itsPos, aBitCount);
		itsPos += aBitCount;
	}

	public void writeInt(int aValue, int aBitCount)
	{
		ensureCapacity(itsPos+aBitCount);
		
		BitUtils.writeInt(getBytes(), itsOffset, aValue, itsPos, aBitCount);
		itsPos += aBitCount;
	}
	
	public void writeBoolean(boolean aValue)
	{
		ensureCapacity(itsPos+1);
		
		BitUtils.writeBoolean(getBytes(), itsOffset, aValue, itsPos);
		itsPos += 1;
	}
	
	/**
	 * Writes a number of bits of the given byte array into this struct
	 * @param aBitCount The number of bits to write.
	 */
	public void writeBytes(byte[] aBytes, int aBitCount)
	{
		ensureCapacity(itsPos+aBitCount);
		
		int i = 0;
		while (aBitCount > 0)
		{
			byte b = aBytes[i++];
			int theBits = Math.min(aBitCount, 8);
			BitUtils.writeByte(getBytes(), itsOffset, b, itsPos, theBits);
			itsPos += theBits;
			aBitCount -= theBits;
		}
	}
	
	/**
	 * Writes the bits from the given byte array into this struct.
	 */
	public void writeBytes(byte[] aBytes)
	{
		writeBytes(aBytes, aBytes.length * 8);
	}
	
	public byte[] readBytes(int aBitCount)
	{
		byte[] theResult = new byte[(aBitCount+7)/8];
		int i = 0;
		while (aBitCount > 0)
		{
			int theBits = Math.min(aBitCount, 8);
			theResult[i++] = BitUtils.readByte(getBytes(), itsOffset, itsPos, theBits);
			aBitCount -= theBits;
			itsPos += theBits;
		}
		
		return theResult;
	}
	
	public long readLong(int aBitCount)
	{
		long theResult = BitUtils.readLong(getBytes(), itsOffset, itsPos, aBitCount);
		itsPos += aBitCount;
		return theResult;
	}
	
	public int readInt(int aBitCount)
	{
		int theResult = BitUtils.readInt(getBytes(), itsOffset, itsPos, aBitCount);
		itsPos += aBitCount;
		return theResult;
	}
	
	public byte readByte(int aBitCount)
	{
		byte theResult = BitUtils.readByte(getBytes(), itsOffset, itsPos, aBitCount);
		itsPos += aBitCount;
		return theResult;
	}
	
	public boolean readBoolean()
	{
		byte theResult = BitUtils.readByte(getBytes(), itsOffset, itsPos, 1);
		itsPos += 1;
		return theResult != 0;
	}
	
	@Override
	public String toString()
	{
		StringBuilder theBuilder = new StringBuilder("BitStruct: ");
		for (int j=itsOffset;j<getBytes().length;j++)
		{
			byte b = getBytes()[j];
			for(int i=0;i<8;i++)
			{
				theBuilder.append((b & 1) == 1 ? "1" : "0");
				b >>>= 1;
			}
			theBuilder.append(' ');
		}
		
		return theBuilder.toString();
	}
}
