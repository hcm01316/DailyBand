// BoardServiceImpl.java
package com.bnd.dailyband.service.qboard;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.mybatis.mapper.QnABoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class QnABoardServiceImpl implements QnABoardService {

    private QnABoardMapper qnABoardMapper;

    @Autowired
    public QnABoardServiceImpl(QnABoardMapper qnABoardMapper){
        this.qnABoardMapper = qnABoardMapper;
    }

    @Override
    public int getBoardListCount() {
        return qnABoardMapper.getBoardListCount();
    }

    @Override
    public ArrayList<Ctgry> getCtgryList(int type) {

        return qnABoardMapper.getCtgryList(type);
    }

    @Override
    public void addBoard(Board board) {
        qnABoardMapper.insertBoard(board);
    }

    @Override
    public List<Board> getBoardList(int page, int limit) {

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow=(page-1)*limit+1;
        int endrow=startrow+limit-1;
        map.put("start", startrow);
        map.put("end", endrow);
        return qnABoardMapper.getBoardList(map);
    }

    @Override
    public Board getBoardById(int id) {
        return qnABoardMapper.getBoardById(id);
    }

    @Override
    public void increaseReadCount(int id) {
        qnABoardMapper.increaseReadCount(id);
    }
    @Override
    public int likeBoard(int id) {
        // 게시글 추천 수 증가 로직
        qnABoardMapper.increaseLikes(id);
        return qnABoardMapper.getLikes(id);
    }

    @Override
    public int dislikeBoard(int id) {
        // 게시글 비추천 수 증가 로직
        qnABoardMapper.increaseDislikes(id);
        return qnABoardMapper.getDislikes(id);
    }

    @Override
    public void deleteBoard(int id) {
        qnABoardMapper.deleteBoard(id);
    }

    @Override
    public void updateBoard(int id, Board updatedBoard) {
        qnABoardMapper.updateBoard(id, updatedBoard);
    }
}

