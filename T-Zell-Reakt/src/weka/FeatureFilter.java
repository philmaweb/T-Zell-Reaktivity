package weka;

import weka.attributeSelection.*;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class FeatureFilter 
{
	Instances processedInstances;
	InfoGainAttributeEval infoGainAttributeEval;
	Ranker	searchRanker;
	
	public int[] rankFeatures(Instances instances, int numToSelect)
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
		ranker.setNumToSelect(-1);				// -1 gib gesamtes Ranking aus
		ranker.setThreshold(-1.7976931348623157E308);	
		
		try
		{
			infoGain.buildEvaluator(instances);
			int[] result = ranker.search(infoGain, instances);
			this.infoGainAttributeEval = infoGain;
			this.searchRanker = ranker;
			processInstances(result, instances, numToSelect);
			
			return result;
		}
		catch(Exception ex)
		{
			System.err.println(ex);
		}

		return null;		
	}
	
	private void processInstances(int[] ranking, Instances instances, int k)
	{
		try
		{
			// insbesondere nicht, wenn k = -1
			if (k > 0)
			{
				// Indices der Attribute, die als nicht wichtig erachtet werden
				int[] indicesToRemove = new int[ranking.length - k];
				
				int iPosition = 0;
				for (int i = 0; i < ranking.length; i++)
				{
					if (i > k - 1)
					{
						indicesToRemove[iPosition] = ranking[i];
						iPosition++;
					}
				}
				
				// Lege einen Remove-Filter an
				Remove rmv = new Remove();
				rmv.setAttributeIndicesArray(indicesToRemove);
				rmv.setInputFormat(instances);
				
				this.processedInstances = Filter.useFilter(instances, rmv);
			}
			else
			{
				this.processedInstances = instances;
			}
		}
		catch (Exception ex)
		{
			System.err.println(ex);
		}
	}
	
	public InfoGainAttributeEval getInfoGain ()
	{
		return this.infoGainAttributeEval;
	}
	
	public Instances getProcessedInstances ()
	{
		return this.processedInstances;
	}
	
	public Ranker getRanker()
	{
		return this.searchRanker;
	}
}
