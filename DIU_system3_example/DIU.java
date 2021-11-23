package DIU_system3_example;
import genDevs.modeling.*;

import java.util.LinkedList;
import java.util.Queue;

import GenCol.*;
import simView.*;

/* ========================================================================================================================
 * DATE : 2021/10/05
 * AUTHER : �Ǻ���(bskwon9428@gmail.com)
 * BRIEF : 1) genr�κ��� content ��ü�� ��� �޽����� ���޹����� classifying state�� õ���� ��, �� ���빰�� ���� AMI Ȥ�� genr�� ������ ������
 * 			  content ��ü�� �����Ѵ�.
 * 				1-1) ��ü�� ��� ������ �����ϸ�, AMI ������ ���� �޽����� �����ϴ� re-writing state�� õ���Ѵ�.
 * 				1-2) ��ü�� ��� ������ ���Ἲ �������� �����ϸ�, genr���� ������ ��û�� ���� �޽����� �����ϴ� re-request state�� õ���Ѵ�. 
 * 		   2) re-writing state������ �� AMI ���� �ٸ� ami_id�� ��� �޽����� ������ �ǹǷ� �ΰ��� ��ü�� ���� �����ѵ� sending state�� õ���Ѵ�.
 * 		   3) re-request state������ �޽����� �ٽ� ���(���� ��ü, msg_id, diu-id, "request")�� ���� �� content �� �����ѵ�
 * 			  sending state�� õ���Ѵ�.   
 * 		   4) sending state������ �����Ϸ��� �޽����� type, ami_id�� Ȯ���Ͽ� ������ outport �� �����Ѵ�. ���� wait state�� õ���Ѵ�. 
 * 				4-1) type(���� ������) == "request" �� ��� genr�� content�� ������.
 * 				4-2) 4-1�� �ش����� �ʴ� ��� ami_id�� ���� ������ outport�� content�� ������.
 * 
 * 			[2021/10/21 �߰�]
 * 			1) in port�� ���� ���޹��� �޽����� Ȯ���Ͽ�, AMI_company�κ��� ���޹��� �޽������ index 2�� ������ Ȯ���Ͽ� 0�̸� �������� �÷��׸�
 * 			   true, 1�̸� �������� �÷��׸� false�� �����Ѵ�. �⺻ ���� false�̴�.
 * 			2) re-writing state���� �������� �÷��װ� true�� �����Ǿ� ������ AMI_company�� �������� �Ǿ��ִ� ������ ��ü�� block�ǵ��� �Ѵ�.
 * 
 * 			[2021/11/1 �߰�]
 * 			1) 10/21 �� �߰��Ǿ��� �������� ��ɿ���, AMI_company�� �������� ������ ��ü�� block �Ǿ��� ��� ť�� �������� �����Ѵ�.
 * 			2) DIU�� 10 Simulation time �̻� ���� �����̰�, AMI_company�� ���� message�� ���� ������ �����̸� ť���� �������� ����
 * 			   AMI_company�� �����ϱ� ���� poll state�� õ���Ѵ�.
 * 			3) poll state������ ť���� �������� ������, AMI_company�� �����ϱ� ���� sending state�� õ���Ѵ�. 
 * 			4) �䱸���� 3�� Simulation time = 10 ������ �����ϱ� ����, sending state���� wait state�� õ���� ��, ta=10�� �ο��Ѵ�
 * 			   
 * ======================================================================================================================== */

public class DIU extends ViewableAtomic
{
	protected content content;
	protected content content_second;
	protected content content_first;
	protected double processing_time;
	protected String[] re_req_content = new String[4];
	protected String[] output_data = new String[8];
	protected String[] bag_of_amis = {"house", "school", "company", "yonghyeon_1", "yonghyeon_2", "yonghyeon_3"};
	protected String DIU_ID = "1001"; /* �ش� DIU ������ȣ */
	protected final content ami_block = new content("",new String[] {null, null, null, null});
	protected boolean ami_company_flag;
	protected Queue<content> ami_c_queue = new LinkedList<content>();
	
	private boolean IntegrityValidation(content ct) {
		/* content�� ��� �������� ���̿�, �� ���빰�� �����Ѵ�. (message size, amis_type, amis_local)
		 * �޽��� ���̰� 8�� �ƴϰ�, ������ AMI�� ���� �ùٸ� ���� 2���� ��� bag�� ������� ������ false ó���Ѵ�. */
		if(ct.get_length() != 8 || !ami_is_contain(content.data[3]) || !ami_is_contain(content.data[4])){
			return false;
		}
		else return true;
	}
	
	private boolean ami_is_contain(String ami_id) {
		/* IntegrityValidation �Լ��κ��� ���޹��� �Ű����� ami_id�� bag_of_amis�� �����ϴ��� Ȯ���Ѵ�. */
		for (int i = 0; i < bag_of_amis.length; i++) {
			if (ami_id == bag_of_amis[i]) return true;
		}
		return false;
	}
	
	private String[] convertValidMessage(content ct, String type) {
		/* content�� �������� �´� AMI�� ������ ���� �޽����� �����Ѵ�. �迭�� 0���� ���°�üid�� ������, �� DIU�� ID�� �����Ѵ�.
		 * �迭 2���� �ڸ��� DIU-ID�� 0������ ��ġ�ϵ��� �����Ǿ����Ƿ�, 2�������� ������ AMI-ID�� �־��ش�. */
		String ami_id = null;
		if (type == "house") ami_id = "2001"; 
		else if (type == "school") ami_id = "2002"; 
		else if (type == "company") ami_id = "2003";
		else if (type == "yonghyeon_1") ami_id = "3001"; 
		else if (type == "yonghyeon_2") ami_id = "3002"; 
		else if (type == "yonghyeon_3") ami_id = "3003";
		String[] conv = new String[] {DIU_ID, ct.data[1], ami_id, ct.data[3], ct.data[4], ct.data[5], ct.data[6], ct.data[7]};
		return conv;
	}
	
	private String[] convertInvalidMessage(content ct) {
		/* ���� ��ü, msg_id, �� DIU�� ID, "request" �� ������ content �޽����� �����Ͽ� ��ȯ�Ѵ�. */
		String[] req_content = new String[] {ct.data[0], ct.data[1], DIU_ID, "request"};
		return req_content;
	}
	
	public DIU()
	{
		this("proc", 20);
	}

	public DIU(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out_ami_house"); //ami_id : 2001
		addOutport("out_ami_school"); //ami_id : 2002
		addOutport("out_ami_company"); //ami_id : 2003
		addOutport("out_ami_yh1"); //ami_id : 3001
		addOutport("out_ami_yh2"); //ami_id : 3002
		addOutport("out_ami_yh3"); //ami_id : 3003
		addOutport("re_request");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		content = new content("",null);
		content_first = new content("",null);
		content_second = new content("",null);
		ami_company_flag = false;
		ami_c_queue.clear();
		holdIn("wait", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					content = (content) x.getValOnPort("in", i);
					//System.out.println(Arrays.toString(content.data));
					//������ �����͸� �о� ami_company�κ����� �޽����� ami_company_flag�� ����Ī �ϰ� wait state�� �����Ѵ�. 
					if (content.data[0] == "2003") {
						if (content.data[2] == "0") {ami_company_flag = true; 
							System.out.println("AMI_company port blocked by stop message"); 
							holdIn("wait", INFINITY);}
						else if (content.data[2] == "1") {ami_company_flag = false;
							System.out.println("AMI_company port activated by resume message");}
							holdIn("wait", 10);
							/* resume message�� ������  */
					}
					else {
						holdIn("classifying", processing_time);
					}
				}
			}
		}
	}
	
	public void deltint()
	{
		if (phaseIs("classifying"))
		{
			/* ��ü�� ��� ������ �����ϸ�, AMI ������ ���� �޽����� �����ϴ� re-writing state�� õ���Ѵ�.
			 * ���Ἲ �˻翡 �����ϸ� re-request �� õ���Ѵ�. */
			if (IntegrityValidation(content))
			{
				holdIn("re-writing", processing_time);
			}
			else
			{
				holdIn("re-request", processing_time);
			}
		}
		else if (phaseIs("re-writing")) {
			/* �ΰ��� ���ο� content ��ü�� �����Ѵ�. �� ��ü�� ��/�б�/ȸ�翡 �ش��ϴ� ami_id�� ������.
			 * �ٸ� ��ü�� ������ �ش��ϴ� ami_id�� ������. convertValidMessage�� ��ü�κ��� output �䱸���׿� �´� String[]�� �����Ѵ�. 
			 * ���� sending state�� õ�� */
			content_first = new content("id:"+content.data[1],convertValidMessage(content, content.data[4]));
			content_second = new content("id:"+content.data[1],convertValidMessage(content, content.data[3]));
			if (content_second.data[2]=="2003" && ami_company_flag == true) {
				ami_c_queue.add(content_second);
				System.out.println("(add) queue : "+ ami_c_queue.size());
				content_second = ami_block;
				//content_second�� ���¼Һ�ü�� type�� �´� ami�� ���ϹǷ�, flag�� true�̸� �������� �ʵ��� ó���Ѵ�. 
			}
			holdIn("sending", processing_time);
		}
		else if (phaseIs("re-request")) {
			/* convertInvalidMessage �Լ��� ����Ͽ� request�� ���� content ��ü�� �����Ѵ�. content_second�� �����۵Ǵ�
			 * ���� ������ content_first �� �����͸� �����Ѵ�. */
			content_first = new content("id:"+content.data[1],convertInvalidMessage(content));
			content_second = content_first; 
			holdIn("sending", processing_time);
		}
		else if (phaseIs("sending")) {
			if(ami_c_queue.isEmpty()) {
				holdIn("wait", INFINITY);
			}
			else {
				holdIn("wait", 10);
			}
		}
		else if (phaseIs("wait")) {
			/* wait state�� �� ť�� ������� �ʰ�, ami_c�� �޽����� ���� �� �ִ� ���¸� poll state�� õ���Ѵ� */
			if(!ami_c_queue.isEmpty() && !ami_company_flag) {
				holdIn("poll", processing_time);
			}
			else {
				holdIn("wait", INFINITY);
			}
		}
		else if (phaseIs("poll")) {
			/* ť���� �������� ���� content_second�� ��� ���� ami�� ���ϴ� content_first�� ����Ѵ�.
			 * ���� sending state�� õ���Ѵ�. */
			content_second = ami_c_queue.poll();
			System.out.println("(poll) queue : "+ ami_c_queue.size());
			content_first = ami_block;
			holdIn("sending", processing_time);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("sending"))
		{
			/* ������ �޽��� ��û ���θ� Ȯ���Ѵ�. */
			if (content_first.data[3]=="request") {
				m.add(makeContent("re_request", content_first));}
			else {
				/* ami_idȮ���� ���� �� �������� �´� ami output�� �Ҵ��Ѵ�. */
				if (content_second.data[2]=="2001") { m.add(makeContent("out_ami_house", content_second)); }
				else if (content_second.data[2]=="2002") { m.add(makeContent("out_ami_school", content_second)); }
				else if (content_second.data[2]=="2003") { m.add(makeContent("out_ami_company", content_second)); }

				if (content_first.data[2]=="3001") { m.add(makeContent("out_ami_yh1", content_first)); }
				else if (content_first.data[2]=="3002") { m.add(makeContent("out_ami_yh2", content_first)); }
				else if (content_first.data[2]=="3003") { m.add(makeContent("out_ami_yh3", content_first)); }
			}
		}
		return m;
	}	
	
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + "not provided";
	}

}