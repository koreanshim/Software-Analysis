package DIU_system_example;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;
import java.util.ArrayList;

/* ========================================================================================================================
 * DATE : 2021/10/05
 * AUTHER : �Ǻ���(bskwon9428@gmail.com)
 * BRIEF : 1) DIU�κ��� content ��ü�� ���޹�����, ��� �޽����� ���޵Ǿ����� �� ���빰�� ����Ѵ�.
 * 		   2) (*�䱸���� ��*) ���޹��� �����͸� �����Ѵ�.
 * ======================================================================================================================== */

public class AMI extends ViewableAtomic
{
	protected content content;
	protected double processing_time;
	protected ArrayList<String[]> aim_list = new ArrayList<String[]>();
	
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
					content = (DIU_system_example.content) x.getValOnPort("in", i);
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