<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
<%@ page isELIgnored="false" %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Home Page</title>
<link rel="stylesheet" type="text/css" href="/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="/css/jumbotron-narrow.css">
<link rel="stylesheet" type="text/css" href="/css/home.css">
<link rel="stylesheet" type="text/css" href="/css/jquery.growl.css" />
<script src="http://code.jquery.com/jquery.js"></script>
<script src="/js/jquery.growl.js" type="text/javascript"></script>
</head>

<body>

	<div class="container">
		<div class="header clearfix"></div>

		<div class="row marketing">
			<div class="col-lg-6">
				<h4>Status Code:</h4>
				<p>${statusCode}</p>
				<h4>message:</h4>
				<p>${message}</p>
			</div>

		</div>

		<footer class="footer">
			<p>&copy; 2020 Company Inc</p>
		</footer>

	</div>



</body>
</html>