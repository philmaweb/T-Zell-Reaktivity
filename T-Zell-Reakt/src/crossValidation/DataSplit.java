
package crossValidation;

import java.util.ArrayList;
import java.util.*;

public class DataSplit {

	private ArrayList<String> ninemereDatabase;
	private ArrayList<ArrayList<String>> dataSet;
	
	public DataSplit(ArrayList<String> ninemeres, int k)
	{
		this.ninemereDatabase = ninemeres;
		this.dataSet = new ArrayList<ArrayList<String>>(k);
		initializeArray(k);
		splitData(k);
	}
		
	private void initializeArray(int k)
	{
		for(int i = 0; i < k; i++)
		{
			this.dataSet.add(new ArrayList<String>());
		}
	}
	
	private void splitData(int k)
	{
		Collections.shuffle(ninemereDatabase);
		
		for(int i = 0; i < ninemereDatabase.size(); i++)
		{
			String add = ninemereDatabase.get(i);
			(dataSet.get(i%k)).add(add);
		}
	}
	
	public ArrayList<ArrayList<String>> getDataSet()
	{
		return this.dataSet;
	}
	
}
