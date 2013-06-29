package io;

import java.io.FileWriter;

public class PredictionWriter 
{
	private FileWriter fileWriter;
	
	public PredictionWriter(String outputFile)
	{	
		try
		{
			this.fileWriter = new FileWriter(outputFile);
		}
		catch (Exception ex)
		{
			System.err.println("Das Outputfile konnte nicht erstellt werden: " + ex);
		}
	}
	
	public void writePeptide(String peptide, int iActivator)
	{
		try
		{
			this.fileWriter.write(peptide + "\t" + iActivator);
		}
		catch (Exception ex)
		{
			System.err.println("Beim Schreiben ins Outputfile ist ein Fehler aufgetreten: " + ex);
		}
	}
	
	public void close()
	{
		try
		{
			this.fileWriter.close();
		}
		catch (Exception ex)
		{
			System.err.println("Beim Schlieﬂen des Outputfiles ist ein Fehler aufgetreten: " + ex);
		}
	}
}
