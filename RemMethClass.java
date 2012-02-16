package probeermi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;


/**
 * This class will contain remote methods.
 * @author Joost
 * 
 * TODO: waar moeten we de tijd increasen? send en receive waarschijnlijk.
 * Ook voor de source node bij het ontvangen van een msg, denk ik.
 *
 */
public class RemMethClass extends UnicastRemoteObject implements RMI_interface, Runnable
{
	String name;
	int nodeNr;
	String textdest;
	boolean debug = true;
	boolean running = true;
	int nrOfNodes;
	int[] timeVector;
	
	LinkedList<MsgObj> buffer;
	LinkedList<MsgObj> history;
	
	/*
	 * Constructor - Initialize all the fields, and register the remote interface
	 */
	protected RemMethClass(int nodeNr, String dest, int n) throws RemoteException
	{
		this.name = "node"+nodeNr;
		this.textdest = dest;
		this.nodeNr = nodeNr;
		nrOfNodes = n;
		initTimeVector(n);
		buffer = new LinkedList<MsgObj>();
		history = new LinkedList<MsgObj>();
		try
		{
			java.rmi.Naming.bind(name, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-2);
		}
	}
	
	private void initTimeVector(int n)
	{
		timeVector = new int[n];
		for (int i = 0; i < n; i++)
		{
			timeVector[i] = 0;
		}
	}
	
	@Override
	public void print(String text) throws RemoteException
	{
		System.out.println(name+" : "+text);
	}
	
	/*
	 * (non-Javadoc)
	 * @see probeermi.RMI_interface#receive_msg(probeermi.MsgObj)
	 */
	@Override
	public void receive_msg(MsgObj msg) throws RemoteException
	{
		//fist, check if msg is deliverable, else store if buffer
		if (deliverable(msg)) 
		{
			deliver(msg);
			
			//TODO: check if msgs in the buffer can be delivered
			
		}
		else store(msg);
	}
	
	/*
	 * Check whether a msg is deliverable or should be stored in the buffer
	 * TODO: ik weet niet 100% zeker of het zo goed is! (maar denk het wel)
	 */
	public boolean deliverable(MsgObj msg)
	{
		boolean d = true;
		for (BufferItem b : msg.getBuffer())
		{
			if (b.getDestination() == nodeNr && (b.getTimeVector(msg.getSource()) > timeVector[msg.getSource()]))
			{
				d = false;
			}
		}
		return d;
	}
	
	/*
	 * Deliver message; for now, store in the LinkedList.
	 * We can check afterwards whether the list is ordered.
	 */
	public void deliver(MsgObj msg)
	{
		history.add(msg);
	}
	
	/*
	 * store a msg in the buffer 
	 */
	public void store(MsgObj msg)
	{
		buffer.add(msg);
	}
	
	public void send(MsgObj msg, String destination)
	{
		try
		{
			RemMethClass destObject = (RemMethClass) java.rmi.Naming.lookup(destination);
			destObject.receive_msg(msg);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	
	

	@Override
	public void run()
	{
		while(running)
		{
				
			try
			{
				debug("sleeping 2000");
				Thread.sleep(2000);
				debug("getting remote host");
				RMI_interface destObject = (RMI_interface) java.rmi.Naming.lookup(textdest);
				debug("printing text");
				destObject.print("lalala groeten van "+name);
				
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void DIE()
	{
		running = false;
		debug("Dieing...");
		try	{
			java.rmi.Naming.unbind(name);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void debug(String msg)
	{
		if (debug) System.out.println(name+" : "+msg);
	}
	
	private static final long serialVersionUID = -4676446028805100856L;
}
