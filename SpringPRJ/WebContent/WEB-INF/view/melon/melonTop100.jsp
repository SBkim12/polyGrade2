<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	$(window).on("load", function() {

		getRank();
	});

	function getRank() {

		$.ajax({
			url : "/melon/getRank.do",
			type : "post",
			dataType : "JSON",
			contentType : "application/json; charset=UTF-8",
			success : function(json) {
				var melon_rank = "";

				for (var i = 0; i < json.length; i++) {
					melon_rank += (json[i].rank + "위| ")
					melon_rank += (json[i].song + " | ")
					melon_rank += (json[i].singer + " | ")
					melon_rank += (json[i].album + "<br>")
				}

				$('#melon_rank').html(melon_rank);
			}
		})
	}
</script>
</head>
<body>
	<h1> 멜론 순위 </h1>
	<hr />
	<div id="melon_rank"></div>
	<br>
	<hr>
</body>
</html>