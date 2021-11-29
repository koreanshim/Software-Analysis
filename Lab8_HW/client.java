package Lab8_HW;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class client extends ViewableAtomic
{
	
	protected double int_arr_time;  
	protected packet packet_msg;
	
	public client() 
	{
		this("client", 30);
	}
  
	public client(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{		
		packet_msg = new packet("none");
		
		holdIn("Active Open", int_arr_time); //0
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("SYN-Sent")) //SYN-Sent일 때 //2
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("SYN+ACK-RCVD", int_arr_time); //SYN-ACK을 받으면 SYN+ACK-RCVD가 됨 2-1
				}
			}
		}
		
		if (phaseIs("Established")) //Established일 때 //6
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("FIN_WAIT", INFINITY); 
				}
			}
		}
		
	}

	public void deltint()
	{
		if (phaseIs("Active Open")) //1-2
		{
			holdIn("SYN-Sent", INFINITY); //Active open -> SYN Sent 1-3
		}
		
		if(phaseIs("SYN+ACK-RCVD"))  //3-2
		{
			holdIn("Established", INFINITY); //SYN+ACK-RCVD -> Established
		}
		
	}

	public message out()
	{
		message m = new message();
		if(phaseIs("Active Open")) { //1-1
			m.add(makeContent("out", new packet("SYN")));
		}
		if(phaseIs("SYN+ACK-RCVD")) { //3-1
			m.add(makeContent("out", new packet("ACK")));
		}
		
		if(phaseIs("FIN_WAIT")) { //4-1
			m.add(makeContent("out", new packet("FIN")));
		}
		
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText();
	}

}
