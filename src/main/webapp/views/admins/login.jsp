<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f"%>

<c:choose>
    <c:when test="${not empty sessionScope.locale}">
        <f:setLocale value="${sessionScope.locale}" scope="session" />
    </c:when>
    <c:otherwise>
        <f:setLocale value="en" scope="session" />
        <c:set var="locale" value="en" scope="session" />
    </c:otherwise>
</c:choose>
<f:setBundle basename="com.java.lang.language" var="bundle" />
<!doctype html>
<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
	crossorigin="anonymous">

<style>
	.img{
		background-image: url("./assets/images/admin-login.jpg");
		width: 172px;
		height: 160px;
		margin: 0 auto;
		background-size: cover; 
		background-position: center;
	}
</style>

<title>Admin</title>
</head>
<body>
	<div class="container">
		<div class="row col-4 offset-4">
			<form action="/btck-atbmhttt/AdminSigninServlet" method="post">
			<c:if test="${not empty message }">
				<div class="alert alert-danger" role="alert">
					  ${message }
					</div>
			</c:if>
				<div class="img"></div>
				<h1 class="h3 mb-3 fw-normal" align="center"><f:message bundle="${bundle}" key="welcome"/> Admin</h1>

				<div class="form-floating mb-3">
					<input type="email" class="form-control" name="username"
						placeholder="name@example.com"> <label for="floatingInput">Email
						address</label>
				</div>
				<div class="form-floating mb-3">
					<input type="password" class="form-control" name="password"
						placeholder="Password"> <label for="floatingPassword"><f:message bundle="${bundle}" key="password"/></label>
				</div>
				<button class="w-100 btn btn-lg btn-primary" type="submit"><f:message bundle="${bundle}" key="signin"/></button>
				<p class="mt-5 mb-3 text-muted">&copy; Covid</p>
			</form>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
		crossorigin="anonymous"></script>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
</body>
</html>