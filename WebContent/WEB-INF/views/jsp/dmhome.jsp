<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Data Migration Tool</title>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- Bootstrap -->
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css"
	rel="stylesheet">

<script type="text/javascript">
	function submitForm(invokeParamVal) {

		$('#pageName').attr('name', invokeParamVal);

		var urlContext = "/DataMigration/dm/";

		if (invokeParamVal != null && invokeParamVal != "null"
				&& invokeParamVal == "home") {
			// submit the form.
			$('#command').attr('method', "GET");
			$('#command').attr('action', urlContext + invokeParamVal).submit();

		} else {
			// submit the form.
			$('#command').attr('action', urlContext + invokeParamVal).submit();
		}

	}
</script>
</head>
<body>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script
		src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>

	<header class="masthead">
		<div class="container">
			<div class="row">
				<div class="col-sm-6">
					<h1>
						<span class="page-header"
							style="color: rgb(0, 0, 204); font-family: 'Times New Roman', Times, serif;">Data
							Migration Tool</span>
					</h1>
				</div>
			</div>
		</div>
	</header>

	<!-- Begin Navbar -->
	<div id="nav">
		<div class="navbar navbar-default navbar-static">
			<div class="container">
				<!-- .btn-navbar is used as the toggle for collapsed navbar content -->
				<a class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse"> <span
					class="glyphicon glyphicon-bar"></span> <span
					class="glyphicon glyphicon-bar"></span> <span
					class="glyphicon glyphicon-bar"></span>
				</a>
				<div class="navbar-collapse collapse">
					<ul class="nav navbar-nav">
						<li class="active"><a href="#" onClick="submitForm('home')">Online Load</a></li>
						<li class="divider"></li>
						<li><a href="#" onClick="submitForm('batchHome')">Batch
								Load</a></li>
						<li class="divider"></li>
						<li><a href="#" onClick="submitForm('getProductLine')">Product
								Line</a></li>
					</ul>
				</div>
			</div>
		</div>
		<!-- /.navbar -->
	</div>



	<form:form method="POST" name="onlineLoadForm"
		action="/DataMigration/dm/submitOnlineLoad" role="form">

		<form:hidden path="pageName" />
		<form:hidden path="pageAction" />

		<div class="container">
			<div class="row">

				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">Source Database :</span>
						</h4>
					</div>

					<div class="col-xs-6 col-sm-4">
						<h4>
							<form:radiobutton path="sourceDatabase" label="MySQL"
								name="sourceDatabase" value="MySQL" />
							<form:radiobutton path="sourceDatabase" label="Oracle"
								name="sourceDatabase" value="Oracle" />
						</h4>
					</div>


				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">Schemas :</span>
						</h4>
					</div>

					<div class="col-xs-6 col-sm-4">
						<h4>
							<form:select path="sourceSchema" items="${schemaDetailsList}" />
						</h4>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">Table List :</span>
						</h4>
					</div>

					<div class="col-xs-6 col-sm-4">
						<h4>
							<form:select path="sourceTableName" items="${tableNameList}" />
						</h4>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">Target Database :</span>
						</h4>
					</div>

					<div class="col-xs-6 col-sm-4">
						<h4>
							<form:radiobutton path="targetDatabase" label="Mongo"
								name="targetDatabase" value="Mongo" />
							<form:radiobutton path="targetDatabase" label="Cassandra"
								name="targetDatabase" value="Cassandra" />
						</h4>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">Target Collection or
								Column Family Name :</span>
						</h4>
					</div>

					<div class="col-xs-6 col-sm-4">
						<h4>
							<form:input path="targetCollectionOrColumnFamilyName" />
						</h4>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">No of Records to be
								Extracted :</span>
						</h4>
					</div>
					<div class="col-xs-6 col-sm-4">
						<h4>
							<form:input path="noOfRecordsToBeExtracted" />
						</h4>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">Include Child Table(s) :</span>
						</h4>
					</div>
					<div class="col-xs-6 col-sm-4">
						<h4>

							<form:radiobutton path="childTableExtractRequired" label="Yes"
								name="childTableExtractRequired" value="true" />
							<form:radiobutton path="childTableExtractRequired" label="No"
								name="childTableExtractRequired" value="false" />
						</h4>
					</div>
				</div>
			</div>
			<br>
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4"></div>
					<div class="col-xs-6 col-sm-4">
						<input class="btn btn-success" type="submit"
							onclick="submitForm('submitOnlineLoad')" value="Migrate the data">
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>