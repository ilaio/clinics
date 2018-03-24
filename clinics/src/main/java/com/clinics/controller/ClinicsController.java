package com.clinics.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clinics.model.TestInfo;

@RestController
@RequestMapping("/v1")
public class ClinicsController {
	
	private ClinicsManager clinicsManager;
	
	@PostConstruct
	public void init() throws Exception {
		clinicsManager = new ClinicsManager();
		clinicsManager.initClinicsData();
	}
	
	// Evaluate test result by given test name free text and result number
	@RequestMapping(value = "/testresult", method = RequestMethod.POST)
	public String getTestResultEvaluation(@RequestBody TestInfo testResultInput) {
		String resultMsg;
		
		// search for relevant test by given free text
		List<String> relatedTestNames = clinicsManager.getMostRelatedTestNamesFromFreeText(testResultInput.getTestName());
		if(relatedTestNames == null || relatedTestNames.isEmpty()) {
			// no relevant test was found
			resultMsg = "Given Test Name does not exist.";
		} else if(relatedTestNames.size() > 1) {
			// too many test names are related to given free text
			resultMsg = "Multiple tests are related to given Test Name. Did you mean to select one of the above: ";
			for(int i = 0; i < relatedTestNames.size(); i++) {
				resultMsg += clinicsManager.getTestDisplayName(relatedTestNames.get(i));
				if(i < relatedTestNames.size()-1) {
					resultMsg += ", ";
				} else {
					resultMsg += "?";
				}
			}
		} else {
			// found one related test name. evaluate the given result
			resultMsg = "The test result for test '" + clinicsManager.getTestDisplayName(relatedTestNames.get(0)) + "' is ";
			
			if(clinicsManager.isTestResultGood(relatedTestNames.get(0), testResultInput.getTestResult())) {
				resultMsg += "Good!";
			} else {
				resultMsg += "Bad!";
			}
		}
		
		return resultMsg;
	}

}
