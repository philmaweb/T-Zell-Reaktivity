package crossValidation;

import java.util.ArrayList;
import java.util.*;

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
		Collections.shuffle(ninemereDatabase);
		
		for(int i = 0; i < ninemereDatabase.size(); i++)
		{
			dataSet.get(i%k).add(ninemereDatabase.get(i));
		}
	}
	
	public ArrayList<ArrayList<String>> getDataSet()
	{
		return this.dataSet;
	}
	
}
