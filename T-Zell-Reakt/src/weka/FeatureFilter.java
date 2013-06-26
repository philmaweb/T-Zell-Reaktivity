package weka;

import weka.attributeSelection.*;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class FeatureFilter 
{
	private Instances processedInstances;
	private InfoGainAttributeEval infoGainAttributeEval;
	private Ranker searchRanker;
	private String stringRanking = "";
	private int[] iRanking;
	
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
		ranker.setNumToSelect(-1);						// -1 gib gesamtes Ranking aus
		ranker.setThreshold(-1.7976931348623157E308);	
		
		try
		{
			infoGain.buildEvaluator(instances);
			this.iRanking = ranker.search(infoGain, instances);
			this.infoGainAttributeEval = infoGain;
			this.searchRanker = ranker;
			processInstances(this.iRanking, instances, numToSelect);
			
			return this.iRanking;
		}
		catch(Exception ex)
		{
			System.err.println(ex);
		}

		return null;		
	}
	
	public void processInstances(int[] ranking, Instances instances, int k)
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
			
			for (int i = 0; i < k; i++)
			{
				this.stringRanking += processedInstances.attribute(i).name() + " ";
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
	
	public String getTopResults()
	{
		return this.stringRanking;
	}
	
	public int[] getRanking()
	{
		return this.iRanking;
	}
}
