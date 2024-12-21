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
    
<div class="container-fluid px-4">
    <h1 class="mt-4"><f:message bundle="${bundle}" key="dashboard"/></h1>
    <ol class="breadcrumb mb-4">
        <li class="breadcrumb-item active"><f:message bundle="${bundle}" key="dashboard"/></li>
    </ol>
    <div class="row">
        <div class="col-xl-3 col-md-6">
            <div class="card bg-primary text-white mb-4">
                <div class="card-body"><f:message bundle="${bundle}" key="account"/></div>
                <div class="card-footer d-flex align-items-center justify-content-between">
                    <span>${count_User }</span>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-warning text-white mb-4">
                <div class="card-body"><f:message bundle="${bundle}" key="products"/></div>
                <div class="card-footer d-flex align-items-center justify-content-between">
                    <span>${count_Product }</span>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6">
            <div class="card bg-success text-white mb-4">
                <div class="card-body"><f:message bundle="${bundle}" key="orders"/></div>
                <div class="card-footer d-flex align-items-center justify-content-between">
                    <span>${count_Order }</span>
                </div>
            </div>
        </div>
    </div>                                            
</div>