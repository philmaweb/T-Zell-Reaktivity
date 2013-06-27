package weka;

import java.util.ArrayList;

import weka.classifiers.Classifier;

public class ModelCollection 
{
	public ArrayList<Classifier> bestClassifiers = new ArrayList<Classifier>();
	public ArrayList<Evaluator> evalsOfBestClassifiers = new ArrayList<Evaluator>();
	public ArrayList<Integer> listOfNumberOfAttributes = new ArrayList<Integer>();
	public ArrayList<FeatureFilter> listOfFeatureFilters = new ArrayList<FeatureFilter>();
}
