package Lab9;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class router extends ViewableAtomic
{
	
	protected Queue q;
	protected packet packet;
	protected double processing_time;
	
	public router()
	{
		this("procQ", 20, 5);
	}

	public router(String name, double Processing_time, int r_size)
	{
		super(name);
    
		addInport("in");
		addOutport("out_s");
		
		for(int i = 1; i <= 5; i++) {
			addOutport("out" + i);
		}
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		q = new Queue();
		packet = new packet("", 0);
		
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
					packet = (packet) x.getValOnPort("in", i); //packet casting
					
					q.add(packet);
					
					if(q.size() == 5) {
						holdIn("sending", processing_time);
					}
					else {
						holdIn("passive", INFINITY);
					}
				}
			}
		}
	}
	
	public void deltint()
	{
		if(phaseIs("sending")) {
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("sending"))
		{
			while(!q.isEmpty()) {
				packet = (packet) q.removeFirst();
				m.add(makeContent("out"+packet.dest, packet));
			}
			m.add(makeContent("out_s", new entity("done")));
		}
		return m;
	}	
	
	/*
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + "queue length: " + q.size()
        + "\n" + "queue itself: " + q.toString();
	}
	*/
}



