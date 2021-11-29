package Lab8;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class server extends ViewableAtomic
{
  
	protected packet packet_msg;
	protected double processing_time;

	public server()
	{
		this("server", 20);
	}

	public server(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{		
		packet_msg = new packet("none");
		holdIn("Listen", INFINITY); //listen 상태로 시작
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("Listen"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("SYN-RCVD", processing_time); //listen일 때 입력받으면 SYN-RCVD상태로 전이
				}
			}
		}
		else if (phaseIs("SYN+ACK-Sent"))
		{
			for(int i = 0; i < x.getLength(); i++) {
				if(messageOnPort(x, "in", i)) {
					holdIn("ACK-RCVD", processing_time); //SYN+ACK-Sent에서 ACK을 받으면 ACK-RCVD상태로 전이
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("SYN-RCVD"))
		{
			holdIn("SYN+ACK-Sent", INFINITY); //SYN+ACK를 보냈을 때 SYN+ACK-Sent 상태가 됨
		}
		else if(phaseIs("ACK-RCVD")) {
			holdIn("Established", INFINITY); //ACK-RCVD상태가 되면 Established 상태로 마무리s
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("SYN-RCVD"))
		{
			m.add(makeContent("out", new packet("SYN+ACK"))); //SYN-RCVD상태일 때 SYN+ACK 전송
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText();
	}

}

