package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ExampleReader 
{
	public ArrayList<String> getSequnces (String fileName)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		try
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String currentLine = bufferedReader.readLine();
			while (currentLine != null)
			{
				if (currentLine.trim().length() == 9)
				{
					result.add(currentLine.trim().toUpperCase());
				}
				currentLine = bufferedReader.readLine();
			}
			fileReader.close();
		}
		catch (Exception ex)
		{
			System.err.println(ex);
		}
		return result;
	}
}
