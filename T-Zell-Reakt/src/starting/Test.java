package starting;

// Feature-Set Klassen
import java.util.ArrayList;

import aaEncoding.AAEncoder;
import aaEncoding.AAEncodingDatabase;
import aaEncoding.AAEncodingFileReader;
import weka.ARFFComponent;
import weka.ARFFFile;

// Weka
import weka.core.Instances;


public class Test {

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
		Double[] encValues = encoder.getEncodedString(db.getEncodingDatabase().get(0), peptideSequence);
		for (int i = 0; i < 9; i++)
		{
			System.out.print(encValues[i] + " ");
		}
		System.out.println("\n\nTestausgabe von WEKA Arbeiten:\n");
		
		/*
		 * Versuche mit WEKA
		 */
		ARFFFile arffFile = new ARFFFile();
		
		// generiere für ein paar Peptidsequenzen ein Dataset	
		ArrayList<ARFFComponent> komponenten = new ArrayList<ARFFComponent>();
		
		String peptide = "KMFPSTWYV";
		komponenten.add(new ARFFComponent(peptide, encoder.getEncodedString(db.getEncodingDatabase().get(0), peptide)));
	
		peptide = "ARNDCQEGH";
		komponenten.add(new ARFFComponent(peptide, encoder.getEncodedString(db.getEncodingDatabase().get(0), peptide)));
		
		peptide = "EGHILKMFP";
		komponenten.add(new ARFFComponent(peptide, encoder.getEncodedString(db.getEncodingDatabase().get(0), peptide)));
		
		Instances dataSet = arffFile.createInstances(komponenten);
		
		System.out.println(dataSet);
		
	}

}
