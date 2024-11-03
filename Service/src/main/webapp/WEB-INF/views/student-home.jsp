<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Include jQuery first -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

    <!-- Include jQuery UI (optional, required for some jqGrid features like sorting) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

    <!-- Include jqGrid CSS and JS from GitHub CDN -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/free-jqgrid@4.15.5/css/ui.jqgrid.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/free-jqgrid@4.15.5/js/jquery.jqgrid.min.js"></script>

    <!-- Include Bootstrap CSS (for navbar styling) -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <title>Student Dashboard</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            height: 100vh;
        }

        
        .main-content {
  
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
            background-color: #f8f9fa;
            padding: 20px;
            margin-left: 250px;
        }

        .main-content h1 {
            font-size: 30px; /* Increased font size */
            color: #333;
        }

        .footer {
            background-color: #343a40;
            color: white;
            text-align: center;
            padding: 10px;
            position: fixed;
            bottom: 0;
            left: 250px;
            right: 0;
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
        position: fixed
    }

    /* Capgemini University title at the top of the sidebar */
    .sidebar-header {
        margin-top:7px;
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

    <!-- Main Content -->
    <div class="main-content">
        <h1>Hi ! Welcome to Capgemini University Dashboard</h1>
    </div>

    <!-- Footer -->
    <div class="footer">
        &copy; 2024 Capgemini University
    </div>
</body>
</html>