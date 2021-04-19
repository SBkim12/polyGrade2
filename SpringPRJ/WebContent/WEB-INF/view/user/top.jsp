<%@ page import="static poly.util.CmmUtil.nvl" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	String user_name = nvl ((String)session.getAttribute("user_name"));

%>

<div style="background-color: skyblue;">
top!!!!!!!!!!!!!!!
<div>
<ul>
<li>
<% if(user_name.isEmpty()) { %>
<a href="/user/userLogin.do">
로그인
</a>
<%} else{ %>
<%=user_name %>님 환영합니다.
<a href="/user/logOut.do">
로그아웃
</a>
<%} %>
</ul></div>
</div>		