function createMessageHtml(message) {
    var isCurrentUser = message.mbr_ID === $('#myid').val();
    var messageClass = isCurrentUser ? 'row align-items-end justify-content-end' : 'row align-items-end';
    var bubbleClass = isCurrentUser ? 'chat-bubble-me' : '';

    return `
                <div class="chat-item">
                    <div class="${messageClass}">
                        ${!isCurrentUser ? `<div class="col-auto"><span class="avatar avatar-sm" style="background-image: url('${message.mbr_PROFL_PHOTO}');"></span></div>` : ''}
                        <div class="col col-lg-6">
                            <div class="chat-bubble ${bubbleClass}">
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
                        ${isCurrentUser ? `<div class="col-auto"><span class="avatar avatar-sm" style="background-image: url('${message.mbr_PROFL_PHOTO}');"></span></div>` : ''}
                    </div>
                </div>
            `;
}

$(document).ready(function() {
    // .nav-link는 채팅방 링크의 클래스 이름
    // 첫 번째 채팅방 클릭하여 초기 메시지 로드
    var principalName = /*[[${#authentication.principal.MBR_NCNM}]]*/ 'defaultName';
    let stompClient =null;
    getchatlist();

    let chatRoomId = $('#CHAT_ROOM_ID').val();
    initializeStompClient(chatRoomId);
    // 채팅방 클릭 이벤트 처리
    $('.nav-link').on('click', function(event) {
        event.preventDefault();
        var chatRoomUrl = $(this).attr('href');
        var chatRoomId = $(this).attr('data-chatroom-id');
        // 채팅방 ID를 hidden input에 설정
        $('#CHAT_ROOM_ID').val(chatRoomId);

        $.ajax({
            url: chatRoomUrl,
            method: 'GET',
            async: false,
            success: function(data) {
                var chatBubbles = $('#showme');
                chatBubbles.empty();
                if (data.length != 0) {
                    data.forEach(function(message) {
                        var messageHtml = createMessageHtml(message);
                        chatBubbles.append(messageHtml);
                    });
                }
            },
            error: function(error) {
                console.error('Error fetching messages:', error);
            }
        });

        if (stompClient) {
            stompClient.deactivate(); // Deactivate existing client
        }
        initializeStompClient(chatRoomId);

    });

    function  initializeStompClient(chatRoomid) {

        console.log(location.host);
        var url  = location.host
        stompClient = new StompJs.Client({
            brokerURL: `ws://${url}/dailyband/gs-guide-websocket`,
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        stompClient.activate();

        stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);

            console.log(`CHAT_ROOM_ID: ${chatRoomid}`);

            if (!isNaN(chatRoomid)) {
                console.log(`Subscribing to /topic/chat/${chatRoomid}`);
                stompClient.subscribe(`/topic/chat/${chatRoomid}`, (chatMsg) => {
                    console.log('Received message: ' + chatMsg.body);
                    try {
                        const parsedMessage = JSON.parse(chatMsg.body);
                        console.log(JSON.stringify(parsedMessage, null, 2) + " 파싱 확인");
                        showMessage(parsedMessage);
                    } catch (e) {
                        console.error('Error parsing message:', e);
                    }
                });
            } else {
                console.error('Invalid CHAT_ROOM_ID value');
            }
        };
        stompClient.onWebSocketError = (error) => {
            console.error('Error with websocket', error);
        };

        stompClient.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };

    }

    function sendchat() {
        const message = {
            MBR_NCNM: document.getElementById("MBR_NCNM").value,
            CHAT_ROOM_ID: parseInt(document.getElementById("CHAT_ROOM_ID").value, 10),
            MSG_CN: document.getElementById("MSG_CN").value,
            SNDNG_DT: getCurrentTime()
        };

        function getCurrentTime() {
            const now = new Date(); // 현재 날짜와 시간
            const year = now.getFullYear(); // 연도
            const month = (now.getMonth() + 1).toString().padStart(2, '0'); // 월 (0부터 시작하므로 1을 더함)
            const day = now.getDate().toString().padStart(2, '0'); // 일
            const hours = now.getHours(); // 시
            const minutes = now.getMinutes(); // 분
            const seconds = now.getSeconds(); // 초

            // 포맷된 날짜와 시간 반환
            return `${year}-${month}-${day} ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        }

        console.log('Sending message: ' + JSON.stringify(message));
        stompClient.publish({
            destination: "/app/Msg",
            body: JSON.stringify(message)
        });

        document.getElementById("MSG_CN").value = '';
    }

    function showMessage(message) {

        const currentChatRoomId = parseInt(document.getElementById("CHAT_ROOM_ID").value, 10);
        console.log(currentChatRoomId);
        if (message.chat_ROOM_ID !== currentChatRoomId) {
            return;
        }

        const currentUserId = document.getElementById("myid").value;
        console.log(currentUserId + " + " + message.mbr_ID);
        let chatBubble = '';
        if (message.mbr_ID === currentUserId) {
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
                                        
                                            <div class="col chat-bubble-author">${message.mbr_NCNM}</div>
                                        
                                            <div class="col-auto chat-bubble-date">${message.sndng_DT}</div>
                                        </div>
                                    </div>
                                    <div class="chat-bubble-body">
                                        <p>${message.msg_CN}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
        }

        const chatContainer = document.getElementById("showme");
        chatContainer.insertAdjacentHTML('beforeend', chatBubble);
        scrollToBottom(chatContainer);
    }

    function scrollToBottom(container) {
        container.scrollTop = container.scrollHeight;
    }


    document.getElementById("MSG_CN").addEventListener("keypress", function(e) {
        if (e.which === 13) {
            sendchat();
            e.preventDefault();
        }
    });

    document.getElementById("send1").addEventListener("click", () => sendchat());

    window.onbeforeunload = () => {
        stompClient.deactivate();
    };
});  //레디 끝


function getchatlist() {
    var chatRoomUrl = $('.nav-link[data-chatroom-id]:first').attr('href');
    console.log(chatRoomUrl);
    var parts = chatRoomUrl.split('/');
    var chatRoomId = parts[parts.length - 2];
    console.log(chatRoomId);
    // 채팅방 ID를 hidden input에 설정
    $('#CHAT_ROOM_ID').val(chatRoomId);

    $.ajax({
        url: chatRoomUrl,
        method: 'GET',
        async: false,
        success: function(data) {
            var chatBubbles = $('#showme');
            chatBubbles.empty();
            if (data.length != 0) {
                data.forEach(function(message) {
                    var messageHtml = createMessageHtml(message);
                    chatBubbles.append(messageHtml);
                });
            }
        },
        error: function(error) {
            console.error('Error fetching messages:', error);
        }
    });
}

