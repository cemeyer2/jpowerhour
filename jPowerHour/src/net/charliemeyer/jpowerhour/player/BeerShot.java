package net.charliemeyer.jpowerhour.player;

public class BeerShot 
{
	private long time;
	private double ounces, alcoholPercent;
	
	public BeerShot(double ounces, double alcoholPercent)
	{
		this.ounces = ounces;
		this.alcoholPercent = alcoholPercent;
		this.time = System.currentTimeMillis();
	}

	public long getTime() {
		return time;
	}

	public double getOunces() {
		return ounces;
	}

	public double getAlcoholPercent() {
		return alcoholPercent;
	}
}
