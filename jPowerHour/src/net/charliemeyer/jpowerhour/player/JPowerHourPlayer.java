package net.charliemeyer.jpowerhour.player;

import java.util.ArrayList;

import net.charliemeyer.jpowerhour.JPowerHourListener;
import net.charliemeyer.jpowerhour.JPowerHourSong;

public class JPowerHourPlayer implements JPowerHourListener
{
	private String name;
	private int age;
	private boolean isMale;
	private int weight; //pounds
	private ArrayList<BeerShot> drinks;
	private long startDrinkingTime = -1;
	private double shotGlassSize, alcholPercentage;
	
	public JPowerHourPlayer(String name, int age, boolean isMale, int weight, double shotGlassSize, double alcoholPercentage)
	{
		this();
		this.name = name;
		this.age = age;
		this.isMale = isMale;
		this.weight = weight;
		this.shotGlassSize = shotGlassSize;
		this.alcholPercentage = alcoholPercentage;
	}
	
	public JPowerHourPlayer()
	{
		drinks = new ArrayList<BeerShot>();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public boolean isMale() {
		return isMale;
	}


	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}


	public int getWeight() {
		return weight;
	}


	public void setWeight(int weight) {
		this.weight = weight;
	}


	public double getShotGlassSize() {
		return shotGlassSize;
	}


	public void setShotGlassSize(double shotGlassSize) {
		this.shotGlassSize = shotGlassSize;
	}


	public double getAlcholPercentage() {
		return alcholPercentage;
	}


	public void setAlcholPercentage(double alcholPercentage) {
		this.alcholPercentage = alcholPercentage;
	}


	public void powerHourFinished() {
		// TODO Auto-generated method stub
		
	}


	public void powerHourPaused() {
		// TODO Auto-generated method stub
		
	}


	public void powerHourResumed() {
		// TODO Auto-generated method stub
		
	}


	public void powerHourStarted() {
		this.startDrinkingTime = System.currentTimeMillis();
	}


	public void songChange(JPowerHourSong currentlyPlaying,
			int currentlyPlayingNumber) {
		BeerShot shot = new BeerShot(shotGlassSize, alcholPercentage);
		drinks.add(shot);
	}
	
	public double computeBAC()
	{
		if(startDrinkingTime == -1)
		{
			return 0;
		}
		
		double numDrinks = drinks.size();
		double density = 0.8;
		double weight = this.weight*16; //16 oz per pound
		double distro = 0.55;
		if(isMale)
		{
			distro = 0.68;
		}
		double elimRate = 0.00017;
		if(isMale)
		{
			elimRate = 0.00015;
		}
		
		long now = System.currentTimeMillis();
		long elapsedMillis = now-startDrinkingTime;
		double elapsedSeconds = elapsedMillis/1000d;
		double elapsedMinutes = elapsedSeconds/60d;
		double elapsedHours = elapsedMinutes/60d;
		
		double alcoholPerDrink = this.shotGlassSize*this.alcholPercentage;
		
		double bac = (numDrinks*density*alcoholPerDrink)/(weight*distro)-(elimRate*elapsedHours);
		bac *= 1000;
		return bac;
		
	}
	
	public String toString()
	{
		return this.getName();
	}
	
}
