<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

<style>
.admin-support {
    padding: 20px;
    font-family: Arial, sans-serif;
    background-color: #f9f9f9;

    h2 {
        text-align: center;
        font-size: 24px;
        margin-bottom: 20px;
        color: #333;
    }

    .table-container {
        overflow-x: auto;
        margin-top: 20px;

        .support-table {
            width: 100%;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);

            thead {
                background-color: #4CAF50;

                th {
                    color: white;
                    padding: 10px;
                    text-align: left;
                    font-weight: bold;
                    font-size: 14px;
                }
            }

            tbody {
                tr {
                    &:nth-child(even) {
                        background-color: #f2f2f2;
                    }

                    td {
                        padding: 10px;
                        font-size: 14px;
                        color: #555;
                        vertical-align: top;

                        .status {
                            font-weight: bold;
                            padding: 5px 10px;
                            border-radius: 4px;
                            text-transform:uppercase;
                            
                            &.CHƯA_XỬ_LÝ {
                                background-color: #ffcc00;
                                color: #fff;
                            }

                            &.ĐANG_XỬ_LÝ {
                                background-color: #007bff;
                                color: #fff;
                            }

                            &.ĐÃ_XỬ_LÝ {
                                background-color: #28a745;
                                color: #fff;
                            }
                        }
                    }
                }
            }

            .action-form {
                display: flex;
                gap: 5px;

                .status-select {
                    padding: 5px;
                    border: 1px solid #ccc;
                    border-radius: 4px;
                    font-size: 14px;
                }

                .btn-update {
                    background-color: #4CAF50;
                    color: white;
                    border: none;
                    padding: 5px 10px;
                    border-radius: 4px;
                    cursor: pointer;
                    transition: background-color 0.3s;

                    &:hover {
                        background-color: #45a049;
                    }
                }
            }
        }
    }
}
</style>

<div class="admin-support">
    <h2><f:message bundle="${bundle}" key="supportRequest"/></h2>
    <div class="table-container">
        <table class="support-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Email</th>
                    <th><f:message bundle="${bundle}" key="subject"/></th>
                    <th><f:message bundle="${bundle}" key="message"/></th>
                    <th><f:message bundle="${bundle}" key="errorType"/></th>
                    <th><f:message bundle="${bundle}" key="status"/></th>
                    <th><f:message bundle="${bundle}" key="action"/></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="support" items="${supports}">
                    <tr>
                        <td>${support.id}</td>
                        <td>${support.email}</td>
                        <td>${support.subject}</td>
                        <td>${support.message}</td>
                        <td>${support.errorType}</td>
						<td>
						    <span class="status ${support.status.replace(" ", "_").toUpperCase()}">${support.status}</span>
						</td>

                        <td>
                            <form action="UpdateSupportStatusServlet" method="POST" class="action-form">
                                <!-- ID yêu cầu hỗ trợ -->
                                <input type="hidden" name="request_id" value="${support.id}" />

                                <select name="status" class="status-select" data-current-status="${support.status}">
                                    <option value="chưa xử lý" ${support.status == 'chưa xử lý' ? 'selected' : ''}>Chưa xử lý</option>
                                    <option value="đang xử lý" ${support.status == 'đang xử lý' ? 'selected' : ''}>Đang xử lý</option>
                                    <option value="đã xử lý" ${support.status == 'đã xử lý' ? 'selected' : ''}>Đã xử lý</option>
                                </select>
                                
                                <c:if test="${support.status != 'đã xử lý' }">
									<button type="submit" class="btn-update"><f:message bundle="${bundle}" key="update"/></button>
								</c:if>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script type="text/javascript">
document.querySelectorAll('.action-form').forEach(form => {
    const selectElement = form.querySelector('.status-select');
    const currentStatus = selectElement.getAttribute('data-current-status');

    // Hàm để cập nhật các tùy chọn status khi chọn
    function updateOptions() {
        selectElement.querySelectorAll('option').forEach(option => {
            option.style.display = ''; // Hiển thị lại tất cả các option
            if (currentStatus === 'chưa xử lý' && option.value === 'chưa xử lý') {
                option.style.display = 'none';
            } else if (currentStatus === 'đang xử lý' && (option.value === 'chưa xử lý' || option.value === 'đang xử lý')) {
                option.style.display = 'none';
            } else if (currentStatus === 'đã xử lý') {
                selectElement.disabled = true; // Vô hiệu hóa select nếu trạng thái là "Đã xử lý"
            }
        });
    }

    // Gọi hàm để cập nhật các tùy chọn khi form được tải lần đầu
    updateOptions();

    // Khi bấm nút "Cập nhật" thì cập nhật lại trạng thái
    form.addEventListener('submit', function (e) {
        e.preventDefault(); // Ngừng form gửi ngay lập tức

        const newStatus = selectElement.value; // Lấy trạng thái mới được chọn
        selectElement.setAttribute('data-current-status', newStatus); // Cập nhật trạng thái hiện tại

        // Gửi form khi đã cập nhật trạng thái
        form.submit();
    });
});
</script>
