package aaEncoding;

public class AAEncoding implements IAAEncoding
{
	/**
	 * Die Aminosäurencodierung findet in einem Array der Länge 20 statt
	 * Dabei ist die Reihenfolge der AS Codierung wie in der AAIndex DB:
	 * A, R, N, D, C, Q, E, G, H, I, L, K, M, F, P, S, T, W, Y, V: Aminosäuren
	 * 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19: Array-Index
	 */
	private Double[] encodingValues;
	private String encodingID;
	private String description;
	
	public AAEncoding ()
	{
		this.encodingID = "";
		this.description = "";
		this.encodingValues = new Double[20];
	}
	
	public String getEncodingID() 
	{
		return encodingID;
	}

	public String getDescription() 
	{
		return description;
	}

	public Double[] getEncodingValues ()
	{
		return encodingValues;
	}
	
	public void setDescription (String desc)
	{
		this.description = desc;
	}
	
	public void setEncodingID (String encID)
	{
		this.encodingID = encID;
	}
	
	public void setEncodingValues(Double[] encValues)
	{
		if (encValues.length == 20)
		{
			this.encodingValues = encValues;
		}
		else
		{
			System.err.println("Error: Not 20 AA in encoding scheme!");
		}
	}
	
}
