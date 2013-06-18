package starting;

// Feature-Set Klassen
import io.ExampleReader;

import java.util.ArrayList;

import aaEncoding.AAEncoder;
import aaEncoding.AAEncoding;
import aaEncoding.AAEncodingDatabase;
import aaEncoding.AAEncodingFileReader;
import weka.ARFFComponent;
import weka.ARFFDataSet;

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
		
		// wähle ein paar Encodings aus
		ArrayList<AAEncoding> selectedEncodings = new ArrayList<AAEncoding>();
		selectedEncodings.add(db.getEncodingDatabase().get(0));
		//selectedEncodings.add(db.getEncodingDatabase().get(1));
		//selectedEncodings.add(db.getEncodingDatabase().get(2));
		
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
		 * Versuche mit WEKA
		 */
		System.out.println("\n\nTestausgabe von WEKA Arbeiten:\n");
		
		ARFFDataSet arffFile = new ARFFDataSet();
		
		// generiere für ein paar Peptidsequenzen ein Dataset	
		ArrayList<ARFFComponent> komponenten = new ArrayList<ARFFComponent>();
		

		for (int i = 0; i < nonbinderPeptides.size(); i++)
		{
			String peptide = binderPeptides.get(i);
			komponenten.add(new ARFFComponent(peptide, encoder.getEncodedString(selectedEncodings, peptide), false));
		}
		
		Instances dataSet = arffFile.createInstances(komponenten);
		
		System.out.println(dataSet);
		
		// Wir verwenden hier jetzt kein LibSVM, das dauert zu lange
		weka.classifiers.functions.SMO sMO = new weka.classifiers.functions.SMO();
		
		// GridSearch
		weka.classifiers.meta.GridSearch gridSearch = new weka.classifiers.meta.GridSearch();
		
	}

}
