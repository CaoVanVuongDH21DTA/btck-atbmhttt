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
				<form action="OrderSearchServlet" method="post">
					<div class="mb-3 form-check">
						<div class="form-check form-check-inline">
							<input class="form-check-input" name="statuses" type="checkbox"
								value="Cho duyet"> <label class="form-check-label"><f:message bundle="${bundle}" key="waitingforapproval"/></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" name="statuses" type="checkbox"
								value="Da duyet"> <label class="form-check-label"><f:message bundle="${bundle}" key="approved"/></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" name="statuses" type="checkbox"
								value="Dang van chuyen"> <label class="form-check-label"><f:message bundle="${bundle}" key="intransit"/></label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" name="statuses" type="checkbox"
								value="Giao thanh cong"> <label class="form-check-label"><f:message bundle="${bundle}" key="successfuldelivery"/></label>
						</div>
						<button type="submit" class="btn btn-primary"><f:message bundle="${bundle}" key="reload"/></button>
					</div>
				</form>

			</div>
			<div class="rounded">
				<div class="table-responsive table-borderless">
					<table class="table">
						<thead>
							<tr>
								<th><f:message bundle="${bundle}" key="id_order"/></th>
								<th><f:message bundle="${bundle}" key="name"/></th>
								<th><f:message bundle="${bundle}" key="address"/></th>
								<th><f:message bundle="${bundle}" key="phone"/></th>
								<th><f:message bundle="${bundle}" key="total"/></th>
								<th><f:message bundle="${bundle}" key="created"/></th>
								<th><f:message bundle="${bundle}" key="status"/></th>
								<th></th>
							</tr>
						</thead>
						<tbody class="table-body">
							<c:forEach items="${listOrders }" var="item">
								<form action="AdminUpdateOrderServlet?id=${item.idOrders } "
									method="post">
									<tr
										class="cell-1 ${item.status == 'Chờ duyệt' ? 'table-primary' : '' }
										${item.status == 'Đã duyệt' ? 'table-danger' : '' }
										${item.status == 'Đang vận chuyển' ? 'table-warning' : '' }
										${item.status == 'Giao thành công' ? 'table-green' : '' }
									">
										<td>#${item.idOrders }</td>
										<td>${item.user.fullname }</td>
										<td>${item.address }</td>
										<td>${item.phone }</td>
										<td><f:formatNumber value="${item.amount}" type="currency" currencySymbol="₫" /></td>
										<td>${item.created }</td>
										<td>
										    <select name="status" class="status-select" data-current-status="${item.status}">
										        <c:forEach items="${listStatus}" var="str">
										            <option value="${str}" ${str == item.status ? 'selected="selected"' : ''}>${str}</option>
										        </c:forEach>
										    </select>
										</td>

										<td>
											<c:if test="${item.status != 'Giao thành công' }">
												<input type="submit" class="btn btn-primary"
													value="Update"></input>
												<a style="color: white;" order-id="${item.idOrders }"
													onclick="confirmCancelOrder(this.getAttribute('order-id'))"
													" class="btn btn-danger">Cancel</a>
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
<div class="modal fade" id="cancelOrder" tabindex="-1"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Confirmation</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="Close"></button>
			</div>
			<div class="modal-body">Are you sure to cancel this order?</div>
			<div class="modal-footer">
				<a id="yesOption" class="btn btn-primary">Yes</a>
				<button type="button" class="btn btn-secondary"
					data-bs-dismiss="modal">No</button>
			</div>
		</div>
	</div>
</div>
<script>
	function confirmCancelOrder(id) {
		$('#yesOption').attr('href',
				'/btck-atbmhttt/OrderCancelServlet?id=' + id)
		$('#cancelOrder').modal('show');
	}
</script>

<script>
	document.querySelectorAll('.status-select').forEach(selectElement => {
	    const currentStatus = selectElement.getAttribute('data-current-status');
	
	    selectElement.querySelectorAll('option').forEach(option => {
	        if (currentStatus === 'Chờ duyệt' && option.value === 'Chờ duyệt') {
	            option.style.display = 'none';
	        } else if (currentStatus === 'Đã duyệt' && (option.value === 'Chờ duyệt' || option.value === 'Đã duyệt')) {
	            option.style.display = 'none';
	        } else if (currentStatus === 'Đang vận chuyển' && (option.value === 'Chờ duyệt' || option.value === 'Đã duyệt' || option.value === 'Đang vận chuyển')) {
	            option.style.display = 'none';
	        } else if (currentStatus === 'Giao thành công') {
	            selectElement.disabled = true;
	        }
	    });
	
	    selectElement.removeEventListener('change', handleStatusChange);
	
	    function handleStatusChange() {
	        const newStatus = this.value;
	        this.querySelectorAll('option').forEach(option => {
	            option.style.display = '';
	            if (newStatus === 'Chờ duyệt' && option.value === 'Chờ duyệt') {
	                option.style.display = 'none';
	            } else if (newStatus === 'Đã duyệt' && (option.value === 'Chờ duyệt' || option.value === 'Đã duyệt')) {
	                option.style.display = 'none';
	            } else if (newStatus === 'Đang vận chuyển' && (option.value === 'Chờ duyệt' || option.value === 'Đã duyệt' || option.value === 'Đang vận chuyển')) {
	                option.style.display = 'none';
	            } else if (newStatus === 'Giao thành công') {
	                this.disabled = true;
	            }
	        });
	
	        this.setAttribute('data-current-status', newStatus);
	    }
	});
</script>
