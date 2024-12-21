<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="container mt-5">
	<div class="d-flex justify-content-center row">
		<div class="col-md-10">
			<div class="row">
				<div class="col-3">
					<a href="UserFormServlet" class="btn btn-success">New User</a>
				</div>
				<c:if test="${not empty message }">
								<div class="alert alert-danger" role="alert">
								  ${message }
								</div>
							</c:if>
				<div class="col-3 offset-6">
					<form action="AdminUserServlet" method="post"> 
						<div class="mb-3 form-check">
							<input type="checkbox" class="form-check-input" name="reload"> <label
								class="form-check-label mr-3">In-active</label>
							<button type="submit" class="btn btn-primary">Reload</button>
						</div>
					</form>
				</div>
			</div>
			<div class="rounded">
				<div class="table-responsive table-borderless">
					<table class="table">
						<thead>
							<tr>
								<th>#</th>
								<th>Fullname</th>
								<th>Email</th>
								<th>Gender</th>
								<th>IsActive?</th>
								<th></th>
							</tr>
						</thead>
						<tbody class="table-body">
							<c:forEach items="${listUsers }" var="item">
								<form action="AdminUpdateOrderServlet?id=${item.idUsers }"
									method="post">
									<tr class="cell-1 ${item.active == false ? 'table-danger' : '' }">
										<td>#${item.idUsers }</td>
										<td>${item.fullname }</td>
										<td>${item.email }</td>
										<td>${item.gender }</td>
										<td>${item.active }</td>
										<td>
											<c:if test="${item.active == true }">
												<a
											href="UserUpdateServlet?id=${item.idUsers }"
											class="btn btn-primary">Update</a>
											<a User-id="${item.idUsers }" onclick="confirmDeleteUser(this.getAttribute('user-id'))" class="btn btn-danger"
												>
												Delete</a>
											</c:if>
											
											<c:if test="${item.active == false }">
												<a href="UserRestoreServlet?id=${item.idUsers }" class="btn btn-success">Restore</a>
											</c:if>
											<c:if test="${item.active == false }">
												<a href="RestoreUserKeyServlet?id=${item.idUsers }" class="btn btn-success">Cấp lại UserKey</a>
											</c:if>
											<c:if test="${item.active == true}">
											    <button 
												    class="btn btn-success" 
												    type="button"
												    onclick="showUserKey(${item.idUsers})">
												    Key
												</button>
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
<div class="modal fade" id="deleteUser" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Confirmation</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Are you sure to delete this user?
      </div>
      <div class="modal-footer">
       <a id="yesOption" class="btn btn-primary">Yes</a>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal Hiển Thị Key -->
<div class="modal fade" id="viewKeyModal" tabindex="-1" aria-labelledby="viewKeyLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="viewKeyLabel">User Key</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- Hiển thị key ngắn gọn -->
                <pre id="userKeyContentShort" style="white-space: pre-wrap;">Đang tải...</pre>
                
                <!-- Ẩn phần tử chứa key đầy đủ -->
				<pre id="userKeyContentFull" style="display:none;"></pre>
                
            </div>
            <div class="modal-footer" style="display: flex; justify-content: space-between ">
                <!-- Nút sao chép -->
                <button class="btn btn-secondary" onclick="copyToClipboard()">Copy</button>
                
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script>
	//xóa tài khoản
	function confirmDeleteUser(id) {
		$('#yesOption').attr('href', '/btck-atbmhttt/UserDeleteServlet?id=' + id)
		$('#deleteUser').modal('show');
	}
	//xem key
	function showUserKey(id) {
	    $('#userKeyContentShort').text('Đang tải...');
	    fetch('/btck-atbmhttt/UserKeyServlet?id=' + id)
	        .then(response => response.text())
	        .then(data => {
	            // Cập nhật nội dung hiển thị ngắn gọn (20 ký tự đầu và 20 ký tự cuối)
	            const shortKey = data.length > 40 ? data.slice(0, 20) + "..." + data.slice(-20) : data;
	            $('#userKeyContentShort').text(shortKey);
	            
	            // Lưu key đầy đủ để sao chép
	            $('#userKeyContentFull').text(data);
	        })
	        .catch(() => {
	            $('#userKeyContentShort').text('Lỗi khi tải Key!');
	        });
	    $('#viewKeyModal').modal('show');
	}
	
	//button copy key
	function copyToClipboard() {
	    // Lấy key đầy đủ từ phần tử hiển thị
	    const fullKey = document.getElementById('userKeyContentFull').textContent;
	    
	    // Sử dụng Clipboard API để sao chép key đầy đủ
	    navigator.clipboard.writeText(fullKey).then(() => {
	        // Thông báo khi sao chép thành công
	        alert('Key đã được sao chép!');
	    }).catch(() => {
	        // Thông báo khi không sao chép được
	        alert('Không thể sao chép key!');
	    });
	}
</script>