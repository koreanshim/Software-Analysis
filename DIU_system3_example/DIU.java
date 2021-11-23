package DIU_system3_example;
import genDevs.modeling.*;

import java.util.LinkedList;
import java.util.Queue;

import GenCol.*;
import simView.*;

/* ========================================================================================================================
 * DATE : 2021/10/05
 * AUTHER : 권보승(bskwon9428@gmail.com)
 * BRIEF : 1) genr로부터 content 객체가 담긴 메시지를 전달받으면 classifying state로 천이한 뒤, 그 내용물에 따라 AMI 혹은 genr로 수정이 가해진
 * 			  content 객체를 전달한다.
 * 				1-1) 객체에 담긴 내용이 무결하면, AMI 전달을 위해 메시지를 수정하는 re-writing state로 천이한다.
 * 				1-2) 객체에 담긴 내용이 무결성 검증에서 실패하면, genr에게 재전송 요청을 위해 메시지를 수정하는 re-request state로 천이한다. 
 * 		   2) re-writing state에서는 각 AMI 별로 다른 ami_id가 담긴 메시지를 보내게 되므로 두개의 객체를 따로 생성한뒤 sending state로 천이한다.
 * 		   3) re-request state에서는 메시지의 핵심 헤더(전력 객체, msg_id, diu-id, "request")만 남긴 새 content 를 생성한뒤
 * 			  sending state로 천이한다.   
 * 		   4) sending state에서는 전달하려는 메시지의 type, ami_id를 확인하여 적절한 outport 로 전송한다. 이후 wait state로 천이한다. 
 * 				4-1) type(종류 데이터) == "request" 인 경우 genr로 content를 보낸다.
 * 				4-2) 4-1에 해당하지 않는 경우 ami_id에 따라 정해진 outport로 content를 보낸다.
 * 
 * 			[2021/10/21 추가]
 * 			1) in port를 통해 전달받은 메시지를 확인하여, AMI_company로부터 전달받은 메시지라면 index 2의 내용을 확인하여 0이면 전송차단 플래그를
 * 			   true, 1이면 전송차단 플래그를 false로 설정한다. 기본 값은 false이다.
 * 			2) re-writing state에서 전송차단 플래그가 true로 설정되어 있으면 AMI_company로 보내도록 되어있는 컨텐츠 객체는 block되도록 한다.
 * 
 * 			[2021/11/1 추가]
 * 			1) 10/21 에 추가되었던 전송차단 기능에서, AMI_company로 보내려던 컨텐츠 객체가 block 되어진 경우 큐에 컨텐츠를 저장한다.
 * 			2) DIU가 10 Simulation time 이상 유휴 상태이고, AMI_company가 현재 message를 수신 가능한 상태이면 큐에서 컨텐츠를 꺼내
 * 			   AMI_company로 전달하기 위해 poll state로 천이한다.
 * 			3) poll state에서는 큐에서 컨텐츠를 꺼내고, AMI_company로 전송하기 위해 sending state로 천이한다. 
 * 			4) 요구사항 3의 Simulation time = 10 조건을 만족하기 위해, sending state에서 wait state로 천이할 때, ta=10을 부여한다
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
	protected String DIU_ID = "1001"; /* 해당 DIU 고유번호 */
	protected final content ami_block = new content("",new String[] {null, null, null, null});
	protected boolean ami_company_flag;
	protected Queue<content> ami_c_queue = new LinkedList<content>();
	
	private boolean IntegrityValidation(content ct) {
		/* content에 담긴 데이터의 길이와, 그 내용물을 검증한다. (message size, amis_type, amis_local)
		 * 메시지 길이가 8이 아니고, 전달할 AMI에 대한 올바른 정보 2개가 모두 bag에 담겨있지 않으면 false 처리한다. */
		if(ct.get_length() != 8 || !ami_is_contain(content.data[3]) || !ami_is_contain(content.data[4])){
			return false;
		}
		else return true;
	}
	
	private boolean ami_is_contain(String ami_id) {
		/* IntegrityValidation 함수로부터 전달받은 매개변수 ami_id가 bag_of_amis에 존재하는지 확인한다. */
		for (int i = 0; i < bag_of_amis.length; i++) {
			if (ami_id == bag_of_amis[i]) return true;
		}
		return false;
	}
	
	private String[] convertValidMessage(content ct, String type) {
		/* content를 목적지에 맞는 AMI로 보내기 위해 메시지를 수정한다. 배열의 0번지 전력객체id를 버리고, 본 DIU의 ID로 변경한다.
		 * 배열 2번지 자리의 DIU-ID는 0번지에 위치하도록 수정되었으므로, 2번지에는 목적지 AMI-ID를 넣어준다. */
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
		/* 전력 객체, msg_id, 본 DIU의 ID, "request" 로 구성된 content 메시지를 생성하여 반환한다. */
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
					//컨텐츠 데이터를 읽어 ami_company로부터의 메시지면 ami_company_flag를 스위칭 하고 wait state를 유지한다. 
					if (content.data[0] == "2003") {
						if (content.data[2] == "0") {ami_company_flag = true; 
							System.out.println("AMI_company port blocked by stop message"); 
							holdIn("wait", INFINITY);}
						else if (content.data[2] == "1") {ami_company_flag = false;
							System.out.println("AMI_company port activated by resume message");}
							holdIn("wait", 10);
							/* resume message를 받으면  */
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
			/* 객체에 담긴 내용이 무결하면, AMI 전달을 위해 메시지를 수정하는 re-writing state로 천이한다.
			 * 무결성 검사에 실패하면 re-request 로 천이한다. */
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
			/* 두개의 새로운 content 객체를 생성한다. 한 객체는 집/학교/회사에 해당하는 ami_id를 가진다.
			 * 다른 객체는 지역에 해당하는 ami_id를 가진다. convertValidMessage는 객체로부터 output 요구사항에 맞는 String[]을 생성한다. 
			 * 이후 sending state로 천이 */
			content_first = new content("id:"+content.data[1],convertValidMessage(content, content.data[4]));
			content_second = new content("id:"+content.data[1],convertValidMessage(content, content.data[3]));
			if (content_second.data[2]=="2003" && ami_company_flag == true) {
				ami_c_queue.add(content_second);
				System.out.println("(add) queue : "+ ami_c_queue.size());
				content_second = ami_block;
				//content_second는 전력소비객체의 type에 맞는 ami를 향하므로, flag가 true이면 보내지지 않도록 처리한다. 
			}
			holdIn("sending", processing_time);
		}
		else if (phaseIs("re-request")) {
			/* convertInvalidMessage 함수를 사용하여 request를 위한 content 객체를 생성한다. content_second가 오전송되는
			 * 일이 없도록 content_first 의 데이터를 복사한다. */
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
			/* wait state일 때 큐가 비어있지 않고, ami_c가 메시지를 받을 수 있는 상태면 poll state로 천이한다 */
			if(!ami_c_queue.isEmpty() && !ami_company_flag) {
				holdIn("poll", processing_time);
			}
			else {
				holdIn("wait", INFINITY);
			}
		}
		else if (phaseIs("poll")) {
			/* 큐에서 컨텐츠를 꺼내 content_second에 담고 지역 ami로 향하는 content_first는 블락한다.
			 * 이후 sending state로 천이한다. */
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
			/* 재전송 메시지 요청 여부를 확인한다. */
			if (content_first.data[3]=="request") {
				m.add(makeContent("re_request", content_first));}
			else {
				/* ami_id확인을 통해 각 목적지에 맞는 ami output에 할당한다. */
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