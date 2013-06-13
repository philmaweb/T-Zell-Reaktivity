package weka;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ARFFFile 
{
	private FastVector createFastVectorTemplate()
	{
		// Peptidsequenz - AA0 AA1 AA2 ... AA8
		// Lege neue Attribute an
		FastVector attributes = new FastVector();
		
		/*
		 * ELEMENT 1
		 */
		// Das erste Attribut ist die Peptidsequenz
		FastVector sequence = new FastVector();
		sequence.addElement("PeptidSequenz");
		
		// füge das erste Attribut der Attributeliste hinzu (Workaround für Peptidsequenz als String)
		attributes.addElement(new Attribute("PeptidSequenz", (FastVector)null));
		
		// weitere Attribute sind dann Double
		for (int i = 0; i < 9; i++)
		{
			attributes.addElement(new Attribute("AA" + i));
		}
		
		// gib Attribute zurück
		return attributes;
	}
	
	
	public Instances createInstances (ArrayList<ARFFComponent> ARFFComponents)
	{
		Instances dataSet = new Instances("DataSetName", createFastVectorTemplate(), 0);

		for (int a = 0; a < ARFFComponents.size(); a++)
		{
			// Anzal der Attribute einer Instance aus Instances entspricht den zuvor festgelegten Parametern
			double[] values = new double[dataSet.numAttributes()];
			
			// ... fülle mit 1-Letter-Code der Peptidsequenz
			values[0] = dataSet.attribute(0).addStringValue(ARFFComponents.get(a).getPeptideSequence());
			
			// ... fülle mit den AAIndices die für diese Peptidsequenz ermittelt wurden
			for (int i = 1; i < 10; i++)
			{
				values[i] = ARFFComponents.get(a).getAaIndices()[i-1];
			}
			
			// füge den Instances (= Dataset) eine Instance (= Dateneintrag) hinzu, Gewichtung 1.0
			dataSet.add(new Instance(1.0, values));
		}
		
		return dataSet;
	}
}
