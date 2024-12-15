<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<div class="container mt-5">
    <div class="d-flex justify-content-center row">
        <div class="col-md-10">
            <!-- Bảng danh sách đơn hàng -->
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
                                    <td><fmt:formatNumber value="${item.amount}" type="currency" currencySymbol="₫" /></td>
                                    <td>${item.created}</td>
                                    <td>
                                        <span class="badge badge-success" style="color: red;">${item.status}</span>
                                    </td>
                                    <td>
                                        <a href="DetailOrderServlet?id=${item.idOrders}" class="btn btn-primary">Detail</a>
                                        <c:if test="${item.status != 'Giao thanh cong'}">
                                            <a style="color: white;" order-id="${item.idOrders}" onclick="confirmCancelOrder(this.getAttribute('order-id'))" class="btn btn-danger">Cancel</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            
            
            <!-- Nút hiển thị các chức năng hỗ trợ đơn hàng -->
				<button type="button" class="btn-info" onclick="toggleForm('confirmOrderForm')">Xác nhận đơn hàng</button>
				<button type="button" class="btn-change" onclick="toggleForm('editOrderForm')">Chỉnh sửa đơn hàng</button>
				<button type="button" class="btn-sign" onclick="toggleForm('signContractForm')">Ký hợp đồng</button>
				<button type="button" class="btn-verify" onclick="toggleForm('verifySignatureForm')">Xác minh chữ ký</button>


            <!-- Form xác nhận đơn hàng -->
            <div id="confirmOrderForm" style="display: none">
            	 <form action="/btck-atbmhttt/confirmOrder" method="post">
                <div class="mb-3">
                    <label for="orderId" class="form-label">Nhập id đơn hàng của bạn</label>
                    <input type="text" class="form-control" id="orderId" name="orderId" placeholder="" required>
                </div>
                <div class="mb-3">
                    <label for="hashCode" class="form-label">Nhập đoạn mã hash (Đã gửi bên gmail của bạn)</label>
                    <input type="text" class="form-control" id="hashCode" name="hashCode" placeholder="" required>
                </div>
                <div class="mb-3">
                    <label for="publicKey" class="form-label">Nhập khóa công khai của bạn</label>
                    <input type="text" class="form-control" id="publicKey" name="publicKey" placeholder="" required>
                </div>
                <button type="submit" class="btn btn-primary">Xác nhận đơn hàng</button>
            </form>
            
            </div>
         

            <!-- Form chỉnh sửa đơn hàng -->
            
            <div id="editOrderForm" style="display: none">
          		  <form action="/btck-atbmhttt/OrderServlet" method="post">
                <input type="hidden" name="action" value="updateOrderStatus" />
                <div class="mb-3">
                    <label for="orderId" class="form-label">Nhập id đơn hàng của bạn</label>
                    <input type="text" class="form-control" id="orderId" name="orderId" placeholder="" value="${item.idOrders}" required>
                </div>
                <div class="mb-3">
                    <label for="address" class="form-label">Địa chỉ</label>
                    <input type="text" class="form-control" id="address" name="address" value="${item.address}" required>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Số điện thoại</label>
                    <input type="text" class="form-control" id="phone" name="phone" value="${item.phone}" required>
                </div>
                <button type="submit" class="btn btn-primary">Cập nhật đơn hàng</button>
            </form>  
            
            </div>
            
            <!-- Form Ký Hợp Đồng -->
				<div id="signContractForm" style="display: none;">
				    <form action="digitalsignature" method="post" enctype="multipart/form-data">
				        <div class="mb-3">
				            <label for="file" class="form-label">Chọn tệp cần ký:</label>
				            <input type="file" class="form-control" name="file" id="file" required>
				        </div>
				
				        <div class="mb-3">
				            <label for="privateKey" class="form-label">Nhập Private Key:</label>
				            <input type="text" class="form-control" name="privateKey" id="privateKey" required>
				        </div>
				
				        <input type="hidden" name="action" value="sign">
				        <button type="submit" class="btn btn-success">Ký chữ ký điện tử</button>
				    </form>
				</div>

			<!-- Form Xác Minh Chữ Ký -->
				<div id="verifySignatureForm" style="display: none;">
			    <form action="digitalsignature" method="post" enctype="multipart/form-data">
			        <div class="mb-3">
			            <label for="originalFilePath" class="form-label">Chọn tệp gốc</label>
			            <input type="file" class="form-control" id="originalFilePath" name="originalFilePath" required>
			        </div>
			
			        <div class="mb-3">
			            <label for="signedFilePath" class="form-label">Chọn tệp đã ký</label>
			            <input type="file" class="form-control" id="signedFilePath" name="signedFilePath" required>
			        </div>
			
			        <div class="mb-3">
			            <label for="publicKey" class="form-label">Nhập Public Key (Base64)</label>
			            <input type="text" class="form-control" id="publicKey" name="publicKey" required>
			        </div>
			
			        <input type="hidden" name="action" value="verify">
			        <button type="submit" class="btn btn-primary">Xác Minh Chữ Ký</button>
			    </form>
			</div>
            
            
            <style>
		     .btn-change, .btn-info, .btn-sign, .btn-verify {
		        background-color: aqua;
		        color: black; 
		        border: none;
		        font-size: 1.3rem;
		        padding: 12px 20p;
		        border-radius: 5px;
		        margin: 5px;
		    }
		
		    .btn-change:hover, .btn-info:hover, .btn-sign:hover, .btn-verify:hover {
		        background-color: #00ced1; 
		        color: white; 
    	}

				</style>
            
            <!-- Hàm ẩn hiện các chức năng support đơn hàng -->
				<script>
				    function toggleForm(formId) {
				        const forms = ['confirmOrderForm', 'editOrderForm', 'signContractForm', 'verifySignatureForm'];
				        forms.forEach(id => {
				            const form = document.getElementById(id);
				            if (formId === id) {
				                form.style.display = (form.style.display === "none" || form.style.display === "") ? "block" : "none";
				            } else {
				                form.style.display = "none"; // Ẩn các form khác
				            }
				        });
				    }
				</script>
            
          

            <!-- Modal xác nhận hủy đơn hàng -->
            <div class="modal fade" id="cancelOrder" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Xác nhận</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            Bạn có chắc chắn muốn hủy đơn hàng này không?
                        </div>
                        <div class="modal-footer">
                            <a id="yesOption" class="btn btn-primary">Có</a>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Không</button>
                        </div>
                    </div>
                </div>
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
