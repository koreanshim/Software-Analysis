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
		holdIn("Listen", INFINITY); //listen ���·� ����
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
					holdIn("SYN-RCVD", processing_time); //listen�� �� �Է¹����� SYN-RCVD���·� ����
				}
			}
		}
		else if (phaseIs("SYN+ACK-Sent"))
		{
			for(int i = 0; i < x.getLength(); i++) {
				if(messageOnPort(x, "in", i)) {
					holdIn("ACK-RCVD", processing_time); //SYN+ACK-Sent���� ACK�� ������ ACK-RCVD���·� ����
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("SYN-RCVD"))
		{
			holdIn("SYN+ACK-Sent", INFINITY); //SYN+ACK�� ������ �� SYN+ACK-Sent ���°� ��
		}
		else if(phaseIs("ACK-RCVD")) {
			holdIn("Established", INFINITY); //ACK-RCVD���°� �Ǹ� Established ���·� ������s
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("SYN-RCVD"))
		{
			m.add(makeContent("out", new packet("SYN+ACK"))); //SYN-RCVD������ �� SYN+ACK ����
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText();
	}

}

