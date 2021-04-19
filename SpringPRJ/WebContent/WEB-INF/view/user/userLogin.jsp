<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>userReg</title>
</head>
<body>
	<form action="/user/userLoginProc.do" method="post">
			<table border="0" cellspacing="0" width="400" height="100">
		
			<tr bgcolor="#00bfff" width="400" height="100">
			<td align="right">
			<font size="2">아이디 :</font>
			</td>
			<td>
			<input type="text" name="id" size="10">
			</td>
			</tr>
			
			<tr bgcolor="#00bfff">
			<td align="right">
			<font size="2"> 비밀번호 : </font>
			</td>
			<td>
			<input type="text" name="pwd" size="10">
			</td>
			</tr>
			<tr bgcolor="#00bfff">
			<td colspan="2" align="center">
			<input type="submit" value="로그인">
			<input type="reset" value="초기화">
			</td>
			</tr>
		</table>
	</form>
</body>
</html>