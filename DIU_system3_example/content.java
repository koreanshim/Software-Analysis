package DIU_system3_example;
import GenCol.*;

/* ========================================================================================================================
 * DATE : 2021/10/05
 * AUTHER : 권보승(bskwon9428@gmail.com)
 * BRIEF : 전달용 객체지만 데이터를 입력 받으면 입력받은 데이터 상태에 따라 수정을 가한다.
 * 		   Case 1 : 초기화 또는 불량 데이터로 인해 null 입력을 받으면 아무것도 하지 않는다.
 * 		   Case 2 : 변수가 4개 이상인 배열을 매개변수로 받았으면 무결성 검사를 수행한다.
 * 				    누락된 데이터(null)가 존재하면 데이터의 길이를 표시하는 len 변수값을 하나씩 감소시킨다. 
 * 					len == 4 이면 request용 메시지로 사용되고, 4 < len <= 7 이면 불량 메시지, len == 8이면 정상 메시지
 * 				    DIU의 IntegrityValidation 함수에서 len을 참조한다. 
 * ======================================================================================================================== */

public class content extends entity
{   
	public String[] data;
	public int len; 
	
	public content(String name, String[] dt)
	{  
		super(name);  
		
		this.data = dt;
		if (dt!=null) this.len = this.data.length;
		if (this.len > 4) { check_integrity();}
	}

	private void check_integrity() {
		for (int i=0; i<len; i++) {
			if (data[i] == null) {
				this.len -= 1;
			}
		}
	}
	
	public int get_length() {
		return this.len;
	}
	
	public String[] get_data() {
		String[] data = this.data;
		return data;
	}
}
