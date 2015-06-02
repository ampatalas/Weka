package com.ampatalas.weka;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class ConfusionMatrix {
	
	int truePositive;
	int trueNegative;
	
	int falsePositive;
	int falseNegative;
	
	int[][] matrix = new int[2][2];
	
	public ConfusionMatrix() {
		trueNegative = 0;
		truePositive = 0;
		falseNegative = 0;
		falsePositive = 0;
	}
	
	public double rate(Instances instaces, Classifier classifier) {
		for (int i = 0; i < instaces.numInstances(); i++) {
			try {
				rate(instaces.get(i), classifier);
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		System.out.println(getMatrixString());
		System.out.println("TP: " + truePositiveRate());
		System.out.println("TN: " + trueNegativeRate());
		return GMean();
	}
	
	public void rate(Instance instance, Classifier classifier) throws Exception {
		double realValue = instance.classValue();
		double classifiedValue = classifier.classifyInstance(instance);
		
		if (realValue == classifiedValue) {
			if (realValue == 0) trueNegative++;
			else truePositive++;
		}
		else {
			if (classifiedValue == 0) falseNegative++;
			else falsePositive++;
		}
	}
	
	public int[][] getMatrix() {
		matrix[0][0] = truePositive;
		matrix[0][1] = falsePositive;
		matrix[1][0] = falseNegative;
		matrix[1][1] = trueNegative;
		return matrix;
	}
	
	public double truePositiveRate() {
		return (double)truePositive / (double)(truePositive + falseNegative);
	}
	
	public double trueNegativeRate() {
		return (double)trueNegative / (double)(trueNegative + falsePositive);
	}
	
	public double GMean() {
		return Math.sqrt(truePositiveRate() * trueNegativeRate());
	}
	
	public String getMatrixString() {
		this.getMatrix();
		return "" + matrix[0][0] + " " + matrix[0][1] + "\n" + matrix[1][0] + " " + matrix[1][1];
	}
}
