package aaEncoding;

import java.util.ArrayList;

public class AAEncoder 
{
	private AAEncoding encodingType;
	private static int iPeptideLength = 9;
	/**
	 * Methode, die auf einen übergebenen String eine angegebene Codierung anwendet.
	 * Diese Codierung basiert auf dem AAIndex  
	 * @param enc Gültiges Aminosäurenencoding AAEncoding
	 * @param target Die zu codierende Peptidsequenz der Länge 9 als String in Einbuchstaben-Codierung
	 * @return Double[] das jeder Aminosäure einen Wert zugewiesen hat
	 */
	public Double[][] getEncodedString(ArrayList<AAEncoding> enc, String target)
	{
		Double[][] result = new Double[enc.size()][iPeptideLength];
		
		// beginne mit Preprocessing des Strings 
		char[] preprocessedString = preprocessString(target);
		
		for (int e = 0; e < enc.size(); e++)
		{
			this.encodingType = enc.get(e);
			
			for (int i = 0; i < iPeptideLength; i++)
			{
				switch (preprocessedString[i])
				{
					case 'A':
						result[e][i] = this.encodingType.getEncodingValues()[0];
						break;
					
					case 'R':
						result[e][i] = this.encodingType.getEncodingValues()[1];
						break;
						
					case 'N':
						result[e][i] = this.encodingType.getEncodingValues()[2];
						break;
						
					case 'D':
						result[e][i] = this.encodingType.getEncodingValues()[3];
						break;
						
					case 'C':
						result[e][i] = this.encodingType.getEncodingValues()[4];
						break;
						
					case 'Q':
						result[e][i] = this.encodingType.getEncodingValues()[5];
						break;
						
					case 'E':
						result[e][i] = this.encodingType.getEncodingValues()[6];
						break;
						
					case 'G':
						result[e][i] = this.encodingType.getEncodingValues()[7];
						break;
						
					case 'H':
						result[e][i] = this.encodingType.getEncodingValues()[8];
						break;
						
					case 'I':
						result[e][i] = this.encodingType.getEncodingValues()[9];
						break;
						
					case 'L':
						result[e][i] = this.encodingType.getEncodingValues()[10];
						break;
						
					case 'K':
						result[e][i] = this.encodingType.getEncodingValues()[11];
						break;
						
					case 'M':
						result[e][i] = this.encodingType.getEncodingValues()[12];
						break;
						
					case 'F':
						result[e][i] = this.encodingType.getEncodingValues()[13];
						break;
						
					case 'P':
						result[e][i] = this.encodingType.getEncodingValues()[14];
						break;
						
					case 'S':
						result[e][i] = this.encodingType.getEncodingValues()[15];
						break;
						
					case 'T':
						result[e][i] = this.encodingType.getEncodingValues()[16];
						break;
						
					case 'W':
						result[e][i] = this.encodingType.getEncodingValues()[17];
						break;
						
					case 'Y':
						result[e][i] = this.encodingType.getEncodingValues()[18];
						break;
						
					case 'V':
						result[e][i] = this.encodingType.getEncodingValues()[19];
						break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Methode zum Preprocessing von Peptidsequenzen
	 * - entfernt führende und abschließende Leerzeichen
	 * - überführt Strings in Großbuchstaben
	 * - kontrolliert auf Gültigkeit des übergebenen Peptids
	 * 
	 * @param input Eingabestring
	 * @return gibt den verifizierten String als Char[] zurück, bei Fehlern wird leeres char[] zurückgegeben
	 */
	private char[] preprocessString (String input)
	{
		char[] result = new char[iPeptideLength];
		
		if (input.trim().length() == iPeptideLength)
		{
			result = input.trim().toUpperCase().toCharArray();
		}
		else
		{
			System.err.println("Error: Wrong string length!\n");
			System.err.println(input.trim().toUpperCase());
		}
		
		return result;
	}
}
