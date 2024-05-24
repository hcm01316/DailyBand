$(document).ready(function(){	
	// submit 버튼 클릭할 때 이벤트 부분
	$("form[name=boardform]").submit(function(){
		
		
		if($.trim($("#BBS_SJ").val()) == ""){
			alert("제목를 입력하세요");
			$("#board_subject").focus();
			return false;
		}
		
		if($.trim($("#BBS_CN").val()) == ""){
			alert("내용을 입력하세요");
			$("#board_content").focus();
			return false;
		}
	});//submit end
});// ready() end