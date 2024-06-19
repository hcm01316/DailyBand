package com.bnd.dailyband.service.comment;

import com.bnd.dailyband.domain.Comment;

import java.util.List;

public interface CommentService {
    // 글의 갯수 구하기
    public int getListCount(int board_num);

    // 댓글 목록 가져오기
    public List<Comment> getCommentList(int board_num, int page);

    // 댓글 등록하기
    public int commentsInsert(Comment c);

    // 댓글 삭제
    public int commentsDelete(int num);

    // 댓글 수정
    public int commentsUpdate(Comment co);

    int commentsReply(Comment comment);
}

