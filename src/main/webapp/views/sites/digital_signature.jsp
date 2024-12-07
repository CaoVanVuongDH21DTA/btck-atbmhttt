<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ký hợp đồng mua bán</title>
    <!-- Sử dụng Bootstrap từ CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Đảm bảo đường dẫn đúng tới file CSS -->
    <link rel="stylesheet" href="/btck-atbmhttt/assets/css/DigitalSignature.css"> 
    <base href="/btck-atbmhttt/" />
   
    <style>
        body {
            background-image: url(../assets/images/backgroundky.jpg);
            font-family: 'Arial', sans-serif;
            padding-top: 20px;
        }

        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        h3 {
            color: #2c3e50;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .btn {
            transition: background-color 0.3s ease;
        }

        .btn-secondary {
            background-color: #3498db;
            border-color: #3498db;
        }

        .btn-secondary:hover {
            background-color: #2980b9;
            border-color: #2980b9;
        }

        .btn.active {
            background-color: #2ecc71;
            border-color: #2ecc71;
        }

        .d-flex {
            justify-content: center;
        }

        .form-label {
            font-weight: bold;
            color: #34495e;
        }

        .form-control {
            border-radius: 5px;
            border: 1px solid #ccc;
            padding: 10px;
        }

        .form-control:focus {
            border-color: #3498db;
            box-shadow: 0 0 8px rgba(52, 152, 219, 0.5);
        }

        .mb-3 {
            margin-bottom: 1.5rem;
        }

        button[type="submit"] {
            width: 100%;
            padding: 12px;
            border-radius: 5px;
            font-weight: bold;
        }

        button[type="submit"].btn-success {
            background-color: #2ecc71;
            border-color: #2ecc71;
        }

        button[type="submit"].btn-primary {
            background-color: #3498db;
            border-color: #3498db;
        }

        button[type="submit"]:hover {
            opacity: 0.9;
        }

        .active-btn-container {
            margin-bottom: 20px;
        }

        /* Responsive design */
        @media (max-width: 768px) {
            .container {
                padding: 20px;
            }

            button[type="submit"] {
                padding: 10px;
            }
        }
    </style>
   
    <script>
        // Hàm để hiển thị form ký hợp đồng
        function showSignForm() {
            document.getElementById("signForm").style.display = 'block';
            document.getElementById("verifyForm").style.display = 'none';
            document.getElementById("signBtn").classList.add('active');
            document.getElementById("verifyBtn").classList.remove('active');
        }

        // Hàm để hiển thị form xác minh chữ ký
        function showVerifyForm() {
            document.getElementById("verifyForm").style.display = 'block';
            document.getElementById("signForm").style.display = 'none';
            document.getElementById("verifyBtn").classList.add('active');
            document.getElementById("signBtn").classList.remove('active');
        }

        // Hiển thị form ký hợp đồng mặc định khi tải trang
        window.onload = function() {
            showSignForm();
        };
    </script>
</head>
<body>
    <div class="container mt-5">
        <h3 class="text-center">Ký và Xác Minh Hợp Đồng</h3>

        <!-- Các nút điều hướng để chuyển đổi giữa hai form -->
        <div class="d-flex justify-content-center mb-4">
            <button id="signBtn" class="btn btn-secondary me-2" onclick="showSignForm()">Ký Hợp Đồng</button>
            <button id="verifyBtn" class="btn btn-secondary" onclick="showVerifyForm()">Xác Minh Chữ Ký</button>
        </div>
        
        

        <!-- Form ký hợp đồng -->
        <div id="signForm" style="display: none;">
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

        <!-- Form xác minh chữ ký -->
        <div id="verifyForm" style="display: none;">
            <form action="digitalsignature" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="verify">
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
                <button type="submit" class="btn btn-primary">Xác Minh Chữ Ký</button>
            </form>
        </div>
    </div>
</body>
</html>
