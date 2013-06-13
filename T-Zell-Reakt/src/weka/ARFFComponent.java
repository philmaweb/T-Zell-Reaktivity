package weka;

public class ARFFComponent
{
	private Double[] aaIndices;
	private String peptideSequence;
	
	public ARFFComponent()
	{
		
	}
	
	public ARFFComponent(String pepSeq, Double[] aaI)
	{
		this.aaIndices = aaI;
		this.peptideSequence = pepSeq;
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
