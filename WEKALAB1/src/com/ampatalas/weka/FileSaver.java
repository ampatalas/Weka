package com.ampatalas.weka;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.core.Instances;

public class FileSaver {
	
	public static void save(Instances data, Classifier classifier) throws Exception  {
		
		for (int i = 0; i < data.size(); i++) {
			double classification = classifier.classifyInstance(data.get(i));
			data.get(i).setClassValue(classification);
		}
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("192193.arff"));
			writer.write(data.toString());
			writer.flush();
			writer.close();
			System.out.println("File succesfully saved.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
public static void save(Instances data, String name) throws Exception  {
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(name + ".arff"));
			writer.write(data.toString());
			writer.flush();
			writer.close();
			System.out.println("File succesfully saved.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
