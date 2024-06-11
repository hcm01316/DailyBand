package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.ChatMsg;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.chat.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.Map;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
        if (member != null) {
            model.addAttribute("profilePhoto", member.getProfilePhoto());
            model.addAttribute("username", member.getUsername());
        }
    }

    @RequestMapping("/chat")
    public ModelAndView chatlist(ModelAndView mv) {
        mv.setViewName("chat/chat_room");
        return mv;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ChatMsg sendMessage(Map<String, Object> message, Principal principal) throws Exception {
        Thread.sleep(1000);
        String id = principal.getName();
        String MBR_NCNM = HtmlUtils.htmlEscape((String) message.get("MBR_NCNM"));
        String MBR_ID = HtmlUtils.htmlEscape(id);
        int BBS_SN = (int) message.get("BBS_SN");
        String MSG_CN = HtmlUtils.htmlEscape((String) message.get("MSG_CN"));
        logger.info(HtmlUtils.htmlEscape((String) message.get("SNDNG_DT")));
        String SNDNG_DT = HtmlUtils.htmlEscape((String) message.get("SNDNG_DT"));
        chatService.chatinsert(id,MSG_CN,BBS_SN);
        String MBR_PROFL_PHOTO = chatService.myprofl(id);

        ChatMsg me = new ChatMsg();
        me.setMBR_NCNM(MBR_NCNM);
        me.setBBS_SN(BBS_SN);
        me.setMSG_CN(MSG_CN);
        me.setSNDNG_DT(SNDNG_DT);
        me.setMBR_ID(MBR_ID);
        me.setMBR_PROFL_PHOTO(MBR_PROFL_PHOTO);
        logger.info("Received message:", me);
        return me;
    }
}
