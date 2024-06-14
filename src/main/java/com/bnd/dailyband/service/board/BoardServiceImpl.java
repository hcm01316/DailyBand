// BoardServiceImpl.java
package com.bnd.dailyband.service.board;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.mybatis.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private BoardMapper boardMapper;

    @Autowired
    public  BoardServiceImpl(BoardMapper boardMapper){
        this.boardMapper = boardMapper;
    }

    @Override
    public int getBoardListCount() {
        return boardMapper.getBoardListCount();
    }

    @Override
    public ArrayList<Ctgry> getCtgryList(int type) {

        return boardMapper.getCtgryList(type);
    }

    @Override
    public void addBoard(Board board) {
        boardMapper.insertBoard(board);
    }

    @Override
    public List<Board> getBoardList(int page, int limit) {

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow=(page-1)*limit+1;
        int endrow=startrow+limit-1;
        map.put("start", startrow);
        map.put("end", endrow);
        return boardMapper.getBoardList(map);
    }

    @Override
    public Board getBoardById(int id) {
        return boardMapper.getBoardById(id);
    }

    @Override
    public void increaseReadCount(int id) {
        boardMapper.increaseReadCount(id);
    }
    @Override
    public int likeBoard(int id) {
        // 게시글 추천 수 증가 로직
        boardMapper.increaseLikes(id);
        return boardMapper.getLikes(id);
    }

    @Override
    public int dislikeBoard(int id) {
        // 게시글 비추천 수 증가 로직
        boardMapper.increaseDislikes(id);
        return boardMapper.getDislikes(id);
    }

    @Override
    public void deleteBoard(int id) {
        boardMapper.deleteBoard(id);
    }

    @Override
    public void updateBoard(int id, Board updatedBoard) {
        boardMapper.updateBoard(id, updatedBoard);
    }
}

