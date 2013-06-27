package starting;

import io.ExampleReader;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;

import configuration.Names;

import weka.ARFFFileGenerator;
import weka.Evaluator;
import weka.FeatureFilter;
import weka.KernelFactory;
import weka.ModelCollection;
import weka.ModelSelection;
import weka.ParameterOptimization;
import weka.SupportVectorMachine;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.meta.GridSearch;
import weka.core.Instances;


import crossValidation.DataSplit;

import aaEncoding.AAEncodingDatabase;
import aaEncoding.AAEncodingFileReader;

public class Training 
{
	/**
	 * Einstiegspunkt zum Trainieren der SVM
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// Konfiguration
		Training training = new Training();
		int numberOfMaxAttributes = 9*9;
		
		training.printMessage("*** TCR-Predictor: Training ***");
		
		training.printMessage("Datenbank von Aminosäure-Codierungen wird eingelesen");
		// Lies die EncodingDB ein
		AAEncodingFileReader aa = new AAEncodingFileReader();
		AAEncodingDatabase db = aa.readAAEncodings("data/AAEncodings_complete.txt");
		training.printMessage("Es wurden " + db.getEncodingDatabase().size() + " Codierunge einglesen");
		
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
		 * Führe die äußere Evaluierung fünfmal durch und wähle das beste Modell
		 */
		for (int outer_run = 0; outer_run < 5; outer_run++)
		{
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
			 * Ermittle den Einfluss der Attributanzahl
			 * 
			 * 
			 */
			for (int numberOfAttributes = 9; numberOfAttributes <= numberOfMaxAttributes; numberOfAttributes += 9)
			{
				/*
				 * 
				 * Ab hier nur noch Arbeiten mit innerer Liste, die Daten zum Evaluieren bekommt Weka vorerst 
				 * nicht zu sehen!
				 * 
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
				featureFilter.rankFeatures(dataSet, numberOfAttributes);					// Wähle die x wichtigsten Features aus
				dataSet = featureFilter.getProcessedInstances();
				training.printMessage("Ausgewählte Features: " + featureFilter.getTopResults());
				
				training.printMessage("Beginne Gridsearch");
				// Gridsearch starten
		
				Kernel kernel = KernelFactory.createKernel(Names.KernelTypes.RBF_KERNEL);
				SupportVectorMachine svm = new SupportVectorMachine();
				SMO sMO = svm.createSMO(kernel, dataSet);
				
				ParameterOptimization optimizer = new ParameterOptimization();
				GridSearch gridSearch = optimizer.performGridSearch(sMO, dataSet);
				training.printMessage("Gefundene Parameter [C, gamma]: " + gridSearch.getValues()); // liefert unter diesen Settings 1.0 und 0.0
				Point2D bestParameters = (Point2D)gridSearch.getValues();							// speichert die besten Werte der GridSearch
	
				// setze die Parameter des Kernels und der SVM anhand der optimalen aus der GridSearch
				String[] kernelParameter = kernel.getOptions();
				kernelParameter[3] = String.valueOf(Math.pow(10, bestParameters.getY()));
				try 
				{
					kernel.setOptions(kernelParameter);
				} 
				catch (Exception ex) 
				{
					System.err.println("Fehler beim Einstellen der Kernelparameter" + ex);
				}
	
				// Setze die Parameter der SVM
				sMO.setC(bestParameters.getX());
	
				// Training der SVM mit gefundenen Parametern beginnen
				training.printMessage("Beginne die SVM mit den optimalen Einstellungen zu trainieren");
				try 
				{
					sMO.buildClassifier(dataSet);
				} 
				catch (Exception e) 
				{
					System.err.println("Fehler bei der Erzeugung des Modells: " + e);
				}
	
				//Classifier bestClassifier = gridSearch.getBestClassifier();
								
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
				eval.printRawData();
				
				/*
				 * Füge das Modell und die externe Evaulation zur Sammlung hinzu
				 */			
				modelCollection.bestClassifiers.add(sMO);
				modelCollection.evalsOfBestClassifiers.add(eval);
				modelCollection.listOfNumberOfAttributes.add(numberOfAttributes);
				modelCollection.listOfFeatureFilters.add(featureFilter);
			}
		}
		
		// Wähle das beste aller Modelle aus
		training.printMessage("Ermittle die allgemein besten Einstellungen");
		ModelSelection modelSelection = new ModelSelection();
		modelSelection.calculateBestModel(modelCollection);
		
		modelSelection.getBestEvaluator().printRawData();
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
