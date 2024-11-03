<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300&display=swap');
        /* SCSS converted to CSS for browser compatibility */
       body {
    background-color: #f0f0f0;
    font-family: 'Roboto', sans-serif;
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    overflow: hidden; /* Prevents scrollbars */
}

.container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%; /* Full width to ensure centering */
}

.form {
    background: #fff;
    width: 400px;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0px 4px 8px rgba(0,0,0,0.2);
    text-align: center;
}

.thumbnail {
    background: #EF3B3A;
    width: 150px;
    height: 150px;
    margin: 0 auto 30px;
    padding: 50px 30px;
    border-radius: 100%;
    box-sizing: border-box;
}

.thumbnail img {
    display: block;
    width: 100%;
}

input {
    background: #f5f5f5;
    width: 100%;
    border: 0;
    margin: 0 0 15px;
    padding: 15px;
    border-radius: 3px;
    box-sizing: border-box;
    font-size: 14px;
}

button {
    background: #EF3B3A;
    border: 0;
    padding: 15px;
    color: white;
    font-size: 14px;
    transition: all .3s ease;
    cursor: pointer;
    border-radius: 3px;
    width: 100%; /* Ensuring button fills the width */
}

.message {
    margin: 15px 0 0;
    color: #666;
    font-size: 12px;
}

.message a {
    color: #EF3B3A;
    text-decoration: none;
}

.error-message {
    color: #ff4d4d;
    text-align: center;
    margin-top: 10px;
}


       
    </style>
</head>
<body>
    <div class="container">
        <div class="form login-form">
            <div class="thumbnail">
                <img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/169963/hat.svg" alt="login thumbnail"/>
            </div>
            <form action="<c:url value='/login'/>" method="post">
            <c:if test="${param.error != null}">
                    <p class="error-message">Invalid username or password.</p>
                </c:if>
                <input type="text" name="username" placeholder="Username" required/>
                <input type="password" name="password" placeholder="Password" required/>
                <button type="submit">Login</button>
                
            </form>
            
        </div>
        
    </div>

   

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
   
</body>
</html>
