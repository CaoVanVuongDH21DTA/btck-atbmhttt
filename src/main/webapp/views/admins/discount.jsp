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
<div class="container mt-5">
    <div class="d-flex justify-content-center row">
        <div class="col-md-10">
        <a class="btn btn-success" href="DiscountFormServlet"><f:message bundle="${bundle}" key="newdiscount"/></a>
        <c:if test="${not empty message }">
								<div class="alert alert-danger" role="alert">
								  ${message }
								</div>
							</c:if>
            <div class="rounded">
            
                <div class="table-responsive table-borderless">
                    <table class="table">
                        <thead>
                            <tr>	                              
                                <th>#</th>
                                <th><f:message bundle="${bundle}" key="name"/></th>		
                                <th><f:message bundle="${bundle}" key="description"/></th>
                                 <th><f:message bundle="${bundle}" key="percent"/></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                          <c:forEach items="${listDiscounts }" var="item">
                          <form>
                          		 <tr class="cell-1">
	                                <td>#${item.idDiscounts }</td>
	                                <td>${item.name }</td>
	                                <td>${item.description }</td>
	                                <td>${item.percent } %</td>	                                
	                                <td>
	                                	<a href="DiscountUpdateServlet?id=${item.idDiscounts }" class="btn btn-primary"><f:message bundle="${bundle}" key="update"/></a>
										 <a href="" class="btn btn-danger"><f:message bundle="${bundle}" key="delete"/></a>
									</td>
	                            </tr>
	                             </form>
                          </c:forEach>
                           
                        </tbody>
                    </table>
                </div>
               
            </div>
        </div>
    </div>
</div>