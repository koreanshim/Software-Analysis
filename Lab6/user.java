package Lab6;
import simView.*;

import genDevs.modeling.*;
import GenCol.*;

public class user extends ViewableAtomic
{
	
	protected double int_arr_time;
	//protected int count;
	protected int op_num;
	protected String op;
	protected int num1, num2;
	protected request req_msg;
	protected result res_msg;
	
	
	public user() 
	{
		this("user", 30);
	}
  
	public user(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out1");
		addOutport("out2");
		addOutport("out3");
		addOutport("out4");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		//count = 1;
		num1 = 0;
		num2 = 0;
		req_msg = new request("none", 0, 0);
		res_msg = new result("none", 0);

		
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
					//holdIn("stop", INFINITY);
				}
			}
		}
		*/
	}

	public void deltint()
	{
		if (phaseIs("active"))
		{
			num1 = (int)(Math.random()*100)+1;
			num2 = (int)(Math.random()*100)+1;
			op_num = (int)(Math.random()*4)+1;
			//req_msg = new request(num1 + " + " + num2, num1, num2);
			//req_msg = new request(num1 + op + num2, num1, num2, op);

			holdIn("active", int_arr_time);
			
			
		
		}
	}

	public message out()
	{
		message m = new message();
		m.add(makeContent("out", req_msg));
		if(op_num == 1) {
			System.out.println("\n===USER===");
			System.out.println("num1:" + num1 + " num2:" + num2);
			m.add(makeContent("out1", new request(num1 + " + " +num2, num1, num2)));
		}
		else if(op_num == 2) {
			System.out.println("\n===USER===");
			System.out.println("num1:" + num1 + " num2:" + num2);
			m.add(makeContent("out2", new request(num1 + " - " +num2, num1, num2)));
		}
		else if(op_num == 3) {
			System.out.println("\n===USER===");
			System.out.println("num1:" + num1 + " num2:" + num2);
			m.add(makeContent("out3", new request(num1 + " * " +num2, num1, num2)));
		}
		else if(op_num == 4) {
			System.out.println("\n===USER===");
			System.out.println("num1:" + num1 + " num2:" + num2);
			m.add(makeContent("out4", new request(num1 + " / " +num2, num1, num2)));
		}
		
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time;
	}

}
