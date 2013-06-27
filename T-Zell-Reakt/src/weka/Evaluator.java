package weka;

import java.text.DecimalFormat;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/***
 * Klasse zum Evaluieren auf dem ‰uﬂeren Datenset
 */
public class Evaluator 
{
	private int tP = 0;
	private int tN = 0;
	private int fP = 0;
	private int fN = 0;
	
	public void classifyDataSet(Classifier sMO, Instances dataSet)
	{
		for (int i = 0; i < dataSet.numInstances(); i++)
		{
			try
			{
				Instance inst = dataSet.instance(i);
				
				double val = sMO.classifyInstance(inst);
				// Hier kˆnnen nun 4 F‰lle unterschieden werden: TP, TN, FP, FN
				
				if (val == 0.0 && inst.classValue() == 0.0)
				{
					tN++;
				}
				else if (val == 1.0 && inst.classValue() == 1.0)
				{
					tP++;
				}
				else if (val == 1.0 && inst.classValue() == 0.0)
				{
					fP++;
				}
				else if (val == 0.0 && inst.classValue() == 1.0)
				{
					fN++;
				}
			}
			catch (Exception ex)
			{
				System.err.println("W‰hrend der Evaluierung trat ein Fehler auf: " + ex);
			}
			
		}
	}
	
	public void printRawData()
	{
		DecimalFormat dcf = new DecimalFormat();
		dcf.applyPattern("0.000");
		
		System.out.println("\n<---- Statistik ---->");
		System.out.println("TP: " + tP + ", TN: " + tN + ", FP: " + fP + ", FN: " + fN);
		System.out.println("ACC: " + dcf.format(getACC()));
		System.out.println("PPV: " + dcf.format(getPPV()));
		System.out.println("NPV: " + dcf.format(getNPV()));
		System.out.println("FDR: " + dcf.format(getFDR()));
		System.out.println("SPC: " + dcf.format(getSPC()));
		System.out.println("MCC: " + dcf.format(getMCC()));
		System.out.println("");
	}
	
	public double getACC()
	{
		return (double)(tP + tN)/(double)(tP + tN + fP + fN);
	}
	
	public double getPPV()
	{
		return (double)tP/(double)(tP + fP);
	}
	
	public double getNPV()
	{
		return (double)tN/(double)(tN + fN);
	}
	
	public double getFDR()
	{
		return (double)fP/(double)(fP + tP);
	}
	
	public double getSPC()
	{
		return (double)tN/(double)(fP + tN);
	}
	
	public double getMCC()
	{
		return (double)((tP * tN)-(fP*fN))/Math.sqrt((tP+fP)*(tP+fN)*(tN+fP)*(tN+fN));
	}
	
	
}
