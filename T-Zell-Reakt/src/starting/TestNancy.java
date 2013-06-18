package starting;

import io.ExampleReader;

import java.util.ArrayList;

import crossValidation.DataSplit;

public class TestNancy 
{
	public static void main(String[] args) 
	{
		ExampleReader exampleReader = new ExampleReader();
		ArrayList<String> positives = exampleReader.getSequnces("data/positive.txt");
		ArrayList<String> negatives = exampleReader.getSequnces("data/negative.txt");
		
		ArrayList<String> allNinemeres = negatives;
		for(int i = 0; i < positives.size(); i++){
			allNinemeres.add(positives.get(i));
		}
		
		
		DataSplit dS = new DataSplit(allNinemeres, 6);
		ArrayList<ArrayList<String>> testDataSet = dS.getDataSet();
		
		
	}
}
