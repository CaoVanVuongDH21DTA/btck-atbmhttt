<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page session="true" %>

<style>
/* Căn giữa toàn bộ trang */
.help-page {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f9f9f9;
    padding: 20px;
    
    /* Container chứa form */
	.container {
	    background-color: #fff;
	    border-radius: 8px;
	    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
	    padding: 40px;
	    width: 100%;
	    max-width: 600px;
	}
	
	/* Tiêu đề */
	.section-heading {
		margin-bottom: 10px;
		
		h1 {
		    text-align: center;
		    font-size: 36px;
		    text-transform: uppercase;
		    font-weight: bold;
		    color: #333;
		}
	}
	
	/* Style cho các input và textarea */
	input[type="text"],
	textarea,
	select {
	    width: 100%;
	    padding: 12px;
	    border: 1px solid #ccc;
	    border-radius: 4px;
	    margin-bottom: 20px;
	    font-size: 16px;
	    box-sizing: border-box;
	}
	
	/* Đặt khoảng cách cho các label */
	label {
	    font-size: 16px;
	    margin-bottom: 8px;
	    color: #555;
	    display: block;
	}
	
	/* Style cho button submit */
	input[type="submit"] {
	    background-color: #4CAF50;
	    color: white;
	    border: none;
	    padding: 12px 20px;
	    font-size: 16px;
	    border-radius: 4px;
	    cursor: pointer;
	    width: 100%;
	}
	
	input[type="submit"]:hover {
	    background-color: #45a049;
	}
	
	/* Responsive */
	@media (max-width: 768px) {
	    .container {
	        padding: 20px;
	        max-width: 100%;
	    }
	
	    .section-heading h1 {
	        font-size: 24px;
	    }
	}
	
	/* Dialog (Hộp thoại thông báo) */
	#dialog-support {
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
	}
	#dialog-support .message {
	    margin-bottom: 20px;
	    font-size: 16px;
	    color: #333;
	}
	#dialog-support button {
	    padding: 10px 20px;
	    background-color: #4CAF50;
	    color: white;
	    border: none;
	    border-radius: 4px;
	    cursor: pointer;
	}
	#dialog-support button:hover {
	    background-color: #45a049;
	}
</style>

<div class="help-page">
    <div class="container">
        <div id="content" class="row">
            <div class="col-md-12">
                <div class="section-heading">
                    <h1>Help Form</h1>
                </div>
            </div>
            <form action="HelpServlet" method="post" onsubmit="return validateForm()">
                <label for="subject">Chủ đề:</label>
			    <input type="text" id="subject" name="subject" required>
		
				<label for="message">Nội dung:</label>
				<textarea id="message" name="message" rows="4" cols="50" required maxlength="200" oninput="updateCharacterCount()"></textarea>
				<div id="char-count">Số ký tự: 0/200</div>

                <label for="errorType">Select Error Type:</label>
                <select id="errorType" name="errorType">
                	<option value="">--Bạn cần hỗ trợ gì?--</option>
                    <option value="Password">Quên mật khẩu</option>
                    <option value="User Key">Mất User Key</option>
                    <option value="Other">Khác</option>
                </select>

                <input type="submit" value="Gửi">
            </form>
        </div>
    </div>
    <!-- Dialog -->
	<div id="dialog-support">
	    <div class="message"></div>
	    <button onclick="closeDialog()">Đóng</button>
		<button id="renewKeyButton" onclick="renewKey()" style="display:none;">Cấp lại key</button>
	</div>
</div>

<script>
	function validateForm() {
	    var subject = document.getElementById("subject").value;
	    var message = document.getElementById("message").value;
	    var errorType = document.getElementById("errorType").value;
	
	    if (!subject || !message || errorType === "") {
	    	alert("Vui lòng điền đầy đủ thông tin, đặc biệt là chọn loại lỗi!");
	        return false;
	    }
	    
	    if (errorType === "User Key") {
            sendSupportRequest(subject, message, errorType);
            showRenewKeyButton();
            return false; // Không gửi form đi
        }
	
	    // Ngăn chặn form gửi và hiển thị dialog
	    sendSupportRequest(subject, message, errorType);
	    return false; // Không gửi form đi
	}
	
	function sendSupportRequest(subject, message, errorType) {
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "HelpServlet", true);
	    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState === 4 && xhr.status === 200) {
	            var response = JSON.parse(xhr.responseText);
	            if (response.status === "success") {
	                document.getElementById("dialog-support").querySelector(".message").innerHTML = response.message;
	                showDialog();
	            } else {
	                alert(response.message);
	            }
	        }
	    };
	
	    xhr.send("subject=" + encodeURIComponent(subject) + 
	             "&message=" + encodeURIComponent(message) + 
	             "&errorType=" + encodeURIComponent(errorType));
	}
	
	
	function renewKey() {
		var userId = document.getElementById("userId").textContent;
		if (userId) {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "/btck-atbmhttt/RestoreUserKeyServlet?id=" + userId, true);
            xhr.onload = function () {
                if (xhr.status == 200) {
                    alert("Key đã được cấp lại thành công!");
                } else {
                    alert("Có lỗi xảy ra khi cấp lại key.");
                }
                closeDialog(); // Đóng dialog sau khi hoàn thành
            };
            xhr.send();
        } else {
            alert("Không có thông tin người dùng.");
        }
    }

	function showRenewKeyButton() {
        document.getElementById("renewKeyButton").style.display = "inline-block";
    }
	
    function showDialog() {
        document.getElementById("overlay").style.display = "block";
        document.getElementById("dialog-support").style.display = "block";
    }

    function closeDialog() {
    	var confirmClose = confirm("Bạn có chắc muốn đóng?");
        document.getElementById("overlay").style.display = "none";
        document.getElementById("dialog-support").style.display = "none";
    }
	
	// bộ đếm ký tự nhập vào trong nội dung
	function updateCharacterCount() {
	    var message = document.getElementById("message").value;
	    var charCount = message.length;
	    document.getElementById("char-count").innerText = "Số ký tự: " + charCount + "/200";
	}
</script>
