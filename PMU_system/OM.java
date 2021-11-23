package PMU_system;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class OM extends ViewableAtomic
{
  
	protected order Order;
	protected double processing_time;

	public OM()
	{
		this("OM", 20);
	}

	public OM(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		//addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		Order = new order("", 0, 0, 0, "", 0);
		
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
					Order = (order)x.getValOnPort("in", i);
					
					holdIn("busy", processing_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			Order = new order("", 0, 0, 0, "", 0);
			
			holdIn("passive", INFINITY);
		}
	}
	/*
	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out", job));
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + job.getName();
	}
	*/
}

