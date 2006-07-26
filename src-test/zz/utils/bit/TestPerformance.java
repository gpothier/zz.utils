/*
 * Created on Jul 26, 2006
 */
package zz.utils.bit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;


public class TestPerformance
{
	@Test public void compare()
	{
		final int theSize = 100000000;
		
		testStream(theSize);
		testBitStruct(theSize);
		testIntBitStruct(theSize);
		
		runTest("Stream", new Runnable()
		{
			public void run()
			{
				testStream(theSize);
			}
		});
		
		runTest("BitStruct", new Runnable()
		{
			public void run()
			{
				testBitStruct(theSize);
			}
		});
		
		runTest("IntBitStruct", new Runnable()
		{
			public void run()
			{
				testIntBitStruct(theSize);
			}
		});
		
	}
	
	private long runTest(String aName, Runnable aRunnable)
	{
		long t0 = System.currentTimeMillis();
		aRunnable.run();
		long t1 = System.currentTimeMillis();
		long dt = t1-t0;
		
		System.out.println(aName + ": "+dt+"ms");
		return dt;
	}

	private void testStream(int aSize)
	{
		try
		{
			DataOutputStream theStream = new DataOutputStream(new ByteArrayOutputStream(aSize));
			Random theRandom = new Random(0);
			while (aSize > 0)
			{
				theStream.writeLong(theRandom.nextLong());
				aSize -= 8;
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void testBitStruct(int aSize)
	{
		int theSize = aSize * 8;
		BitStruct theBitStruct = new BitStruct(theSize);
		Random theRandom = new Random(0);
		while (theSize >= 63)
		{
			theBitStruct.writeLong(theRandom.nextLong(), 63);
			theSize -= 63;
		}
	}
	
	private void testIntBitStruct(int aSize)
	{
		int theSize = aSize * 8;
		IntBitStruct theBitStruct = new IntBitStruct(theSize);
		Random theRandom = new Random(0);
		while (theSize >= 63)
		{
			theBitStruct.writeLong(theRandom.nextLong(), 63);
			theSize -= 63;
		}
	}
}
