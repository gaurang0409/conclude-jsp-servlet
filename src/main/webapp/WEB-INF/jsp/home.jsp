<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
<%@ page isELIgnored="false" %>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<style>
table, th, td {
	border: 1px solid black;
}

.heading4 {
	background-color: #708090;
}
</style>

<body>

	<div class="container">
		<div class="topnav">
			<nav>
				<ul class="nav nav-pills pull-right">
					<right>
					<li id="qsLogoutBtn"><a href="#" style="float: right;"btnbtn-linknavbar-btnnavbar-link ">Logout</a></li>

					</right>
				</ul>
			</nav>

			<center>
				<div class="logo">
					<a target="_blank" rel="noopener"> <img
						src="https://media-exp1.licdn.com/dms/image/C4E0BAQFA676dWZCBJQ/company-logo_200_200/0?e=1602720000&v=beta&t=OF82_DjJH6_V3YWehCl2xZ4ThBODY5gptBzf9vVswHs"
						class="center"></a>
				</div>
			</center>
		</div>

		<div class="row marketing">
			<div class="col-lg-6">
				<%-- <h4 class="heading4" align="center" text="bold">Access token</h4>
				<p>${accessToken}</p> --%>

				<h4>
				<p class="text-right" text="bold">AccessToken</p>
				</h4>

			</div>

			<table class="table table-striped">
				<thead>
					<tr>
						<th scope="col">#</th>
						<th scope="col">Alogrithm</th>
						<th scope="col">Issuer</th>
						<th scope="col">UserId</th>
						<th scope="col">Audience</th>
						<th scope="col">Scope</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th scope="row">1</th>
						<td>${algorithm}</td>
						<td>${issuer}</td>
						<td>${userid}</td>
						<td>${audience}</td>
						<td>${scope}</td>
					</tr>
				</tbody>
			</table>


		</div>

		<div class="row marketing">
			<div class="col-lg-6">
				<%-- <h4 class="heading4" align="center" text="bold">Access token</h4>
				<p>${accessToken}</p> --%>

				<h4>
				<p class="text-right" text="bold">ID Token</p>
				</h4>

			</div>

			<table class="table table-striped">
				<thead>
					<tr>
						<th scope="col">#</th>
						<th scope="col">Nick Name</th>
						<th scope="col">Name</th>
						<th scope="col">Picture</th>
						<!-- <th scope="col">Issuer</th> -->
						<th scope="col">UserId</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th scope="row">1</th>
						<td>${nickName}</td>
						<td>${name}</td>
						<%-- <td>${picture}</td> --%>
						<td>${iss}</td>
						<td>${sub}</td>
					</tr>
				</tbody>
			</table>


		</div>



<!-- 		<footer class="footer">
			<p>&copy; 2020 Company Inc</p>
		</footer>
 -->


	</div>

	<script type="text/javascript">
		$("#qsLogoutBtn")
				.click(
						function(e) {
							e.preventDefault();
							$("#home").removeClass("active");
							$("#password-login").removeClass("active");
							$("#qsLogoutBtn").addClass("active");
							// assumes we are not part of SSO so just logout of local session
							window.location = "${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI, '')}/logout";
						});
	</script>

</body>
</html>