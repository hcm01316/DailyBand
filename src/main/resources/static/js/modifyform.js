$(document).ready(function(){
	let check = 0;
	
	// submit 버튼 클릭할 때 이벤트 부분
	$("form[name=modifyform]").submit(function(){
		const $board_subject = $("#BBS_SJ");
		if($.trim($BBS_SJ.val()) == ""){
			alert("제목을 입력하세요");
			$board_subject.focus();
			return false;
		}
		const $board_content = $("#BBS_CN");
		if($.trim($BBS_CN.val()) == ""){
			alert("내용을 입력하세요");
			$board_content.focus();
			return false;
		}	
	});//submit end
});//ready() end