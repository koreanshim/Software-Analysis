package PMU_system;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;


public class patient extends ViewableAtomic
{

	protected double int_arr_time;
	//protected int count;
	protected order Order;
	int msg_id;
	int pat_id;
	int pmu_id;
	int ran_pill;
	String pill_id;
	int case_quantity;
  
	public patient() 
	{
		this("patient", 40);
	}
  
	public patient(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		msg_id = 1000000;
		pat_id = 1000;
		pmu_id = 10;
		ran_pill = (int)(Math.random() * 3) + 1; //A,B,C
		pill_id = " ";
		case_quantity = (int)(Math.random() * 10) + 10; //10~20
		holdIn("active", int_arr_time);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		/*
		if (phaseIs("active"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("stop", INFINITY);
				}
			}
		}
		*/
	}

	public void deltint()
	{
		if (phaseIs("active"))
		{
			msg_id++;
			pat_id++;
			pmu_id++;
			ran_pill = (int)(Math.random() * 3) + 1; //A,B,C
			if(ran_pill == 1) {
				pill_id = "A";
			}
			else if(ran_pill == 2) {
				pill_id = "B";
			}
			else if(ran_pill == 3) {
				pill_id = "C";
			}
			case_quantity = (int)(Math.random() * 10) + 10; //10~20
			
			Order = new order("msg: " + msg_id, msg_id, pat_id, pmu_id, pill_id, case_quantity);
			
			holdIn("active", int_arr_time);
		}
	}

	public message out()
	{
		message m = new message();
		m.add(makeContent("out", Order));
		
		System.out.println("====PATIENT====");
		System.out.println("msg_id:" + msg_id);
		System.out.println("pat_id:" + pat_id);
		System.out.println("pmu_id:" + pmu_id);
		System.out.println("pill_id:" + pill_id);
		System.out.println("case_quan:" + case_quantity);

		return m;
	}
	/*
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " msg_id: " + count;
	}
	*/
}
