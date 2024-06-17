package com.bnd.dailyband.service.notify;

import com.bnd.dailyband.domain.Notification;
import com.bnd.dailyband.mybatis.mapper.NotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SseServiceImpl implements SseService {


    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private NotificationMapper dao;




    @Autowired
    public SseServiceImpl(NotificationMapper dao) {
        this.dao = dao;
    }

    // private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;  //60분
    /*
      SseEmitter를 사용하여 Server-Sent Events (SSE)를 생성하는 메서드입니다.
      SSE는 서버에서 클라이언트로 데이터를 스트리밍하는 데 사용됩니다.
     */
    public SseEmitter createEmitter(String userId) {
        //userId에 해당하는 SSEEmitter를 생성합니다.
        //SseEmitter emitter = new SseEmitter();
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        //생성된 emitter를 this.emitters 맵에 저장합니다. 맵은 userId를 키로 사용하고, 생성된 emitter를 값으로 갖습니다.
        this.emitters.put(userId, emitter);

        // emitter의 완료 이벤트와 타임아웃 이벤트가 발생할 때마다 해당 userId에 해당하는 emitter를 맵에서 제거하는 작업을 수행합니다.
        emitter.onCompletion(() ->
            this.emitters.remove(userId)
        );
        emitter.onTimeout(() ->
            this.emitters.remove(userId)
        );

        List<Notification> list =  dao.getList(userId);

        //503에러를 방지하기 위한 더미 이벤트 전송
        try {
            //알림은 emitter.send() 메서드를 사용하여 전송됩니다.
            //SseEmitter.event().name("notification").data(message)를 사용하여 이름이 "notification"이고
            //데이터가 message인 이벤트를 생성하고 전송합니다.
            if(list.size() == 0 ) {
                emitter.send(SseEmitter.event().name("notification").data(""));
            }else {  //로그인 했을 때 읽지 않은 알림을 보냅니다.
                for(Notification notification : list)
                    emitter.send(SseEmitter.event().name("notification").data(notification.getMESSAGE() + "&url=" + notification.getPASSING_URL() + "&cat=" + notification.getMESSAGE_CAT() + "&num=" + notification.getNOTIFY_ID()));
            }
        } catch (IOException e) {
            //전송 중 예외가 발생하면(IOException), 해당 emitter를 에러 상태로 완료합니다.
            emitter.completeWithError(e);
        }




        return emitter;
    }

    //특정 사용자에게 알림을 보내는 메서드입니다.
    public void sendNotification(String userId, String message, String url, int message_cat) {
        //userId를 사용하여 this.emitters 맵에서 해당 사용자에 대한 SseEmitter를 가져옵니다.
        SseEmitter emitter = this.emitters.get(userId);
        //alarm테이블에 데이터 삽입 insert (데이터를 하나르 넣고 시작)

        Notification notification = new Notification(userId, message, url, message_cat);

        dao.insert(notification);

        if (emitter != null) {
            try {
                //알림은 emitter.send() 메서드를 사용하여 전송됩니다.
                //SseEmitter.event().name("notification").data(message)를 사용하여 이름이 "notification"이고
                //데이터가 message인 이벤트를 생성하고 전송합니다.
                    emitter.send(SseEmitter.event().name("notification").data(message + "&url=" + url + "&cat=" + message_cat + "&num=" + notification.getNOTIFY_ID()));
            } catch (IOException e) {
                //전송 중 예외가 발생하면(IOException), 해당 emitter를 에러 상태로 완료합니다.
                emitter.completeWithError(e);
            }
        }
    }

    @Override
    public void delete(int num) {
        dao.delete(num);
    }
}