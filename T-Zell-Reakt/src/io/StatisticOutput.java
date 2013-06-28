package io;

import java.io.FileWriter;
import java.io.IOException;

public class StatisticOutput 
{
	FileWriter fWriter;
	
	public StatisticOutput (String fName)
	{
		try
		{
			this.fWriter = new FileWriter(fName);
		}
		catch(Exception ex)
		{
			System.err.println("Beim Erstellen des Statistikfiles ist ein Fehler aufgetreten: " + ex);
		}
	}
	
	public void writeString (String content)
	{
		try 
		{
			this.fWriter.write(content);
			this.fWriter.flush();
		} 
		catch (IOException ex) 
		{
			System.err.println("Beim Schreiben in das Statistikfile ist ein Fehler aufgetreten: " + ex);
		}
	}
	
	public void close()
	{
		try 
		{
			this.fWriter.close();
		} 
		catch (IOException ex) 
		{
			System.err.println("Beim Schlieﬂen des Statistikfile ist ein Fehler aufgetreten: " + ex);
		}
	}
}
