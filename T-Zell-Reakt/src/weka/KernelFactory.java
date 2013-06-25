package weka;

import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import configuration.Names.KernelTypes;

public class KernelFactory 
{
	public static Kernel createKernel(KernelTypes kernelType) throws Exception
	{
		switch (kernelType)
		{
			case RBF_KERNEL:	
				return new RBFKernel();
				
			case POLY_KERNEL:
				return new PolyKernel();
				
			default:
				throw new Exception("Kein gültiger Kernel ausgewählt!");
		}
	}
}
