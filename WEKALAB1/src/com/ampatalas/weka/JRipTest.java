package com.ampatalas.weka;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.CVParameterSelection;
import weka.classifiers.meta.Stacking;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.ZeroR;
import weka.core.Instances;

public class JRipTest {
	
	public static void main(String[] args) throws Exception {
		
		DataReader reader = new DataReader("traning.arff", "testing.arff", "creditKaggleTestUN2015.arff");
		
		Instances training = reader.learningData();
		Instances testing = reader.testData();
		
		
		ConfusionMatrix matrix;
		
		
		
		Map<String, Classifier> classifiers = new TreeMap<String, Classifier>();
		
		Classifier selection = JRipTest.optimizedJRip();
		classifiers.put("Classic", selection);
		
		//for (int i = 1; i < 20; i+= i) {
			for (int j = 1; j < 20; j++) {
				classifiers.put("folds: " + 16 + ", optimizations: " + j, JRipTest.testJRip(16, j));
			}
		//}
		
		/*try {
			selection.buildClassifier(reader.fullTraningData());
			System.out.println("Build.");
			FileSaver.save(reader.contestData(), selection);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		
		double maxmean = 0;
		String bestparms = "";
		for (Entry<String, Classifier> entry : classifiers.entrySet()) {
		    String key = entry.getKey() + "";
		    Classifier classifier = entry.getValue();
		    
		    System.out.println(key + " start...");
		    try {
				classifier.buildClassifier(training);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    System.out.println(key + " build.");
		    
		    matrix = new ConfusionMatrix();
		    double rate = matrix.rate(testing, classifier);
		    if (rate > maxmean){
		    	maxmean = rate;
		    	bestparms = key;
		    }
		    System.out.println(key + " rate: " + matrix.rate(testing, classifier) + "\n");
		    
		}
		System.out.println("Best score: " + maxmean + " with settings: " + bestparms);
	}
	
	private static Classifier optimizedJRip() {
		CVParameterSelection selection = new CVParameterSelection();
		JRip optimized = new JRip();
		optimized.setFolds(5);
		optimized.setUsePruning(true);
		optimized.setMinNo(1);
		optimized.setOptimizations(4);
		selection.setClassifier(optimized);
		return selection;
	}
	
	private static Classifier testJRip(int folds, int optimizations) {
		CVParameterSelection selection = new CVParameterSelection();
		JRip optimized = new JRip();
		optimized.setFolds(folds); //3
		optimized.setUsePruning(true);
		optimized.setMinNo(1);
		optimized.setOptimizations(optimizations); //10
		selection.setClassifier(optimized);
		return selection;
	}

}
