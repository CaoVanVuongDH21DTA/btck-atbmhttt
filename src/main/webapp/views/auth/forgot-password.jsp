<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ForgotPassword</title>
</head>

<style>
	/* Đặt nền cho trang */
body {
    font-family: Arial, sans-serif;
    background-color: #f0f0f0;
    margin: 0;
    padding: 0;
    height: 100vh; 
    display: flex;
    justify-content: center; 
    align-items: center;
    background: #76b852;
	background: -webkit-linear-gradient(to top, #76b852, #8DC26F);
	background: -moz-linear-gradient(to top, #76b852, #8DC26F);
	background: -o-linear-gradient(to top, #76b852, #8DC26F);
	background: linear-gradient(to top, #76b852, #8DC26F);
	background-size: cover;
	background-attachment: fixed;
	font-family: 'Roboto', sans-serif;
	
}

/* Container chính */
.main-w3layouts {
    width: 100%;
    max-width: 600px;
    margin: 50px auto;
    padding: 20px;
    background-color: #fff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* Tiêu đề */
h1 {
    text-align: center;
    color: #333;
    font-size: 24px;
}

/* Phần form */
.main-agileinfo {
    margin-top: 20px;
}

.agileits-top{
    position: relative;
}

/* Các trường input */
.text {
    width: 100%;
    padding: 10px;
    margin: 10px 0;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
}

/* Phần thông báo lỗi */
.alert {
    background-color: #f44336;
    color: white;
    padding: 10px;
    margin-bottom: 20px;
    border-radius: 4px;
}

/* Nút submit */
input[type="submit"] {
    width: 100%;
    padding: 10px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

input[type="submit"]:hover {
    background-color: #45a049;
}
	
</style>

<body>
    <!-- main -->
    <div class="main-w3layouts wrapper">
        <h1>Quên mật khẩu</h1>
        <div class="main-agileinfo">
            <div class="agileits-top">
                <!-- Hiển thị thông báo lỗi hoặc trạng thái -->
                <c:if test="${not empty messageForgetPassword}">
                    <div class="alert alert-danger" role="alert">
                        ${messageForgetPassword}
                    </div>
                    <% session.removeAttribute("messageForgetPassword"); %>
                </c:if>

                <!-- Form xác minh email và privateKey -->
                <c:if test="${empty sessionScope.forgotPasswordMode}">
                    <form action="/btck-atbmhttt/ForgotPasswordServlet" method="post">
                        <input class="text email" type="email" name="email" placeholder="Nhập email của bạn" required="">
                        <input class="text" type="text" name="publicKey" placeholder="Nhập khóa công khai" required="">
                        <input type="submit" value="Gửi mã xác minh">
                    </form>
                </c:if>

                <!-- Form đặt lại mật khẩu -->
                <c:if test="${sessionScope.forgotPasswordMode eq 'reset'}">
                    <form action="/btck-atbmhttt/ResetPasswordServlet" method="post">
                        <input type="hidden" name="email" value="${sessionScope.email}">
                        <input class="text password" type="password" name="newPassword" placeholder="Nhập mật khẩu mới" required="">
                        <input class="text password" type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu mới" required="">
                        <input type="submit" value="Tạo mật khẩu mới">
                    </form>
                </c:if>
            </div>
        </div>
    </div>
    
</body>
</html>
