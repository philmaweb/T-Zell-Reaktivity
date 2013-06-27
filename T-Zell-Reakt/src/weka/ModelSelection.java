package weka;

import weka.classifiers.Classifier;

public class ModelSelection 
{
	private Classifier bestClassifier;
	private Evaluator bestEvaluator;
	private int bestNumberOfAttributes;
	private FeatureFilter bestListOfFeatures;
	
	public void calculateBestModel(ModelCollection mCollect)
	{
		double bestACC = -1;
		
		for (int i = 0; i < mCollect.bestClassifiers.size(); i++)
		{
			double tempACC = mCollect.evalsOfBestClassifiers.get(i).getACC();
			
			if (tempACC >= bestACC)
			{
				this.bestClassifier = mCollect.bestClassifiers.get(i);
				this.bestEvaluator = mCollect.evalsOfBestClassifiers.get(i);
				this.bestNumberOfAttributes = mCollect.listOfNumberOfAttributes.get(i);
				this.bestListOfFeatures = mCollect.listOfFeatureFilters.get(i);
			}
		}
	}

	public Classifier getBestClassifier() 
	{
		return bestClassifier;
	}

	public Evaluator getBestEvaluator() 
	{
		return bestEvaluator;
	}

	public int getBestNumberOfAttributes() 
	{
		return bestNumberOfAttributes;
	}

	public FeatureFilter getBestListOfFeatures() 
	{
		return bestListOfFeatures;
	}
	
	
}
