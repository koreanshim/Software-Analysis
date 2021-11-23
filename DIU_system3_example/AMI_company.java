package DIU_system3_example;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;
import java.util.ArrayList;

/* ========================================================================================================================
 * DATE : 2021/10/20
 * AUTHER : �Ǻ���(bskwon9428@gmail.com)
 * BRIEF : 1) DIU�κ��� content ��ü�� ���޹�����, ��� �޽����� ���޵Ǿ����� �� ���빰�� ����Ѵ�.
 * 		   2) (*�䱸���� ��*) ���޹��� �����͸� �����Ѵ�.
 * 		   3) DIU�κ��� ��ü�� 3�� ���� ������ DIU���� stop content��ü�� ������ prediction state�� õ���Ѵ�.
 * 		   4) prediction state�� Simulation time 50�� ������ DIU���� resume content��ü�� ������ wait state�� õ���Ѵ�.
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
		/* content ��ü�� ����ִ� �������� ���빰�� ����� �� aim_list�� �����Ѵ�.
		 * ���� �̹� ����Ǿ� �ִ� �����͸� �� �ٽ� ���޹����� �ߺ� �˸��� ����Ѵ�. */
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
			/* content �޽��� ��� �� content ���� ����*/
			stock_message(content);
			
			content = new content("", null);
			
			if (msg_count == 3) { //3�� �޽��� ������ sending state��
				output_message = stop_message;
				holdIn("sending", processing_time);
			}
			else {
				holdIn("wait", INFINITY);
			}
		}
		else if (phaseIs("sending"))
		{
			if (msg_count == 3) { //�޽��� ī���Ͱ� 3�̸� �޽��� ���� �� prediction state��
				holdIn("prediction", 50);
			}
			else { //ī���� 0�̸� �޽��� ���� �� wait state��
				holdIn("wait", INFINITY);
			}
		}
		else if (phaseIs("prediction"))
		{ //�޽��� ī���� �ʱ�ȭ �� �簳 �޽��� ���� �� sending state��
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

