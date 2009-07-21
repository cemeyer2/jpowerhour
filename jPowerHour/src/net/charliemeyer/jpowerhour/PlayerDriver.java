package net.charliemeyer.jpowerhour;

import java.io.File;

import javazoom.jlgui.basicplayer.BasicPlayerException;

public class PlayerDriver 
{
	public static void main(String[] args) throws BasicPlayerException
	{
		File f = new File("secret.mp3");
		JPowerHourPlayer player = new JPowerHourPlayer();
		player.openFile(f);
		player.play(10);
	}
}
