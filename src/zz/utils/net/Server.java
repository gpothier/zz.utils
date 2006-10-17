/*
 * Created on Aug 25, 2006
 */
package zz.utils.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Manages incoming connections: starts a server socket that waits 
 * for incoming connections, and calls an abstract method whenever
 * a new client is connected.
 * @author gpothier
 */
public abstract class Server extends Thread
{
	private ServerSocket itsServerSocket;
	
	/**
	 * Creates a socket thread that accepts incoming connections
     * @param aStart Whether to start the thread immediately.
	 */
	public Server(int aPort)
	{
		try
		{
			setName(getClass().getSimpleName());
			itsServerSocket = new ServerSocket(aPort);
			start();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public final void run()
	{
		try
		{
			while (accept())
			{
				if (Thread.currentThread().isInterrupted())
				{
					return;
				}
				
				accepting();
				Socket theSocket = itsServerSocket.accept();
				accepted(theSocket);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				itsServerSocket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method indicates whether to accept new connections.
	 */
	protected boolean accept()
	{
		return true;
	}
	
	/**
	 * This method is called when this socked starts waiting for incoming connections.
	 */
	protected void accepting()
	{
	}

	/**
	 * This method is called when this server accepted a connection. 
	 */
	protected abstract void accepted(Socket aSocket);
	
	/**
	 * Stops accepting connections
	 */
	public void disconnect()
	{
		try
		{
			itsServerSocket.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

}