package net.charliemeyer.jpowerhour.itunes;

public class ITunesUtils 
{
	public static boolean hasItunes()
	{
		String os = System.getProperty("os.name");
		System.out.println(os);
		
		if(os.startsWith("Mac OS X"))
		{
			//find itunes mac os x
		}
		else if(os.startsWith("Windows"))
		{
			//find itunes windows
		}
		
		return false;
	}
	
	public static void main(String[] args)
	{
		System.out.println(hasItunes());
	}
}