package DIU_system3_example;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;
import java.util.ArrayList;

/* ========================================================================================================================
 * DATE : 2021/10/05
 * AUTHER : 권보승(bskwon9428@gmail.com)
 * BRIEF : 1) DIU로부터 content 객체를 전달받으면, 어떠한 메시지가 전달되었는지 그 내용물을 출력한다.
 * 		   2) (*요구사항 외*) 전달받은 데이터를 보관한다.
 * ======================================================================================================================== */

public class AMI extends ViewableAtomic
{
	protected content content;
	protected double processing_time;
	protected ArrayList<String[]> aim_list = new ArrayList<String[]>();
	
	private void stock_message(content con) {
		/* content 객체에 들어있던 데이터의 내용물을 출력한 뒤 aim_list에 저장한다.
		 * 만약 이미 저장되어 있던 데이터를 또 다시 전달받으면 중복 알림을 출력한다. */
		System.out.print("Received Data : {");
		for (int i = 0; i < 7; i++) {
			System.out.print(con.data[i] + ", ");
		}
		if (!aim_list.contains(con.data)) {
			aim_list.add(con.data);
			System.out.println(con.data[7] + "} len : " + aim_list.size());
			}
		else { System.out.println("message_id already exist"); }
	}

	public AMI()
	{
		this("proc", 20);
	}

	public AMI(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		content = new content("", null);
		aim_list.clear();
		holdIn("wait", INFINITY);
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
					content = (DIU_system3_example.content) x.getValOnPort("in", i);
					holdIn("processing", processing_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("processing"))
		{
			/* content 메시지 출력 및 content 저장 수행*/
			stock_message(content);
			
			content = new content("", null);
			
			holdIn("wait", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("processing"))
		{

		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + content.getName();
	}

}

