package Lab7;
import GenCol.*;

public class number extends entity
{   

	int num;
	boolean isLast;
	
	
	public number(String name, int _num)
	{  
		super(name);  
		num = _num;
	}
	
	public number(String name, int _num, boolean _isLast)
	{  
		super(name);  
		
		num = _num;
		isLast = _isLast;
	}
	
}
