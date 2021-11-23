package DIU_system3_example;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;
import java.util.Arrays;

/* ========================================================================================================================
 * DATE : 2021/10/05
 * AUTHER : 권보승(bskwon9428@gmail.com)
 * BRIEF : 1) contents 배열에 저장되어 있는 데이터를 행 단위로 DIU에 전달한다. 전달을 위해 content 객체에 데이터를 담고, sending state로 천이한다. 
 * 			  1-1) read_arr state 에서 DIU로부터 request 메시지를 전달 받으면 즉시 searching state로 천이한다.
 * 			  1-2) searching state는 request 메시지에 담겨있던 content로부터 메시지id를 참조하여 contents_fixed 배열에서 동일한 id를 찾는다.
 * 				   동일한 id를 발견하면 전달을 위해 content 객체에 해당 행의 데이터를 담고 sending state로 천이한다.
 * 			  1-3) read_arr state 에서 contents 배열의 마지막 행까지 모두 데이터를 이미 전송했다면 wait state로 천이한다.
 * 		   2) sending state 에서는 content 객체를 담은 메시지를 DIU에게 즉각 전송시키고, read_arr state로 천이한다. 
 * 		   3) wait state는 1-1, 1-2 와 동일한 역할을 수행한다.
 * 			[2021/10/20 수정]
 * 			요구사항2 검증을 위해, company를 type으로 가진 message 10개를 연속으로 내보낸다.
 * ======================================================================================================================== */

public class genr extends ViewableAtomic
{
	protected double int_read_arr_time;
	protected double int_sending_time;
	protected double int_searching_time;
	protected int count;
	protected content content;
	protected String request;
	protected String[] content_temp;
	protected String[][] contents = { 
			//obj-id,   message-id,  diu-id,           type,    				region,  			local,     			date,        usage
			{"001", "1000001", "1001", "company", "yonghyeon_1", "incheon", "20210801", "1035"},
			{"001", "1000002", "1001", "company", "yonghyeon_1", "incheon", "20210728", "1380"},
			{"001", "1000003", "1001", "company", "yonghyeon_2", "incheon", "20210615", "3140"},
			{"001", "1000004", "1001", "company", "yonghyeon_3", "incheon", "20210713", "2135"},
			{"001", "1000005", "1001", "company", "yonghyeon_2", "incheon", "20210705", "2114"},
			{"001", "1000006", "1001", "company", "yonghyeon_3", "incheon", "20210817", "559"},
			{"001", "1000007", "1001", "company", "yonghyeon_2", "incheon", "20210701", "209"},
			{"001", "1000008", "1001", "company", "yonghyeon_2", "incheon", "20210822", "1850"},
			{"001", "1000009", "1001", "company", "yonghyeon_3", "incheon", "20210727", "339"},
			{"001", "1000010", "1001", "company", "yonghyeon_1", "incheon", "20210631", "155"}
	};
	protected String[][] contents_fixed = {
			{"001", "1000001", "1001", "company", "yonghyeon_1", "incheon", "20210801", "1035"},
			{"001", "1000002", "1001", "company", "yonghyeon_1", "incheon", "20210728", "1380"},
			{"001", "1000003", "1001", "company", "yonghyeon_2", "incheon", "20210615", "3140"},
			{"001", "1000004", "1001", "company", "yonghyeon_3", "incheon", "20210713", "2135"},
			{"001", "1000005", "1001", "company", "yonghyeon_2", "incheon", "20210705", "2114"},
			{"001", "1000006", "1001", "company", "yonghyeon_3", "incheon", "20210817", "559"},
			{"001", "1000007", "1001", "company", "yonghyeon_2", "incheon", "20210701", "209"},
			{"001", "1000008", "1001", "company", "yonghyeon_2", "incheon", "20210822", "1850"},
			{"001", "1000009", "1001", "company", "yonghyeon_3", "incheon", "20210727", "339"},
			{"001", "1000010", "1001", "company", "yonghyeon_1", "incheon", "20210631", "155"}
	};
	/*protected String[][] contents_origin = { 
			//obj-id,   message-id,  diu-id,           type,    				region,  			local,     			date,        usage
			{"001", "1000001", "1001", "company", "yonghyeon_1", "incheon", "20210801", "1035"},
			{"001", "1000002", "1001","company", "yonghyeon_2", "incheon", "20210801", "176"},
			{"001", "1000003", "1001", "company", "yonghyeon_1", "incheon", "20210728", "1380"},
			{"001", "1000004", "1001", "company", "yonghyeon_3", "incheon", "20210530", "2680"},
			{"001", "1000005", "1001", "company", "yonghyeon_3", "incheon", "20210802", "251"},
			//{"001", "1000006","1001",  "yonghyeon_1"},
			{"001", "1000006", "1001", "company", "yonghyeon_3", "incheon", "20210802", "251"},
			{"001", "1000007", "1001", "company", "yonghyeon_2", "incheon", "20210615", "3140"},
			{"001", "1000008", "1001", "company", "yonghyeon_2", "incheon", "20210617", "93"},
			{"001", "1000009", "1001", "company", "yonghyeon_1", "incheon", "20210802", "132"},
			{"001", "1000010", "1001", "company", "sinsuldong", "incheon", "20210713", "2135"},
			{"001", "1000011", "1001", "hospital", "yonghyeon_1", "incheon", "20210802", "4119"},
			{"001", "1000012", "1001", "company", "yonghyeon_2", "incheon", "20210705", null},
			{"001", "1000013", "1001", "company", "yonghyeon_3", "incheon", "20210817", "559"},
			{"001", "1000014", "1001", "house", "yonghyeon_1", "incheon", "20210623", "128"},
			{"001", "1000015", "1001", "school", "yonghyeon_3", "incheon", null, "1176"},
			{"001", "1000016", "1001", "house", "yonghyeon_1", "incheon", "20210802", "132"},
			{"001", "1000017", "1001", "house", "yonghyeon_2", "incheon", "20210701", "209"},
			{"001", "1000018", "1001", null, "yonghyeon", "incheon", "20210822", "1850"},
			{"001", "1000019", "1001", "house", "yonghyeon_3", "incheon", "20210727", "339"},
			{"001", "1000020", "1001", "yonghyeon_1", "incheon", "20210631", "155"}
	};
	protected String[][] contents_fixed_origin = {
			{"001", "1000001", "1001", "company", "yonghyeon_1", "incheon", "20210801", "1035"},
			{"001", "1000002", "1001", "house", "yonghyeon_2", "incheon", "20210801", "176"},
			{"001", "1000003", "1001", "company", "yonghyeon_1", "incheon", "20210728", "1380"},
			{"001", "1000004", "1001", "school", "yonghyeon_3", "incheon", "20210530", "2680"},
			{"001", "1000005", "1001", "house", "yonghyeon_3", "incheon", "20210802", "251"},
			{"001", "1000006", "1001", "company", "yonghyeon_1", "incheon", "20210725", "1351"},
			{"001", "1000007", "1001", "company", "yonghyeon_2", "incheon", "20210615", "3140"},
			{"001", "1000008", "1001", "house", "yonghyeon_2", "incheon", "20210617", "93"},
			{"001", "1000009", "1001", "house", "yonghyeon_1", "incheon", "20210802", "132"},
			{"001", "1000010", "1001", "company", "yonghyeon_3", "incheon", "20210713", "2135"},
			{"001", "1000011", "1001", "house", "yonghyeon_1", "incheon", "20210802", "4119"},
			{"001", "1000012", "1001", "company", "yonghyeon_2", "incheon", "20210705", "2114"},
			{"001", "1000013", "1001", "company", "yonghyeon_3", "incheon", "20210817", "559"},
			{"001", "1000014", "1001", "house", "yonghyeon_1", "incheon", "20210623", "128"},
			{"001", "1000015", "1001", "school", "yonghyeon_3", "incheon", "20210719", "1176"},
			{"001", "1000016", "1001", "house", "yonghyeon_1", "incheon", "20210802", "132"},
			{"001", "1000017", "1001", "house", "yonghyeon_2", "incheon", "20210701", "209"},
			{"001", "1000018", "1001", "company", "yonghyeon_2", "incheon", "20210822", "1850"},
			{"001", "1000019", "1001", "house", "yonghyeon_3", "incheon", "20210727", "339"},
			{"001", "1000020", "1001", "house", "yonghyeon_1", "incheon", "20210631", "155"}
	};*/
	protected int content_max;
	
	/* 행 번호를 매개변수로 받아 contents[행 번호]를 반환한다. */
	private String[] getIndex(int counter) {
		if (counter<=content_max) {
			return contents[counter];
		}
		else {
			System.out.println("Requested index number exceed last of index");
			return new String[] {null};
		}
	}
	/* msg_id를 매개변수로 받아 contents_fixed 내의 msg_id와 일치하는 행의 데이터를 반환한다. */
	private String[] getfixedIndex(String msg_id) {
		for (int j=0; j<content_max; j++) {
			if (msg_id == contents_fixed[j][1]) {
				return contents_fixed[j];
			}
		}
		System.out.println("Requested message_id not exist in fixed table");
		return new String[] {null};
	}
	/* content 객체를 매개변수로 받아 msg_id 열의 데이터만 뽑아낸다. 추후 확장성을 위해 함수화 한다. */ 
	private String getMessageId(content re_req) {
		return re_req.data[1];
	}
	
	public genr() 
	{
		this("genr", 10);
	}
  
	public genr(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
    
		int_read_arr_time = Int_arr_time;
		int_sending_time = 0;
		int_searching_time = 0;
	}
  
	public void initialize()
	{
		content = new content("",null);
		content_max = contents.length;
		content_temp = null;
		count = 0;
		holdIn("read_arr", int_read_arr_time);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("read_arr") || phaseIs("wait")) //받은 message가 있으면 searching으로 천이한다. 다른 state 에서의 입력은 고려치 않는다.
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					//getMessageId(재요청 content 객체)는 입력 행으로부터 1열의 msg_id만 추출하여 request String에 담는다.
					request = getMessageId((content) x.getValOnPort("in", i));
					holdIn("searching", 0);
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("read_arr"))
		{
			/* 보낼 contents 의 행이 남아있다면, getIndex(행 번호)를 통해 contents의 행을 받아온다.
			 simview 상에서의 식별을 위한 msg_id 열의 7자리 코드와 전송할 행의 전체 데이터를 담은 새 객체 content 를 생성한다. 
			 sending state로 천이하면서, 전송할 다음 행의 지시를 위해 count를 1 증가시킨다. 더이상 행이 남지 않았으면, wait state로 천이한다. */
			if (count < content_max) { 
				content_temp =  getIndex(count);
				content = new content("id:" + content_temp[1] , content_temp);
				System.out.println("Send content : id:"+ content_temp[1]);
				holdIn("sending", int_sending_time);
				count = count + 1;
			}
			else { 
				holdIn("wait", INFINITY);
			}
		}
		else if (phaseIs("sending")) {
			holdIn("read_arr", int_read_arr_time);
		}
		else if (phaseIs("searching")) {
			/* 보냈던 content에 대한 재전송 요청 메시지가 들어왔다면 getfixedIndex(재요청 content 객체)를 통해 정상적인 데이터를 받아온다.
			 * 이후 sending state로 천이한다. */
			content = new content("id:" + request ,getfixedIndex(request));
			System.out.println("Re-send by request(id:"+ request+") " + Arrays.toString(content.data));
			holdIn("sending", int_read_arr_time);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("sending"))
		{
			m.add(makeContent("out", content));
		}
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_read_arr_time
        + "\n" + " count: " + count;
	}

}
