package net.charliemeyer.jpowerhour;

public interface JPowerHourListener {
	
	public void songChange(JPowerHourSong currentlyPlaying, int currentlyPlayingNumber);
	
	public void paused();
	
	public void resumed();
	
	public void finished();
}
