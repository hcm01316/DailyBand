const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:9000/dailyband/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (ChatMsg) => {
        try {
            const parsedMessage = JSON.parse(ChatMsg.body);
            console.log(JSON.stringify(parsedMessage, null, 2) + "파싱확인"); // JSON 문자열로 변환 후 출력
            showGreeting(parsedMessage);
        } catch (e) {
            console.error('Error parsing message:', e);
        }
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function sendName() {

    console.log(getCurrentTime());
    const message = {
        MBR_NCNM: $("#MBR_NCNM").val(),
        BBS_SN: 43,
        MSG_CN: $("#MSG_CN").val(),
        SNDNG_DT: getCurrentTime()
    };

    console.log(message);
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify(message)
    });

    $("#MSG_CN").val('');
}
function showGreeting(message) {
    // 현재 사용자의 아이디 (이 부분은 실제 구현에 맞게 설정하세요)
    const currentUserId  = $("#myid").val()
    console.log(currentUserId + " + " + message.mbr_ID)
    try {
        let chatBubble = '';
        if (message.mbr_ID === currentUserId) {
            // 내가 보낸 메시지
            chatBubble = `
                <div class="chat-item">
                    <div class="row align-items-end justify-content-end">
                        <div class="col col-lg-6">
                            <div class="chat-bubble chat-bubble-me">
                                <div class="chat-bubble-title">
                                    <div class="row">
                                        <div class="col chat-bubble-author">${message.mbr_NCNM}</div>
                                        <div class="col-auto chat-bubble-date">${message.sndng_DT}</div>
                                    </div>
                                </div>
                                <div class="chat-bubble-body">
                                    <p>${message.msg_CN}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-auto">
                            <span class="avatar" style="background-image: url('${message.mbr_PROFL_PHOTO}');"></span>
                        </div>
                    </div>
                </div>
            `;
        } else {
            // 다른 사람이 보낸 메시지
            chatBubble = `
                <div class="chat-item">
                    <div class="row align-items-end">
                        <div class="col-auto">
                            <span class="avatar" style="background-image: url('${message.mbr_PROFL_PHOTO}');"></span>
                        </div>
                        <div class="col col-lg-6">
                            <div class="chat-bubble">
                                <div class="chat-bubble-title">
                                    <div class="row">
                                        <div class="col chat-bubble-author">${message.mbr_NCNM}</div><!-- 닉네임 -->
                                        <div class="col-auto chat-bubble-date">${message.sndng_DT}</div>
                                    </div>
                                </div>
                                <div class="chat-bubble-body">
                                    <p>${message.msg_CN}</p> <!-- 내용 -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        }

        $("#showme").append(chatBubble);
    } catch (error) {
        console.error('Error in showGreeting:', error);
        console.error('Message object:', message);
    }
}

function getCurrentTime() {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
}

$(function () {

    $("form").on('submit', (e) => e.preventDefault());
    // 웹페이지가 로드될 때 자동으로 WebSocket 연결 설정
    stompClient.activate();
    // 메시지 전송 버튼 이벤트 핸들러
    $( "#send1" ).click(() => sendName());
    // 사용자가 페이지를 벗어날 때 WebSocket 연결 해제
    window.onbeforeunload = () => {
        stompClient.deactivate();
    };
});