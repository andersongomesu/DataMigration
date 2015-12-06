<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Data Migration Tool</title>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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

	function showImage(imageURL) {
		
		imageURL1 = 'data:image/png;base64,' + imageURL;

		alert('1');
		if (imageURL != null && imageURL != "null" && imageURL != "") {
			alert(imageURL1);
			$('productLineImage').attr('src', imageURL1);
		}

	}
</script>
</head>
<body onload="showImage(${productLineImageAsString})">
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
						<li><a href="#" onClick="submitForm('home')">Online Load</a></li>
						<li class="divider"></li>
						<li><a href="#" onClick="submitForm('batchHome')">Batch
								Load</a></li>
						<li class="divider"></li>
						<li class="active"><a href="#">Product Line</a></li>
					</ul>
				</div>
			</div>
		</div>
		<!-- /.navbar -->
	</div>



	<form:form method="POST" name="productLinesForm"
		action="/DataMigration/dm/submitOnlineLoad" role="form" >


		<div class="container">
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">Product Line :</span>
						</h4>
					</div>
					<div class="col-xs-6 col-sm-4">
						<h4>
							<form:select path="productLine" items="${productLinesList}" />
						</h4>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="col-xs-6 col-sm-4">
						<h4>
							<span class="label label-default">Product Image :</span>
						</h4>
					</div>
					<div class="col-xs-6 col-sm-4">
						<h4>
<%-- 							<img
								src="data:image/png;base64,<c:out value='${productLineImageAsString}'/>"
								alt="product line image" /> --%>
							<img
								src="image"
								alt="product line image" />								
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
							onclick="submitForm('showProductLine')"
							value="Get Product Line Image">
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>