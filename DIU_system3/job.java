package DIU_system3;
import GenCol.*;

public class job extends entity
{  
	int _ami_id;
	int _msg_id;
	int _diu_id;
	String _facility;
	String _address;
	String _region;
	String _date;
	int _power;
	
	public job(String name, int ami_id, int msg_id, int diu_id, 
			String facility, String address, String region, String date, int power)
	{  
		super(name);
		_ami_id = ami_id;
		_msg_id = msg_id;
		_diu_id = diu_id;
		_facility = facility;
		_address = address;
		_region = region;
		_date = date;
		_power = power;
	}
	
	public job(String name, int ami_id, int msg_id, int diu_id)
	{  
		super(name);
		_ami_id = ami_id;
		_msg_id = msg_id;
		_diu_id = diu_id;
	}
	
}
