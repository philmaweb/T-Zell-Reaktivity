package aaEncoding;

import java.io.*;


public class AAEncodingFileReader
{
	private AAEncodingDatabase dataBase = new AAEncodingDatabase();
	
	public AAEncodingDatabase readAAEncodings(String fileName)
	{
		try
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String currentLine = bufferedReader.readLine();
			while (currentLine != null)
			{
				// Es beginnt eine neue Codierungsdefinition
				if (currentLine.startsWith("H"))
				{
					// Lege ein neues Encoding an
					AAEncoding encoding = new AAEncoding();
					// Setze die ID des aktuellen Encodings
					encoding.setEncodingID(currentLine.split(" ")[1]);
					
					// Lese die nächste Zeile ein, erwarte Beschreibung D
					currentLine = bufferedReader.readLine();
					if (currentLine.startsWith("D"))
					{
						// füge die Beschreibung zum Encoding hinzu
						encoding.setDescription(currentLine.substring(2));
						
						// Lese weiter bis zum Block I, der Codierungszahlen enthält
						while (!currentLine.startsWith("I"))
						{
							currentLine = bufferedReader.readLine();
						}
						
						// Zeile mit I gefunden nächste Zeile enthält erste Definitione
						currentLine = bufferedReader.readLine().trim();
						String[] columns = currentLine.split("[ ]+");
						
						Double[] encValues = new Double[20];
						for (int i = 0; i < columns.length; i++)
						{
							encValues[i] = Double.parseDouble(columns[i]);
						}
						
						// Nächste Zeile enthält die weiteren Definitionen
						currentLine = bufferedReader.readLine().trim();
						columns = currentLine.split("[ ]+");
						
						for (int i = 0; i < columns.length; i++)
						{
							encValues[i+10] = Double.parseDouble(columns[i]);
						}
						// Schreibe den neue Eintrag in die Datenbank
						encoding.setEncodingValues(encValues);
						this.dataBase.addEncodingDatabase(encoding);
						
						currentLine = bufferedReader.readLine();
						while (currentLine.startsWith("//"))
						{
							currentLine = bufferedReader.readLine();
							
							// Das Dateiende wurde erreicht
							if (currentLine == null)
								break;
						}
					}
					else
					{
						System.err.println("Error: Database file corruption!");
						break;
					}
					
				}
			}
			
			bufferedReader.close();
		}
		catch (Exception ex)
		{
			System.err.println(ex);
		}
		return dataBase;
	}
		
}
