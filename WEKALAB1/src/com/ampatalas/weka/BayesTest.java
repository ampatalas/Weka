package com.ampatalas.weka;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.CVParameterSelection;
import weka.classifiers.meta.Stacking;
import weka.classifiers.rules.JRip;
import weka.core.Instances;

public class BayesTest {
	
	public static void main(String[] args) throws Exception {
		
		DataReader reader = new DataReader("creditKaggleTrain.arff", "creditKaggleTestUN080515.arff");
		
		Instances training = reader.learningData();
		Instances testing = reader.testData();
		
		Map<Integer, Classifier> classifiers = new TreeMap<Integer, Classifier>();
		
		classifiers.put(1, optimizedBayes());
		
		Classifier finalC = optimizedBayes();
		finalC.buildClassifier(reader.fullTraningData());
		FileSaver.save(reader.contestData(), finalC);
		
		/*Stacking stack = new Stacking();
		Classifier[] stackArray  = {optimizedBayes(), new JRip()};
		stack.setClassifiers(stackArray);
		classifiers.put(2, stack);*/
		
		printResults(classifiers, training, testing);
		
	}
	
	private static void printResults(Map<Integer, Classifier> classifiers, Instances training, Instances testing) {
		for (Entry<Integer, Classifier> entry : classifiers.entrySet()) {
		    String key = entry.getKey() + "";
		    Classifier classifier = entry.getValue();
		    
		    System.out.println(key + " start...");
		    try {
				classifier.buildClassifier(training);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    System.out.println(key + " build.");
		    
		    ConfusionMatrix matrix = new ConfusionMatrix();
		    System.out.println(key + " rate: " + matrix.rate(testing, classifier) + "\n");
		    
		}
	}
	
	private static Classifier optimizedBayes() {
		//Pure 0.774560409852315
		//CVParameterSelection Makes no difference whatsoever
		//AdaBoostM1 +0.1
		BayesNet net = new BayesNet();
		
		AdaBoostM1 boost = new AdaBoostM1();
		boost.setClassifier(net);
		
		return boost;
	}

}
