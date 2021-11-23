package Lab9;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class sender extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
  
	public sender() 
	{
		this("genr", 30);
	}
  
	public sender(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 1;
		
		holdIn("active", int_arr_time);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("active", int_arr_time);
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("active"))
		{
			count = count + 1;
			
			if(count > 5) {
				count = 1; //다시 1로 초기화
				holdIn("wait", INFINITY);
			}
			else {
				holdIn("active", int_arr_time);
			}
		}
	}

	public message out()
	{
		message m = new message();
		m.add(makeContent("out", new packet("job" + count, (int)(Math.random()* 5 + 1)))); //1~5 랜덤숫자
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

}
