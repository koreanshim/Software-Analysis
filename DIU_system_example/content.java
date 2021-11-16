package DIU_system_example;
import GenCol.*;

/* ========================================================================================================================
 * DATE : 2021/10/05
 * AUTHER : �Ǻ���(bskwon9428@gmail.com)
 * BRIEF : ���޿� ��ü���� �����͸� �Է� ������ �Է¹��� ������ ���¿� ���� ������ ���Ѵ�.
 * 		   Case 1 : �ʱ�ȭ �Ǵ� �ҷ� �����ͷ� ���� null �Է��� ������ �ƹ��͵� ���� �ʴ´�.
 * 		   Case 2 : ������ 4�� �̻��� �迭�� �Ű������� �޾����� ���Ἲ �˻縦 �����Ѵ�.
 * 				    ������ ������(null)�� �����ϸ� �������� ���̸� ǥ���ϴ� len �������� �ϳ��� ���ҽ�Ų��. 
 * 					len == 4 �̸� request�� �޽����� ���ǰ�, 4 < len <= 7 �̸� �ҷ� �޽���, len == 8�̸� ���� �޽���
 * 				    DIU�� IntegrityValidation �Լ����� len�� �����Ѵ�. 
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
	
}