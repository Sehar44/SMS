<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<!-- Include jQuery first -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<!-- Include jQuery UI (optional, required for some jqGrid features like sorting) -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
<!-- Include Bootstrap CSS (for navbar styling) -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">


<title>Add Student</title>
<style>
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	margin: 0;
	padding: 0;
	display: flex;
}

.main-content {
	margin-left: 250px; /* Space for the sidebar */
	padding: 20px;
	width: calc(100% - 250px);
	position: relative; /* To position breadcrumbs relative to this div */
}

 /* Breadcrumbs styling */
  .breadcrumbs-container {
    position: absolute;
    top: 0;
    left: 0;
    margin: 0; /* Remove any default margin */
    padding-left: 10px; /* Align it properly with the sidebar */
}

h1 {
	color: #333;
	text-align: center;
	margin-top: 40px;
}

/* Form container styling */
.form-container {
	margin-top: 15px; 
	width : 80%;
	margin: 20px auto;
	background-color: #fff;
	padding: 20px;
	box-shadow: 0 0 15px 1px rgba(0, 0, 0, 0.4);
	border-radius: 10px;
	width: 80%;
}

.form-container form {
	margin-top: 20px; display : flex;
	flex-direction: column;
	display: flex;
}

.form-container label {
	font-weight: bold;
	margin-top: 10px;
}

.form-container input, .form-container select {
	padding: 15px;
	border: 1px solid #ccc;
	border-radius: 3px;
	margin-bottom: 10px;
	width: 100%;
	box-sizing: border-box;
	font-size: 14px;
}

.form-container input[type="submit"], .form-container input[type="reset"]
	{
	width: auto;
	background: #27AE60;
	font-weight: bold;
	color: white;
	border: 0 none;
	border-radius: 1px;
	padding: 10px 20px;
	cursor: pointer;
	margin-right: 10px;
}

.form-container input[type="submit"]:hover {
	background-color: #218838;
}

.form-container input[type="reset"] {
	background-color: #ffc107;
}

.form-container input[type="reset"]:hover {
	background-color: #e0a800;
}

/* Add these styles for input group and icon alignment */
.input-group {
	position: relative;
	margin-bottom: 15px;
}

.input-group input {
	padding-left: 40px; /* Create space for the icon */
}

.input-group .input-icon {
	position: absolute;
	left: 10px;
	top: 50%;
	transform: translateY(-50%);
	color: #555;
}

.input-group-icon i {
	font-size: 18px;
	pointer-events: none;
}
</style>
</head>

<body>
	<%@ include file="sidebar.jsp"%>
	<div class="main-content">

		<!-- Breadcrumbs at top-left -->
        <div class="breadcrumbs-container">
            <%@ include file="breadcrumbs.jsp" %>
        </div>
		<h1>Student Management System</h1>
        
		<!-- Success Message Alert -->
		<c:if test="${not empty error}">
			<script>
	        $(document).ready(function() {
		        alert("${error}");
	        })
	        </script>
	        </c:if>
		<c:if test="${not empty successMessage}">
			<script>
	        $(document).ready(function() {
	            alert("${successMessage}");
	        });
	    </script>
		</c:if>

		<!-- Form to Add/Edit Student -->
		<div class="form-container">
			<form action="/students/add" method="post">

				<div class="input-group input-group-icon">
					<input type="text" id="name" name="name" value="${student.name}"
						placeholder="Full Name" required>
					<div class="input-icon">
						<i class="fa fa-user"></i>
					</div>
				</div>

				<div class="input-group input-group-icon">
					<input type="email" id="email" name="email"
						value="${student.email}" placeholder="Email Address" required>
					<div class="input-icon">
						<i class="fa fa-envelope"></i>
					</div>
				</div>

				<div class="input-group input-group-icon">
					 <input type="number" id="age" name="age" value="${student.age}" placeholder="Age" required min="16" max="35">
					<div class="input-icon">
						<i class="fa fa-calendar-alt"></i>
						<!-- Icon for Age -->
					</div>
				</div>

				<div class="input-group input-group-icon">
					<input type="text" id="course" name="course"
						value="${student.course}" placeholder="Course" required>
					<div class="input-icon">
						<i class="fa fa-book"></i>
						<!-- Icon for Course -->
					</div>
				</div>

				<div class="input-group input-group-icon">
					<input type="password" id="password" name="password"
						value="${student.password}" placeholder="Password" required
						pattern="(?=.*[A-Z]).{8,}"
						title="Password must be at least 8 characters long and contain at least one uppercase letter">
					<div class="input-icon">
						<i class="fa fa-key"></i>
					</div>
				</div>

				<div class="input-group">
					<input type="submit" value="Add Student"> <input
						type="reset" value="Reset">
				</div>
			</form>
		</div>
	</div>
</body>
</html>
