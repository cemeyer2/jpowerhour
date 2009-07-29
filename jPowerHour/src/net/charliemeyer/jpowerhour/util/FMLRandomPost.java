package net.charliemeyer.jpowerhour.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.server.UID;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class FMLRandomPost 
{
	String author, text, category;
	
	public FMLRandomPost()
	{
		parseFML(getFML());
	}
	
	private InputStream getFML()
	{
		String url = "http://api.betacie.com/view/random/nocomment?key=readonly&language=en";
		try
		{
			URLConnection con;
			UID uid = new UID();

			con = new URL(url).openConnection();
			con.connect();

			return con.getInputStream();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return null;
	}
	
	private void parseFML(InputStream in)
	{
		try
		{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			Element items = root.getChild("items");
			Element item = items.getChild("item");
			Element author = item.getChild("author");
			this.author = author.getText();
			Element text = item.getChild("text");
			this.text = text.getText();
			Element category = item.getChild("category");
			this.category = category.getText();
			
			in.close();
		}
		catch(IOException ioe)
		{
			
		} 
		catch (JDOMException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getAuthor() {
		return author;
	}

	public String getText() {
		return text;
	}

	public String getCategory() {
		return category;
	}
}
