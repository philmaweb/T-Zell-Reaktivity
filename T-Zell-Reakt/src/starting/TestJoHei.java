package starting;

// Feature-Set Klassen
import io.ExampleReader;

import java.util.ArrayList;

import crossValidation.DataSplit;

import aaEncoding.AAEncoder;
import aaEncoding.AAEncoding;
import aaEncoding.AAEncodingDatabase;
import aaEncoding.AAEncodingFileReader;
import weka.ARFFComponent;
import weka.ARFFDataSet;
import weka.ARFFFileGenerator;
import weka.FeatureFilter;

// Weka
import weka.core.Instances;


public class TestJoHei {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		System.out.println("Testausgabe von AAEncodings");
		
		// Lese die EncodingDB ein
		AAEncodingFileReader aa = new AAEncodingFileReader();
		
		AAEncodingDatabase db = aa.readAAEncodings("data/AAEncodings.txt");
		System.out.println("Database size: " + db.getEncodingDatabase().size());
		
		// Teste die Transcodierung
		AAEncoder encoder = new AAEncoder();
		//ARNDCQEGHILKMFPSTWYV
		String peptideSequence = "KMFPSTWYV";
		
		// füge alle Encodings hinzu und schreibe in file
		ArrayList<AAEncoding> selectedEncodings = new ArrayList<AAEncoding>();
		selectedEncodings.addAll(db.getEncodingDatabase());
		
		
		
		Double[][] encValues = encoder.getEncodedString(selectedEncodings, peptideSequence);
		for (int e = 0; e < selectedEncodings.size(); e++)
		{
			for (int i = 0; i < 9; i++)
			{
				System.out.print(encValues[e][i] + "\t");
			}
			System.out.println();
		}

		/*
		 * Versuche mit IO
		 */
		
		// Lese Binder/Nichtbinder ein
		ExampleReader exampleReader = new ExampleReader();
		ArrayList<String> binderPeptides = exampleReader.getSequnces("data/positive.txt");
		ArrayList<String> nonbinderPeptides = exampleReader.getSequnces("data/negative.txt");
		
		System.out.println("\npositives\tnegatives");
		System.out.println("---------\t---------\n");
		for (int i = 0; i < binderPeptides.size(); i ++)
		{
			System.out.println(binderPeptides.get(i)  + "\t" + nonbinderPeptides.get(i));
		}
		
		/*
		 * Splitting Tests
		 */
		
		//DataSplit dS = new DataSplit(binderPeptides, 6);
		//ArrayList<ArrayList<String>> testDataSet = dS.getDataSet();
		
		/*
		 * Versuche mit WEKA
		 */
		System.out.println("\n\nTestausgabe von WEKA Arbeiten:\n");
		
		ARFFFileGenerator arff = new ARFFFileGenerator();

		Instances dataSet = arff.createARFFFile(binderPeptides, nonbinderPeptides, selectedEncodings);
		
		// schreibe ARFF Datei
		arff.writeARFFFile("data/allEncodings.arff", dataSet);
		
		System.out.println(dataSet);
		
		// Feature Filtering
		dataSet.setClass(dataSet.attribute("activator"));
		FeatureFilter fFilter = new FeatureFilter();
		
		System.out.println("\nFeature Ranking:\n");
		int[] ranking = fFilter.rankFeatures(dataSet);
		
		for (int i = 0; i < ranking.length; i++)
		{
			System.out.println(ranking[i]);
		}
		
		// Wir verwenden hier jetzt kein LibSVM, das dauert zu lange
		weka.classifiers.functions.SMO sMO = new weka.classifiers.functions.SMO();
		
		// GridSearch
		weka.classifiers.meta.GridSearch gridSearch = new weka.classifiers.meta.GridSearch();
		
	}

}
