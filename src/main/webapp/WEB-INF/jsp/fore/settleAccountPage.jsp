<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<title>结算页面</title>

<%@include file="../include/fore/header.jsp" %>
<%@include file="../include/fore/topnavigator.html" %>

<form action="forecreateOrder" method="post">
	<%@include file="settleAccountAddress.html" %>
	<%@include file="settleAccountOrderInfo.html" %>
</form>

<%@include file="../include/fore/footer.html" %>
