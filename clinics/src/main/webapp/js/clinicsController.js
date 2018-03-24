var app = angular.module('myApp', []);

app.controller('clinicsCtrl', function($scope) {
    $scope.testName = "";
    $scope.testResult = "";
    $scope.resultMsg = "";
    
    $scope.evaluateTestResult = function(){
    	if(!$scope.testName || !$scope.testResult){
    		alert('You must enter Test Name and Test Result.');
    		return;
    	}
    	
    	var url = "api/v1/testresult";
    	var params = {testName:$scope.testName,testResult:$scope.testResult};
    	
    	var xhttp = new XMLHttpRequest();
        xhttp.open("POST", url, true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.onreadystatechange = function() {
            if (xhttp.readyState === XMLHttpRequest.DONE && xhttp.status === 200) {
                $scope.resultMsg = xhttp.responseText;
            }
        }
        xhttp.send(JSON.stringify(params));
    }
    
});