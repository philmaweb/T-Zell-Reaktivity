package crossValidation;

import java.util.ArrayList;

import aaEncoding.AAEncoding;
import aaEncoding.AAEncodingDatabase;

public class DataSplit {

	private AAEncodingDatabase database =  new AAEncodingDatabase();
	private ArrayList<AAEncoding> testSet = new ArrayList<AAEncoding>();
	private ArrayList<AAEncoding> trainingSet = new ArrayList<AAEncoding>();
	
	public DataSplit(AAEncodingDatabase aadatabase, int k){
		this.database = aadatabase;
		splitData(k);
	}
	
	private void splitData(int k){
		for(int i = 0; i < database.size(); i++){
			if((i%k) == 0)
				testSet.add(database.get(i));
			else
				trainingSet.add(database.get(i));
		}
		return;
	}
	
	public ArrayList<AAEncoding> getTestSet()
	{
		return this.testSet;
	}
	
	public ArrayList<AAEncoding> getTrainingSet()
	{
		return this.trainingSet;
	}
	
}
