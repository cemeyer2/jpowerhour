package net.charliemeyer.jpowerhour;

public interface PowerHourListener {
	
	public void songChange(PowerHourSong currentlyPlaying, int currentlyPlayingNumber);
	
	public void paused();
	
	public void resumed();
}
