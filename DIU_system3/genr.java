package DIU_system3;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class genr extends ViewableAtomic
{
	protected job job;
	protected double int_arr_time;
	protected int ami_id;
	protected int msg_id;
	protected int diu_id;
	protected String facility;
	protected String address;
	protected String region;
	protected String date;
	protected int power;
	protected int count;
	
	public genr() 
	{
		this("genr", 40);
	}
  
	public void initialize()
	{
		job = new job("none", 0, 0, 0, "none", "none", "none", "none", 0);
				
		ami_id = (int)(Math.random() * 10);
		//1~3:ac+ay1~3, 4~6:ah+ay1~3, 7~9:as+ay1~3
		msg_id = 101; //message count
		diu_id = (int)(Math.random() * 1000) + 1001; //1001~2000 3ï¿½ï¿½ ï¿½ï¿½ï¿? re_request
		facility = "fac_default";
		address = "add_default";
		region = "INCHEON";
		date = "2021.08.01";
		power = (int)(Math.random() * 10) + 1000; //1000~1009kw
	
		count = 0;
		
		holdIn("sending", int_arr_time);
	}
	
	public genr(String name, double Int_arr_time)
	{
		super(name);

		addInport("in");
		addOutport("out");
    
		int_arr_time = Int_arr_time;
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		
		
		if (phaseIs("sending"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					job = (job)x.getValOnPort("in", i);
					
					if(count < 0) {
						holdIn("checking", 13);
					}
					if(job._facility == "RE_REQUEST") {
						holdIn("reading", 0);
					}
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("sending"))
		{
			job = new job(null, 0, 0, 0, null, null, null, null, 0);
			
			if(count == 0) {
				ami_id = (int)(Math.random() * 3) + 1;
				holdIn("checking", 13);
			}
			else {
				ami_id = (int)(Math.random() * 9) + 1;
				holdIn("sending", int_arr_time);
			}
			//1~3:ac+ay1~3, 4~6:ah+ay1~3, 7~9:as+ay1~3
			diu_id = (int)(Math.random() * 1000) + 1001; //1001~2000 3ï¿½ï¿½ ï¿½ï¿½ï¿? re_request
			facility = "fac_default";
			address = "add_default";
			region = "INCHEON";
			date = "2021/8/1";
			power = (int)(Math.random() * 10) + 1000; //1000~1009kw
			
			msg_id++;
		}
		if (phaseIs("checking")) {
			job = new job(null, 0, 0, 0, null, null, null, null, 0);
			
			ami_id = (int)(Math.random() * 3) + 1;
			//1~3:ac+ay1~3, 4~6:ah+ay1~3, 7~9:as+ay1~3, 0:null
			diu_id = 1001; //1001:OK, 1002:NO
			facility = "fac_default";
			address = "add_default";
			region = "INCHEON";
			date = "2021/8/1";
			power = (int)(Math.random() * 10) + 1000; //1000~1009kw
			
			
			if(count < 10) {
				holdIn("checking", 13);
			}
			else {
				holdIn("sending", int_arr_time);
			}
			
			count++;
			if(count > 1) {
				msg_id++;
			}
		}
		
		else if (phaseIs("reading")) {
			job = new job(null, 0, 0, 0, null, null, null, null, 0);

			msg_id--;
			
			holdIn("resending", int_arr_time);
		}
		else if(phaseIs("resending")) {
			job = new job(null, 0, 0, 0, null, null, null, null, 0);
			
			ami_id = (int)(Math.random() * 9) + 1;
			//1~3:ac+ay1~3, 4~6:ah+ay1~3, 7~9:as+ay1~3, 0:null
			diu_id = (int)(Math.random() * 2) + 1001; //1001:OK, 1002:NO
			facility = "fac_default";
			address = "add_default";
			region = "INCHEON";
			date = "2021/8/1";
			power = (int)(Math.random() * 10) + 1000; //1000~1009kw

			msg_id++;
			
			holdIn("sending", int_arr_time);
		}
	}

	public message out()
	{
		if(ami_id == 1) {
			facility = "company";
			address = "yonghyun1";
		}
		else if(ami_id == 2) {
			facility = "company";
			address = "yonghyun2";
		}
		else if(ami_id == 3) {
			facility = "company";
			address = "yonghyun3";
		}
		else if(ami_id == 4) {
			facility = "house";
			address = "yonghyun1";
		}
		else if(ami_id == 5) {
			facility = "house";
			address = "yonghyun2";
		}
		else if(ami_id == 6) {
			facility = "house";
			address = "yonghyun3";
		}
		else if(ami_id == 7) {
			facility = "school";
			address = "yonghyun1";
		}
		else if(ami_id == 8) {
			facility = "school";
			address = "yonghyun2";
		}
		else if(ami_id == 9) {
			facility = "school";
			address = "yonghyun3";
		}

		message m = new message();
		
		m.add(makeContent("out", new job("id: " + msg_id, ami_id, msg_id, diu_id, 
				facility, address, region, date, power)));
		
		System.out.println("\n========GENR========");
		System.out.println("AMI_ID: " + ami_id);
		System.out.println("MSG_ID: " + msg_id);
		System.out.println("DIU_ID: " + diu_id);
		System.out.println("FACILITY: " + facility);
		System.out.println("ADDRESS: " + address);
		System.out.println("REGION: " + region);
		System.out.println("DATE: " + date);
		System.out.println("POWER(KW): " + power);
		System.out.println("===================");
		
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + msg_id;
	}

}
