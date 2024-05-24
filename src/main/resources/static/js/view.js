let option = 1; //선택한 등록순과 최신순을 수정, 삭제, 추가 후에도 유지되도록 하기위한 변수로 사용됩니다.


function likePost() {
    var bbs_sn = $("#BBS_SN").val(); // hidden input에서 값을 가져옴
    var action = "like"; // 기본적으로 추천 동작

    // 추천 버튼 요소 가져오기
    var likeButton = $(".btn-like");
    
    // 현재 추천 여부 확인
    var isLiked = likeButton.hasClass("liked");
    
    // 이미 추천한 경우, 추천을 취소하고 서버에 요청을 보냄
    if (isLiked) {
        action = "notLike"; // 취소 동작으로 변경
    }

    // 추천 버튼 비활성화
    likeButton.prop("disabled", true);

    // 서버에 추천 또는 취소 요청 보내기
    $.ajax({
        url: "BoardLike.bo",
        type: "POST",
        data: {
            bbs_sn: bbs_sn,
            action: action
        },
        success: function(response) {
            if (response === "Success") {
                var likeCount = $("#LikeCount"); // 추천수를 표시할 요소 가져오기
                var currentCount = parseInt(likeCount.text());

                // 클릭 토글링
                if (isLiked) {
                    likeButton.removeClass("liked");
                    likeCount.text(currentCount - 1); // 추천수 감소
                } else {
                    likeButton.addClass("liked");
                    likeCount.text(currentCount + 1); // 추천수 증가
                }
            } else {
                console.error("Failed to toggle like status.");
            }
        },
        complete: function() {
            // 요청 완료 후 추천 버튼 활성화
            likeButton.prop("disabled", false);
        }
    });
}

function dislikePost() {
    var bbs_sn = $("#BBS_SN").val(); // hidden input에서 값을 가져옴
    var action = "notLike"; // 기본적으로 비추천 동작

    // 비추천 버튼 요소 가져오기
    var dislikeButton = $(".btn-dislike");
    
    // 비추천 상태에 따라 액션 변경
    if (dislikeButton.hasClass("disliked")) {
        action = "like";
    }

    $.ajax({
        url: "BoardLikeAction",
        type: "POST",
        data: {
            bbs_sn: bbs_sn,
            action: action
        },
        success: function(response) {
            if (response === "Success") {
                var dislikeCount = $("#NotLikeCount"); // 비추천수를 표시할 요소 가져오기

                // 클릭 토글링
                if (action === "notLike") {
                    dislikeButton.addClass("disliked");
                    dislikeCount.text(parseInt(dislikeCount.text()) + 1); // 비추천수 증가
                } else {
                    dislikeButton.removeClass("disliked");
                    dislikeCount.text(parseInt(dislikeCount.text()) - 1); // 비추천수 감소
                }
            } else {
                console.error("Failed to toggle dislike status.");
            }
        }
    });
}

function getList(state) {//현재 선택한 댓글 정렬방식을 저장합니다. 1=>등록순, 2=>최신순
	console.log(state)
	option = state;
	$.ajax({
		type: "post",
		url: "CommentList.bo",
		data: { "BBS_SN": $("#BBS_SN").val(), state: state },
		dataType: "json",
		success: function(rdata) {
			$('#count').text(rdata.listcount).css('font-family', 'arial,sans-serif')
			let red1 = 'red';
			let red2 = 'red';
			if (state == 1) {
				red2 = 'gray';
			} else if (state == 2) {
				red1 = 'gray';
			}

			let output = "";

			if (rdata.commentlist.length > 0) {
				output += '<li class="comment-order-item ' + red1 + '" >'
					+ '   <a href="javascript:getList(1)" class="comment-order-button">등록순 </a>'
					+ '</li>'
					+ '<li class="comment-order-item ' + red2 + '" >'
					+ '   <a href="javascript:getList(2)" class="comment-order-button">최신순</a>'
					+ '</li>';
				$('.comment-order-list').html(output);

				output = '';
				$(rdata.commentlist).each(function() {
					const lev = this.CMNT_LEV;
					let comment_reply = '';
					if (lev == 1) {
						comment_reply = ' comment-list-item--reply lev1';
					} else if (lev == 2) {
						comment_reply = ' comment-list-item--reply lev2';
					}
					const profile = this.MBR_PROFL_PHOTO;
					let src = 'image/profile.png';
					if (profile) {
						src = 'memberupload/' + profile;
					}

					output += '<li id="' + this.CMNT_SN + '" class="comment-list-item' + comment_reply + '">'
						+ '   <div class="comment-nick-area">'
						+ '     <img src="' + src + '" altr="프로필 사진" width="36 height="36">'
						+ '	  <div class="comment-box">'
						+ '	    <div class="comment-nick-box">'
						+ '            <div class="comment-nick-info">'
						+ '               <div class="comment-nickname">' + this.MBR_ID + '</div>'
						+ '			 </div>' //comment-nick-info
						+ '		 </div>' // comment-nick-box
						+ ' 		</div>' // comment-box
						+ '	<div class="comment-text-box">'
						+ '      <p class="comment-text-view">'
						+ '        <span class="text-comment">' + this.CMNT_CN + '</span> '
						+ '	   </p>'
						+ ' 	</div>' //comment-text-box
						+ '   <div class="comment-info-box">'
						+ '	  <span class="comment-info-date">' + this.REG_DT + '</span>';
					if (lev < 1) {
						output += '  <a href="javascript:replyform(' + this.CMNT_SN + ','
							+ lev + ',' + this.CMNT_SEQ + ','
							+ this.CMNT_REF + ')" class="comment-info-button">답글쓰기</a>'
					}
					output += ' 	</div>' //comment-info-box;

					if ($("#loginid").val() == this.MBR_ID) {
						output += '<div class="comment-tool">'
							+ '	  <div title="더보기" class="comment-tool-button">'
							+ '		<div>&#46;&#46;&#46;</div>'
							+ '   </div>'
							+ '   <div id="comment-list-item-layer' + this.CMNT_SN + '"  class="LayerMore">'
							+ '	   <ul class="layer-list">'
							+ '		<li class="layer-item">'
							+ ' 	<a href="javascript:updateForm(' + this.CMNT_SN + ')"'
							+ '		   class="layer-button">수정</a>&nbsp;&nbsp;'
							+ '		<a href="javascript:del(' + this.CMNT_SN + ')"'
							+ ' 		class="layer-button">삭제</a></li></ul>'
							+ '		</div>'//LayerMore
							+ '  	</div>'//comment-tool

					}

					output += '</div>'// comment-nick-area
						+ '</li>'//li.comment-list-item
				})//each end

				$('.comment-list').html(output);
			}//if(rdata.commentlist.length>0)
			else {//댓글 1개가 있는 상태에서 삭젷는 경우 갯수는 0이라 if문을 수행하지 않고 이곳으로 옵니다.
				//이곳에서 아래의 두 영역을 없앱니다.
				$('.comment-list').empty();
				$('.comment-order-list').empty();

			}
		}//success end
	});//ajax end
}//function(getList) end

//더보기-수정 클릭한 경우에 수정 폼을 보여줍니다.
function updateForm(CMNT_SN) { //num : 수정할 댓글 글번호

	//수정 폼이 있는 상태에서 더보기를 클릭할 수 없도록 더 보기 영역을 숨겨요
	$(".comment-tool").hide();

	$(".LayerMore").hide(); //수정 삭제 영역도 숨겨요

	let $num = $('#' + CMNT_SN);

	//선택한 내용을 구합니다.
	const content = $num.find('.text-comment').text();

	const selector = '#' + CMNT_SN + '> .comment-nick-area'
	$(selector).hide(); //selector 영역 숨겨요-수정에서 취소를 선택하면 보여줄 예정입니다.

	//$('.comment-list+.comment-write').clone() : 기본 글쓰기 영역 복사합니다.
	//글이 있던 영역에 글을 수정할 수 있는 폼으로 바꿉니다.
	$num.append($('.comment-list+.comment-write').clone());

	//수정 폼의 <textarea>에 내용을 나타냅니다.
	$num.find('textarea').val(content);

	//'.btn-register' 영역에 수정할 글 번호를 속성 'data-id'에 나타내고 클래스 'update'를 추가합니다.
	$num.find('.btn-register').attr('data-id', CMNT_SN).addClass('update').text('수정완료');

	//폼에서 취소를 사용할 수 있도록 보이게 합니다.
	$num.find('.btn-cancel').css('display', 'block');

	const count = content.length;
	$num.find('.comment-write-area-count').text(count + "/200");
}//function(updateForm) end



//더보기 -> 삭제 클릭한 경우 실행하는 함수
function del(CMNT_SN) {//num : 댓글 번호
	if (!confirm('정말 삭제하시겠습니까')) {
		$('#comment-list-item-layer' + CMNT_SN).hide(); //'수정 삭제' 영역 숨겨요
		return;
	}

	$.ajax({
		url: 'CommentDelete.bo',
		data: { CMNT_SN: CMNT_SN },
		success: function(rdata) {
			if (rdata == 1) {
				getList(option);
			}
		}
	})
}//function(del) end


//답글 달기 폼
function replyform(CMNT_SN, CMNT_LEV, CMNT_SEQ, CMNT_REF) {
	//수정 삭제 영역 선택 후 답글쓰기를 클릭한 경우
	$(".LayerMore").hide(); // 수정 삭제 영역 숨겨요

	let output = '<li class="comment-list-item comment-list-item--reply lev' + CMNT_LEV + '"></li>';
	const $num = $('#' + CMNT_SN);
	//선택한 글 뒤에 답글 폼을 추가합니다.
	$num.after(output);

	//글쓰기 영역 복사합니다.
	output = $('.comment-list+.comment-write').clone();

	const $num_next = $num.next();
	//선택한 글 뒤에 답글 폼 생성합니다.
	$num_next.html(output);

	//답글 폼의 <textarea>의 속성 'placeholder'를 '답글을 남겨보세요'로 바꾸어 줍니다.
	$num_next.find('textarea').attr('placeholder', '답글을 남겨보세요');

	//답글 폼의 '.btn-cancel'을 보여주고 클래스 'reply-cancel'를 추가합니다.
	$num_next.find('.btn-cancel').css('display', 'block').addClass('reply-cancel');

	//답글 폼의 '.btn-register'에 클래스 'reply' 추가합니다.
	//속성 'data-ref'에 ref, 'data-lev'에 lev, 'data-seq'에 seq값을 설정합니다.
	//등록을 답글 완료로 변경합니다.
	$num_next.find('.btn-register').addClass('reply')
		.attr('data-ref', CMNT_REF).attr('data-lev', CMNT_LEV).attr('data-seq', CMNT_SEQ).text('답글완료');
}//function(replyform) end

$(function() {

	getList(option);  //처음 로드 될때는 등록순 정렬

	$('.comment-area').on('keyup', '.comment-write-area-text', function() {
		const length = $(this).val().length;
		$(this).prev().text(length + '/200');
	});// keyup','.comment-write-area-text', function() {


	//댓글 등록을 클릭하면 데이터베이스에 저장 -> 저장 성공 후에 리스트 불러옵니다.
	$('ul+.comment-write .btn-register').click(function() {
		const content = $('.comment-write-area-text').val();
		if (!content) {//내용없이 등록 클릭만 경우
			alert("댓글을 입력하세요");
			return;
		}

		$.ajax({
			url: 'CommentAdd.bo', //원문 등록
			data: {
				MBR_ID: $("#loginid").val(),
				CMNT_CN: content,
				BBS_SN: $("#BBS_SN").val(),

				CMNT_LEV: 0, //원문인 경우 comment_re_seq는 0,
				//comment_re_ref는 댓글의 원문 글번호
				CMNT_SEQ: 0
			},
			type: 'post',
			success: function(rdata) {
				if (rdata == 1) {
					getList(option);
				}
			}
		})//ajax

		$('.comment-write-area-text').val('');//trxtare초기화
		$('.comment-write-area-count').text('0/200');//이력한 글 카운트 초기화

	})// $('.btn-register').click(function(){


	//더보기를 클릭한 경우
	$(".comment-list").on('click', '.comment-tool-button', function() {
		//더보기를 클릭하면 수정과 삭제 영역이 나타나고 다시 클릭하면 사라져요
		$(this).next().toggle();

		//클릭 한 곳만 수정 삭제 영역이 나타나도록 합니다.
		$(".comment-tool-button").not(this).next().hide();
	})

	//수정 후 수정완료를 클릭한 경우
	$('.comment-area').on('click', '.update', function() {
		const content = $(this).parent().parent().find('textarea').val();
		if (!content) {//내용없이 등록 클릭한 경우
			alert("수정할 글을 입력하세요");
			return;
		}
		const num = $(this).attr('data-id');
		$.ajax({
			url: 'CommentUpdate.bo',
			data: { CMNT_SN: num, CMNT_CN: content },
			success: function(rdata) {
				if (rdata == 1) {
					getList(option);
				}//if
			}//success
		});//ajax
	})//수정 후 수정완료를 클릭한 경우


	//수정 후 취소 버튼을 클릭한 경우
	$('.comment-area').on('click', '.btn-cancel', function() {
		//댓글 번호를 구합니다.
		const num = $(this).next().attr('data-id');
		const selector = '#' + num;

		//.comment-write 영역 삭제 합니다.
		$(selector + ' .comment-write').remove();

		//숨겨두었던 .comment-nick-area 영역 보여줍니다.
		$(selector + '>.comment-nick-area').css('display', 'block');


		//수정 폼이 있는 상태에서 더보기를 클릭할 수 없도록 더 보기 영역을 숨겼는데 취소를 선택하면 보여주도록 합니다.
		$(".comment-tool").show();
	})//수정 후 취소 버튼을 클릭한 경우



	//답글완료 클릭한 경우
	$('.comment-area').on('click', '.reply', function() {

		const content = $(this).parent().parent().find('.comment-write-area-text').val();
		if (!content) {//내용없이 답글완료 클릭한 경우
			alert("답글을 입력하세요");
			return;
		}
		const comment_re_ref = $(this).attr('data-ref');
		const lev = $(this).attr('data-lev');
		const seq = $(this).attr('data-seq');

		$.ajax({
			url: 'CommentReply.bo',
			data: {
				MBR_ID: $("#loginid").val(),
				CMNT_CN: content,
				BBS_SN: $("#BBS_SN").val(),
				CMNT_LEV: lev,
				CMNT_REF: comment_re_ref,
				CMNT_SEQ: seq
			},
			type: 'post',
			success: function(rdata) {
				if (rdata == 1) {
					getList(option);
				}
			}
		})//ajax


	})//답글완료 클릭한 경우


	//답글쓰기 후 취소 버튼을 클릭한 경우
	$('.comment-area').on('click', '.reply-cancel', function() {
		$(this).parent().parent().parent().remove();
		$(".comment-tool").show(); //더 보기 영역 보이도록 합니다.
	})//답글쓰기  후 취소 버튼을 클릭한 경우


	//답글쓰기 클릭 후 계속 누르는 것을 방지하기 위한 작업
	$('.comment-area').on('click', '.comment-info-button', function(event) {
		//답변쓰기 폼이 있는 상태에서 더보기를 클릭할 수 없도록 더 보기 영역을 숨겨요
		$(".comment-tool").hide();

		//답글쓰기 폼의 갯수를 구합니다.
		const length = $(".comment-area .btn-register.reply").length;
		if (length == 1) {	//답글쓰기 폼이 한 개가 존재하면 anchor 태그(<a>)의 기본 이벤트를 막아
			//또 다른 답글쓰기 폼이 나타나지 않도록 합니다.
			event.preventDefault();
		}
	})//답글쓰기 클릭 후 계속 누르는 것을 방지하기 위한 작업

})//ready









