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
		<form action="ProductFormServlet" method="post">
			<div class="mb-3">
				<label class="form-label fw-bold"><f:message bundle="${bundle}" key="name"/></label> <input type="text" name="name"
					class="form-control" required="required">
			</div>
			<div class="mb-3">
				<label class="form-label fw-bold"><f:message bundle="${bundle}" key="price"/></label> <input type="number" name="price"
					class="form-control" required="required">
			</div>
			<div class="mb-3">
				<label class="form-label fw-bold"><f:message bundle="${bundle}" key="description"/></label>
				<textarea class="form-control" placeholder="<f:message bundle="${bundle}" key="content-description"/>"
					name="description" style="height: 100px" required="required"></textarea>
			</div>
			<div class="mb-3">
				<label class="form-label fw-bold"><f:message bundle="${bundle}" key="brand"/></label> <select
					class="form-control" name="id_brand" aria-label="Default select example" required>
					<option value=""></option>
					<c:forEach items="${listBrands }" var="item" varStatus="loop">
						<option value="${item.idBrands}">${item.name }</option>
					</c:forEach>
				</select>
			</div>
			<div class="mb-3">
				<label class="form-label fw-bold"><f:message bundle="${bundle}" key="category"/></label> <select
					class="form-control" name="id_category" aria-label="Default select example" required>
					<option value=""></option>
					<c:forEach items="${listCategories }" var="item" varStatus="loop">
						<option value="${item.idCategorys }">${item.name }</option>
					</c:forEach>
				</select>
			</div>
			<div class="mb-3">
				<label class="form-label fw-bold"><f:message bundle="${bundle}" key="discount"/></label> <select
					class="form-control" name="id_discount" aria-label="Default select example" required>
					<option value=""></option>
					<c:forEach items="${listDiscounts }" var="item" varStatus="loop">
						<option value="${item.idDiscounts}"}>${item.name }</option>
					</c:forEach>
				</select>
			</div>
			<div class="mb-3">
				<label class="form-label fw-bold"><f:message bundle="${bundle}" key="image"/></label> 
				<input type="text" name="image" class="form-control" required="required">
			</div>
			<input type="submit" class="btn btn-primary" value="<f:message bundle="${bundle}" key="submit"/>"></input>
		</form>
	</div>
</div>