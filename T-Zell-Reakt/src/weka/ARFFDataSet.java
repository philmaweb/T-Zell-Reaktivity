package weka;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ARFFDataSet 
{
	private int dimensions;
	
	private FastVector createFastVectorTemplate(int dim)
	{
		this.dimensions = dim;
		// Peptidsequenz - AA0 AA1 AA2 ... AA8
		// Lege neue Attribute an
		FastVector attributes = new FastVector();
		
		// Das erste Attribut ist die Peptidsequenz
		FastVector sequence = new FastVector();
		sequence.addElement("PeptidSequenz");
		
		// füge das erste Attribut der Attributeliste hinzu (Workaround für Peptidsequenz als String)
		attributes.addElement(new Attribute("PeptidSequenz", (FastVector)null));
		
		// weitere Attribute sind dann Double
		for (int i = 0; i < (9 * dimensions); i++)
		{
			attributes.addElement(new Attribute("AA" + i));
		}
		// füge das Attribut für Z-Zell aktivierend hinzu
		FastVector activatorFV = new FastVector();
		activatorFV.addElement("0");
		activatorFV.addElement("1");
		
		attributes.addElement(new Attribute("activator", activatorFV));

		// gib Attribute zurück
		return attributes;
	}
	
	
	public Instances createInstances (ArrayList<ARFFComponent> aRFFComponents)
	{
		this.dimensions = aRFFComponents.get(0).getDim();
		
		Instances dataSet = new Instances("DataSetName", createFastVectorTemplate(this.dimensions), 0);

		for (int a = 0; a < aRFFComponents.size(); a++)
		{
			// Anzal der Attribute einer Instance aus Instances entspricht den zuvor festgelegten Parametern
			double[] values = new double[dataSet.numAttributes()];
			
			// ... fülle mit 1-Letter-Code der Peptidsequenz
			values[0] = dataSet.attribute(0).addStringValue(aRFFComponents.get(a).getPeptideSequence());
			
			// ... fülle mit den AAIndices die für diese Peptidsequenz ermittelt wurden
			for (int i = 1; i < (9 * this.dimensions + 1); i++)
			{
				values[i] = aRFFComponents.get(a).getAaIndices()[i-1];
			}
			// Setze Wert für Binder/Nicht-Binder
			values[dataSet.numAttributes()-1] = aRFFComponents.get(a).getBinder();
			
			// füge den Instances (= Dataset) eine Instance (= Dateneintrag) hinzu, Gewichtung 1.0
			dataSet.add(new Instance(1.0, values));
		}
		
		return dataSet;
	}
}
