package starting;

import io.ExampleReader;
import io.StatisticOutput;

import java.util.ArrayList;
import java.util.Date;


import weka.ARFFFileGenerator;
import weka.Evaluator;
import weka.FeatureFilter;
import weka.ModelCollection;
import weka.ModelSelection;
import weka.ParameterOptimization;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.GridSearch;
import weka.core.Instances;
import weka.core.SerializationHelper;


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
		// Konfiguration
		Training training = new Training();
		int numberOfAttributes = 18;
		StatisticOutput statisticWriter = new StatisticOutput("data/statistics.txt");
		
		training.printMessage("*** TCR-Predictor: Training ***");
		
		training.printMessage("Datenbank von Aminosäure-Codierungen wird eingelesen");
		// Lies die EncodingDB ein
		AAEncodingFileReader aa = new AAEncodingFileReader();
		AAEncodingDatabase db = aa.readAAEncodings("data/AAEncodings.txt");
		training.printMessage("Es wurden " + db.getEncodingDatabase().size() + " Codierungen einglesen");
		
		training.printMessage("Trainingsdatensatz wird eingelesen und prozessiert");
		// Lies zunächst die gesamten Trainingsdaten ein
		ExampleReader exampleReader = new ExampleReader();
		
		// Spalte das Datenset
		DataSplit dataSplit_positives = new DataSplit(exampleReader.getSequnces("data/positive.txt"), 5);
		ArrayList<ArrayList<String>> complete_list_positiv = dataSplit_positives.getDataSet();
		
		DataSplit dataSplit_negatives = new DataSplit(exampleReader.getSequnces("data/negative.txt"), 5);
		ArrayList<ArrayList<String>> complete_list_negativ = dataSplit_negatives.getDataSet();
		
		// Lege Listen für die besten Klassifizierer und deren Evaluation an
		ModelCollection modelCollection = new ModelCollection();
		
		/*
		 * 
		 * Beginne Feature Selection
		 * 
		 */
		ArrayList<String> positivesForFeatureSelection = training.concatenateLists(complete_list_positiv);
		ArrayList<String> negativesForFeatureSelection = training.concatenateLists(complete_list_negativ);
		
		training.printMessage("Convertiere Daten ins Weka ARFF Format");		
		// Convertiere Daten in Wekas File Format
		ARFFFileGenerator arff = new ARFFFileGenerator();
		Instances dataSet = arff.createARFFFile(positivesForFeatureSelection, negativesForFeatureSelection, db.getEncodingDatabase());
		dataSet.setClass(dataSet.attribute("activator"));			// Lege das nominale Attribut fest, wonach klassifiziert wird
		dataSet.deleteStringAttributes(); 							// Entferne String-Attribute
		
		training.printMessage("Führe Feature Selection (Filtering) aus");
		// Beginne Feature Selection
		FeatureFilter featureFilter = new FeatureFilter();
		featureFilter.rankFeatures(dataSet, numberOfAttributes);					// Wähle die x wichtigsten Features aus
		dataSet = featureFilter.getProcessedInstances();
		training.printMessage("Ausgewählte Features: " + featureFilter.getTopResults());

		/*
		 * Führe die äußere Evaluierung fünfmal durch und wähle das beste Modell
		 */
		for (int outer_run = 0; outer_run < 5; outer_run++)
		{
			statisticWriter.writeString("===== Äußere Evaluation " + (outer_run + 1) + "/5 =====\n\n");
			
			
			ArrayList<ArrayList<String>> list_positives = new ArrayList<ArrayList<String>>();
			list_positives.addAll(complete_list_positiv);
			
			ArrayList<ArrayList<String>> list_negatives = new ArrayList<ArrayList<String>>();
			list_negatives.addAll(complete_list_negativ);
			
			// Lege das erste Fragment beider Listen für nasted-Crossvalidation beiseite
			ArrayList<String> outer_List_pos = new ArrayList<String>();
			outer_List_pos.addAll(list_positives.get(outer_run));
			list_positives.remove(outer_run);
			
			ArrayList<String> outer_List_neg = new ArrayList<String>();
			outer_List_neg.addAll(list_negatives.get(outer_run));
			list_negatives.remove(outer_run);
			
			// Füge die verbleibende Liste zu einer Zusammen
			ArrayList<String> inner_List_pos = training.concatenateLists(list_positives);
			ArrayList<String> inner_List_neg = training.concatenateLists(list_negatives);
				

			/*
			 * 
			 * Ab hier nur noch Arbeiten mit innerer Liste, die Daten zum Evaluieren bekommt Weka vorerst 
			 * nicht zu sehen!
			 * 
			 */
			training.printMessage("Convertiere Daten ins Weka ARFF Format");		
			// Convertiere Daten in Wekas File Format
			arff = new ARFFFileGenerator();
			dataSet = arff.createARFFFile(inner_List_pos, inner_List_neg, db.getEncodingDatabase());
			dataSet.setClass(dataSet.attribute("activator"));			// Lege das nominale Attribut fest, wonach klassifiziert wird
			dataSet.deleteStringAttributes(); 							// Entferne String-Attribute
			
			featureFilter.processInstances(featureFilter.getRanking(), dataSet, numberOfAttributes); // Filtere das innere Datenset nach Vorgabe
			dataSet = featureFilter.getProcessedInstances();
			
			training.printMessage("Beginne Gridsearch");
			// Gridsearch starten
	
			
			
			ParameterOptimization optimizer = new ParameterOptimization();
			String logFileName = outer_run + "_" + numberOfAttributes;
			GridSearch gridSearch = optimizer.performGridSearch(dataSet, logFileName);
			training.printMessage("Gefundene Parameter [C, gamma]: " + gridSearch.getValues()); // liefert unter diesen Settings 1.0 und 0.0

			SMO sMO = (SMO)gridSearch.getBestClassifier();
							
			/*
			 * 
			 * Evaluationsbeginn 
			 *
			 */
			training.printMessage("Evaluiere die Performance gegen das äußere Datenset");
			training.printMessage("Transcodierung des Evaluationsdatensatzes");
			arff = new ARFFFileGenerator();
			dataSet = arff.createARFFFile(outer_List_pos, outer_List_neg, db.getEncodingDatabase());
			dataSet.setClass(dataSet.attribute("activator"));			// Lege das nominale Attribut fest, wonach klassifiziert wird
			dataSet.deleteStringAttributes(); 							// Entferne String-Attribute
			
			// Führe Feature-Filtering mit den Einstellungen der GridSearch aus
			training.printMessage("Führe Feature Selection (Filtering) auf GridSearch-Basis aus");
			// Beginne Feature Selection
			featureFilter.processInstances(featureFilter.getRanking(), dataSet, numberOfAttributes);	 // Wähle die x wichtigsten Features aus
			dataSet = featureFilter.getProcessedInstances();
			
			
			
			training.printMessage("Ermittle Performance");
			Evaluator eval = new Evaluator();
			eval.classifyDataSet(sMO, dataSet);
			training.printMessage(eval.printRawData());
			
			/*
			 * Füge das Modell und die externe Evaulation zur Sammlung hinzu
			 */			
			modelCollection.bestClassifiers.add(sMO);
			modelCollection.evalsOfBestClassifiers.add(eval);
			modelCollection.listOfNumberOfAttributes.add(numberOfAttributes);
			modelCollection.listOfFeatureFilters.add(featureFilter);
			
			statisticWriter.writeString("Verwendete Attribute: " + featureFilter.getTopResults());
			statisticWriter.writeString(eval.printRawData());
			
		}
		statisticWriter.close();
		
		// Wähle das beste aller Modelle aus
		training.printMessage("Ermittle die allgemein besten Einstellungen");
		ModelSelection modelSelection = new ModelSelection();
		modelSelection.calculateBestModel(modelCollection);
		
		training.printMessage("Das beste Model: ");
		training.printMessage(modelSelection.getBestEvaluator().printRawData());
		System.out.println("------ SMO ------");
		for (int i = 0; i < modelSelection.getBestClassifier().getOptions().length; i++)
		{
			System.out.print(modelSelection.getBestClassifier().getOptions()[i] + " ");
		}
		System.out.println("\n--- Features ---");
		System.out.println(modelSelection.getBestListOfFeatures().getTopResults());
		
		// Schreibe das Modell in eine Datei
		training.printMessage("Das beste Modell wird auf Festplatte geschrieben");
		try
		{
			SerializationHelper.write("data/bestPredictor.model", modelSelection.getBestClassifier());
			SerializationHelper.write("data/ranking.filter", modelSelection.getBestListOfFeatures().getRanking());
			SerializationHelper.write("data/components.i", (modelSelection.getBestListOfFeatures().getProcessedInstances().numAttributes()-1));
		}
		catch (Exception ex)
		{
			System.err.println("Fehler beim Schreiben des Modells auf Festplatte: " + ex);
		}
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
