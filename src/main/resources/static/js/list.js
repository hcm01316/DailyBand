
$(function(){
	$("button").click(function(){
		location.href="BoardWrite.bo?BBStype="+$("#selectbbs").val();
	})
	$("#selectbbs").change(function(){
		selectbbs = $(this).val();
		location.href = "GBoardListMain.bo?BBStype="+selectbbs;
	});// change end
})