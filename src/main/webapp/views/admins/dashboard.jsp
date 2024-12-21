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
    
<!DOCTYPE html>
<html lang="en">
<!-- Bootstrap icons -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">

    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <base href="/btck-atbmhttt/" />
        <title>${pageInfo.title }</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@latest/dist/style.css" rel="stylesheet" />
        <link href="${pageContext.request.contextPath }/bt-admin/css/styles.css" rel="stylesheet" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="assets/css/order.css">
    	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        	
        <style>
        	.toast-container {
			    z-index: 1050;
			    max-height: 100vh; 
			    overflow-y: auto; 
			    padding: 1rem;
			}
			
			.toast {
				position: relative;
			    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
			    border-radius: 5px;
			    overflow: hidden;
			    margin-bottom: 10px;
			    transition: top 0.5s ease-in-out;
			}
			
			.toast-header {
			    font-weight: bold;
			    border-bottom: 1px solid #ddd;
			}
			
			.toast-body {
			    font-size: 14px;
			    padding: 10px;
			}
			        	
        </style>
    </head>
    <body class="sb-nav-fixed">
        <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            <!-- Navbar Brand-->
            <a class="navbar-brand ps-3" href="AdminHomeServlet">Admin</a>
            <!-- Sidebar Toggle-->
            <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><i class="fas fa-bars"></i></button>
        
            <!-- Navbar-->
            <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"><i class="fas fa-user fa-fw"></i></a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">                       
                        <li><a class="dropdown-item" href="AdminLogoutServlet"><f:message bundle="${bundle}" key="logout" /></a></li>
                    </ul>
                </li>
                <li class="nav-item" style="position: relative;">
				    <button class="btn" id="lang-icon-button">
				        <i class="bi bi-globe" style="font-size: 26px; color: white;"></i>
				    </button>
				    <div class="select-lang-dropdown" id="lang-dropdown" style="display: none">
				        <ul class="selected">
						    <li><a href="javascript:void(0)" onclick="changeLanguage('vi')">Tiếng Việt</a></li>
						    <li><a href="javascript:void(0)" onclick="changeLanguage('en')">English</a></li>
						</ul>
				    </div>
				</li>
            </ul>
        </nav>
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                    <div class="sb-sidenav-menu">
                        <div class="nav">
                            <div class="sb-sidenav-menu-heading"><f:message bundle="${bundle}" key="core" /></div>
                            <a class="nav-link" href="AdminHomeServlet">
                                <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                                <f:message bundle="${bundle}" key="overview" />
                            </a>
                            <div class="sb-sidenav-menu-heading"><f:message bundle="${bundle}" key="manage" /></div>
                            <a class="nav-link collapsed" href="AdminUserServlet" aria-expanded="false" aria-controls="collapsePages">
                                <div class="sb-nav-link-icon"><i class="fas fa-book-open"></i></div>
                                <f:message bundle="${bundle}" key="account" />
                            </a>
                           
                            <a class="nav-link collapsed" href="AdminProductServlet" aria-expanded="false" aria-controls="collapsePages">
                                <div class="sb-nav-link-icon"><i class="fas fa-book-open"></i></div>
                                <f:message bundle="${bundle}" key="products" />
                            </a>
                             
                             <a class="nav-link collapsed" href="AdminDiscountServlet" aria-expanded="false" aria-controls="collapsePages">
                                <div class="sb-nav-link-icon"><i class="fas fa-book-open"></i></div>
                                <f:message bundle="${bundle}" key="discount" />
                            </a> 
                            
                            <a class="nav-link collapsed" href="AdminOrderServlet" aria-expanded="false" aria-controls="collapsePages">
                                <div class="sb-nav-link-icon"><i class="fas fa-book-open"></i></div>
                                <f:message bundle="${bundle}" key="orders" />
                            </a>  
                            <a class="nav-link collapsed" href="AdminSupportServlet" aria-expanded="false" aria-controls="collapsePages">
                                <div class="sb-nav-link-icon"><i class="fas fa-book-open"></i></div>
                                <f:message bundle="${bundle}" key="support" />
                            </a>     
                        </div>
                    </div>
                    <div class="sb-sidenav-footer">
					    <div class="small"><f:message bundle="${bundle}" key="logged" />:</div>
					    ${admin}
					</div>

                </nav>
            </div>
            <div id="layoutSidenav_content">
                <main>
                    <jsp:include page="${pageInfo.contentUrl }"></jsp:include>
                </main>
                <footer class="py-4 bg-light mt-auto">
                    <div class="container-fluid px-4">
                        <div class="d-flex align-items-center justify-content-between small">
                            <div class="text-muted">Copyright &copy; Your Website 2024</div>
                            <div>
                                <a href="#">Privacy Policy</a>
                                &middot;
                                <a href="#">Terms &amp; Conditions</a>
                            </div>
                        </div>
                    </div>
                </footer>
            </div>
            
			<!-- Phần thông báo (Toast) -->
			<div class="toast-container position-fixed top-0 end-0 p-3">
			    <div class="toast" id="toast-template" style="display: none;">
			        <div class="toast-header bg-primary text-white">
			            <strong class="me-auto"><f:message bundle="${bundle}" key="notification" /></strong>
			            <small class="time-notification"></small>
			            <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
			        </div>
			        <div class="toast-body">
			            <p><f:message bundle="${bundle}" key="notification" />: <span class="order-id"></span></p>
			            <p><f:message bundle="${bundle}" key="notification" />: <span class="buyer-name"></span></p>
			        </div>
			    </div>
			</div>
        </div>
        
        <!-- Thêm SweetAlert2 từ CDN -->
		<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
		<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
		<!-- Moment.js -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
		<!-- Moment.js locale tiếng Việt -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/locale/vi.min.js"></script>
		
		<script type="text/javascript">
			<%
			    if (session.getAttribute("admin") == null) {
			        response.sendRedirect("/btck-atbmhttt/AdminSigninServlet");
			        return;
			    }
			%>
		
		    moment.locale('vi'); 
		
		    let lastNotifiedOrderIds = []; // Lưu các ID đơn hàng đã thông báo
		
		    // Hàm tính toán thời gian đã trôi qua từ thời điểm tạo đơn hàng
		    function formatTimeDifference(createdTime) {
		        const now = moment();
		        const createdDate = moment(createdTime);
		        return createdDate.from(now); 
		    }
		
		    // Hàm thông báo đơn hàng mới
		    function notifyNewOrder(order) {
		        // Kiểm tra xem đã thông báo cho đơn hàng này chưa
		        if (lastNotifiedOrderIds.includes(order.idOrders)) {
		            return; // Nếu đã thông báo rồi, không thông báo lại
		        }
		
		        // Lưu ID của đơn hàng đã thông báo
		        lastNotifiedOrderIds.push(order.idOrders);
		
		        const toastTemplate = $('#toast-template');
		        const newToast = toastTemplate.clone();
		        newToast.attr('id', `toast-${order.idOrders}`);
		        newToast.find('.order-id').text(order.idOrders);
		        newToast.find('.buyer-name').text(order.user.fullname);
		
		        // Tính thời gian chênh lệch
		        const timeDifference = formatTimeDifference(order.created);
		        newToast.find('.time-notification').text(timeDifference);
		
		        // Hiển thị thông báo
		        newToast.css('display', 'block');
		        $('.toast-container').prepend(newToast);
		
		        newToast.toast({
		            delay: 10000
		        }).toast('show');
		        
		        setTimeout(function() {
		            newToast.css('display', 'none'); // Ẩn toast sau khi delay
		        }, 10000);
		        
		        newToast.on('hidden.bs.toast', function() {
		            // Di chuyển các toast phía dưới lên khi toast này bị đóng
		            $(this).prevAll().each(function(index, toast) {
		                $(toast).stop(true, true).animate({ top: "0px" }, 500); // Di chuyển các toast phía dưới lên
		            });
		        });
		    }
		
		    // Kiểm tra đơn hàng mới
		    function checkNewOrder() {
		        $.ajax({
		            url: 'CheckNewOrderServlet',
		            method: 'GET',
		            success: function (response) {
		                // Kiểm tra thông báo đơn hàng mới
		                if (response.newOrderNotification) {
		                    response.orderIds.forEach(order => {
		                        // Hiển thị thông báo cho các đơn hàng mới
		                        if (order.status === "Đã duyệt" && !lastNotifiedOrderIds.includes(order.idOrders)) {
		                            notifyNewOrder(order);
		                        }
		                    });
		                }
		
		                // Kiểm tra thông báo đơn hàng mới nhất
		                if (response.latestOrderNotification) {
		                    const latestOrder = response.latestOrder;
		                    // Hiển thị thông báo cho đơn hàng mới nhất
		                    if (latestOrder.status === "Đã duyệt" && !lastNotifiedOrderIds.includes(latestOrder.idOrders)) {
		                        notifyNewOrder(latestOrder);  
		                    }
		                }
		            },
		            error: function () {
		                console.error("Lỗi khi kiểm tra đơn hàng mới.");
		            },
		            
		            complete: function () {
		                setTimeout(checkNewOrder, 3000);  
		            }
		        });
		    }
		
		 	// Kiểm tra nếu ${admin} có giá trị
		    <%
		        String adminUsername = (String) session.getAttribute("admin");
		        if (adminUsername != null && !adminUsername.isEmpty()) {
		    %>
		        // Nếu có giá trị, gọi hàm checkNewOrder
		        checkNewOrder();
		    <%
		        }
		    %>
		    
		 // Toggle the side navigation
		    const sidebarToggle = document.body.querySelector('#sidebarToggle');
		    if (sidebarToggle) {
		        sidebarToggle.addEventListener('click', event => {
		            event.preventDefault();
		            document.body.classList.toggle('sb-sidenav-toggled');
		            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
		        });
		    }
		    
		 	// Kiểm tra và xử lý khi nhấn vào lang-icon-button
			$('#lang-icon-button').click(function (event) {
			    const langDropdown = $('#lang-dropdown');
			    langDropdown.toggle(); // Hiển thị hoặc ẩn menu
			});
		 
			// Ẩn lang-dropdown khi click ngoài
		    $(document).click(function (event) {
		        if (!$(event.target).closest('#lang-icon-button, #lang-dropdown').length) {
		            $('#lang-dropdown').hide(); // Ẩn dropdown nếu click ngoài
		        }
		    });
			
		    function changeLanguage(lang) {
			    $.ajax({
			        url: 'changeLanguage',
			        type: 'POST',
			        data: { lang: lang },
			        success: function(response) {
			            if (response.status === "success") {
			                location.reload(); // Reload lại trang để áp dụng ngôn ngữ mới
			            } else {
			                alert("Failed to change language: " + response.message);
			            }
			        },
			        error: function() {
			            alert("Error occurred while changing language.");
			        }
			    });
			}
		</script>
  </body>
</html>
