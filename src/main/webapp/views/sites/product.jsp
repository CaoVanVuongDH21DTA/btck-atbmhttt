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
    
<style>
  .product-item img {
    width: 100%;  
    height: auto; 
    object-fit: contain;
  }
</style>
    
<div class="products">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <div class="filters">
              <ul>
                  <li><a href="${pageContext.request.contextPath }/ProductServlet"><f:message bundle="${bundle }" key="allProduct"/></a></li>
                  <c:forEach items="${listCategories }" var="item"> 
                  		<a href="${pageContext.request.contextPath }/CategoryServlet?id=${item.idCategorys}"><li>${item.name }</li></a>
                  </c:forEach>
              </ul>
            </div>
          </div>
          <div class="col-md-12">
            <div class="filters-content">
                <div class="row grid">
                    <c:forEach items="${listProducts }" var="item">
                    	<div class="col-lg-4 col-md-4 all gra">
	                      <div class="product-item">
	                      <div class="col-2 mt-2 discount-badge">-${item.discount.percent }%</div>	
	                        <a href="DetailServlet?id=${item.idProducts }"><img src="${item.image }" alt=""></a>
	                        <div class="down-content">
	                          <a href="href="DetailServlet?id=${item.idProducts }><h4>${item.name }</h4></a>
	                          <div class="price-content">
	                          	  <h6><strike><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫" /></strike></h6>
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
            </div>
          </div>
          <div class="col-md-12">
            <ul class="pages">
              <c:forEach begin="1" end="${pageCount }" var="i">
              		<li class="${i == index ? 'active' : '' }"><a href="${pageContext.request.contextPath }/ProductServlet?page=${i }">${i}</a></li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </div>
    </div>
