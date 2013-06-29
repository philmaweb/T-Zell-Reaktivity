package weka;

import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import configuration.Names.KernelTypes;

public class KernelFactory 
{
	public static Kernel createKernel(KernelTypes kernelType)
	{
		switch (kernelType)
		{
			case RBF_KERNEL:
			{
				RBFKernel kernel = new RBFKernel();
				kernel.setCacheSize(0);
				kernel.setDebug(false);
				kernel.setChecksTurnedOff(true);
				return kernel;
			}
				
			case POLY_KERNEL:
				return new PolyKernel();
				
			default:
				System.err.println("Kein gültiger Kernel ausgewählt!");
				return null;
		}
	}
}
