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

<div class="container">
	<div class="row col-6 offset-3">
		<h1 align="center"><f:message bundle="${bundle}" key="discount"/> Form</h1>
		<form action="DiscountFormServlet" method="post">
			<div class="mb-3">
				<label class="form-label"><f:message bundle="${bundle}" key="name"/></label> <input type="text"
					class="form-control" required="required" name="name">
			</div>
			<div class="mb-3">
				<label class="form-label"><f:message bundle="${bundle}" key="description"/></label> <input type="text"
					class="form-control" required="required" name="description">
			</div>
			<div class="mb-3">
				<label class="form-label"><f:message bundle="${bundle}" key="percent"/></label> <input type="number"
					class="form-control" min="0" max="100" required="required"
					name="percent">
			</div>
			<button type="submit" class="btn btn-primary"><f:message bundle="${bundle}" key="submit"/></button>
		</form>
	</div>
</div>