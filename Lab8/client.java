package Lab8;
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
		
		holdIn("Active Open", int_arr_time);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("SYN-Sent")) //SYN-Sent일 때
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("SYN+ACK-RCVD", int_arr_time); //SYN-ACK을 받으면 SYN+ACK-RCVD가 됨
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("Active Open"))
		{
			holdIn("SYN-Sent", INFINITY); //Active open -> SYN Sent
		}
		if(phaseIs("SYN+ACK-RCVD")) 
		{
			holdIn("Established", INFINITY); //SYN+ACK-RCVD -> Established
		}
	}

	public message out()
	{
		message m = new message();
		if(phaseIs("Active Open")) {
			m.add(makeContent("out", new packet("SYN")));
		}
		else if(phaseIs("SYN+ACK-RCVD")) {
			m.add(makeContent("out", new packet("ACK")));
		}
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText();
	}

}
