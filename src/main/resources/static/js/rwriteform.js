$(document).ready(function(){	
    let check=0;
    function show(){
		// 파일이름이 없는 경우 remove 이미지를 보이게 하고
		// 파일 이름이 없는 경우 remove 이미지 보이지 않게 합니다.
		if($('#filevalue').text()==''){
			$(".remove").css('display','none');
		}else {
			$(".remove").css({'display': 'inline-block',
								'position':'reative','top':'-5px'});
		}
	}
	
	show();
	
	$(".remove").click(function(){
		$('#filevalue, #board_file').text('');
		$(this).css('display','none');
	})
	
	$("#upfile").change(function () {
		check++;
		console.log($(this).val()) // c:\fakepath\upload.png
		const inputfile = $(this).val().split('\\');
		$('#filevalue').text(inputfile[inputfile.length - 1]);
		show();
		console.log(check);
	});
	
	// submit 버튼 클릭할 때 이벤트 부분
	$("form[name=boardform]").submit(function(){
		
		var rel_check = document.querySelectorAll('input[name="re_realm"]:checked').length;
		if (rel_check == 0) {
			alert('모집분야를 최소 1개 입력해주세요.')
			return false;
		}
		
		
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
	});
	
	
	//submit end
});// ready() end