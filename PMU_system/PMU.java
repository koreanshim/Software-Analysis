package PMU_system;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class PMU extends ViewableAtomic
{
  
	protected order Order;
	protected double processing_time;
	protected int a_pill;
	protected int b_pill;
	protected int c_pill;
	protected int ord_id;
	protected int om_id;
	protected int left_quantity;
	public PMU()
	{
		this("PMU", 20);
	}

	public PMU(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("pat_out");
		addOutport("om_out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		Order = new order("", 0, 0, 0, "", 0);
		a_pill = 300;
		b_pill = 200;
		c_pill = 100;
		ord_id = 100000;
		om_id = 0;
		left_quantity = 0;
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
			//ord_id++;
			//om_id++;
			Order = new order("", 0, 0, 0, "", 0);
			
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			System.out.println("====PMU===");
			System.out.println(Order.msg_id);
			if(Order.pill_id == "A") {
				if(a_pill - Order.case_quantity > 150) {
					a_pill = a_pill - Order.case_quantity;
					Order = new order("pmu_id: " + Order.msg_id, Order.pmu_id, Order.pat_id, Order.pill_id, Order.case_quantity);
					System.out.println("patA: " + Order.pmu_id + " " + Order.pat_id + " " + Order.pill_id + " " + Order.case_quantity);
					m.add(makeContent("pat_out", Order));
				}
				else {
					ord_id++;
					om_id++;
					left_quantity = a_pill - Order.case_quantity;
					Order.ord_id = ord_id;
					Order.om_id = om_id;
					System.out.println("om: " + Order.msg_id + " " + Order.pmu_id + " " + om_id + " " + Order.pill_id + " " + left_quantity);
					Order = new order("msg_id: " + Order.msg_id, Order.pmu_id, om_id, Order.pill_id, left_quantity);
					m.add(makeContent("om_out", Order));
				}
			}
			else if(Order.pill_id == "B") {
				if(b_pill - Order.case_quantity > 100) {
					b_pill = b_pill - Order.case_quantity;
					Order = new order("msg_id: " + Order.msg_id, Order.pmu_id, Order.pat_id, Order.pill_id, Order.case_quantity);
					System.out.println("patB: " + Order.pmu_id + " " + Order.pat_id + " " + Order.pill_id + " " + Order.case_quantity);
					m.add(makeContent("pat_out", Order));
				}
				else {
					ord_id++;
					om_id++;
					Order.ord_id = ord_id;
					Order.om_id = om_id;
					left_quantity = b_pill - Order.case_quantity;
					System.out.println("om: " + Order.msg_id + " " + Order.pmu_id + " " + om_id + " " + Order.pill_id + " " + left_quantity);
					Order = new order("msg_id: " + Order.msg_id, Order.pmu_id, om_id, Order.pill_id, left_quantity);
					m.add(makeContent("om_out", Order));
				}
			}
			else if(Order.pill_id == "C") {
				if(c_pill - Order.case_quantity > 50) {
					c_pill = c_pill - Order.case_quantity;
					Order = new order("msg_id: " + Order.msg_id, Order.pmu_id, Order.pat_id, Order.pill_id, Order.case_quantity);
					System.out.println("patC: " + Order.pmu_id + " " + Order.pat_id + " " + Order.pill_id + " " + Order.case_quantity);
					m.add(makeContent("pat_out", Order));
				}
				else {
					ord_id++;
					om_id++;
					Order.ord_id = ord_id;
					Order.om_id = om_id;
					left_quantity = c_pill - Order.case_quantity;
					System.out.println("om: " + Order.msg_id + " " + Order.pmu_id + " " + om_id + " " + Order.pill_id + " " + left_quantity);
					Order = new order("msg_id: " + Order.msg_id, Order.pmu_id, om_id, Order.pill_id, left_quantity);
					m.add(makeContent("om_out", Order));
				}
			}
			
			System.out.println("-----------------");
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "msg: " + Order.getName();
	}

}

