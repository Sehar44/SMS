<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date" %>
<% Date now = new Date(); %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            display: flex;
        }

        .container {
            margin-left: 310px; /* Same as sidebar width + padding */
            background-color: #ffffff;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
            border-radius: 8px;
            padding: 20px;
            margin-top: 50px;
            width: 70%;
        }

        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .profile-info {
            margin: 0 auto;
            width: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .profile-picture {
            position: relative;
            margin-bottom: 20px; /* Space between image and table */
        }

        .profile-picture img {
            width: 150px; /* Adjust the size as needed */
            height: 150px;
            border-radius: 50%; /* Circle image */
            transition: transform 0.3s;
        }

        .profile-picture:hover img {
            transform: scale(1.1); /* Scale up on hover */
        }

        .edit-icon {
            position: absolute;
            bottom: 5px;
            right: 5px;
            background-color: rgba(255, 255, 255, 0.8);
            border-radius: 50%;
            padding: 5px;
            cursor: pointer;
            display: none; /* Hide by default */
        }

        .profile-picture:hover .edit-icon {
            display: block; /* Show on hover */
        }

        .profile-info table {
            width: 80%;
            margin-top: 20px;
        }

        .profile-info th, .profile-info td {
            padding: 12px 20px;
            text-align: left;
        }

        .profile-info th {
            background-color: #f8f9fa;
            color: #333;
            font-weight: bold;
            width: 40%;
        }

        .profile-info td {
            background-color: #ffffff;
            font-size: 16px;
            color: #666;
        }

        .profile-info .icon {
            color: #495057;
            margin-right: 10px;
        }

        .profile-info th i {
            margin-right: 8px;
        }
        
        .sidebar {
            background-color: #343a40;
            width: 250px;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            align-items: center;
            padding: 20px 0;
            color: white;
            position: fixed;
        }

        .sidebar-header {
            margin-top: 7px;
            font-size: 22px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
        }

        .sidebar ul {
            list-style: none;
            padding: 0;
            margin: 0;
            display: flex;
            flex-direction: column;
            align-items: center; /* Center navbar items horizontally */
        }

        .sidebar li {
            margin: 8px 0;
        }

        .sidebar a {
            color: white;
            text-decoration: none;
            font-size: 18px;
            display: block;
            padding: 10px 20px;
            transition: background-color 0.3s, color 0.3s;
            text-align: center;
            width: 100%;
        }

        .sidebar a:hover, .sidebar a.active {
            background-color: #495057;
            color: #ffc107;
            font-weight: bold;
        }

        .btn-logout {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
            margin: 20px;
        }

        .btn-logout:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <!-- Sidebar Header -->
        <div class="sidebar-header">
            Capgemini University
        </div>

        <!-- Navigation Menu -->
        <ul>
            <li><a href="/students/studentHome" class="${page == 'home' ? 'active' : ''}">Home</a></li>
            <li><a href="/students/Profile" class="${page == 'students' ? 'active' : ''}">View Profile</a></li>
            <li><a href="/notices/studentNotice" class="${page == 'notices' ? 'active' : ''}">Announcements</a></li>
        </ul>

        <div>
            <a href="/logout" class="btn-logout">Logout</a>
        </div>
    </div>

    <!-- Profile Section -->
    <div class="container">
        <h2>Student Profile</h2>
        <div class="profile-info">
            <div class="profile-picture">
                <img src="${student.profileImagePath}?t=${now.time}" alt="Profile" />
                <div class="edit-icon" data-toggle="modal" data-target="#uploadModal">
                    <i class="fas fa-pencil-alt"></i>
                </div>
            </div>
            <table class="table table-bordered">
                <tr>
                    <th><i class="fas fa-id-card icon"></i>ID</th>
                    <td>${student.id}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-user icon"></i>Name</th>
                    <td>${student.name}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-envelope icon"></i>Email</th>
                    <td>${student.email}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-calendar-alt icon"></i>Age</th>
                    <td>${student.age}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-book icon"></i>Course</th>
                    <td>${student.course}</td>
                </tr>
            </table>
        </div>
    </div>

    <!-- Modal for Uploading Image -->
    <div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="uploadModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="uploadModalLabel">Upload Profile Image</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="/students/uploadProfileImage" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="profileImage">Choose Image:</label>
                            <input type="file" class="form-control-file" id="profileImage" name="profileImage" accept="image/*" required>
                        </div>
                        <input type="hidden" name="studentId" value="${student.id}">
                        <button type="submit" class="btn btn-primary">Upload Image</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
