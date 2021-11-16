package Lab7;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class accumulator extends ViewableAtomic
{
	
	protected Queue q;
	protected number req_num;
	protected double processing_time;
	
	protected int result;
	
	public accumulator()
	{
		this("accumulator", 20);
	}

	public accumulator(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		q = new Queue();
		req_num = new number("", 0, false);
		result = 0;
		
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					req_num = (number) x.getValOnPort("in", i);
					q.add(req_num);
				
					holdIn("passive", processing_time);
				}
			}
		}
	}
	
	public void deltint()
	{
		if (phaseIs("passive"))
		{
			req_num = (number) q.getLast();
			System.out.println(req_num.isLast);
			
			if (req_num.isLast) //true==last
			{
				System.out.println(q);
				for(int i=0; i<10; i++)
				{
					req_num = (number) q.removeFirst();
					
					result = result + req_num.num;
				
					holdIn("processing", processing_time);
				}
			}
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("processing"))
		{
			m.add(makeContent("out", new number(Integer.toString(result), result)));
				
			holdIn("finished", INFINITY);
		}
		
		return m;
	}	
	
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + "queue length: " + q.size()
        + "\n" + "queue itself: " + q.toString();
	}

}



