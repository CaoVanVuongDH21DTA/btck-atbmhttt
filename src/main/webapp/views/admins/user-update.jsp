<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="container">
	<div class="row col-6 offset-3">
		<form action="UserUpdateServlet?id=${user.idUsers }" method="post">
		<c:if test="${ not empty message}">
					<div class="alert alert-danger" role="alert">
					  ${message }
					</div>
				</c:if>
			<div class="mb-3">
				<label class="form-label fw-bold">Fullname</label> <input type="text" name="fullname" value="${user.fullname }"
					class="form-control">
			</div>
			<div class="mb-3">
				<label class="form-label fw-bold disable">Email</label> <input type="email" name="email" value="${user.email }"
					class="form-control">
			</div>
			<div class="mb-3">
				<label class="form-label fw-bold">Password</label> <input type="password" name="password" value="${user.password }"
					class="form-control">
			</div>
			<div class="mb-3">
			<label class="form-label fw-bold">Gender</label> <br />
						<div class="form-check form-check-inline">
		  <input class="form-check-input" type="radio" name="gender" value="Male" ${user.gender == 'Male' ? 'checked="checked"' : '' } >
		  <label class="form-check-label">Male</label>
		</div>
		<div class="form-check form-check-inline">
		  <input class="form-check-input" type="radio" name="gender" value="Female" ${user.gender == 'Female' ? 'checked="checked"' : ''}>
		  <label class="form-check-label">Female</label>
		</div>
		<div class="form-check form-check-inline">
		  <input class="form-check-input" type="radio" name="gender" value="Other" ${user.gender == 'Other' ? 'checked="checked"' : '' }>
		  <label class="form-check-label">Other</label>
		</div>
			</div>
			<input type="submit" class="btn btn-primary" value="Submit"></input>
		</form>
	</div>
</div>