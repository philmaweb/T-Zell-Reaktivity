package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Weil der Weka Output doch nicht so Gnuplot kompatibel ist, wird in ein besseres Format umgewandelt
 *
 */
public class StatisticOutputProcessor 
{
	
	public static void createProcessedOutput (String inputName)
	{
		try
		{
			FileReader fileReader = new FileReader("gridSearchLogFiles/run_" + inputName);
			FileWriter fileWriter = new FileWriter("gridSearchLogFiles/run_" + inputName + ".csv");
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			// Schreibe Header
			//fileWriter.write("C\tgamma\tCC\tRMSE\tRRSE\tMAE\tRAE\tCOMB\tACC\tKAP\n");
			String str = bufferedReader.readLine();
			while (str != null)
			{
				if (!str.equals("") && str.startsWith("Performance "))
				{
					str = str.replaceAll("Performance \\(\\[", "");
					str = str.replaceAll(": cached=false", "");
					str = str.replaceAll("\\]\\):", "\t");
					str = str.replaceAll(", ", "\t");
					str = str.replaceAll(" \\([A-Z]*\\)", "");
					String[] columns = str.split("\t");
					
					fileWriter.write(columns[0] + "\t" + columns[1] + "\t" + columns[8] + "\n");
				}
				str = bufferedReader.readLine();
			}
			fileReader.close();
			fileWriter.close();
			
		}
		catch (Exception ex)
		{
			System.err.println(ex);
		}
	}
}
