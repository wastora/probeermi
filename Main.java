package probeermi;

import java.rmi.RemoteException;



public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RemMethClass a;
		RemMethClass b;
		
		try
		{
			a = new RemMethClass(1, "node2", 2);
			b = new RemMethClass(2, "node1", 2);
			
			new Thread(a).start();
			new Thread(b).start();
			
			Thread.sleep(10000);
			
			a.DIE();
			b.DIE();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-99);
		}
		System.exit(0);
	}

}
