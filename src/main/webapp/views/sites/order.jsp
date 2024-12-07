<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mt-5">
    <div class="d-flex justify-content-center row">
        <div class="col-md-10">
            <c:if test="${not empty message}">
                <div class="alert alert-danger" role="alert">${message}</div>
            </c:if>
            <div class="rounded">
                <div class="table-responsive table-borderless">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Order #</th>
                                <th>Customer Name</th>
                                <th>Address</th>
                                <th>Phone</th>
                                <th>Total</th>
                                <th>Created</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <c:forEach items="${listOrders}" var="item">
                                <tr class="cell-1">
                                    <td>#${item.idOrders}</td>
                                    <td>${item.user.fullname}</td>
                                    <td>${item.address}</td>
                                    <td>${item.phone}</td>
                                    <td>${item.amount}</td>
                                    <td>${item.created}</td>
                                    <td>
                                        <span class="badge badge-success" style="color: red;">${item.status}</span>
                                    </td>
                                    <td>
                                        <a href="DetailOrderServlet?id=${item.idOrders}" class="btn btn-primary">Detail</a>
                                        <c:if test="${item.status != 'Giao thanh cong'}">
                                            <a style="color: white;" order-id="${item.idOrders}" onclick="confirmCancelOrder(this.getAttribute('order-id'))" class="btn btn-danger">Cancel</a>
                                        </c:if>
                                         <!-- Nút Ký hợp đồng -->
    									<a href="digitalsignature" class="btn btn-success">Ký hợp đồng</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- Form nhập mã order và mã hash xác nhận đơn hàng -->
			<form action="/btck-atbmhttt/confirmOrder" method="post">
			    <div class="mb-3">
			        <label for="orderId" class="form-label" >Enter Order ID</label>
			        <input type="text" class="form-control" id="orderId" name="orderId" placeholder="Nhập mã đơn hàng của bạn muốn xác nhận" required>
			    </div>
			    <div class="mb-3">
			        <label for="hashCode" class="form-label">Enter Confirmation Code</label>
			        <input type="text" class="form-control" id="hashCode" name="hashCode" placeholder="Nhập mã code tôi đã gửi bạn để xác nhận" required>
			    </div>
			    <button type="submit" class="btn btn-primary">Confirm Order</button>
			</form>
        </div>
    </div>
</div>



<!-- Modal xác nhận hủy đơn hàng -->
<div class="modal fade" id="cancelOrder" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Confirmation</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure to cancel this order?
            </div>
            <div class="modal-footer">
                <a id="yesOption" class="btn btn-primary">Yes</a>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>
            </div>
        </div>
    </div>
</div>

<script>
    // Xử lý sự kiện khi người dùng xác nhận hủy đơn hàng
    function confirmCancelOrder(id) {
        $('#yesOption').attr('href', '/btck-atbmhttt/CancelOrderServlet?id=' + id)
        $('#cancelOrder').modal('show');
    }
</script>
