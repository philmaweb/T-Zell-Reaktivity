package weka;

import weka.classifiers.functions.*;
import weka.classifiers.functions.supportVector.*;
import weka.core.*;

public class SupportVectorMachine 
{
	// Standards
	private int numFolds = -1;
	private int randomSeed = 1;
	private double toleranceParameter = 0.001;
	private double c = 2.2;
	private double epsilon = 1.0E-12;
	
	public SMO createSMO(Kernel kernel, Instances dataSet)
	{
		SMO sMO = new SMO();
		
		sMO.setKernel(kernel);
		sMO.setNumFolds(this.numFolds);
		sMO.setRandomSeed(this.randomSeed);
		sMO.setToleranceParameter(this.toleranceParameter);
		
		try 
		{
			sMO.buildClassifier(dataSet);
		} 
		catch (Exception e) 
		{
			System.err.println("Building sMO classifier failed");
			e.printStackTrace();
		}
		
		return sMO;
	}
}