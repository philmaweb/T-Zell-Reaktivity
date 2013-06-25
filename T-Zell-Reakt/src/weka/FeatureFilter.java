package weka;

import weka.attributeSelection.*;
import weka.core.Instances;

public class FeatureFilter 
{
	public int[] rankFeatures(Instances instances)
	{
		// Preprocessing
		instances.deleteStringAttributes();				// entferne String-Attribute
		
		// Attribute Evaulator
		InfoGainAttributeEval infoGain = new InfoGainAttributeEval();
		infoGain.setBinarizeNumericAttributes(false);
		infoGain.setMissingMerge(true);
		
		// Search Method
		Ranker ranker = new Ranker();
		ranker.setGenerateRanking(true);
		ranker.setNumToSelect(-1); 						// gib gesamtes Ranking aus
		ranker.setThreshold(-1.7976931348623157E308);	
		
		try
		{
			infoGain.buildEvaluator(instances);
			
			return ranker.search(infoGain, instances);
		}
		catch(Exception ex)
		{
			System.err.println(ex);
		}

		return null;		
	}
}
