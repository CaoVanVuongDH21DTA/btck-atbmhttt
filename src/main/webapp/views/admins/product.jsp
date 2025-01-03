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
         <div class="row">
				<div class="col-2">
					<a href="ProductFormServlet" class="btn btn-success"><f:message bundle="${bundle}" key="newproduct"/></a>
				</div>
				<div class="col-7">
					<form action="ProductUploadServlet" method="post" enctype="multipart/form-data">
					<c:if test="${not empty sessionScope.message }">
						<div class="alert alert-danger" role="alert">
						  ${sessionScope.message}
						</div>
						 <%
					        // Xóa thông báo sau khi hiển thị
					        session.removeAttribute("message");
					    %>
					</c:if>
						<div class="input-group mb-3">
						  <input type="file" class="form-control" name="excelFile">
						</div>
						<input type="submit" class="btn btn-primary" value="<f:message bundle="${bundle}" key="import"/>"></input>	
					</form>
				</div>
				<div class="col-3">
					<form action="AdminProductServlet" method="post">
						<div class="mb-3 form-check">
							<input type="checkbox" class="form-check-input" name="reload">
							<label class="form-check-label mr-3"><f:message bundle="${bundle}" key="inActive"/></label>
							
						</div>
						<button type="submit" class="btn btn-primary"><f:message bundle="${bundle}" key="reload"/></button>
					</form>
				</div>
			</div>
            <div class="rounded">
            
                <div class="table-responsive table-borderless">
                    <table class="table">
                        <thead>
                            <tr>	                              
                                <th>#</th>
                                <th><f:message bundle="${bundle}" key="name"/></th>		
                                <th><f:message bundle="${bundle}" key="price"/></th>
                                <th><f:message bundle="${bundle}" key="discount"/></th>
                                <th><f:message bundle="${bundle}" key="isActive"/>?</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                          <c:forEach items="${listProducts }" var="item">
                          <form>
                          		 <tr class="cell-1 ${item.active == false ? 'table-danger' : '' }">
	                                <td>#${item.idProducts }</td>
	                                <td>${item.name }</td>
	                                <td><f:formatNumber value="${item.price}" type="currency" currencySymbol="₫" /></td>
	                                <td>${item.discount.percent } %</td>	 
	                                <td>${item.active }</td>                              
	                                <td>
	                                	<c:if test="${item.active == true }">
	                                		<a href="ProductUpdateServlet?id=${item.idProducts }" class="btn btn-primary"><f:message bundle="${bundle}" key="update"/></a>
										 <a product-id="${item.idProducts }" onclick="confirmDeleteProduct(this.getAttribute('product-id'))" class="btn btn-danger"
												>
												<f:message bundle="${bundle}" key="delete"/></a>
	                                	</c:if>
	                                	
	                                	<c:if test="${item.active == false }">
	                                		<a href="ProductRestoreServlet?id=${item.idProducts }" class="btn btn-success"><f:message bundle="${bundle}" key="restore"/></a>
	                                	</c:if>
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

<!-- Modal -->
<div class="modal fade" id="deleteProduct" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel"><f:message bundle="${bundle}" key="confirmation"/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Are you sure to delete this product?
        <f:message bundle="${bundle}" key="title-confirm"/>
      </div>
      <div class="modal-footer">
       <a id="yesOption" class="btn btn-primary"><f:message bundle="${bundle}" key="yes"/></a>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><f:message bundle="${bundle}" key="no"/></button>
      </div>
    </div>
  </div>
</div>
<script>
	function confirmDeleteProduct(id) {
		$('#yesOption').attr('href', '/btck-atbmhttt/ProductDeleteServlet?id=' + id)
		$('#deleteProduct').modal('show');
	}
</script>