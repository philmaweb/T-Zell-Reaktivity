package starting;

import java.util.ArrayList;
import java.util.Date;

import weka.ARFFFileGenerator;
import weka.FeatureFilter;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import io.ExampleReader;
import io.PredictionWriter;
import aaEncoding.AAEncodingDatabase;
import aaEncoding.AAEncodingFileReader;


public class Prediction {

	/**
	 * Haupteinstiegspunkt für die Anwendung
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Prediction pred = new Prediction();
		pred.printMessage("*** TCR-Predictor: Vorhersage ***");
		
		
		//Modell deserialisieren
		try 
		{
			Classifier bestClassifier = (Classifier) SerializationHelper.read("data/bestPredictor.model");
			int[] iRanking = (int[]) SerializationHelper.read("data/ranking.filter");
			
			
			// Lies die EncodingDB ein
			pred.printMessage("Datenbank von Aminosäure-Codierungen wird eingelesen");
			AAEncodingFileReader aa = new AAEncodingFileReader();
			AAEncodingDatabase db = aa.readAAEncodings("data/AAEncodings.txt");
			pred.printMessage("Es wurden " + db.getEncodingDatabase().size() + " Codierungen einglesen");
			
			
			// Lies zunächst die gesamten vrherzusagenden Datensatz ein
			pred.printMessage("Zu vorhersagender Datensatz wird eingelesen und prozessiert");
			ExampleReader exampleReader = new ExampleReader();
			ArrayList<String> peptidesToPredict = exampleReader.getSequnces(args[0]);
			PredictionWriter predWriter = new PredictionWriter(args[1]);
			
			
			// Convertiere Daten in Wekas File Format
			pred.printMessage("Daten werd ins Weka ARFF Format konvertiert");
			ARFFFileGenerator arff = new ARFFFileGenerator();
			Instances dataSet = arff.createARFFFile(peptidesToPredict, null, db.getEncodingDatabase());
			dataSet.setClass(dataSet.attribute("activator"));			// Lege das nominale Attribut fest, wonach klassifiziert wird
			dataSet.deleteStringAttributes(); 							// Entferne String-Attribute
			
			
			// FeatureFilter aufbauen
			pred.printMessage("Filtere die Features nach Vorgabe");
			FeatureFilter featureFilter = new FeatureFilter();
			featureFilter.processInstances(iRanking, dataSet, 9);
			pred.printMessage("Ausgewählte Features: " + featureFilter.getTopResults());
			dataSet = featureFilter.getProcessedInstances();
			
			
			// Beginne Vorhersage für jedes Peptid
			for (int i = 0; i < peptidesToPredict.size(); i++)
			{
				Instance inst = dataSet.instance(i);
				double val = bestClassifier.classifyInstance(inst);
				if (val == 0.0)
				{
					predWriter.writePeptide(peptidesToPredict.get(i), 0);
				}
				else
				{
					predWriter.writePeptide(peptidesToPredict.get(i), 1);
				}
			}
			predWriter.close();		
		} 
		catch (Exception e) 
		{
			System.err.println("Fehler beim Einlesen des Modells: " + e);
		}	
	}
	
	private void printMessage(String string)
	{
		Date date = new Date();
		System.out.println(date + ": " + string + " ...");
	}

}
