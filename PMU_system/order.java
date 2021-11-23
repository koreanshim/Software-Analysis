package PMU_system;
import GenCol.*;

public class order extends entity
{   
	int msg_id;
	int pat_id;
	int pmu_id;
	String pill_id;
	int case_quantity;
	int ord_id;
	int om_id;
	int left_quantity;
	
	public order(String name, int _msg_id, int _pat_id, int _pmu_id, String _pill_id, int _case_quantity)
	{  
		super(name); //str
		msg_id = _msg_id; //int
		pat_id = _pat_id; //int
		pmu_id = _pmu_id; //int
		pill_id = _pill_id; //str
		case_quantity = _case_quantity; //int
	}
	
	//pat output
	public order(String name, int _pmu_id, int _pat_id, String _pill_id, int _case_quantity)
	{  
		super(name);
		pmu_id = _pmu_id;
		pat_id = _pat_id;
		pill_id = _pill_id;
		case_quantity = _case_quantity;
	}
	
}
