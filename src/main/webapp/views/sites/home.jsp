<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
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

<div class="latest-products">
	<div class="container">
		<div id="content" class="row">
			<div class="col-md-12">
				<div class="section-heading">
					<h2><f:message bundle="${bundle }" key="newProduct"/></h2>
				</div>
			</div>
			<c:forEach items="${listProducts }" var="item">
				<div class="product col-md-4">
					<div class="product-item">	
						<div class="col-2 mt-2 discount-badge">-${item.discount.percent }%</div>	
						<a href="DetailServlet?id=${item.idProducts }"><img src="${item.image }" alt=""></a>
						<div class="down-content">
							<a href="DetailServlet?id=${item.idProducts }"><h4>${item.name }</h4></a>
							<div class="price-content">
	                          	  <h6><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫" /></h6>
		                          <h5 style="color: red">
		                          <fmt:formatNumber value="${item.price * (100 - item.discount.percent)/100 }" type="currency" currencySymbol="₫" /></h5>
	                          </div>
							<p>${item.description }</p>
							<div class="bottom-product">
								<a href="AddCartServlet?id=${item.idProducts }" class="btn btn-primary"><f:message bundle="${bundle }" key="addCart"/></a>
								<div class="review">
									<span><f:message bundle="${bundle }" key="view"/> (${item.view })</span>
									<span class="rate-comment" style="display: flex; gap: 10px;">
									    <div style="display: flex;">
									    	<fmt:formatNumber value="${item.averageStar}" type="number" maxFractionDigits="1" />
									    	<i class="bi bi-star-fill"></i>
									    </div>
										<f:message bundle="${bundle }" key="rate"/> (${item.rate })
									</span>
								</div>
							  </div>
						</div>
					</div>
				</div>
			</c:forEach>

		</div>
		<button onclick="loadMore()" class="btn btn-primary"><f:message bundle="${bundle }" key="loadMore"/></button>
	</div>
</div>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
		function loadMore() {
			var exist = document.getElementsByClassName("product").length;
			console.log(exist);
			$.ajax({
				  url: "/btck-atbmhttt/LoadServlet?amount=" + exist,
				  type: "get",
				  
				  success: function(response) {
				    var content = document.getElementById("content");

				    content.innerHTML += response;
				  },
				  error: function(xhr) {
				    console.log(xhr);
				  }
				});
		}
	</script>