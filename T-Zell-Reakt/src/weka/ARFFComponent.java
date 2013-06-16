package weka;

public class ARFFComponent
{
	private Double[] aaIndices;
	private String peptideSequence;
	private int binder;
	private int dim;
	
	public ARFFComponent(int dimensions)
	{
		this.binder = 0;
		this.dim = dimensions;
		this.aaIndices = new Double[9*this.dim];
		this.peptideSequence = "";
	}
	
	public ARFFComponent(String pepSeq, Double[][] aaI, boolean activator)
	{
		// Codiere die Eigenschaft ob T-Zell-Aktivierend mit 1 oder 0 da innerhalb von ARFF alles als Zahl codiert ist
		if (activator)
		{
			binder = 1;
		}
		else
		{
			binder = 0;
		}
		
		this.dim = aaI.length;
		this.aaIndices = new Double[9 * this.dim];
		// Die Codierungen werden spaltenweise in ein Array geschrieben
		/*
		 * 				AA0 AA1 AA2 AA3 AA4 AA5 AA6 AA7 AA8
		 * Encoding1	a	a'							a'''''''
		 * Encoding2	b	b'							b'''''''
		 * ...
		 * EncodingN	z	z'							z'''''''
		 * 
		 * wird also zu: a b ... z a' b' ... z' ... z'''''''
		 */

		for (int e = 0; e < aaI.length; e++)
		{
			for (int a = 0; a < 9; a++)
			{
				this.aaIndices[(9*e) + a] = aaI[e][a];
			}
		}
		
		this.peptideSequence = pepSeq;
	}
		
	public int getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}
		
	public int getBinder() {
		return binder;
	}

	public void setBinder(int binder) {
		this.binder = binder;
	}

	public Double[] getAaIndices() 
	{
		return aaIndices;
	}
	public void setAaIndices(Double[] aaIndices) 
	{
		this.aaIndices = aaIndices;
	}
	public String getPeptideSequence() 
	{
		return peptideSequence;
	}
	public void setPeptideSequence(String peptideSequence) 
	{
		this.peptideSequence = peptideSequence;
	}
}
