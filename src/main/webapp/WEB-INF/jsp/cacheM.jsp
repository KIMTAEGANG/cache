<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
userid와 pcode로 검색
<form method="get" action="getId" >
    <input type="userid" id="userid" name="userid" value="${userid}"/>
    <input type="pcode" id="pcode" name="pcode" value="${pcode}"/>
    <input type="submit" id="sBtn" value="검색" />
</form>
<br/>
ehcacheM : ${ehcachKeyM}<br/><br/>
redisM : ${redisValueM}<br/><br/>
dbM조회 : ${tempM}<br/><br/>





</body>
</html>