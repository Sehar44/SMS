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

<!-- Include jqGrid CSS and JS from GitHub CDN -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/free-jqgrid@4.15.5/css/ui.jqgrid.min.css" />
<script
	src="https://cdn.jsdelivr.net/npm/free-jqgrid@4.15.5/js/jquery.jqgrid.min.js"></script>
<title>Student Management</title>
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

.form-container {
	margin-bottom: 20px;
}

.form-container form {
	background-color: #f9f9f9;
	padding: 15px;
	border: 1px solid #ddd;
	border-radius: 5px;
}

.form-container label {
	margin-right: 10px;
}

.form-container input[type="text"], .form-container input[type="email"],
	.form-container input[type="number"], .form-container input[type="password"]
	{
	margin-right: 10px;
	width: 25%; /* Set the width to 50% of the parent container */
	height: 30px;
	padding: 10px; /* Add some padding for a better user experience */
	margin-bottom: 10px; /* Add space between input fields */
	font-size: 14px;
}

.form-container input[type="submit"] {
	font-size: 15px;
	padding: 5px 10px;
	background-color: #28a745;
	color: white;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.form-container input[type="submit"]:hover {
	background-color: #218838;
}

.form-container input[type="reset"] {
	font-size: 15px;
	padding: 5px 10px;
	background-color: #ffc107;
	color: white;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.form-container input[type="reset"]:hover {
	background-color: #e0a800;
}

table {
	width: 100%;
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

.btn {
	font-size: 12px;
	padding: 5px 10px;
	text-decoration: none;
	border-radius: 5px;
	cursor: pointer;
	margin: 5px;
}

.btn-delete {
	background-color: #dc3545;
	color: white;
	border: none;
}

.btn-delete:hover {
	background-color: #c82333;
}

.ui-jqgrid .ui-jqgrid-htable th {
	text-align: center;
}

.ui-jqgrid .ui-jqgrid-btable td {
	text-align: center;
}

/* Styling the jqGrid table header */
.ui-jqgrid .ui-jqgrid-htable th {
	font-weight: bold;
	text-align: center; /* Center align the header text */
}

/* Styling the jqGrid table rows */
.ui-jqgrid .ui-jqgrid-btable td {
	font-size: 14px; /* Slightly larger text */
	color: #333; /* Darker text */
}

/* Add hover effect for table rows */
.ui-jqgrid .ui-jqgrid-btable tr:hover {
	background-color: #f1f1f1; /* Light grey background on hover */
}

/* Customizing the action buttons (e.g., delete) */
.btn-delete {
	background-color: #ff4d4d; /* Red background */
	color: white; /* White text */
	padding: 10px 15px;
	border-radius: 8px;
	cursor: pointer;
	border: none;
}

.btn-delete:hover {
	background-color: #c82333; /* Darker red on hover */
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
		<h1>Student Management System</h1>
		<!-- Student Grid -->
		<table id="studentGrid"></table>
		<div id="studentPager"></div>

		 <button id="exportExcel" class="btn btn-success">Download as Excel</button>
		 
    
	</div>
	<script>
	
	$(document).ready(function () {
	    $('#exportExcel').click(function () {
	        window.location.href = '/students/exportToExcel';
	    });
	});

        var currentPage = ${currentPage};
        var size = ${size};

        $(document).ready(function () {
            if (typeof $.fn.jqGrid === 'undefined') {
                console.error('jqGrid is not loaded or is not recognized');
                return;
            }

            var initialSortName = 'id';  // Store initial sort column
            var initialSortOrder = 'asc'; // Store initial sort order

            $("#studentGrid").jqGrid({
                url: '/students/gridData',  // Backend URL for fetching data
                datatype: "json",           // Data type is JSON
                mtype: "GET",               // Method to get data
                colModel: [
                    { name: 'name', label: 'Name', width: 150, sorttype: 'text', sortable: true, search: true, editable: true },
                    { name: 'email', label: 'Email', width: 200, sorttype: 'text', sortable: true, search: true, editable: true },
                    { name: 'age', label: 'Age', width: 100, sorttype: 'int', sortable: true, search: false, editable: true },
                    { name: 'course', label: 'Course', width: 150, sorttype: 'text', sortable: true, search: true, editable: true },
                    {
                        name: 'actions',
                        label: 'Actions',
                        width: 150,
                        formatter: actionFormatter,
                        search: false,
                        sortable: false 
                    }
                ],
                viewrecords: true,
                height: 370,
                width:925,
                rowNum: 10,
                rowList: [5, 10, 15, 20, 30],
                rownumbers: true,
                pager: "#studentPager",
                caption: "Student Grid",
                sortname: initialSortName,  // Default sorting column
                sortorder: initialSortOrder, // Default sorting order
                cellEdit: true,
                cellsubmit: 'remote',  // Sends cell's data remotely to server
                cellurl: '/students/update', 
                serializeCellData: function(postData) {
                    var rowData = $("#studentGrid").jqGrid('getRowData', postData.id);
                    var email = rowData.email;

                    // Email validation regex
                    var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                    if (!emailPattern.test(email)) {
                        alert('Invalid email format. Please enter a valid email.');
                        return false;  // Prevent the submission
                    }
                    postData.name = rowData.name;
                    postData.email = rowData.email;
                    postData.age = rowData.age;
                    postData.course = rowData.course;
                    return postData;
                },
                afterSubmitCell: function(response, rowid, cellname, value, iRow, iCol) {
                    var responseData = $.parseJSON(response.responseText);
                    if (responseData.status === 'error') {
                        alert(responseData.errorMessage);  // Display error message to the user
                        return [false, responseData.errorMessage];  // Prevent further actions on error
                    }
                    return [true, ""];  // Proceed normally if no error
                },
                serializeGridData: function(postData) {
                    var searchData = $("#studentGrid").jqGrid('getGridParam', 'postData');
                    if (searchData.filters) {
                        var filters = $.parseJSON(searchData.filters);
                        if (filters.rules) {
                            $.each(filters.rules, function(index, rule) {
                                if (rule.field === 'name') {
                                    postData.name = rule.data;
                                } else if (rule.field === 'email') {
                                    postData.email = rule.data;
                                } else if (rule.field === 'course') {
                                    postData.course = rule.data;
                                }
                            });
                        }
                    }
                    return postData;
                },
                loadComplete: function() {
                    var grid = $("#studentGrid");
                    grid.jqGrid('filterToolbar', {
                        searchOnEnter: false
                    });
                    $('.ui-search-clear').hide();
                }
            });

            // Action buttons for edit/delete
            function actionFormatter(cellvalue, options, rowObject) {
                return '<form action="/students/delete/' + rowObject.id + '?page=' + currentPage + '&size=' + size + '" method="post" style="display:inline;">' +
                    '<button type="submit" class="btn btn-delete" style="border:none;background:none; padding:6px 10px; border-radius:8px; cursor:pointer;">' +
                    '<i class="fa fa-trash-alt" style="font-size:18px; color:red;"></i>' + 
                    '</button>' +
                    '</form>';
            }

            // Custom handler for refresh button
            $("#studentGrid").navGrid("#studentPager", { add: false, edit: false, del: false });
            $("#studentGrid").navButtonAdd("#studentPager", {
                caption: "Reset",
                buttonicon: "ui-icon-refresh",
                onClickButton: function () {
                    // Reset sortname and sortorder to the initial values
                    $("#studentGrid").jqGrid('setGridParam', {
                        sortname: initialSortName,
                        sortorder: initialSortOrder,
                        page: 1  // Reset to first page
                    }).trigger('reloadGrid', [{ page: 1 }]);  // Reload the grid
                }
            });
        });

    </script>
</body>

</html>