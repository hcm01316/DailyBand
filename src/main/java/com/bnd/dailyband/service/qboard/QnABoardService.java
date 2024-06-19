// BoardService.java
package com.bnd.dailyband.service.qboard;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;

import java.util.ArrayList;
import java.util.List;

public interface QnABoardService {
    int getBoardListCount();
    public ArrayList<Ctgry> getCtgryList(int type);
    void addBoard(Board board);
    public List<Board> getBoardList(int page, int limit);
    Board getBoardById(int id);
    void increaseReadCount(int id);
    int likeBoard(int id);
    int dislikeBoard(int id);
    void deleteBoard(int id);
    void updateBoard(int id, Board updatedBoard);
}

