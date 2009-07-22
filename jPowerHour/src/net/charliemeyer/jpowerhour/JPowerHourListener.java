package net.charliemeyer.jpowerhour;

public interface JPowerHourListener {
	
	public void songChange(JPowerHourSong currentlyPlaying, int currentlyPlayingNumber);
	
	public void powerHourPaused();
	
	public void powerHourResumed();
	
	public void powerHourFinished();
	
	public void powerHourStarted();
}
