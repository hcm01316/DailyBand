package com.bnd.dailyband.service.comment;

import com.bnd.dailyband.domain.Comment;
import com.bnd.dailyband.mybatis.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentMapper dao;

    @Autowired
    public CommentServiceImpl(CommentMapper dao) {
        this.dao = dao;
    }

    @Override
    public int getListCount(int board_num) {
        return dao.getListCount(board_num);
    }

    @Override
    public List<Comment> getCommentList(int board_num, int page) {
        int startrow = 1;
        int endrow = page;
        //double i = 1/0;

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("board_num", board_num);
        map.put("start", startrow);
        map.put("end", endrow);
        return dao.getCommentList(map);
    }

    @Override
    public int commentsInsert(Comment c) {
        return dao.commentsInsert(c);
    }

    @Override
    public int commentsDelete(int num) {
        return dao.commentsDelete(num);
    }

    @Override
    public int commentsUpdate(Comment co) {
        return dao.commentsUpdate(co);
    }

    @Override
    public int commentsReply(Comment comment) {
        return dao.commentsReply(comment);
    }

    @Override
    public String getWriter(int num) {
        return dao.getWriter(num);
    }

}
