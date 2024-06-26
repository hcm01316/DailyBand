package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Comment;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.comment.CommentService;
import com.bnd.dailyband.service.notify.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/comment")
public class CommentController {

    private CommentService commentservice;
    private final SseService sseService;

    @Autowired
    public CommentController(CommentService commentservice, SseService sseService) {
        this.commentservice = commentservice;
        this.sseService = sseService;
    }


    private static final Logger Logger = LoggerFactory.getLogger(CommentController.class);

    @PostMapping(value = "/add")
    public int CommentAdd(Comment co, Principal userPrincipal, @AuthenticationPrincipal Member member) {
        int result = commentservice.commentsInsert(co);
        String writer = commentservice.getWriter(co.getBBS_SN());
        String Url = "gboard/music/detail/" + co.getBBS_SN();
        sseService.sendNotification(writer, member.getMBR_NCNM()+"님이 댓글을 추가하셨습니다.", Url, 1);

        return result;
    }


    @GetMapping(value = "/list")
    public Map<String,Object> CommentList(@RequestParam("BBS_SN") int BBS_SN, @RequestParam("page") int page) {
        List<Comment> list = commentservice.getCommentList(BBS_SN, page);
        int listcount = commentservice.getListCount(BBS_SN);
        Map<String,Object> map = new HashMap<>();
        map.put("commentlist", list);
        map.put("listcount", listcount);
        Logger.info("/comment/list");
        return map;
    }

    @PostMapping(value = "/update")
    public int CommentUpdate(Comment co) {
        return commentservice.commentsUpdate(co);
    }

    @PostMapping(value = "/delete")
    public int CommentDelete(@RequestBody Map<String, Integer> request) {
        int num = request.get("num");
        return commentservice.commentsDelete(num);
    }

    @PostMapping(value = "/reply")
    public int CommentModify(@ModelAttribute Comment comment) {
        return commentservice.commentsReply(comment);
    }

}


