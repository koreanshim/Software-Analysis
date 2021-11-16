package DIU_system;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class ami_yonghyun1 extends ViewableAtomic
{
  
	protected job job_ex;
	protected double processing_time;

	public ami_yonghyun1()
	{
		this("ami_yonghyun1", 10);
	}

	public ami_yonghyun1(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job_ex = new job("none", 0, 0, 0, "none", "none", "none", "none", 0);
		
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
			
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out", job_ex));
			System.out.println("\n===AMI_YONGHYUN1====");
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

