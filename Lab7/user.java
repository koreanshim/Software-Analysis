package Lab7;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class user extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
	protected number req_num;
	protected int num;
	protected int ran_num[];
	protected Queue queue;
	
	public user() 
	{
		this("user", 30);
	}
  
	public user(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
	
	public boolean isPrime(int temp) {
		if(temp < 2) { //1은 소수 아님
			return false;
		}
		
		if(temp == 2) { //2는 소수임
			return true;
		}
		
		for(int i = 2; i < temp; i++) {
			if(temp % i == 0) {
				return false; //소수가 아님
			}
		}
		
		return true;
	}
  
	public void initialize()
	{
		req_num = new number("none", 0 , false);
		ran_num = new int[10]; //random number을 저장할 배열
		num = 0;
		count = 1;
		queue = new Queue();
		
		for(int i = 0; i < 10; i++) {
			ran_num[i] = (int)(Math.random() * 50) + 1; //1~50 random number 생성
			System.out.println("generated ran_num: " + ran_num[i]);
			
			if(isPrime(ran_num[i])) { //소수인 경우
				i--; //해당 index 삭제
			}
			
			for(int j = 0; j < i; j++) {
				if(ran_num[i] == ran_num[j]) { //중복인 경우
					i--; //해당 index  삭제
				}
			}
			
		}
		
		/*
		int temp = 0;
		for(int i = 0; i < 10; i++) {
			System.out.println(ran_num[i]);
			temp += ran_num[i];
		}
		System.out.println(temp);
		*/
		
		for(int i = 0; i < 10; i++) {
			queue.add(ran_num[i]); //queue에 ran_num 배열을 하나씩 입력
		}
		/*
		queue.add(2);
		queue.add(6);
		queue.add(4);
		queue.add(4);
		queue.add(7);
		queue.add(1);
		queue.add(8);
		queue.add(5);
		queue.add(2);
		queue.add(1);
		*/
		holdIn("start", 0);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("active"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("finished", INFINITY);
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("start"))
		{
			holdIn("active", 0);
			//req_num = new number(Integer.toString(num),num);
    	}
		if(phaseIs("active")) {
			count = count + 1;

			if(queue.size() > 0)
			{
				num = (int)queue.removeFirst();
				if(queue.size() == 0)
				{
					req_num = new number(Integer.toString(num),num, true);
				}
				else
				{
					req_num = new number(Integer.toString(num),num);
					holdIn("active", int_arr_time);
				}
			}
			else if(queue.size() == 0)
			{
				holdIn("passive", INFINITY);
	    	}   
    	}
	}

	public message out()
	{
		message m = new message();
		if(phaseIs("active"))
		{
			m.add(makeContent("out", req_num));
		}
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

}
