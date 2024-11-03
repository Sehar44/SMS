<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Notice Management</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script> <!-- Include jQuery -->
    <style>
         body {
            font-family: 'Roboto', sans-serif; 
            margin: 0;
            padding: 0;
            display: flex;
        }

        .content {
            margin-left: 270px; /* Same as sidebar width + padding */
            padding: 20px;
            width: calc(100% - 270px);
        }

        .notice-card {
            background: #ffffff;
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: box-shadow 0.3s;
        }

        .notice-card:hover {
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }

        .notice-card h4 {
            margin-top: 0;
            font-size: 20px;
            color: #333;
        }

        .notice-card p {
            color: #555;
        }

        .btn-group button {
            margin-right: 5px;
        }

        .comment {
            margin-top: 10px;
            padding: 10px;
            background-color: #eef0f2;
            border-radius: 5px;
        }

        .btn {
            margin-right: 6px;
        }
        
        h3{
        text-align : center;
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
        .comment-form{
        padding-top:10px;
        padding-bottom:10px;}
    </style>
    <script>
        $(document).ready(function() {
            // Handle comment submission
            $('.comment-form').submit(function(event) {
                event.preventDefault(); // Prevent the default form submission

                var form = $(this);
                var actionUrl = form.attr('action');
                var noticeId = form.find('input[name="noticeId"]').val(); // Get notice ID from the form
                var commentText = form.find('textarea[name="comment"]').val(); // Get comment text

                $.ajax({
                    url: actionUrl,
                    method: 'POST',
                    data: {
                        comment: commentText
                    },
                    success: function(response) {
                        // Assuming response includes the updated comments list or just the new comment
                        // Append the new comment dynamically
                        var newComment = '<div class="comment"><strong>' + response.commentBy + '</strong>: ' + response.commentText + ' <small>(' + response.createdAt + ')</small></div>';
                        $('.notice-card[data-notice-id="' + noticeId + '"] .comments-container').append(newComment);

                        // Clear the textarea
                        form.find('textarea[name="comment"]').val('');
                    }
                });
            });

            // Handle reaction submission
            $('.reaction-form').submit(function(event) {
                event.preventDefault(); // Prevent the default form submission

                var form = $(this);
                var actionUrl = form.attr('action');
                var noticeId = form.find('input[name="noticeId"]').val(); // Get notice ID
                var reactionValue = form.find('button[type="submit"]').attr('name'); // Get reaction type

                $.ajax({
                    url: actionUrl,
                    method: 'POST',
                    data: {
                        reaction: reactionValue
                    },
                    success: function(response) {
                        // Update the reaction counts based on the response
                        form.closest('.notice-card').find('.thumbsUp').text(' (' + response.thumbsUp + ')');
                        
                    }
                });
            });
        });
    </script>
</head>
<body>
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

    <div class="content">
       
        

        <!-- Notices List -->
        <div class="notices-list">
            <h3>Notices</h3>
            <!-- Loop to display notices -->
            <c:forEach var="notice" items="${notices}">
                <div class="notice-card" data-notice-id="${notice.id}">
                    <h4>${notice.subject}</h4>
                    <p>${notice.message}</p>
                   
                    <form class="reaction-form" action="/notices/react/${notice.id}" method="post">
                        <input type="hidden" name="noticeId" value="${notice.id}"/>
                        <button type="submit" name="reaction" value="thumbsUp">&#128077; <span class="thumbsUp">(${notice.thumbsUp})</span></button>
                        
                    </form>

                    <h5>Comments</h5>
                    <div class="comments-container">
                        <c:forEach var="comment" items="${notice.comments}">
                            <div class="comment">
                                <strong>
                                    <c:choose>
                                        <c:when test="${comment.student != null}">
                                            ${comment.student.name}
                                        </c:when>
                                        <c:when test="${comment.admin != null}">
                                            Admin:
                                        </c:when>
                                    </c:choose>
                                </strong>: ${comment.commentText} 
                                <small>(${comment.createdAt})</small>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Add a comment -->
                    <form class="comment-form" action="/notices/comment/${notice.id}" method="post">
                        <input type="hidden" name="noticeId" value="${notice.id}"/>
                        <textarea name="comment" rows="2" class="form-control" placeholder="Add a comment"></textarea>
                        <button type="submit" class="btn btn-primary mt-2">Post Comment</button>
                    </form>

                   
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>



