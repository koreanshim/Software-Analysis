package Lab9_HW;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class router extends ViewableAtomic
{
	
	protected Queue q;
	protected packet packet;
	protected double processing_time;
	//protected int qcounter; 
	
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
		
		//qcounter = 1;
		
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
						holdIn("passive", 0);	
					}
				}
			}
		}
	}
	
	public void deltint()
	{
		if(phaseIs("passive")) {
			if(q.size() == 5) { //q 크기가 5가 되면 passive->sending상태로 천이
				holdIn("sending", processing_time);
			}
		}
		if(phaseIs("sending")) {
			if(!q.isEmpty()) { //q가 비어있지 않는 한 sending 상태
				holdIn("sending", processing_time);
			}
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("sending"))
		{
			// /*
			if (!q.isEmpty()) {
				packet = (packet) q.removeFirst();
				m.add(makeContent("out"+packet.dest, packet));
			}
			else {
				m.add(makeContent("out_s", new entity("done")));
				holdIn("passive", INFINITY); //q가 비면 out_s 출력 후 passive 상태로 천이
			}
			// */
			
			/*
			while(!q.isEmpty()) {
				packet = (packet) q.removeFirst();
				m.add(makeContent("out"+packet.dest, packet));
			}
			if(q.isEmpty()) {
				m.add(makeContent("out_s", new entity("done")));
				holdIn("passive", INFINITY);
			}
			*/
			
			
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



