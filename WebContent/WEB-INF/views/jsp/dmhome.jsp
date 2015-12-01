<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Data Migration Tool</title>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- Bootstrap -->
<link href="<%=request.getServletPath()%>/WebContent/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/WebContent/resources/css/bootstrap.min.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/WebContent/resources/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/WebContent/resources/js/bootstrap.min.js"></script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<nav class="navbar navbar-default">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">Data Migration</a>
		</div>

	</div>

	<!-- /.container-fluid -->
</nav>

<form:form method="POST" name="onlineLoadForm" action="/DataMigration/dm/submitOnlineLoad">
	<h4>
		<span class="label label-default">Source Database :</span>
	</h4>
	<div class="radio">
		<form:radiobutton path="sourceDatabase" label="MySQL" name="sourceDatabase" value="MySQL" />
		<form:radiobutton path="sourceDatabase" label="Oracle" name="sourceDatabase" value="Oracle" />
	</div>	
	<h4>
		<span class="label label-default">MySQL Schemas :</span>
	</h4>
	
	<div >
		<form:select path="sourceSchema" items="${schemaDetailsList}" />
	</div>
	<h4>
		<span class="label label-default">Table List :</span>
	</h4>	
	<div >
		<form:select path="sourceTableName" items="${tableNameList}" />
	</div>
	<h4>
		<span class="label label-default">Target Database :</span>
	</h4>
	<div class="radio">
		<form:radiobutton path="targetDatabase" label="Mongo" name="targetDatabase" value="Mongo" />
		<form:radiobutton path="targetDatabase" label="Cassandra" name="targetDatabase" value="Cassandra" />
	</div>
	<h4>
		<span class="label label-default">Target Collection or Column Family Name :</span>
	</h4>
	<div >
		<form:input path="targetCollectionOrColumnFamilyName" />
	</div>		
	<h4>
		<span class="label label-default">No of Records to be Extracted :</span>
	</h4>
	<div >
		<form:input path="noOfRecordsToBeExtracted" />
	</div>
	<h4>
		<span class="label label-default">Include Child Table(s) :</span>
	</h4>
	<div class="radio">
		<form:radiobutton path="childTableExtractRequired" label="Yes" name="childTableExtractRequired" value="true" />
		<form:radiobutton path="childTableExtractRequired" label="No" name="childTableExtractRequired" value="false" />
	</div>			
	<br>
	<div >
		<input type="submit" value="Migrate the data" />
	</div>
	
</form:form>
</html>