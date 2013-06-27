package weka;

import weka.classifiers.functions.SMO;
import weka.classifiers.meta.GridSearch;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.AllFilter;

public class ParameterOptimization 
{
	public GridSearch performGridSearch(SMO sMO, Instances dataSet)
	{
		GridSearch gridSearch = new GridSearch();
		
		// setze Parameter für GridSearch
		gridSearch.setEvaluation(new SelectedTag(GridSearch.EVALUATION_ACC, GridSearch.TAGS_EVALUATION));
		gridSearch.setGridIsExtendable(false);						// erweitere automatisch das Grid, falls Top-Werte am Rand
		gridSearch.setFilter(new AllFilter());						// Filtere hier nichts mehr, da Featureselection zuvor ausgeführt wurde
		gridSearch.setClassifier(sMO);
		gridSearch.setXProperty("classifier.c");
		gridSearch.setXMin(0);
		gridSearch.setXMax(20);
		gridSearch.setXStep(0.25);
		gridSearch.setXExpression("I"); 							// I testet auf Parameter C
		
		gridSearch.setYProperty("classifier.kernel.gamma");
		gridSearch.setYMin(-10);
		gridSearch.setYMax(1);
		gridSearch.setYBase(10);									// Teste Parameter gamma zur angegebenen Basis, also 10^Y
		gridSearch.setYStep(0.1);		
		gridSearch.setYExpression("pow(BASE,I)");					// Testet auf gamma
			
		try
		{
			gridSearch.buildClassifier(dataSet);	
		}
		catch (Exception ex)
		{
			System.err.println("Fehler bei der Durchführung der GridSearch!\n" + ex);
		}
		
		return gridSearch;
	}
}
