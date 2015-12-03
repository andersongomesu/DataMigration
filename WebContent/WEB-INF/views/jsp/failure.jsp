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

		// submit the form.
		$('#command').attr('action', urlContext + invokeParamVal).submit();

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
						<li class="active"><a href="#">Online Load</a></li>
						<li class="divider"></li>
						<li><a href="#" onClick="submitForm('batchHome')">Batch
								Load</a></li>
					</ul>
				</div>
			</div>
		</div>
		<!-- /.navbar -->
	</div>
	<form:form method="GET" name="successForm"
		action="/DataMigration/dm/home">

		<div class="container">
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-danger">${onlineLoadForm.message}</span>
						</h4>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<input type="submit" class="btn btn-success" value="Goto Home" />
					</div>
				</div>
			</div>
		</div>

	</form:form>
</html>