package starting;

	import io.ExampleReader;

import java.util.ArrayList;

import aaEncoding.AAEncoder;
import aaEncoding.AAEncoding;
import aaEncoding.AAEncodingDatabase;
import aaEncoding.AAEncodingFileReader;

import weka.ARFFComponent;
import weka.ARFFDataSet;
import weka.ARFFFileGenerator;
import weka.classifiers.*;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.core.Instance;
import weka.core.Instances;

	
public class TestPhilipp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
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
				selectedEncodings.add(db.getEncodingDatabase().get(1));
				selectedEncodings.add(db.getEncodingDatabase().get(2));
				
				Double[][] encValues = encoder.getEncodedString(selectedEncodings, peptideSequence);
//				for (int e = 0; e < selectedEncodings.size(); e++)
//				{
//					for (int i = 0; i < 9; i++)
//					{
//						System.out.print(encValues[e][i] + "\t");
//					}
//					System.out.println();
//				}

				/*
				 * Versuche mit IO
				 */
				
				// Lese Binder/Nichtbinder ein
				ExampleReader exampleReader = new ExampleReader();
				ArrayList<String> binderPeptides = exampleReader.getSequnces("data/positive.txt");
				ArrayList<String> nonbinderPeptides = exampleReader.getSequnces("data/negative.txt");
				
//				System.out.println("\npositives\tnegatives");
//				System.out.println("---------\t---------\n");
//				for (int i = 0; i < binderPeptides.size(); i ++)
//				{
//					System.out.println(binderPeptides.get(i)  + "\t" + nonbinderPeptides.get(i));
//				}
				
				/*
				 * Versuche mit WEKA
				 */
//				System.out.println("\n\nTestausgabe von WEKA Arbeiten:\n");
				
				ARFFFileGenerator arff = new ARFFFileGenerator();

				Instances dataSet = arff.createARFFFile(binderPeptides, nonbinderPeptides, selectedEncodings);
				
				System.out.println(dataSet);
				
				weka.classifiers.functions.SMO sMO = new weka.classifiers.functions.SMO();
				
				//entfernen der StringAttributes, da sMO diese nicht brauchen kann
				dataSet.deleteStringAttributes();
//				dataSet.setClass(att)
				
				//weka.classifiers.functions.SMO -C 2.2 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K ;
				//setzen der variablen gemäß Versuch von Johannes:
				sMO.setC(2.2);
				sMO.setEpsilon(1.0E-12);

				//polyKernel
//				weka.classifiers.functions.supportVector.PolyKernel polyKernel = new weka.classifiers.functions.supportVector.PolyKernel();
//				polyKernel.setCacheSize(250007);
//				polyKernel.setExponent(1);
				
				//rbfKernel
				weka.classifiers.functions.supportVector.RBFKernel rBFKernel = new weka.classifiers.functions.supportVector.RBFKernel();
				rBFKernel.setCacheSize(250007);
				rBFKernel.setGamma(0.05);
				
				sMO.setKernel(rBFKernel);
				sMO.setNumFolds(-1);
				sMO.setRandomSeed(1);
				sMO.setToleranceParameter(0.001);
				try {
					sMO.buildClassifier(dataSet);
				} catch (Exception e) {
					System.out.println("Building sMO classifier failed");
					e.printStackTrace();
				}
				
			
				

				

		
		}

}
