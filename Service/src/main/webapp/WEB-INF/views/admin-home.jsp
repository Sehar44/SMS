
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

    <title>Admin Dashboard</title>
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
            margin-left: 230px;
            position: relative; /* Make breadcrumbs relative to this div */
        }

		 .breadcrumbs-container {
            position: absolute;
            top: 10px;
            left: 10px;
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
            left: 230px;
            right: 0;
        }

    </style>
</head>
<body>
   <%@ include file="sidebar.jsp" %>
   
    <!-- Main Content -->
    <div class="main-content">
         <!-- Breadcrumbs -->
        <div class="breadcrumbs-container">
            <%@ include file="breadcrumbs.jsp" %>
        </div>
        <h1>Hi Admin! Welcome to Capgemini University Dashboard</h1>
    </div>

    <!-- Footer -->
    <div class="footer">
        &copy; 2024 Capgemini University
    </div>
</body>
</html>
