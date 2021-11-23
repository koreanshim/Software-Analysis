package DIU_system3_example;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;
import java.util.ArrayList;

/* ========================================================================================================================
 * DATE : 2021/10/20
 * AUTHER : 권보승(bskwon9428@gmail.com)
 * BRIEF : 1) DIU로부터 content 객체를 전달받으면, 어떠한 메시지가 전달되었는지 그 내용물을 출력한다.
 * 		   2) (*요구사항 외*) 전달받은 데이터를 보관한다.
 * 		   3) DIU로부터 객체를 3번 전달 받으면 DIU에게 stop content객체를 보내고 prediction state로 천이한다.
 * 		   4) prediction state의 Simulation time 50이 지나면 DIU에게 resume content객체를 보내고 wait state로 천이한다.
 * ======================================================================================================================== */

public class AMI_company extends ViewableAtomic
{
	protected content content;
	protected double processing_time;
	protected ArrayList<String[]> aim_list = new ArrayList<String[]>();
	protected int msg_count;
	protected final String ami_id = "2003";
	protected final String DIU_id = "1001";
	protected final content resume_message = new content("resume", new String[] {ami_id, DIU_id, "1"});
	protected final content stop_message= new content("stop", new String[] {ami_id, DIU_id, "0"});
	protected content output_message;
	
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

	public AMI_company()
	{
		this("proc", 20);
	}

	public AMI_company(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		content = new content("", null);
		output_message = new content("", null);
		aim_list.clear();
		msg_count = 0;
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
					content = (content) x.getValOnPort("in", i);
					msg_count++;
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
			
			if (msg_count == 3) { //3번 메시지 받으면 sending state로
				output_message = stop_message;
				holdIn("sending", processing_time);
			}
			else {
				holdIn("wait", INFINITY);
			}
		}
		else if (phaseIs("sending"))
		{
			if (msg_count == 3) { //메시지 카운터가 3이면 메시지 보낸 뒤 prediction state로
				holdIn("prediction", 50);
			}
			else { //카운터 0이면 메시지 보낸 뒤 wait state로
				holdIn("wait", INFINITY);
			}
		}
		else if (phaseIs("prediction"))
		{ //메시지 카운터 초기화 및 재개 메시지 설정 후 sending state로
			msg_count = 0; 
			output_message = resume_message;
			holdIn("sending", processing_time);
		}
		
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("sending"))
		{
			m.add(makeContent("out", output_message));
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

