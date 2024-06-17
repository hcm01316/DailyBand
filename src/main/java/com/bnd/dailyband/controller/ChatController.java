package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.*;
import com.bnd.dailyband.service.chat.ChatService;
import com.bnd.dailyband.service.rboard.RboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private ChatService chatService;
    private RboardService rboardService;
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public ChatController(ChatService chatService, RboardService rboardService,SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.rboardService = rboardService;
        this.messagingTemplate = messagingTemplate;
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
        if (member != null) {
            model.addAttribute("profilePhoto", member.getProfilePhoto());
            model.addAttribute("username", member.getUsername());
        }
    }

    @RequestMapping("/chat")
    public ModelAndView chatlist(ModelAndView mv,Principal principal) {

        String id = principal.getName();
        String myname = chatService.MyName(id);
        List<ChatRoom> Chatroom = chatService.ChatRoomList(id);
        mv.addObject("Chatroom",Chatroom);
        mv.addObject("myname",myname);
        mv.setViewName("chat/chat_room");
        return mv;
    }

    @MessageMapping("/Msg")
    public void sendMessage(Map<String, Object> message, Principal principal) throws Exception {
        Thread.sleep(1000);
        String id = principal.getName();
        String MBR_NCNM = HtmlUtils.htmlEscape((String) message.get("MBR_NCNM"));
        String MBR_ID = HtmlUtils.htmlEscape(id);
        int CHAT_ROOM_ID = (int) message.get("CHAT_ROOM_ID");
        String MSG_CN = HtmlUtils.htmlEscape((String) message.get("MSG_CN"));
        logger.info(HtmlUtils.htmlEscape((String) message.get("SNDNG_DT")));
        String SNDNG_DT = HtmlUtils.htmlEscape((String) message.get("SNDNG_DT"));
        chatService.chatinsert(id,MSG_CN,CHAT_ROOM_ID);
        String MBR_PROFL_PHOTO = chatService.myprofl(id);

        ChatMsg me = new ChatMsg();
        me.setMBR_NCNM(MBR_NCNM);
        me.setCHAT_ROOM_ID(CHAT_ROOM_ID);
        me.setMSG_CN(MSG_CN);
        me.setSNDNG_DT(SNDNG_DT);
        me.setMBR_ID(MBR_ID);
        me.setMBR_PROFL_PHOTO(MBR_PROFL_PHOTO);
        logger.info("Received message:", me);
        messagingTemplate.convertAndSend("/topic/chat/" + CHAT_ROOM_ID, me);
        logger.info("Message sent to /topic/chat/" + CHAT_ROOM_ID);
    }

    @GetMapping("/room/{chatRoomId}/messages")
    public List<Chatlist> getMessagesByChatRoomId(@PathVariable int chatRoomId) {
        try {
            return chatService.chatlist(chatRoomId);
        } catch (Exception e) {
            logger.error("Error fetching messages for chatRoomId " + chatRoomId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch messages", e);
        }
    }

    @GetMapping("/chatstart")
    public ModelAndView chatstart(Principal principal,ModelAndView mv,String id) {
        String myid = principal.getName();
        int ischat = chatService.IsChat(id,myid);


        if (ischat == 0)
        {
            String myname = chatService.MyName(myid);
            String name = chatService.MyName(id);

            chatService.ChatCreate(myname,name);
            int num = rboardService.getChatNum();
            rboardService.BandChatJoin(num,id);
            rboardService.BandChatJoin(num,myid);

        }
        List<ChatRoom> Chatroom = chatService.ChatRoomList(myid);
        mv.addObject("Chatroom",Chatroom);
        mv.setViewName("chat/chat_room");

        return mv;

    }

}
