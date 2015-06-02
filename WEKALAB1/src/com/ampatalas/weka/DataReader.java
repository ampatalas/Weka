package com.ampatalas.weka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instance;
import weka.core.Instances;

public class DataReader {
	
	private Instances learningData;
	private Instances trainingData;
	private Instances testingData;
	
	private String contestPath;
	
	public DataReader(String trainPath, String contestPath) {
		
		this.contestPath = contestPath;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(trainPath));
			learningData = new Instances(reader);
			learningData.setClassIndex(learningData.numAttributes() - 1);
			setWeights(learningData);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		trainingData = new Instances(learningData, 0);
		testingData = new Instances(learningData, 0);
		
		for (int i = 0; i < learningData.numInstances(); i++) {
			Instance currentInstance = learningData.get(i);
			if (Math.random() > 0.2) {
				trainingData.add(currentInstance);
			}
			else {
				testingData.add(currentInstance);
			}
		}
		setWeights(trainingData);
	}
	
	
	public DataReader(String pathTraning, String pathTesting, String contestPath) {
		this.contestPath = contestPath;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(pathTraning));
			trainingData = new Instances(reader);
			trainingData.setClassIndex(trainingData.numAttributes() - 1);
			setWeight(trainingData, 7.02);
			
			reader = new BufferedReader(new FileReader(pathTesting));
			testingData = new Instances(reader);
			testingData.setClassIndex(testingData.numAttributes() - 1);
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void setWeight(Instances data, double weight) {
		for (int i = 0; i < data.numInstances(); i++) {
			if (data.get(i).classValue() == 1) {
				data.get(i).setWeight(weight);
			}
		}
	}


	/*
	 * Problem niezblansowania pojawia siê, gdy mamy znacznie wiêcej danych z jednej grupy.
	 * Wtedy model jest lepszy w wykrywaniu tej dominuj¹cej grupy, a ma problem z grup¹
	 * rzadziej wystepuj¹c¹.
	 * 'Obci¹¿enie w stronê klasy dominuj¹cej'
	 * Podniesienie jednego ze sk³adników obywa siê kosztem tego drugiego.
	 */
	private void setWeights(Instances data) {
		int numberOfDominated = 0;
		int numberOfDominating = 0;
		
		for (int i = 0; i < data.numInstances(); i++) {
			if (data.get(i).classValue() == 0) numberOfDominating++;
			else numberOfDominated++;
		}
		
		
		double weight  = (double)numberOfDominating / (double)numberOfDominated;
		
		for (int i = 0; i < data.numInstances(); i++) {
			if (data.get(i).classValue() == 1) {
				data.get(i).setWeight(weight);
			}
		}
	}
	/*
	 * SMOTE - dostêpny w WEKA
	 * p - 100% procent próbek, które chcemy wygenerowaæ (100% - dwa razy tyle)
	 * k - liczba najbli¿szych s¹siadów
	 * 
	 * 3 najbli¿si s¹siedzi obserwacji - na losowym miejscu miêdzy obserwacj¹ a 
	 * s¹siadem tworzymy syntetyczn¹ oberwacjê
	 */
	
	/*
	 * Podejœcie wra¿liwe na koszt - zwiêkszamy wartoœæ obserwacji klasy zdominowanej.
	 * Waga oberwacji
	 * metoda setWeight
	 * suma wag dominowej = suma wag dominuj¹cej
	 */

	public Instances learningData() {
		return this.trainingData;
	}
	
	public Instances testData() {
		return this.testingData;
	}
	
	public Instances fullTraningData() {
		return this.learningData;
	}
	
	public Instances contestData() {
		Instances finalData = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(contestPath));
			finalData = new Instances(reader);
			finalData.setClassIndex(finalData.numAttributes() - 1);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return finalData;	
	}

}
