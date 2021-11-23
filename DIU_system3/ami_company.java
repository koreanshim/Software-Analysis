package DIU_system3;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class ami_company extends ViewableAtomic
{
  
	protected job job_ex;
	protected double processing_time;
	protected int msg;

	public ami_company()
	{
		this("ami_company", 5);
	}

	public ami_company(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job_ex = new job("none", 0, 0, 0, "none", "none", "none", "none", 0);
		msg = 0;
		
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
					job_ex = (job)x.getValOnPort("in", i);
					
					holdIn("busy", processing_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			job_ex = new job("none", 0, 0, 0, "none", "none", "none", "none", 0);
			
			msg++;
			
			if(msg == 2) {
				holdIn("prediction", 50);
			}
			else {
				holdIn("passive", INFINITY);
			}
		}
		else if(phaseIs("prediction"))
		{
			job_ex = new job("none", 0, 0, 0, "none", "none", "none", "none", 0);
			
			msg = 0;
			
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			if (msg == 0) {
				//m.add(makeContent("out", job_ex));
				job_ex._diu_id = 1;
				System.out.println("\n=====RESTART=====");
				System.out.println("AMI_ID: " + job_ex._ami_id);
				System.out.println("MSG_ID: " + job_ex._msg_id);
				System.out.println("DIU_ID: " + job_ex._diu_id);
				System.out.println("FACILITY: " + job_ex._facility);
				System.out.println("ADDRESS: " + job_ex._address);
				System.out.println("REGION: " + job_ex._region);
				System.out.println("DATE: " + job_ex._date);
				System.out.println("POWER(KW): " + job_ex._power);
				System.out.println("=====================");
			}
			
			if(msg == 1) {
				job_ex._diu_id = 0;
				m.add(makeContent("out", new job("STOP: " + job_ex._msg_id, job_ex._ami_id, job_ex._msg_id, job_ex._diu_id)));

				System.out.println("\n=====RESTRICTION=====");
				System.out.println("AMI_ID: " + job_ex._ami_id);
				System.out.println("MSG_ID: " + job_ex._msg_id);
				System.out.println("DIU_ID: " + job_ex._diu_id);
				System.out.println("FACILITY: " + job_ex._facility);
				System.out.println("ADDRESS: " + job_ex._address);
				System.out.println("REGION: " + job_ex._region);
				System.out.println("DATE: " + job_ex._date);
				System.out.println("POWER(KW): " + job_ex._power);
				System.out.println("=====================");
			}
		}
		
		else if (phaseIs("prediction")) {
			job_ex._diu_id = 1;
			
			msg = 0;
			
			m.add(makeContent("out", new job("START: " + job_ex._msg_id, job_ex._ami_id, job_ex._msg_id, job_ex._diu_id)));
			
			System.out.println("\n=====PREDICTION=====");
			System.out.println("AMI_ID: " + job_ex._ami_id);
			System.out.println("MSG_ID: " + job_ex._msg_id);
			System.out.println("DIU_ID: " + job_ex._diu_id);
			System.out.println("FACILITY: " + job_ex._facility);
			System.out.println("ADDRESS: " + job_ex._address);
			System.out.println("REGION: " + job_ex._region);
			System.out.println("DATE: " + job_ex._date);
			System.out.println("POWER(KW): " + job_ex._power);
			System.out.println("=====================");
		}
		
		
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + job_ex.getName();
	}

}

