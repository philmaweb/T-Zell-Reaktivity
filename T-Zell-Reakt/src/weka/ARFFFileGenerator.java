package weka;

import java.util.ArrayList;

import aaEncoding.AAEncoder;
import aaEncoding.AAEncoding;
import weka.core.Instances;

public class ARFFFileGenerator 
{
	public Instances createARFFFile (ArrayList<String> activators, ArrayList<String> nonActivators, ArrayList<AAEncoding> selectedEncodings)
	{
		// Das eigentlich Datenset
		ARFFDataSet arffFile = new ARFFDataSet();
		AAEncoder encoder = new AAEncoder();
		
		// generiere für ein paar Peptidsequenzen ein Dataset	
		ArrayList<ARFFComponent> komponenten = new ArrayList<ARFFComponent>();
		
		if (activators != null)
		{
			for (int i = 0; i < activators.size(); i++)
			{
				String peptide = activators.get(i);
				komponenten.add(new ARFFComponent(peptide, encoder.getEncodedString(selectedEncodings, peptide), true));
			}
		}
		if (nonActivators != null)
		{
			for (int i = 0; i < nonActivators.size(); i++)
			{
				String peptide = nonActivators.get(i);
				komponenten.add(new ARFFComponent(peptide, encoder.getEncodedString(selectedEncodings, peptide), false));
			}
		}
		
		return arffFile.createInstances(komponenten);
	}
}
