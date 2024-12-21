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

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<link
	href="https://fonts.googleapis.com/css?family=Poppins:100,200,300,400,500,600,700,800,900&display=swap"
	rel="stylesheet">

<base href="/btck-atbmhttt/" />
<title>${pageInfo.title }</title>

<!-- Bootstrap icons -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">

<!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<!-- Additional CSS Files -->
<link rel="stylesheet" href="assets/css/fontawesome.css">
<link rel="stylesheet" href="assets/css/index.css">
<link rel="stylesheet" href="assets/css/owl.css">
<link rel="stylesheet" href="assets/css/cart.css">
<link rel="stylesheet" href="assets/css/order.css">
</head>

<body>

	<div id="overlay"></div>

	<!-- ***** Preloader Start ***** -->
	<div id="preloader">
		<div class="jumper">
			<div></div>
			<div></div>
			<div></div>
		</div>
	</div>
	<!-- ***** Preloader End ***** -->

	<!-- Header -->
	<header class="">
		<nav class="navbar navbar-expand-lg">
			<div class="container">
				<a class="navbar-brand" href="HomeServlet"><h2>
						GearNl <em>Pro</em>
					</h2></a>
				<form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3" action="SearchServlet" method="post">
					<input type="search" class="form-control form-control-dark"
						placeholder=" <f:message bundle="${bundle}" key="search"/>..." aria-label="Search" name="keyword" onblur="this.form.submit()">
				</form>
				<button class="navbar-toggler" type="button" data-toggle="collapse"
					data-target="#navbarResponsive" aria-controls="navbarResponsive"
					aria-expanded="false" aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>
				<div class="collapse navbar-collapse" id="navbarResponsive">
					<ul class="navbar-nav ml-auto">
						<li class="nav-item ${flag == 'Home' ? 'active' : '' }">
							<a href="HomeServlet" class="nav-link"><f:message bundle="${bundle}" key="home"/></a></li>
						<li class="nav-item ${flag == 'Products' ? 'active' : '' }">
							<a class="nav-link"
							href="ProductServlet"><f:message bundle="${bundle}" key="products"/></a></li>
						<c:if test="${sessionScope.user == null }">
							<li class="nav-item"><a class="nav-link" href="LoginServlet">
							      <f:message bundle="${bundle}" key="signin"/></a></li>
							<li class="nav-item"><a class="nav-link"
								href="Register	Servlet"><f:message bundle="${bundle}" key="signup"/></a>
						</c:if>
						<c:if test="${sessionScope.user != null }">
							<li class="nav-item ${flag == 'Cart' ? 'active' : '' }"><a class="nav-link" href="CartServlet">
								<f:message bundle="${bundle}" key="cart" />									
							</a></li>
							<li class="nav-item ${flag == 'Orders' ? 'active' : '' }"><a class="nav-link" href="OrderServlet">
								<f:message bundle="${bundle}" key="order" />									
							</a></li>
							<li class="nav-item" id="user-menu">
							    <button id="user-icon-button" class="btn">
							        <i class="bi bi-person-circle" style="font-size: 26px; color: white;"></i>
							    </button>
							    <div id="user-dropdown" class="dropdown-menu">
							        <h6><f:message bundle="${bundle}" key="username_login" />: ${sessionScope.user.fullname}</h6>
							        <h6 id="userId" style="display: none;">${sessionScope.user.idUsers}</h6>
							        <h6>Email: ${sessionScope.user.email}</h6>
							        <div class="action-user">
							        	<a class="user-link" href="HelpServlet">
								        	<i class="bi bi-question-circle"></i>
								        	<f:message bundle="${bundle}" key="help" />
								        </a>
							        	<a class="user-link" href="LogoutServlet"><f:message bundle="${bundle}" key="logout" /></a>
							        </div>
							    </div>
							</li>
						</c:if>
						<li class="nav-item">
						    <button class="btn" id="lang-icon-button">
						        <i class="bi bi-globe" style="font-size: 26px; color: white;"></i>
						    </button>
						    <div class="select-lang-dropdown" id="lang-dropdown">
						        <ul class="selected">
								    <li><a href="javascript:void(0)" onclick="changeLanguage('vi')">Tiếng Việt</a></li>
								    <li><a href="javascript:void(0)" onclick="changeLanguage('en')">English</a></li>
								</ul>
						    </div>
						</li>
					</ul>
				</div>
			</div>
		</nav>
	</header>

	<!-- Page Content -->
	<!-- Banner Starts Here -->
	<div class="banner header-text">
		<div class="owl-banner owl-carousel">
			<div class="banner-item-01">
				<div class="text-content">
					<h4>Best Offer</h4>
					<h2>GearNl Pro Idol</h2>
				</div>
			</div>
			<div class="banner-item-02">
				<div class="text-content">
					<h4>Flash Deals</h4>
					<h2>GearNl Pro</h2>
				</div>
			</div>
			<div class="banner-item-03">
				<div class="text-content">
					<h4>Last Minute</h4>
					<h2>Chúa tể laptop</h2>
				</div>
			</div>
		</div>
	</div>
	<!-- Banner Ends Here -->
	<c:if test="${pageInfo != null }">
		<jsp:include page="${pageInfo.contentUrl }"></jsp:include>
	</c:if>
	<div id="logout-modal" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; padding: 20px; box-shadow: 0 4px 8px rgba(0,0,0,0.2); border-radius: 5px;">
	    <p><f:message bundle="${bundle}" key="sessionNotification" /></p>
	    <button id="confirm-logout"><f:message bundle="${bundle}" key="confirm" /></button>
	</div>

	<div class="best-features">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="section-heading">
						<h2><f:message bundle="${bundle }" key="about"/> GearNl Pro</h2>
					</div>
				</div>
				<div class="col-md-6">
					<div class="left-content">
						<h4>GearNl Pro</h4>
						<p>GearNl Pro <f:message bundle="${bundle }" key="about_us"/></p>
						<ul class="featured-list">
							<li><a href="#"><f:message bundle="${bundle }" key="best_price"/></a></li>
							<li><a href="#"><f:message bundle="${bundle }" key="fast_service"/></a></li>
							<li><a href="#"><f:message bundle="${bundle }" key="trusted_brands"/></a></li>
							<li><a href="#"><f:message bundle="${bundle }" key="other_promotions"/></a></li>
						</ul>
					</div>
				</div>
				<div class="col-md-6">
					<div class="right-image">
						<img src="assets/images/profile-page.jpg" alt="">
					</div>
				</div>
			</div>
		</div>
	</div>

	<footer>
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="inner-content">
						<p>Copyright &copy; 2024 GearNl Pro</p>
					</div>
				</div>
			</div>
		</div>
	</footer>
	
	<!-- Additional Scripts -->
	<script src="assets/js/user.js"></script>
	<script src="assets/js/owl.js"></script>
	<script src="assets/js/slick.js"></script>
	<script src="assets/js/isotope.js"></script>
	<script src="assets/js/accordions.js"></script>

	<script language="text/Javascript"> 
      cleared[0] = cleared[1] = cleared[2] = 0; 
      function clearField(t){                   
      if(! cleared[t.id]){                      
          cleared[t.id] = 1;  
          t.value='';        
          t.style.color='#fff';
          }
      }
    </script>
    <script>
	    var hasSessionUser = ${sessionScope.user != null};
	</script>
	
	<script>
		function changeLanguage(lang) {
		    $.ajax({
		        url: 'changeLanguage',
		        type: 'POST',
		        data: { lang: lang },
		        success: function(response) {
		            if (response.status === "success") {
		                location.reload(); // Reload lại trang để áp dụng ngôn ngữ mới
		            } else {
		                alert("Failed to change language: " + response.message);
		            }
		        },
		        error: function() {
		            alert("Error occurred while changing language.");
		        }
		    });
		}

	</script>
</body>

</html>