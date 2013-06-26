package starting;

import io.ExampleReader;

import java.util.ArrayList;
import java.util.Date;

import configuration.Names;

import weka.ARFFFileGenerator;
import weka.FeatureFilter;
import weka.KernelFactory;
import weka.ParameterOptimization;
import weka.SupportVectorMachine;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.meta.GridSearch;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.AllFilter;

import crossValidation.DataSplit;

import aaEncoding.AAEncodingDatabase;
import aaEncoding.AAEncodingFileReader;

public class Training 
{
	/**
	 * Einstiegspunkt zum Trainieren der SVM
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		Training training = new Training();
		
		training.printMessage("*** TCR-Predictor: Training ***");
		
		training.printMessage("Datenbank von Aminosäure-Codierungen wird eingelesen");
		// Lies die EncodingDB ein
		AAEncodingFileReader aa = new AAEncodingFileReader();
		AAEncodingDatabase db = aa.readAAEncodings("data/AAEncodings_complete.txt");
		
		training.printMessage("Trainingsdatensatz wird eingelesen und prozessiert");
		// Lies zunächst die gesamten Trainingsdaten ein
		ExampleReader exampleReader = new ExampleReader();
		
		// Spalte das Datenset
		DataSplit dataSplit_positives = new DataSplit(exampleReader.getSequnces("data/positive.txt"), 5);
		ArrayList<ArrayList<String>> list_positiv = dataSplit_positives.getDataSet();
		
		DataSplit dataSplit_negatives = new DataSplit(exampleReader.getSequnces("data/negative.txt"), 5);
		ArrayList<ArrayList<String>> list_negativ = dataSplit_negatives.getDataSet();
		
		// Lege das erste Fragment beider Listen für nasted-Crossvalidation beiseite
		ArrayList<String> outer_List_pos = new ArrayList<String>();
		outer_List_pos.addAll(list_positiv.get(0));
		list_positiv.remove(0);
		
		ArrayList<String> outer_List_neg = new ArrayList<String>();
		outer_List_neg.addAll(list_negativ.get(0));
		list_negativ.remove(0);
		
		// Füge die verbleibende Liste zu einer Zusammen
		ArrayList<String> inner_List_pos = training.concatenateLists(list_positiv);
		ArrayList<String> inner_List_neg = training.concatenateLists(list_negativ);
			
		/*
		 * Ab hier nur noch Arbeiten mit innerer Liste, die Daten zum Evaluieren bekommt Weka vorerst 
		 * nicht zu sehen!
		 */
		training.printMessage("Convertiere Daten ins Weka ARFF Format");		
		// Convertiere Daten in Wekas File Format
		ARFFFileGenerator arff = new ARFFFileGenerator();
		Instances dataSet = arff.createARFFFile(inner_List_pos, inner_List_neg, db.getEncodingDatabase());
		dataSet.setClass(dataSet.attribute("activator"));			// Lege das nominale Attribut fest, wonach klassifiziert wird
		dataSet.deleteStringAttributes(); 							// Entferne String-Attribute
		
		training.printMessage("Führe Feature Selection (Filtering) aus");
		// Beginne Feature Selection
		FeatureFilter featureFilter = new FeatureFilter();
		featureFilter.rankFeatures(dataSet, 27);					// Wähle die x wichtigsten Features aus
		dataSet = featureFilter.getProcessedInstances();
		training.printMessage("Ausgewählte Features: " + featureFilter.getTopResults());
		
		training.printMessage("Beginne Gridsearch");
		// Gridsearch starten

		Kernel kernel = KernelFactory.createKernel(Names.KernelTypes.RBF_KERNEL);
		SupportVectorMachine svm = new SupportVectorMachine();
		SMO sMO = svm.createSMO(kernel, dataSet);
		
		ParameterOptimization optimizer = new ParameterOptimization();
		GridSearch gridSearch = optimizer.performGridSearch(sMO, dataSet);
		training.printMessage("Gefundene Parameter C und gamma: " + gridSearch.getValues()); // liefert unter diesen Settings 28.0 und -3.0
		
		
		training.printMessage("Evaluiere die gefundenen Parameter gegen das äußere Datenset");
		// hier folgt nun die Evaluation...
		

		
		
	}
	
	private ArrayList<String> concatenateLists(ArrayList<ArrayList<String>> list)
	{
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++)
		{
			result.addAll(list.get(i));
		}	
		return result;
	}
	
	private void printMessage(String string)
	{
		Date date = new Date();
		System.out.println(date + ": " + string + " ...");
	}

}
