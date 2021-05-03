<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	$(window).on("load", function(){
		//페이지 로딩 완료 후, 멜론 순위가져오기 함수 실행함
		getRank();
	});
	
	//멜론 순위 가져오기
	function getRank(){
		
		//Ajax 호출
		$.ajax({
			url : "/melon/getCompareRank.do",
			type : "post",
			dataType : "JSON",
			contentType : "application/json; charset=UTF-8",
			success : function(json){
				
				var melon_rank = "";
				
				for(var i =0; i<json.length; i++){
					melon_rank += (json[i].song + " | ");
					melon_rank += (json[i].singer + " | ");
					melon_rank += (json[i].current_rank + "위 | ");
					melon_rank += (json[i].pre_rank + "위 | ");

					var change_rank = json[i].change_rank;
					
					if(change_rank.indexOf("하락") > 0){
						 melon_rank += ("<font color='blue'>" + json[i].change_rank + "</font><br>");
					}else{
						melon_rank += ("<font color='red'>" + json[i].change_rank + "</font><br>");
					}
				}
				$('#melon_rank').html(melon_rank);
			}	 
		})
	}
</script>
</head>
<body>
	<h1>멜론 노래별 이전 랭킹과 비교</h1>
	<hr/>
	<div id="melon_rank"></div>
	<br/>
	<hr/>
</body>
</html>