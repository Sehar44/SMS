<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Deleted Students</title>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/free-jqgrid@4.15.5/css/ui.jqgrid.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/free-jqgrid@4.15.5/js/jquery.jqgrid.min.js"></script>
    <link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
    body {
        font-family: 'Roboto', sans-serif;
        margin: 0;
        padding: 0;
        display: flex;
    }

    /* Sidebar Styles */
    .main-content {
        margin-left: 250px; /* Space for the sidebar */
        padding: 20px;
        width: calc(100% - 250px);
    }

    h1 {
        color: #333;
        text-align: center;
        margin-bottom: 20px;
    }

    .btn {
        font-size: 10px; /* Reduce button font size */
        padding: 5px 8px; /* Adjust button padding */
        text-decoration: none;
        border-radius: 5px;
        cursor: pointer;
        margin: 5px;
    }

    /* Reduce the size of the restore button */
    .restore-btn {
        font-size: 10px; /* Smaller font size */
        padding: 3px 5px; /* Reduced padding */
        border-radius: 4px; /* Slightly round corners */
        background-color: #28a745; /* Green background */
        color: white; /* White text */
        border: none; /* No border */
    }

    .restore-btn:hover {
        background-color: #218838; /* Darker green on hover */
    }

    .ui-jqgrid {
        background-color: #fff; /* White background for the entire grid */
        border: 1px solid #ddd; /* Light border */
        border-radius: 5px; /* Rounded edges */
        box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); /* Light shadow */
        width: 100%; /* Full width */
    }

    table {
        width: 100%; /* Increase the width of the table */
        border-collapse: collapse;
        margin-top: 20px;
    }

    table, th, td {
        border: 1px solid #ddd;
    }

    th, td {
        padding: 10px;
        text-align: left;
    }

    th {
        background-color: #f4f4f4;
    }

    .ui-jqgrid .ui-jqgrid-htable th {
        text-align: center;
    }

    .ui-jqgrid .ui-jqgrid-btable td {
        text-align: center;
    }

    /* Add hover effect for table rows */
    .ui-jqgrid .ui-jqgrid-btable tr:hover {
        background-color: #f1f1f1; /* Light grey background on hover */
    }

    /* jqGrid overall grid background */
    .ui-jqgrid {
        background-color: #fff; /* White background for the entire grid */
        border: 1px solid #ddd; /* Light border */
        border-radius: 5px; /* Rounded edges */
        box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); /* Light shadow */
    }
    </style>
</head>
<body>
  <jsp:include page="/WEB-INF/views/sidebar.jsp" />
  <div class="main-content">
    <div class="breadcrumbs-container">
        <%@ include file="breadcrumbs.jsp" %>
    </div>
    <h1>Deleted Students</h1>

    <table id="deletedStudentsGrid"></table>
    <div id="deletedStudentsPager"></div>
  </div>
<script>
    $(function () {
        // Fetch deleted students data and store it in a variable
        let studentData = [];

        $.get('/students/deleted/list', function(data) {
            studentData = data; // Store fetched data in studentData variable

            $("#deletedStudentsGrid").jqGrid({
                datatype: 'local', // Use local data
                data: studentData, // Set the local data
                colNames: ['ID', 'Name', 'Email', 'Course', 'Restore'],
                colModel: [
                    { name: 'id', index: 'id', width: 60, key: true, sorttype: "int" },
                    { name: 'name', index: 'name', width: 100, sorttype: "string" },
                    { name: 'email', index: 'email', width: 100, sorttype: "string" },
                    { name: 'course', index: 'course', width: 100, sorttype: "string" },
                    {
                        name: 'restore',
                        index: 'restore',
                        width: 100, // Set width for restore column
                        formatter: function (cellvalue, options, rowObject) {
                            return '<button class="restore-btn" onclick="restoreStudent(' + rowObject.id + ')">Restore</button>';
                        }
                    }
                ],
                pager: '#deletedStudentsPager',
                rowNum: 10,
                rowList: [5, 10, 20, 30],
                sortname: 'id',
                sortorder: 'asc',
                viewrecords: true,
                height: 'auto',
                width:925,
                caption: 'Deleted Students',
                loadonce: true, // Load data once for client-side operations
                jsonReader: {
                    repeatitems: false,
                    id: "0"
                }
            });

            // Add searching capabilities
            $("#deletedStudentsGrid").jqGrid('filterToolbar', {
                stringResult: true,
                searchOnEnter: false,
                defaultSearch: "cn" // Default search type (contains)
            });
        });

    });

    function restoreStudent(id) {
        $.get('/students/restore/' + id, function (data) {
            alert(data); // Show success message
            // Remove the restored student from the grid
            $("#deletedStudentsGrid").jqGrid('delRowData', id);
        }).fail(function () {
            alert('Error restoring student');
        });
    }
</script>

</body>
</html>
