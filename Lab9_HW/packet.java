package Lab9_HW;
import GenCol.*;

public class packet extends entity
{   
	int dest;
	
	public packet(String name, int _dest)
	{  
		super(name);
		
		this.dest = _dest;
	}
	
}
