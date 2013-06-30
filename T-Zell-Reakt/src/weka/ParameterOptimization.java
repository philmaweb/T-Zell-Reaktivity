package weka;

import io.StatisticOutputProcessor;

import java.awt.geom.Point2D;
import java.io.File;

import configuration.Names;

import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.meta.GridSearch;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.AllFilter;

public class ParameterOptimization 
{
	public GridSearch performGridSearch(Instances dataSet, String logname)
	{
		
		GridSearch gridSearch;
		try
		{
			// Erste Greedy Stufe
			double[] vals = new double[6];
			vals[0] = 0.5; 			// 0.5
			vals[1] = 25; 			// 25
			vals[2] = 0.5;			//0.5
			vals[3] = -15;
			vals[4] = 1;
			vals[5] = 0.5;			// 0.5
			gridSearch = setUpGridSearch(dataSet, logname + ".0", vals);
			gridSearch.buildClassifier(dataSet);
			StatisticOutputProcessor.createProcessedOutput(logname + ".0");
			
			// Zweite Greedy Stufe
			Point2D bestParameters = (Point2D)gridSearch.getValues();
			vals[0] = Math.round((bestParameters.getX() * 100))/100. - 0.25;
			vals[1] = Math.round((bestParameters.getX() * 100))/100. + 0.25;
			vals[2] = 0.01;
			vals[3] = Math.round((bestParameters.getY() * 100))/100. - 0.25;
			vals[4] = Math.round((bestParameters.getY() * 100))/100. + 0.25;
			vals[5] = 0.01;
			gridSearch = setUpGridSearch(dataSet, logname + ".1", vals);
			gridSearch.buildClassifier(dataSet);
			StatisticOutputProcessor.createProcessedOutput(logname + ".1");
	
			
			return gridSearch;
		}
		catch (Exception ex)
		{
			System.err.println("Fehler bei der Durchführung der GridSearch!\n" + ex);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param sMO
	 * @param gridSearch
	 * @param dataSet
	 * @param logname
	 * @param vals 0: XMin, 1: XMax, 2: XStep, 3: YMin, 4: YMax, 5: YStep
	 * @return
	 */
	private GridSearch setUpGridSearch(Instances dataSet, String logname, double[] vals)
	{
		GridSearch gridSearch = new GridSearch();
		
		Kernel kernel = KernelFactory.createKernel(Names.KernelTypes.RBF_KERNEL);
		SupportVectorMachine svm = new SupportVectorMachine();
		SMO sMO = svm.createSMO(kernel, dataSet);
		
		// setze Parameter für GridSearch
		gridSearch.setEvaluation(new SelectedTag(GridSearch.EVALUATION_ACC, GridSearch.TAGS_EVALUATION));
		gridSearch.setGridIsExtendable(false);						// erweitere automatisch das Grid, falls Top-Werte am Rand, wirft nur Fehler
		gridSearch.setFilter(new AllFilter());						// Filtere hier nichts mehr, da Featureselection zuvor ausgeführt wurde
		gridSearch.setClassifier(sMO);
		gridSearch.setXProperty("classifier.c");
		gridSearch.setXMin(vals[0]);
		gridSearch.setXMax(vals[1]);
		gridSearch.setXStep(vals[2]);
		gridSearch.setXExpression("I"); 							// I testet auf Parameter C
		
		gridSearch.setYProperty("classifier.kernel.gamma");
		gridSearch.setYMin(vals[3]);
		gridSearch.setYMax(vals[4]);
		gridSearch.setYBase(10);									// Teste Parameter gamma zur angegebenen Basis, also 10^Y
		gridSearch.setYStep(vals[5]);		
		gridSearch.setYExpression("pow(BASE,I)");					// Testet auf gamma
		
		gridSearch.setLogFile(new File("gridSearchLogFiles/run_" + logname));
		
		return gridSearch;
	}
}
