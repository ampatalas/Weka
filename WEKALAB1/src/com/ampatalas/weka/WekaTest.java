package com.ampatalas.weka;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.rules.JRip;
import weka.core.Instances;

public class WekaTest {
	
	public static void main(String[] args) throws Exception {
		
		DataReader reader = new DataReader("creditKaggleTrain.arff", "creditKaggleTestUN240415.arff");
		
		Instances training = reader.learningData();
		Instances testing = reader.testData();
		
		/*Losers:
		 * NaiveBayes
		 * KStar
		 * IBK
		 * J48
		 */
		
		Map<String, Classifier> classifiers = new TreeMap<String, Classifier>();
		
		//JRip
		Classifier JRIP = new JRip();
		classifiers.put("JRip", JRIP);
		
		//Logistic
		Classifier logistic = new Logistic();
		classifiers.put("Logistic", logistic);
		
		//SMO
		Classifier smo = new SMO();
		classifiers.put("SMO", smo);
		
		//BayesNet
		Classifier bayesNet = new BayesNet();
		classifiers.put("Bayes Net", bayesNet);
		
		ConfusionMatrix matrix;
		
		for (Entry<String, Classifier> entry : classifiers.entrySet()) {
		    String key = entry.getKey();
		    Classifier classifier = entry.getValue();
		    
		    System.out.println(key + " start...");
		    classifier.buildClassifier(training);
		    System.out.println(key + " build.");
		    
		    matrix = new ConfusionMatrix();
		    System.out.println(key + " rate: " + matrix.rate(testing, classifier) + "\n");
		    
		}
		
	}

}
