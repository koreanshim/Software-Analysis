package Lab6;
import genDevs.modeling.message;


import simView.ViewableAtomic;

public class multi extends ViewableAtomic
{
  
	protected double processing_time;

	protected int result;
	protected request req_msg;
	protected result res_msg;
	
	
	
	public multi()
	{
		this("multi", 10);
	}

	public multi(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		result = 0;
		req_msg = new request("none", 0, 0);
		res_msg = new result("none",0);
		
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					req_msg = (request)x.getValOnPort("in", i);
					
				
					result = req_msg.num1 * req_msg.num2;
					System.out.println("---MUTLI---");
					System.out.println("num1:" + req_msg.num1 + " num2:" + req_msg.num2);
					System.out.println("result is "+ result + "\n");
					holdIn("busy", processing_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			
			//result = 0;
			req_msg = new request("none", 0, 0);
			res_msg = new result("none", 0);
			
			holdIn("passive", INFINITY);
			
			
			//res_msg = new result(Integer.toString(result),result);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out", new result(Integer.toString(result),result)));
			//m.add(makeContent("out", res_msg));
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText();
	}

}

