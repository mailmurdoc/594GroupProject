package edu.upenn.cit594.processor;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import edu.upenn.cit594.datamanagement.ReadPopulationFile;
import edu.upenn.cit594.datamanagement.ReadProperties;

public class Questions {
	
	/*
	 * Create static maps for memoization
	 * Many maps for many possible future uses
	 * Repeated method is check if already memoized,
	 * If not, then perform file reading and memoization
	 */
	
	public static HashMap<Integer, Integer> populations = new HashMap<Integer, Integer>();
	public static TreeMap<Integer, Integer> fines = new TreeMap<Integer, Integer>();
	public static TreeMap<Integer, Double> finesPerCapitas = new TreeMap<Integer, Double>();
	public static HashMap<Integer, ArrayList <Integer>> marketValues = new HashMap<Integer, ArrayList<Integer>>();
	public static HashMap<Integer, ArrayList<Integer>> livableAreas = new HashMap<Integer, ArrayList<Integer>>();
	public static HashMap<Integer, Long> totalMarketValues = new HashMap<Integer, Long>();
	public static HashMap<Integer, Long> totalLivableAreas = new HashMap<Integer, Long>();
	public static HashMap<Integer, Double> averageMarketValues = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> averageLivableAreas = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> marketPerCapitas = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> marketPerFinePerCapitas = new HashMap<Integer, Double>();
	
	public void q1TotalPopulation(String populationFileName) {
		if (populations.isEmpty()) {
			ReadPopulationFile readPopulationFile = new ReadPopulationFile();
			populations = readPopulationFile.readPopulationFile(populationFileName);
		}
		int totalPopulation = 0;
		for (int population : populations.keySet()) {         //Simply totaling the individual populations
			totalPopulation +=  populations.get(population);
		}
		System.out.println(totalPopulation);
	}

	public void q2TotalFinesPerCapita(String fileType, String parkingFileName, String populationFileName) {
		FileDecision fd = new FileDecision();
		
		/*
		 * If not already stored or memoized, obtain
		 * Otherwise use stored information
		 */
		
		if (populations.isEmpty()) {
			ReadPopulationFile readPopulationFile = new ReadPopulationFile();
			populations = readPopulationFile.readPopulationFile(populationFileName);
		}
		if (finesPerCapitas.isEmpty()) {
			fines = fd.fileDecision(fileType, parkingFileName);
		}
		for (Integer zipCode : fines.keySet()) {
			
			/*
			 * @Cayde
			 * checking the fines_per_capita hashmap for already having solution
			 */
			
			if ((fines.get(zipCode) != null) && populations.get(zipCode) != null) {
				double avgFinePerCap = (double) fines.get(zipCode) / (double) populations.get(zipCode);
				finesPerCapitas.put(zipCode, avgFinePerCap);    //Memoize if not already stored
				DecimalFormat decForm = new DecimalFormat("##.##");
				System.out.println(zipCode + " $" + decForm.format(avgFinePerCap));
			}
		}
	}
	
	/*
	 * If not already stored or memoized, obtain
	 * Note do not assume if we have market values for a zip
	 * code already have total.  This allows for increased modularity
	 * if there were other prompts regarding the market values.
	 */
	
	
	/*  @Cayde
	 * Use strategy design pattern and/or separate methods
	 * to decrease repetition
	 */
	public void q3AverageMarketValue(String propertiesFileName) {
		System.out.println("Please pick a zipcode");
		Scanner i = new Scanner(System.in);
		int zip = 0;
		zip = i.nextInt();;
		ArrayList<Integer> marketValueList = new ArrayList<Integer>();  //all the market values for a zip code
		if (marketValues.containsKey(zip) == false) {
			ReadProperties readProperties = new ReadProperties();
			marketValueList = readProperties.readProperties(3, propertiesFileName, zip);
			marketValues.put(zip, marketValueList);  			//Memoize if not already stored
		}	
		if (totalMarketValues.containsKey(zip) == false) {   	//@Cayde this may be unnecessary step or previous may be
			long totalMarketValue = 0; 						//@Cayde probably could be double
			for (int marketValue: marketValues.get(zip)) {
				totalMarketValue += marketValue;
			}
			totalMarketValues.put(zip, totalMarketValue);  		//Memoize if not already stored
		}
		if (averageMarketValues.containsKey(zip) == false) {
			double averageMarketValue = (double) totalMarketValues.get(zip) /
					(double) marketValues.get(zip).size();
			averageMarketValues.put(zip, averageMarketValue);  	//Memoize if not already stored
		}
		System.out.println("Average Market value for " + zip + " is " + Math.round(averageMarketValues.get(zip)));
	}
	
	public void q4AverageLivableArea(String propertiesFileName) {
		System.out.println("Please pick a zipcode");
		Scanner scanner = new Scanner(System.in);							//@Cayde use strategy design pattern to decrease repetition
		int zip = 0;
		zip = scanner.nextInt();
		ArrayList<Integer> livableAreaList = new ArrayList<Integer>();
		if (livableAreas.containsKey(zip) == false) {
			ReadProperties rp = new ReadProperties();
			livableAreaList = rp.readProperties(4, propertiesFileName, zip);
			livableAreas.put(zip, livableAreaList);  			//Memoize if not already stored
		}	
		if (totalLivableAreas.containsKey(zip) == false) {
			long totalLivableArea = 0;
			for (int livableArea: livableAreas.get(zip)) {
				totalLivableArea += livableArea;
			}
			totalLivableAreas.put(zip, totalLivableArea);		//Memoize if not already stored
		}
		if (averageLivableAreas.containsKey(zip) == false) {
			double averageLivableArea = (double) totalLivableAreas.get(zip) /
					(double) livableAreas.get(zip).size();
			averageLivableAreas.put(zip, averageLivableArea);	//Memoize if not already stored
		}
		System.out.println("Average livable area for " + zip + " is " + Math.round(averageLivableAreas.get(zip)));
	}
	
	public void q5TotalMarketValuePerCapita(String propertiesFileName, String populationFileName) {
		System.out.println("Please pick a zipcode");
		Scanner scanner = new Scanner(System.in);
		int zip = 0;
		zip = scanner.nextInt();
		//i.close();
		ArrayList<Integer> marketValueList = new ArrayList<Integer>();
		if (marketPerCapitas.containsKey(zip) == false) {
			if (totalMarketValues.containsKey(zip) == false) {
				if (marketValues.containsKey(zip) == false) {
					ReadProperties rp = new ReadProperties();
					marketValueList = rp.readProperties(3, propertiesFileName, zip);
					marketValues.put(zip, marketValueList);				//Memoize if not already stored
				}
				long totalMarketValue = 0;
				for (int marketValue: marketValues.get(zip)) {
					totalMarketValue += marketValue;
				}
				totalMarketValues.put(zip, totalMarketValue);			//Memoize if not already stored
			}
			if (populations.containsKey(zip) == false) {
				ReadPopulationFile rpf = new ReadPopulationFile();
				populations = rpf.readPopulationFile(populationFileName);
			}
			double marketPerCapita = (double) totalMarketValues.get(zip) / 
				(double) populations.get(zip);
			marketPerCapitas.put(zip, marketPerCapita);					//Memoize if not already stored
		}
		System.out.println("Total Market Value per Capital for " + zip + " is " + Math.round(marketPerCapitas.get(zip)));
	}
	
	public void q6TotalMarketValuePerTotalFinesPerCapita(String parkingFileType, String propertiesFileName, 
			String populationFileName, String parkingFileName) {
		System.out.println("Please pick a zipcode");
		Scanner scanner = new Scanner(System.in);
		int zip = 0;
		zip = scanner.nextInt();
		ArrayList<Integer> marketValueList = new ArrayList<Integer>();
		if (marketPerFinePerCapitas.containsKey(zip) == false) {
			if (totalMarketValues.containsKey(zip) == false) {
				if (marketValues.containsKey(zip) == false) {
					ReadProperties rp = new ReadProperties();
					marketValueList = rp.readProperties(3, propertiesFileName, zip);
					marketValues.put(zip, marketValueList);				//Memoize if not already stored
				}
				long totalMarketValue = 0;
				for (int marketValue: marketValues.get(zip)) {
					totalMarketValue += marketValue;
				}
				totalMarketValues.put(zip, totalMarketValue);			//Memoize if not already stored
			}
			if (populations.containsKey(zip) == false) {
				ReadPopulationFile rpf = new ReadPopulationFile();
				populations = rpf.readPopulationFile(populationFileName);	//Memoize if not already stored
			}
			if (finesPerCapitas.isEmpty()) {
				FileDecision fd = new FileDecision();
				fines = fd.fileDecision(parkingFileType, parkingFileName);	//Memoize if not already stored
			}
			double marketPerFinePerCapita = ((double) totalMarketValues.get(zip) / (double) fines.get(zip)) /
				(double) populations.get(zip);
			marketPerFinePerCapitas.put(zip, marketPerFinePerCapita);//Memoize if not already stored
		}
		System.out.println(Math.round(marketPerFinePerCapitas.get(zip)));
	}
}
