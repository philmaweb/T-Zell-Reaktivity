package aaEncoding;

import java.util.ArrayList;

public class AAEncodingDatabase 
{
	private ArrayList<AAEncoding> encodingDatabase = new ArrayList<AAEncoding>();
	

	public ArrayList<AAEncoding> getEncodingDatabase() 
	{
		return this.encodingDatabase;
	}

	public void setEncodingDatabase(ArrayList<AAEncoding> encodingDatabase) 
	{
		this.encodingDatabase = encodingDatabase;
	}
	
	public void addEncodingDatabase(AAEncoding newEncoding)
	{
		this.encodingDatabase.add(newEncoding);
	}
	
}
