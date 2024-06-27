var varType; // 결재자/참조자 구분
var appArr = []; // 선택한 결재자
var refArr = []; // 선택한 결재자
var appArrText = []; // 화면에 보여줄 결재자 리스트
var refArrText = []; // 화면에 보여줄 참조자 리스트
// 결재자/참조자 선택 모달
function appBtn(type) {
  varType = type;
  console.log(varType);
  if (type === "app") {
    $(".modal-title").html("결재자 선택");
    $("#s-text").html("결재자");
    $("#s-list").html(appArrText.join("<br>"));
  } else if (type === "ref") {
    $(".modal-title").html("참조자 선택");
    $("#s-text").html("참조자");
    $("#s-list").html(refArrText.join("<br>"));
  }
  $("#modal-scrollable").css('display', 'flex').hide().fadeIn();
  $.ajax({
    url: "../apv/mbrlist",
    type: "get",
    dataType: "json",
    success: function (mList) {
      $("#s-keyword").val(""); // 검색 입력창 지우기
      appList(mList, type);
      console.log(mList);
    },
    error: function (xhr, status, error) {
      console.error("서버에서 데이터를 받아오는 중 에러가 발생했습니다:", status, error);
    }
  });
}

$(document).on('shown.bs.modal', '#modal-scrollable', function () {
  $('.m-list').scrollTop(0);
});

// 디바운스 적용
var searchMembersDebounced = _.debounce(function (keyword) {
  searchMembers(keyword);
}, 300);

$("#s-keyword").on("input", function () {
  var keyword = $("#s-keyword").val();
  searchMembersDebounced(keyword);
});

function searchMembers(keyword) {
  $.ajax({
    url: "../apv/mbrSearch",
    type: "get",
    dataType: "json",
    data: {
      searchKeyword: keyword
    },
    success: function (mList) {
      appList(mList, varType);
    },
    error: function (xhr, status, error) {
      console.error("서버에서 데이터를 받아오는 중 에러가 발생했습니다:", status, error);
    }
  });
}

$("#confirm").click(function () {
  appSelView();
});

// 회원 목록 불러오기
function appList(mList, type) {
  $("#m-list-table tbody").html(""); // 테이블 값 지우기
  var tr;
  $.each(mList, function (i) {
    var mbrType = "";
    if (mList[i].mbr_TY === "ROLE_ADMIN") {
      mbrType = "관리자";
    } else if (mList[i].mbr_TY === "ROLE_MANAGER") {
      mbrType = "매니저";
    }
    tr += '<tr class="tr"><td style="display:none;">' + mList[i].mbr_ID
        + '</td><td>' + mList[i].mbr_NCNM
        + '</td><td>' + mbrType + '</td></tr>';
  });
  $("#m-list-table tbody").append(tr);
  // 선택된 항목 표시
  if (type === "app") {
    appArr.forEach(function (selected) {
      $("#m-list-table tbody tr[data-id='" + selected.mbr_ID + "']").addClass('selected-row');
    });
  } else if (type === "ref") {
    refArr.forEach(function (selected) {
      $("#m-list-table tbody tr[data-id='" + selected.mbr_ID + "']").addClass('selected-row');
    });
  }
  appSelect(type); // 결재자/참조자 선택
}

// 결재자/참조자 선택
function appSelect(type) {
  $("#m-list-table tbody tr").click(function () {
    var trArr = new Object(); // 한 행의 배열을 담을 객체 선언
    var tdArr = new Array(); // 배열 선언
    var tr = $(this);
    var td = tr.children();

    // 반복문을 이용해서 배열에 값을 담아 사용 가능
    td.each(function (i) {
      tdArr.push(td.eq(i).text());
    });

    // td.eq(index)를 통해 값 가져와서 trArr 객체에 넣기
    trArr.mbr_ID = td.eq(0).text();
    trArr.mbr_NCNM = td.eq(1).text();
    trArr.mbr_TY = td.eq(2).text();

    console.log(trArr);

    // 객체에 데이터가 있는지 여부 판단
    if (type == "app") { // 결재자
      var checkedArrIdx = _.findIndex(appArr, {mbr_ID: trArr.mbr_ID}); // 동일한 값 인덱스 찾기
      appArrText = []; // 배열 비우기
      if (checkedArrIdx > -1) {
        _.remove(appArr, {mbr_ID: trArr.mbr_ID}); // 동일한 값 지우기
        tr.removeClass('selected-row');
      } else {
        if (appArr.length < 3) { // 선택한 결재자 수가 3보다 작으면
          appArr.push(trArr); // 객체를 배열에 담기
          tr.addClass('selected-row');
        } else {
          Swal.fire("결재자는 3명까지만 선택 가능합니다.");
        }
      }
      appArrText = appArr.map(function (el) {
        return `${el.mbr_NCNM} ${el.mbr_TY}`;
      });
      appArrHtml = appArr.map(function (el) {
        return `
      <span class="tag">
        ${el.mbr_NCNM} ${el.mbr_TY}
        <a href="#" class="btn-close" onclick="removeSelected('app', '${el.mbr_ID}')">×</a>
      </span>
    `;
      });
      $("#s-list").html(appArrHtml.join(" ")); // 개행해서 s-list 영역에 출력
    } else if (type == "ref") { // 참조자
      var checkedArrIdx = _.findIndex(refArr, {mbr_ID: trArr.mbr_ID}); // 동일한 값 인덱스 찾기
      refArrText = []; // 배열 비우기
      if (checkedArrIdx > -1) {
        _.remove(refArr, {mbr_ID: trArr.mbr_ID}); // 동일한 값 지우기
        tr.removeClass('selected-row');
      } else {
        refArr.push(trArr);
        tr.addClass('selected-row');
      }
      refArrText = refArr.map(function (el) {
        return `${el.mbr_NCNM} ${el.mbr_TY}`;
      });
      refArrHtml = refArr.map(function (el) {
        return `
      <span class="tag">
        ${el.mbr_NCNM} ${el.mbr_TY}
        <a href="#" class="btn-close" onclick="removeSelected('ref', '${el.mbr_ID}')">×</a>
      </span>
    `;
      });
      $("#s-list").html(refArrHtml.join(" ")); // 개행해서 s-list 영역에 출력
    }
  });
}

// 선택한 결재자/참조자 문서 작성 페이지에 표시
function appSelView() {
  if (varType === "app") {
    for (var i = 0; i < 3; i++) { // 전에 입력한 값이 있을 경우 대비 초기화
      $("#r-app" + i).text("");
      $("#name-app" + i).text("");
      $("#num-app" + i).val("");
    }
    var app = []; // 결재자 담을 배열 선언
    appArr.forEach(function (el, i) {
      $("#r-app" + i).text(el.mbr_TY);
      $("#name-app" + i).text(el.mbr_NCNM);
      app[i] = el.mbr_ID;
    });
    $("#num-app").val(app);
  } else if (varType == "ref") {
    $("#ref-list").text(refArrText.join(", "));
    var ref = []; // 참조자 담을 배열 선언
    refArr.forEach(function (el, i) {
      ref[i] = el.mbr_ID;
    })
    $("#num-ref").val(ref);
  }
}

//선택된 항목 제거
function removeSelected(type, mbr_ID) {
  if (type === "app") {
    _.remove(appArr, {mbr_ID: mbr_ID}); // 선택된 결재자 제거
    appArrText = appArr.map(function (el) {
      return `
        <span class="tag">
          ${el.mbr_NCNM} ${el.mbr_TY}
          <a href="#" class="btn-close" onclick="removeSelected('app', '${el.mbr_ID}')">×</a>
        </span>
      `;
    });
    $("#s-list").html(appArrText.join(" "));
  } else if (type === "ref") {
    _.remove(refArr, {mbr_ID: mbr_ID}); // 선택된 참조자 제거
    refArrText = refArr.map(function (el) {
      return `
        <span class="tag">
          ${el.mbr_NCNM} ${el.mbr_TY}
          <a href="#" class="btn-close" onclick="removeSelected('ref', '${el.mbr_ID}')">×</a>
        </span>
      `;
    });
    $("#s-list").html(refArrText.join(" "));
  }
}