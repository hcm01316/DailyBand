package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {

    int getListCount(int board_num);

    int commentsInsert(Comment c);

    List<Comment> getCommentList(Map<String, Integer> map);

    int commentsDelete(int num);

    int commentsUpdate(Comment co);

    int commentsReply(Comment comment);
}
