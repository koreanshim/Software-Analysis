
package Lab3;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class transd extends  ViewableAtomic
{
	
	protected Function arrived, solved;
	//THRU, TURN: 일이 언제 시작되고 언제 해결되는지 알아보기 위한 부분
	protected double clock, total_ta, observation_time;

	public transd(String name, double Observation_time)
	{
		super(name);
    
		addOutport("out");
		addInport("ariv");
		addInport("solved");
    
		arrived = new Function();
		solved = new Function();
    
		observation_time = Observation_time;
	}
  
	public transd()
	{
		this("transd", 1000);
	}

	public void initialize()
	{	
		clock = 0;
		total_ta = 0;
    
		arrived = new Function();
		solved = new Function();
		
		holdIn("on", observation_time);
	}

	public void deltext(double e, message x) //in-let
	{
		clock = clock + e;
		Continue(e);
		entity val;
 
		if(phaseIs("on"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "ariv", i))
				{
					val = x.getValOnPort("ariv", i);
					arrived.put(val.getName(), new doubleEnt(clock));
				}
				if (messageOnPort(x, "solved", i))
				{
					val = x.getValOnPort("solved", i);
					if (arrived.containsKey(val.getName()))
					{
						entity ent = (entity) arrived.assoc(val.getName());
					
						doubleEnt num = (doubleEnt) ent;
						double arrival_time = num.getv();
          
						double turn_around_time = clock - arrival_time;
          
						total_ta = total_ta + turn_around_time;
          
						solved.put(val, new doubleEnt(clock));
					}
				}
			}
			show_state();
		}
	}

	public void deltint()
	{
		if (phaseIs("on"))
		{
			clock = clock + sigma; //clock 시점별 시간 계산용
			System.out.println("--------------------------------------------------------");
	   		show_state();
	   		System.out.println("--------------------------------------------------------");
	   		
	   		holdIn("off", 0);
		}
	}
  
	public message out() // 70초 에서 TA 출력
	{
		message m = new message();
		
		if (phaseIs("on"))
		{
			m.add(makeContent("out", new entity("TA: " + compute_TA())));
		}
		
		return m;
	}

	public double compute_TA() //TA 계산 함수
	{
		double avg_ta_time = 0;
		if (!solved.isEmpty())
		{
			avg_ta_time = ( (double) total_ta) / solved.size(); //평균 구하기
			//주의 : 약분하면 안됨. 약분 시 의미가 달라짐!
		}
		return avg_ta_time;
	}

  
	public String compute_Thru()
	{
		String thruput = "";
		if (clock > 0)
		{
			thruput = solved.size() + " / " + clock;
		}
		return thruput;
	}

	public void show_state() 
	{
		System.out.println("state of  " + name + ": ");
		System.out.println("phase, sigma : " + phase + " " + sigma + " ");
		
		if (arrived != null && solved != null)
		{
			System.out.println("Total jobs arrived : "+ arrived.size());
			System.out.println("Total jobs solved : " + solved.size());
			System.out.println("AVG TA = " + compute_TA());
			System.out.println("THRUPUT = " + compute_Thru());
			System.out.println("TA = " + total_ta);
		}
	}	
  
	public String getTooltipText()
	{
		String s = "";
		if (arrived != null && solved != null)
		{
			s = "\n" + "jobs arrived :" + arrived.size()
			+ "\n" + "jobs solved :" + solved.size()
			+ "\n" + "AVG TA = " + compute_TA()
			+ "\n" + "THRUPUT = " + compute_Thru();
		}
		return super.getTooltipText() + s;
	}

}
