package com.clinics.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.clinics.model.TestInfo;

public class ClinicsManager {
	
	private Map<String, TestInfo> clinicsTests;
	private Map<String, List<String>> testNameMapping;
	
	// initialize the clinics tests and test name mappings from dataset file
	public void initClinicsData() throws Exception {
		clinicsTests = new HashMap<String, TestInfo>();
		testNameMapping = new HashMap<String, List<String>>();
		
		// read dataset file
		BufferedReader br = new BufferedReader(new FileReader("/dataset.txt"));
		String line = null;
		while((line = br.readLine()) != null) {
			
			// convert line to test category and threshold
			String[] splitLine = line.split("=");
			String testCategory = splitLine[0].trim();
			Double testThreshold = Double.parseDouble(splitLine[1].trim());
			
			// add testInfo to clinicsTests
			TestInfo testInfo = new TestInfo();
			testInfo.setTestName(testCategory);
			testInfo.setTestResult(testThreshold);
			clinicsTests.put(testCategory.toLowerCase(), testInfo);
			
			// map words to test names
			String[] testNameWords = testCategory.toLowerCase().split("[\\s\\(\\),\\-:/\\!]+");
			for(String testNameWord: testNameWords) {
				List<String> relaventTestsToWord = testNameMapping.get(testNameWord);
				if(relaventTestsToWord == null) {
					relaventTestsToWord = new ArrayList<String>();
				}
				if(!relaventTestsToWord.contains(testCategory.toLowerCase())) {
					relaventTestsToWord.add(testCategory.toLowerCase());
				}
				testNameMapping.put(testNameWord, relaventTestsToWord);
			}
			
		}
		br.close();
	}
	
	// find the most suited test name\names from a given free text
	public List<String> getMostRelatedTestNamesFromFreeText(String inputTestName){
		List<String> relatedTestNames = new ArrayList<String>();
		
		// counter for number of words related to test name
		Map<String, Integer> testNameFrequency = new HashMap<String, Integer>();
		
		// split free text to words and check if the words relate to any test names, using test name mapping
		String[] inputTestNameWords = inputTestName.toLowerCase().split("[\\s\\(\\),\\-:/\\!]+");
		for(String inputTestNameWord: inputTestNameWords) {
			if(testNameMapping.get(inputTestNameWord) != null) {
				// add related test names for given words to the counter
				for(String testName: testNameMapping.get(inputTestNameWord)) {
					if(testNameFrequency.get(testName) != null) {
						testNameFrequency.put(testName, testNameFrequency.get(testName)+1);
					} else {
						testNameFrequency.put(testName, 1);
					}
				}
			}
		}
		
		// run on testNameFrequency to find most related test name
		Integer highest = 0;
		for(Entry<String, Integer> testNameCount: testNameFrequency.entrySet()) {
			if(testNameCount.getValue() > highest) {
				relatedTestNames.clear();
				relatedTestNames.add(testNameCount.getKey());
				highest = testNameCount.getValue();
			} else if(testNameCount.getValue() == highest) {
				relatedTestNames.add(testNameCount.getKey());
			}
		}
		
		return relatedTestNames;
	}
	
	// return test display name from given (lower case) test name
	public String getTestDisplayName(String testName) {
		return clinicsTests.get(testName).getTestName();
	}
	
	// evaluate test result buy given test name
	public Boolean isTestResultGood(String testName, Double testResult) {
		if(clinicsTests.get(testName).getTestResult() >= testResult) {
			return true;
		} else {
			return false;
		}
	}

}
