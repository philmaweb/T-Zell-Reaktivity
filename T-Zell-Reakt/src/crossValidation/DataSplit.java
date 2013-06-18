package crossValidation;

import java.util.ArrayList;

public class DataSplit {

	private ArrayList<String> ninemereDatabase;
	private ArrayList<ArrayList<String>> dataSet;
	
	public DataSplit(ArrayList<String> ninemeres, int k)
	{
		this.ninemereDatabase = ninemeres;
		splitData(k);
	}
	
	private void splitData(int k)
	{
		RandomNumberGenerator rNG = new RandomNumberGenerator();
		System.out.println();
		
		for(int i = 0; i < ninemereDatabase.size(); i++)
		{
			int random = rNG.getRandom();
			dataSet.get(random%k).add(ninemereDatabase.get(i));
		}
	}
	
	public ArrayList<ArrayList<String>> getDataSet()
	{
		return this.dataSet;
	}
	
}
