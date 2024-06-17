// 공통 SweetAlert 호출
function showConfirmationDialog(title, text, icon, confirmButtonText, cancelButtonText, onConfirm) {
  Swal.fire({
    title: title,
    text: text,
    icon: icon,
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: confirmButtonText,
    cancelButtonText: cancelButtonText,
    heightAuto: false
  }).then((result) => {
    if (result.isConfirmed) {
      onConfirm();
    }
  });
}

// 공통 URL
function redirectToUrl(url) {
  window.location.href = url;
}

// 밴드 해체
function confirmBreakup(data) {
  var num = data.getAttribute('data-bbs-sn');
  var chat = data.getAttribute('data-chat-num');
  showConfirmationDialog(
      '정말 밴드를 해체 하시겠습니까?',
      '이 작업은 되돌릴 수 없습니다.',
      'warning',
      '해체',
      '취소',
      () => redirectToUrl(`./breakup?num=${num}&chat=${chat}`)
  );
}

// 밴드 강퇴
function confirmResign(data) {
  var id = data.getAttribute('data-mbr-id');
  var num = data.getAttribute('data-bbs-sn');
  var chat = data.getAttribute('data-chat-num');
  console.log(id);
  console.log(num);
  showConfirmationDialog(
      '정말 강퇴 하시겠습니까?',
      '강퇴 시 복구할 수 없습니다.',
      'warning',
      '강퇴',
      '취소',
      () => redirectToUrl(`../rboard/resign?id=${id}&num=${num}&chat=${chat}`)
  );
}

// 밴드 탈퇴
function confirmLeave(data) {
  var id = data.getAttribute('data-mbr-id');
  var num = data.getAttribute('data-bbs-sn');
  var chat = data.getAttribute('data-chat-num');
  showConfirmationDialog(
      '정말 밴드를 탈퇴 하시겠습니까?',
      '탈퇴 시 복구할 수 없습니다.',
      'warning',
      '탈퇴',
      '취소',
      () => redirectToUrl(`./leave?id=${id}&num=${num}&chat=${chat}`)
  );
}

// 밴드 가입 수락
function confirmAccept(data) {
  var id = data.getAttribute('data-mbr-id');
  var num = data.getAttribute('data-bbs-sn');
  var chat = data.getAttribute('data-chat-num');
  showConfirmationDialog(
      '정말 수락 하시겠습니까?',
      '',
      'question',
      '수락',
      '취소',
      () => redirectToUrl(`../rboard/accept?id=${id}&num=${num}&chat=${chat}`)
  );
}

// 밴드 가입 거절
function confirmRefuse(data) {
  var id = data.getAttribute('data-mbr-id');
  var num = data.getAttribute('data-bbs-sn');
  showConfirmationDialog(
      '정말 거절 하시겠습니까?',
      '거절 시 복구할 수 없습니다.',
      'question',
      '거절',
      '취소',
      () => redirectToUrl(`../rboard/refuse?id=${id}&num=${num}`)
  );
}

// 밴드원 모집 게시글 삭제
function confirmDelete(data) {
  var num = data.getAttribute('data-bbs-sn');
  var chat = data.getAttribute('data-chat-num');
  showConfirmationDialog(
      '정말 삭제 하시겠습니까?',
      '삭제 시 복구할 수 없습니다.',
      'warning',
      '삭제',
      '취소',
      () => redirectToUrl(`../rboard/delete?num=${num}&chat=${chat}`)
  );
}
