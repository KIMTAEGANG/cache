<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Insert title here</title>
</head>
<link href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script>
    $(function (){
        $("#sBtn").click(function(){
            if($("#check").val()=="web"){
                location.href ="getId?userid="+$("#userid").val()+"&pcode="+$("#pcode").val();
            }else if($("#check").val()=="mobile"){
                location.href ="getIdM?userid="+$("#userid").val()+"&pcode="+$("#pcode").val();
            }
        });

        //ehcache 삭제
        $("#ehcacheBtn").click(function(){
            location.href="ehcacheMDel"
        });

        //redis cache 삭제
        $("#redisBtn").click(function(){
            location.href="redisMDel"
        });
    });
</script>
<body>
<div class="container">
    <h3>userid와 pcode로 검색</h3>
    <hr>
    <div class="row">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">view</h3>
            </div>
            <table class="table">
                <tr>
                    <td width="50px;">
                        <select id="check" style="width:100px;height: 33px;">
                            <option value="web">web</option>
                            <option value="mobile" selected>mobile</option>
                        </select>
                    </td>
                    <td><input type="text" class="form-control" id="userid" name="userid" value="${userid}"/></td>
                    <td><input type="text" class="form-control" id="pcode" name="pcode" value="${pcode}"/></td>
                    <td><input type="submit" class="form-control" id="sBtn" value="검색" /></td>
                </tr>
            </table>
            <table class="table">
                <tr>
                    <td width="100">ehcacheM</td>
                    <td>${ehcachValM}<br/><br/></td>
                    <td style="text-align:right;"><button type="button" class="btn btn-primary" id="ehcacheBtn">Delete</button></td>
                </tr>
                <tr>
                    <td width="100">redisM</td>
                    <td>${redisValueM}<br/><br/></td>
                    <td style="text-align:right;"><button type="button" class="btn btn-primary" id="redisBtn">Delete</button></td>
                </tr>
                <tr>
                    <td width="100">dbM</td>
                    <td>${getDBM}<br/><br/></td>
                    <td style="text-align:right;"></td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>