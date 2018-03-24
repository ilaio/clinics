<!DOCTYPE html>
<html>
<head>
	<title>Submit blood test results</title>
</head>


<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>

<body>
	<h2>Submit blood test results</h2>
	<div ng-app="myApp" ng-controller="clinicsCtrl">
		<p>Test Name: <input type="text" ng-model="testName"><p>
		<p>Test Result: <input type="number" ng-model="testResult"><p>
		<button ng-click="evaluateTestResult()">Submit</button><br><br>
		<p>{{resultMsg}}</p>
	</div>
	
	<script src="js/clinicsController.js"></script>
</body>

</html>
