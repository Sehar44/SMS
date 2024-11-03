<!DOCTYPE html>
<html lang="en">
<head>
<title>Sidebar</title>
<style>
       .sidebar {
        background-color: #343a40;
        font-family: 'Roboto', sans-serif;
        width: 230px;
        height: 100vh;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        padding: 20px 0;
        color: white;
        position: fixed;
    }

    /* Capgemini University title at the top of the sidebar */
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

    .sidebar a:hover,
    .sidebar a.active {
        background-color: #495057;
        color: #ffc107;
        font-weight: bold;
    }

    /* Centering and styling the Logout button */
    .btn-logout {
        background-color: #dc3545;
        color: white;
        border: none;
        padding: 10px 20px; /* Adjusted padding for consistent look */
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
        transition: background-color 0.3s;
        text-align: center; /* Ensures text is centered */
        display: block;
        width: 100%; /* Ensures button takes up the full width like the links */
        margin: 20px 0; /* Adjusted margin for better spacing */
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
        <li><a href="/admin/home" class="${page == 'home' ? 'active' : ''}">Home</a></li>
        <li><a href="/students/all" class="${page == 'students' ? 'active' : ''}">Manage Students</a></li>
        <li><a href="/admin/addStudent" class="${page == 'addStudent' ? 'active' : ''}">Enroll Student</a></li>
        <li><a href="/students/deleted" class="${page == 'deleted' ? 'active' : ''}">Deleted Students</a></li>
        <li><a href="/notices" class="${page == 'notices' ? 'active' : ''}">Announcements</a></li>
    </ul>

    <div>
        <a href="/logout" class="btn-logout" onclick="clearBreadcrumbs()">Logout</a>
    </div>
</div>
<script>
    function clearBreadcrumbs() {
        // Clear the breadcrumbs stored in localStorage
        localStorage.removeItem('breadcrumbHistory');
    }
</script>
<head>