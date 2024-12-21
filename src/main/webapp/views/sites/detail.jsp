<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
.rate-product {
  display: flex;
  align-items: center;
  gap: 10px;
}

.radio {
  display: flex;
  justify-content: center;
  gap: 10px;
  flex-direction: row;
}

.radio > input {
  position: absolute;
  appearance: none;
}

.radio > label {
  cursor: pointer;
  font-size: 30px;
  position: relative;
  display: inline-block;
  transition: transform 0.3s ease;
}

.radio > label > svg {
  fill: #E4E5E9;
  transition: fill 0.3s ease;
}

.radio > label:hover {
  transform: scale(1.2);
  animation: pulse 0.6s infinite alternate;
}

.radio > label:hover > svg {
  fill: #ff9e0b;
  filter: drop-shadow(0 0 15px rgba(255, 158, 11, 0.9));
  animation: shimmer 1s ease infinite alternate;
}

/* Highlight stars on hover */
.radio > label:hover,
.radio > label:hover ~ label {
  fill: #ff9e0b;
}

/* Highlight stars when the input is checked */
.radio > input:checked + label > svg {
  fill: #ff9e0b;
  filter: drop-shadow(0 0 15px rgba(255, 158, 11, 0.9));
  animation: pulse 0.8s infinite alternate;
}

.radio > input:checked + label ~ label > svg,
.radio > input:checked + label > svg {
  fill: #ff9e0b;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  100% {
    transform: scale(1.2);
  }
}

@keyframes shimmer {
  0% {
    filter: drop-shadow(0 0 10px rgba(255, 158, 11, 0.5));
  }
  100% {
    filter: drop-shadow(0 0 20px rgba(255, 158, 11, 1));
  }
}

/* Apply fill color for checked input */
.radio > input:checked + label,
.radio > input:checked + label ~ label {
  fill: #ff9e0b;
}

/* Change color of stars when checked */
.radio input:checked ~ label svg {
  fill: #ffa723; 
}

/* Adjust the color of the next label on hover */
.radio > input:checked + label:hover,
.radio > input:checked + label:hover ~ label {
  fill: #e58e09;
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

<div class="row">
	<div class="product col-md-4 offset-1">
		<a class="font-size-sm" href="ProductServlet">
			<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-chevron-left" style="width: 1rem; height: 1rem;">
				<polyline points="15 18 9 12 15 6"></polyline>
			</svg>
			Continue shopping
		</a>
		<div class="product-item">
			<div class="col-2 mt-2 discount-badge">-${item.discount.percent }%</div>
			<a href="DetailServlet?id=${item.idProducts }"><img src="${item.image }" alt=""></a>
			<div class="down-content">
				<a href="DetailServlet?id=${item.idProducts }"><h4>${item.name }</h4></a>
				<div class="price-content">
               	    <h6><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫" /></h6>
                	<h5 style="color: red">
                	<fmt:formatNumber value="${item.price * (100 - item.discount.percent)/100 }" type="currency" currencySymbol="₫" /></h5>
                </div>
				<p>${item.description }</p>
				<div class="bottom-product">
					<a href="AddCartServlet?id=${item.idProducts }" class="btn btn-primary"><f:message bundle="${bundle }" key="addCart"/></a>
					<div class="review">
						<span><f:message bundle="${bundle }" key="view"/> (${item.view })</span>
						<span class="rate-comment" style="display: flex; gap: 10px;">
						    <div style="display: flex;">
						    	<fmt:formatNumber value="${item.averageStar}" type="number" maxFractionDigits="1" />
						    	<i class="bi bi-star-fill"></i>
						    </div>
							<f:message bundle="${bundle }" key="rate"/> (${item.rate })
						</span>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="col-1"></div>
	
	<c:if test="${not empty message}">
	    <div class="alert alert-${alertType} alert-dismissible fade show custom-alert position-fixed top-0 end-0 m-3" role="alert">
	        <div class="alert-content d-flex align-items-center">
	            <div style="display: flex; flex-direction: column;">
	            	<div style="display: flex; flex-direction: row; align-items: center;">
	            		<i class="alert-icon me-2"></i>
		            	<p style="text-transform: uppercase;">AlertType: ${alertType}</p>
	            	</div>
		            <span>${message}</span>
	            </div>
	        </div>
	    </div>
	    <c:remove var="message" scope="session" />
	    <c:remove var="alertType" scope="session" />
	</c:if>
	
	<div class="col-4">
		<h3 class="mt-3" style="color: blue; font-weight: bolder;">Reviews</h3>
		<ul class="list-group mb-3">
			<c:forEach items="${listReviews }" var="item">
				<li class="list-group-item list-group-item-primary">${item.user.fullname } said:</li>
				<li class="list-group-item list-group-item-danger">${item.comment }</li>
			</c:forEach>
		</ul>
		<form action="CommentServlet?id=${item.idProducts }" method="post" onsubmit="this.querySelector('input[type=submit]').disabled = true;">
		    <div class="form-floating mb-3">
		        <textarea class="form-control" name="comment" placeholder="Leave a comment here" id="floatingTextarea2" style="height: 100px"></textarea>
		    </div>
			<div class="rate-product">
				<label for="rating" class="form-label">Rate this product:</label>
			    <div class="radio mb-3">
					<c:forEach begin="1" end="5" var="i">
						<input value="${i}" name="rating" type="radio" id="rating-${i}" />
						<label title="${i} star(s)" for="rating-${i}">
							<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 576 512" fill="${i <= 3 ? '#FFD700' : '#E4E5E9'}">
								<path d="M316.9 18C311.6 7 300.4 0 288.1 0s-23.4 7-28.8 18L195 150.3 51.4 171.5c-12 1.8-22 10.2-25.7 21.7s-.7 24.2 7.9 32.7L137.8 329 113.2 474.7c-2 12 3 24.2 12.9 31.3s23 8 33.8 2.3l128.3-68.5 128.3 68.5c10.8 5.7 23.9 4.9 33.8-2.3s14.9-19.3 12.9-31.3L438.5 329 542.7 225.9c8.6-8.5 11.7-21.2 7.9-32.7s-13.7-19.9-25.7-21.7L381.2 150.3 316.9 18z"></path>
							</svg>
						</label>
					</c:forEach>
				</div>
			</div>
		    <input type="submit" class="btn btn-primary" value="Comment"></input>
		</form>
	</div>
</div>

<script>
// Lắng nghe sự kiện khi chọn một input (radio button)
document.querySelectorAll('.radio input').forEach(input => {
  input.addEventListener('change', function() {
    const ratingValue = this.value;

    // Đổi màu cho tất cả các label trước sao đã chọn
    document.querySelectorAll('.radio label').forEach((label, index) => {
      if (index < ratingValue) {
        label.querySelector('svg').style.fill = '#ff9e0b'; // Màu sao sáng
      } else {
        label.querySelector('svg').style.fill = '#E4E5E9'; // Màu sao mờ
      }
    });
  });
});
</script>
