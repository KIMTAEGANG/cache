<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<head>
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@600;900&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/4b9ba14b0f.js" crossorigin="anonymous"></script>
</head>
<style>
    body {
        background-color: #95c2de;
    }

    .mainbox {
        background-color: #95c2de;
        margin: auto;
        height: 600px;
        width: 600px;
        position: relative;
    }

    .err {
        color: #ffffff;
        font-family: 'Nunito Sans', sans-serif;
        font-size: 11rem;
        position:absolute;
        left: 20%;
        top: 8%;
    }

    .far {
        position: absolute;
        font-size: 8.5rem;
        left: 42%;
        top: 15%;
        color: #ffffff;
    }

    .err2 {
        color: #ffffff;
        font-family: 'Nunito Sans', sans-serif;
        font-size: 11rem;
        position:absolute;
        left: 68%;
        top: 8%;
    }

    .msg {
        text-align: center;
        font-family: 'Nunito Sans', sans-serif;
        font-size: 1.6rem;
        position:absolute;
        left: 16%;
        top: 45%;
        width: 75%;
    }

    a {
        text-decoration: none;
        color: white;
    }

    a:hover {
        text-decoration: underline;
    }
</style>
<body>
<div class="mainbox">
    <div class="err">4</div>
    <i class="far fa-question-circle fa-spin"></i>
    <div class="err2">4</div>
    <div class="msg">주소를 잘못 입력하셨습니다
        <p>기본 페이지로 이동하시려면 아래를 눌러주세요</p>
            <a href="/getId?userid=&pcode=">web</a>
            <a href="/getIdM?userid=&pcode=">mobile</a>
    </div>
</div>
</body>
</html>