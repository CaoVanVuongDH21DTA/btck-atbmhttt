<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	/* Dialog (Hộp thoại thông báo) */
	#dialog-order {
	    display: none;
	    position: fixed;
	    top: 50%;
	    left: 50%;
	    transform: translate(-50%, -50%);
	    background-color: white;
	    padding: 20px;
	    border: 1px solid #ccc;
	    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
	    text-align: center;
	    z-index: 999999;
	    width: 300px;

			& .message {
			    margin-bottom: 20px;
			    font-size: 16px;
			    color: #333;
			}

		    & button {
		    padding: 10px 20px;
		    background-color: #4CAF50;
		    color: white;
		    border: none;
		    border-radius: 4px;
		    cursor: pointer;
		    
			    &:hover {
			    background-color: #45a049;
			}
		}
	}
	#address_order {
	    position: relative; 
	    max-width: 100px;  
	    overflow: hidden; 
	    p {
	        display: -webkit-box;
	        -webkit-line-clamp: 1; 
	        -webkit-box-orient: vertical;
	        overflow: hidden;
	        text-overflow: ellipsis;  
	        font-size: 16px;
	        margin: 0; 
	    }
	
	    .address_full {
	        position: absolute; 
	        top: 100%;  
	        left: 0;
	        background-color: #f8f9fa;
	        padding: 10px;
	        border: 1px solid #ddd;
	        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	        z-index: 10;
	        max-width: 300px;  
	        font-size: 14px;
	        white-space: normal;  
	        word-wrap: break-word;
	    }
	}
	
	.btn-change, .btn-info, .btn-sign, .btn-verify {
		        background-color: aqua;
		        color: black; 
		        border: none;
		        font-size: 1.3rem;
		        padding: 12px 20p;
		        border-radius: 8px;
		        margin: 5px;
		    }
		
		    .btn-change:hover, .btn-info:hover, .btn-sign:hover, .btn-verify:hover {
		        background-color: #00ced1; 
		        color: white; 
    	}
	.custom-alert {
	    z-index: 999999; /* Luôn hiển thị trên cùng */
	    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Đổ bóng */
	    border-radius: 8px; /* Bo góc */
	    padding: 1rem; /* Khoảng cách nội dung */
	    min-width: 300px; /* Chiều rộng tối thiểu */
	    max-width: 500px; /* Chiều rộng tối đa */
	    animation: fadeInOut 5s ease-in-out forwards; /* Hiệu ứng xuất hiện và tự ẩn */
	    opacity: 0; /* Trạng thái ban đầu */
	}
	
	.custom-alert p{
		text-align: center;
		font-size: 14px;
	}
	
	/* Hiệu ứng fade-in và fade-out */
	@keyframes fadeInOut {
	    0% {
	        opacity: 0;
	        transform: translateY(-20px); /* Xuất hiện từ phía trên */
	    }
	    10%, 90% {
	        opacity: 1;
	        transform: translateY(0); /* Hiển thị tại vị trí */
	    }
	    100% {
	        opacity: 0;
	        transform: translateY(-20px); /* Ẩn trở lại */
	    }
	}
	
	/* Icon đại diện cho từng loại thông báo */
	.alert-success .alert-icon::before {
	    content: "✔"; /* Icon thành công */
	    color: #28a745; /* Màu xanh */
	    font-size: 1.5rem;
	}
	
	.alert-danger .alert-icon::before {
	    content: "✖"; /* Icon thất bại */
	    color: #dc3545; /* Màu đỏ */
	    font-size: 1.5rem;
	}
	
	.alert-warning .alert-icon::before {
	    content: "⚠"; /* Icon cảnh báo */
	    color: #ffc107; /* Màu vàng */
	    font-size: 1.5rem;
	}
	
	.alert-info .alert-icon::before {
	    content: "ℹ"; /* Icon thông tin */
	    color: #17a2b8; /* Màu xanh dương nhạt */
	    font-size: 1.5rem;
	}
	
	/* Điều chỉnh cho nút close */
	.btn-close {
	    color: #000; /* Màu sắc nút đóng */
	}

</style>

<div class="container mt-5">
    <div class="d-flex justify-content-center row">
        <div class="col-md-10" style="width:100%">
			<c:if test="${not empty message}">
			    <div class="alert alert-${alertType} alert-dismissible fade show custom-alert position-fixed top-0 end-0 m-3" role="alert">
			        <div class="alert-content d-flex align-items-center">
			            <div style="display: flex; flex-direction: column;">
			            	<div style="display: flex; flex-direction: row">
			            		<i class="alert-icon me-2"></i>
				            	<p >AlertType: ${alertType}</p>
			            	</div>
				            <span>${message}</span>
			            </div>
			        </div>
			        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
			    </div>
			    <c:remove var="message" scope="session" />
			    <c:remove var="alertType" scope="session" />
			</c:if>
            <div class="rounded">
                <div class="table-responsive table-borderless">
				<table class="table">
				    <thead>
				        <tr>
				            <th><f:message bundle="${bundle }" key="id_order"/></th>
				            <th><f:message bundle="${bundle }" key="name_customer"/></th>
				            <th><f:message bundle="${bundle }" key="address_order"/></th>
				            <th><f:message bundle="${bundle }" key="phone"/></th>
				            <th><f:message bundle="${bundle }" key="total"/></th>
				            <th><f:message bundle="${bundle }" key="created"/></th>
				            <th><f:message bundle="${bundle }" key="status"/></th>
				            <th><f:message bundle="${bundle }" key="action"/></th>
				        </tr>
				    </thead>
				    <tbody class="table-body">
				        <c:forEach items="${listOrders}" var="item">
				            <tr class="cell-1">
				                <td>${item.idOrders}</td>
				                <td>${item.user.fullname}</td>
				                <td id="address_order">
								    <p>${item.address}</p>
								    <div class="address_full">${item.address}</div>
								</td>

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
				
				<!-- Pagination Controls -->
				<div class="pagination" style="justify-content: center; align-items: center; gap: 5px;">
				
					<c:if test="${currentPage > 2}">
				        <!-- Link Previous -->
				        <a href="OrderServlet?page=1" class="btn"><i class="bi bi-chevron-double-left"></i></a>
				    </c:if>
				    <c:if test="${currentPage > 1}">
				        <!-- Link Previous -->
				        <a href="OrderServlet?page=${currentPage - 1}" class="btn"><i class="bi bi-chevron-left"></i></a>
				    </c:if>
				    
				    <!-- Hiển thị thông tin trang hiện tại và tổng số trang -->
				    <span>Trang ${currentPage} trong ${totalPages}</span>
				    
				    <c:if test="${currentPage < totalPages}">
				        <!-- Link Next -->
				        <a href="OrderServlet?page=${currentPage + 1}" class="btn"><i class="bi bi-chevron-right"></i></a>
				    </c:if>
				    <c:if test="${currentPage < (totalPages - 1)}">
				        <!-- Link Next -->
				        <a href="OrderServlet?page=${totalPages}" class="btn"><i class="bi bi-chevron-double-right"></i></a>
				    </c:if>
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

        </div>
    </div>
</div>


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

<!-- Dialog -->
<div id="dialog-order">
    <div class="message"></div>
    <button onclick="closeDialog()">Đóng</button>
</div>

<script>
    // Xử lý sự kiện khi người dùng xác nhận hủy đơn hàng
    function confirmCancelOrder(id) {
        $('#yesOption').attr('href', '/btck-atbmhttt/CancelOrderServlet?id=' + id)
        $('#cancelOrder').modal('show');
    }
    
 	// Xử lý sự kiện khi người dùng xác nhận đơn hàng
    function submitConfirmOrder(event) {
 		//ngăn chặn việc gửi dữ liệu sang trang mới
    	event.preventDefault(); 
 		
	    var orderId = document.getElementById("orderId").value;
	    var hashCode = document.getElementById("hashCode").value;
	    var publicKey = document.getElementById("publicKey").value;
	
	    // Gửi dữ liệu bằng AJAX
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "/btck-atbmhttt/confirmOrder", true);
	    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState === 4 && xhr.status === 200) {
	            var response = JSON.parse(xhr.responseText);
	            
	            // Kiểm tra kết quả trả về
	            if (response.status === "success") {
	                // Hiển thị hộp thoại với thông báo thành công
	                document.getElementById("dialog-order").querySelector(".message").innerHTML = response.message;
	                showDialog();
	            } else {
	                alert(response.message); // Thông báo lỗi nếu có
	            }
	        }
	    };
	
	    // Gửi dữ liệu
	    xhr.send("orderId=" + encodeURIComponent(orderId) + 
	             "&hashCode=" + encodeURIComponent(hashCode) + 
	             "&publicKey=" + encodeURIComponent(publicKey));
	}
 	
    function showDialog() {
        document.getElementById("overlay").style.display = "block";
        document.getElementById("dialog-order").style.display = "block";
    }

    // Đóng hộp thoại mà không reload trang
    function closeDialog() {
        document.getElementById("overlay").style.display = "none";
        document.getElementById("dialog-order").style.display = "none";
    }

 // Kiểm tra chiều rộng của phần tử và hiển thị tooltip nếu bị cắt bớt
    document.addEventListener('DOMContentLoaded', function () {
        var addressElement = document.querySelector('#address_order');
        var address_full = addressElement.querySelector('.address_full');
        var pElement = addressElement.querySelector('p');

        // Hàm kiểm tra xem văn bản có bị cắt bớt không
        function checkAddressOverflow() {
            if (pElement.scrollWidth > pElement.clientWidth) {
                address_full.style.display = 'block';  // Hiển thị tooltip nếu bị cắt bớt
            } else {
                address_full.style.display = 'none';  // Ẩn tooltip nếu không bị cắt bớt
            }
        }

        // Kiểm tra khi trang tải xong
        checkAddressOverflow();

        // Kiểm tra lại khi cửa sổ thay đổi kích thước
        window.addEventListener('resize', function () {
            checkAddressOverflow();
        });
    });
 
    <!-- Hàm ẩn hiện các chức năng support đơn hàng -->
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