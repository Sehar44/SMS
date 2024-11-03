<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Notice Management</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script> <!-- Include jQuery -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script> <!-- Include jQuery -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> <!-- Include Bootstrap JS -->
    
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

		h3 {
		text-align: center;
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
            $('#newPostButton').click(function() {
                $('#noticeForm')[0].reset(); // Clear form
                $('#noticeModalLabel').text('Add Notice');
                $('#noticeForm').attr('action', '/notices/save'); // Set form action for new notice
                $('#noticeModal').modal('show'); // Show modal
            });

            // Trigger the modal with filled fields for editing a notice
            $('.editNoticeButton').click(function() {
                var noticeId = $(this).data('id');
                var noticeSubject = $(this).data('subject');
                var noticeMessage = $(this).data('message');
                
                $('#noticeModalLabel').text('Edit Notice');
                $('#subject').val(noticeSubject); // Set subject in the form
                $('#message').val(noticeMessage); // Set message in the form
                $('#noticeForm').attr('action', '/notices/update/' + noticeId); // Set form action for updating
                $('#noticeModal').modal('show'); // Show modal
            });
            
            
        });
    </script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/sidebar.jsp" />
  
    <div class="content">
      <div class="breadcrumbs-container">
            <%@ include file="breadcrumbs.jsp" %>
        </div>
        <!-- New Post Button -->
        <button id="newPostButton" class="btn btn-primary mb-4">New Post</button>
       
        

        <!-- Notices List -->
        <div class="notices-list">
            <h3>Notices</h3>
            <!-- Loop to display notices -->
            <c:forEach var="notice" items="${notices}">
                <div class="notice-card" data-notice-id="${notice.id}">
                    <h4>${notice.subject}</h4>
                    <p>${notice.message}</p>
                   
                    
                        <input type="hidden" name="noticeId" value="${notice.id}"/>
                        <button type="submit" name="reaction" value="thumbsUp">&#128077; <span class="thumbsUp">(${notice.thumbsUp})</span></button>
                        
                    

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
                                            Admin
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
  		           <div class="btn-group">
                        <button class="btn btn-secondary editNoticeButton" 
                            data-id="${notice.id}" 
                            data-subject="${notice.subject}" 
                            data-message="${notice.message}">
                            Edit
                        </button>
                        <a href="/notices/delete/${notice.id}" class="btn btn-danger">Delete</a>
                    </div>
                   
                </div>
            </c:forEach>
        </div>
    </div>
    
    <!-- Bootstrap Modal -->
    <div class="modal fade" id="noticeModal" tabindex="-1" role="dialog" aria-labelledby="noticeModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="noticeModalLabel">Add Notice</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form id="noticeForm" action="/notices/save" method="post">
                        <div class="form-group">
                            <label for="subject">Subject:</label>
                            <input type="text" id="subject" name="subject" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label for="message">Message:</label>
                            <textarea id="message" name="message" class="form-control" rows="4" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Save</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <script >
</script>
</body>
</html>



