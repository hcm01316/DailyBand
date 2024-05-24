//회원가입 버튼 클릭했을 경우
$(document).ready(function() {
	let idcheck_value = '';

	// 회원가입 버튼 클릭할때 이벤트 처리 부분
	//ID
	$("#myform").submit(function() {
		//$.trim(문자열)는 문자열의 앞, 뒤 공백을 제거합니다.
		const id = $("#id");
		if ($.trim(id.val()) == "") {
			alert("아이디를 입력하세요");
			id.focus();
			return false;
		}
		
		
		if(!id.prop('readOnly')){	//회원가입 폼과 정보 수정 폼에서 동시에 사용할 js입니다.
									//회원가입 폼에서만 사용할 문장들 입니다.
									//정보 수정 폼에서는 아이디를 수정하지 않기 때문에 필요없는 부분입니다.
		console.log(id.prop('readOnly'))
		const submit_id_value=$.trim(id.val())
		if(submit_id_value != idcheck_value){//submit 당시 아이디값과 아이디 중복검사에 사용된 아이디를 비교합니다.
			alert("ID 중복검사를 하세요");
			$("#opener_message").text('');
			return false;
		}
		
		//아이디 중복 검사를 했지만 사용중인 아이디인 경우에는 submit시 경고창이 나타납니다.
		const result=$("#result").val();
		if(result == '-1'){
			alert("사용 가능한 아이디로 다시 입력하세요");
			id.val('').focus();
			$("#opener_message").text('');
			return false;
		}
	}//if(!id.prop('readOnly'))
		
		
		


		//비밀번호
		const pass = $("#pass");
		if ($.trim(pass.val()) == '') {
			alert("비밀번호를 입력하세요");
			pass.focus();
			return false;
		}


		//주민번호 앞자리 공백 유효성 검사
		const jumin1 = $("#jumin1");
		if ($.trim(jumin1.val()) == "") {
			alert("주민번호 앞자리르 입력하세요");
			jumin1.focus();
			return false;
		}


		//주민번호 앞자리 - 일부분만 입력한 경우가 있어 체크합니다.
		if ($.trim(jumin1.val()).length != 6) {
			alert("주민번호 앞자리 6자리를 입력하세요");
			jumin1.val("").focus();
			return false;
		}


		//주민번호 뒷자리 공백 유효성 검사
		const jumin2 = $("#jumin2");
		if ($.trim(jumin2.val()) == "") {
			alert("주민번호 뒷자리를 입력하세요");
			jumin2.focus();
			return false;
		}


		//주민번호 뒷자리 - 일부분만 입력한 경우가 있어 체크합니다.
		if ($.trim(jumin2.val()).length != 7) {
			alert("주민번호 뒷자리 7자리를 입력하세요");
			jumin2.val("").focus();
			return false;
		}


		//E-Mail
		const email = $("#email");
		if ($.trim(email.val()) == "") {
			alert("E-Mail 입력하세요");
			email.focus();
			return false;
		}


		//성별
		let cnt = $('input:radio:checked').langth;
		if (cnt == 0) {
			alert("남, 여 중에서 1개를 선택하세요");
			return false;
		}


		//취미 2개 이상 체크하도록 하는 유효성 검사
		cnt = $('input:radio:checked').langth;
		if (cont < 2) {
			alert("취미는 2개이상 선택하세요");
			return false;
		}


		//우편번호
		const post1 = $("#post1");
		if ($.trim(post1.val()) == "") {
			alert("우편번호을 입력하세요");
			post1.focus();
			return false;
		}

		//우편번호 숫자만 입력가능
		if (!$.isNumeric(post1.val())) {
			alert("우편번호는 숫자만 입력 가능 합니다.");
			post1.val("").focus();
			return false;
		}

		//주소
		const address = $("#address");
		if ($.trim(address.val()) == "") {
			alert("주소를 입력하세요");
			address.focus();
			return false;

		}

		//자기소개
		const intro = $("#intro");
		if ($.trim(intro.val()) == "") {
			alert("자기소개를 입력하세요 ");
			intro.focus();
			return false;
		}

	});//submit() end




	//회원가입 버튼 클릭했을 경우


	//ID 중복검사
	$("#idcheck").click(function() {

		const id = $("#id");
		// $.trim(문자열)는 문자열의 앞, 뒤 공백을 제거합니다.
		const id_value = $.trim(id.val());
		if (id.value == "") {//ID를 입력하지 않았을 경우
			alert("아이디를 입력하세요");
			id.focus();
			return false;
		} else {
			pattern = /^[A-Z][a-zA-Z0-9_]{3,19}$/;
				const ref = "idcheck.net?id=" + id_value ;
			if (pattern.test(id_value)) {
				idcheck_value = id_value;
				window.open(ref, "idcheck", "width=300, height=250");
				
			} else {
				alert("첫글자는 대문자이고 두번째부터는 대소문자, 숫자, _로 총 4개 이상이어야 합니다.");
				id.val('').focus();
			}
		}
	});//$("#idcheck").click


	//우편 검색 버튼 클릭
	$("#Postcode").click(function(){
	//window.open("post.html", "post", "width=400, height=500, scrollbars=yes");
	Postcode();
	});

	// 도메인 선택 부분
	$("#sel").change(function(){
		const domain = $("#domain");
		if ($(this).val() == ""){ // 직접입력 선택한 경우
			domain.val("").focus();
			domain.prop("readOnly", false);
		}else{	// 도메인 선택한 경우
			domain.val($(this).val());
			domain.prop("readOnly", true);
		}
	}); //$("#sel").change()

	//주민번호
	
		//주민번호 앞자리 - 일부분만 입력한 경우가 있어 체크합니다.
		$("#jumin1").keyup(function(){
			const jumin1Value = $(this).val();
			if ($.trim(jumin1Value).length == 6){
				const pattern = /^[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$/;
				if (pattern.test(jumin1Value)){
					$("#jumin2").focus();//주민번호 뒷자리로 포커스 이동
				}else{
					alert("숫자 또는 형식에 맞게 입력하세요[yymmdd]");
					$(this).val('').focus();
				}
			}
		}); //$("#jumin1").keyup()
		
		
		$("#jumin2").keyup(function(){
			const jumin2Value = $(this).val();
			if ($.trim(jumin2Value).length == 7){
				const pattern = /^[1234][0-9]{6}$/;
				if (pattern.test(jumin2Value)){
					const c = Number(jumin2Value.substring(0, 1));
					const index = (c - 1) % 2;// c=1,3이면 index=0
											  // c=2,4이면 index=1
					$("input[type=radio]").eq(index)
										  .prop("checked", true);
				}else{
					alert("형식에 맞게 입력하세요[1234][0-9]{6}");
					$(this).val('').focus();
				}
			}
			});//$("#jumin2").keyup()
			
			
		function Postcode() {
        new daum.Postcode({
            oncomplete: function(data) {
            	console.log(data.zonecode)
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
 
                // 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullRoadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 도로명 조합형 주소 변수
 
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }
                // 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가한다.
                if(fullRoadAddr !== ''){
                    fullRoadAddr += extraRoadAddr;
                }
 
                
                // 우편번호와 주소 정보를 해당 필드에 넣는다.
             	$("#post1").val(data.zonecode); //5자리 새 우편번호 사용
                $("#address").val(fullRoadAddr);
            }
        }).open();
    }//function Postcode()


});//ready() end