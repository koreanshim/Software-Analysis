package DIU_system_example;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;
import java.util.Arrays;

/* ========================================================================================================================
 * DATE : 2021/10/05
 * AUTHER : �Ǻ���(bskwon9428@gmail.com)
 * BRIEF : 1) contents �迭�� ����Ǿ� �ִ� �����͸� �� ������ DIU�� �����Ѵ�. ������ ���� content ��ü�� �����͸� ���, sending state�� õ���Ѵ�. 
 * 			  1-1) read_arr state ���� DIU�κ��� request �޽����� ���� ������ ��� searching state�� õ���Ѵ�.
 * 			  1-2) searching state�� request �޽����� ����ִ� content�κ��� �޽���id�� �����Ͽ� contents_fixed �迭���� ������ id�� ã�´�.
 * 				   ������ id�� �߰��ϸ� ������ ���� content ��ü�� �ش� ���� �����͸� ��� sending state�� õ���Ѵ�.
 * 			  1-3) read_arr state ���� contents �迭�� ������ ����� ��� �����͸� �̹� �����ߴٸ� wait state�� õ���Ѵ�.
 * 		   2) sending state ������ content ��ü�� ���� �޽����� DIU���� �ﰢ ���۽�Ű��, read_arr state�� õ���Ѵ�. 
 * 		   3) wait state�� 1-1, 1-2 �� ������ ������ �����Ѵ�.
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
			{"001", "1000002", null,"house", "yonghyeon_2", "incheon", "20210801", "176"},
			{"001", "1000003", "1001", "company", "yonghyeon_1", "incheon", "20210728", "1380"},
			{"001", "1000004", "1001", null, "yonghyeon_3", "incheon", "20210530", "2680"},
			{"001", "1000005", "1001", "house", "yonghyeon_3", "incheon", "20210802", "251"},
			{"001", "1000006","1001",  "yonghyeon_1"},
			{"001", "1000007", "1001", "company", "yonghyeon_2", "incheon", "20210615", "3140"},
			{"001", "1000008", "1001", "house", null, "incheon", "20210617", "93"},
			{"001", "1000009", "1001", "house", "yonghyeon_1", "incheon", "20210802", "132"},
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
	protected String[][] contents_fixed = {
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
	};
	protected int content_max;
	
	/* �� ��ȣ�� �Ű������� �޾� contents[�� ��ȣ]�� ��ȯ�Ѵ�. */
	private String[] getIndex(int counter) {
		if (counter<=content_max) {
			return contents[counter];
		}
		else {
			System.out.println("Requested index number exceed last of index");
			return new String[] {null};
		}
	}
	/* msg_id�� �Ű������� �޾� contents_fixed ���� msg_id�� ��ġ�ϴ� ���� �����͸� ��ȯ�Ѵ�. */
	private String[] getfixedIndex(String msg_id) {
		for (int j=0; j<content_max; j++) {
			if (msg_id == contents_fixed[j][1]) {
				return contents_fixed[j];
			}
		}
		System.out.println("Requested message_id not exist in fixed table");
		return new String[] {null};
	}
	/* content ��ü�� �Ű������� �޾� msg_id ���� �����͸� �̾Ƴ���. ���� Ȯ�强�� ���� �Լ�ȭ �Ѵ�. */ 
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
		if (phaseIs("read_arr") || phaseIs("wait")) //���� message�� ������ searching���� õ���Ѵ�. �ٸ� state ������ �Է��� ���ġ �ʴ´�.
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					//getMessageId(���û content ��ü)�� �Է� �����κ��� 1���� msg_id�� �����Ͽ� request String�� ��´�.
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
			/* ���� contents �� ���� �����ִٸ�, getIndex(�� ��ȣ)�� ���� contents�� ���� �޾ƿ´�.
			 simview �󿡼��� �ĺ��� ���� msg_id ���� 7�ڸ� �ڵ�� ������ ���� ��ü �����͸� ���� �� ��ü content �� �����Ѵ�. 
			 sending state�� õ���ϸ鼭, ������ ���� ���� ���ø� ���� count�� 1 ������Ų��. ���̻� ���� ���� �ʾ�����, wait state�� õ���Ѵ�. */
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
			/* ���´� content�� ���� ������ ��û �޽����� ���Դٸ� getfixedIndex(���û content ��ü)�� ���� �������� �����͸� �޾ƿ´�.
			 * ���� sending state�� õ���Ѵ�. */
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