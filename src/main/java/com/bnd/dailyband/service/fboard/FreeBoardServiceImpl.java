// BoardServiceImpl.java
package com.bnd.dailyband.service.fboard;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.mybatis.mapper.FreeBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class FreeBoardServiceImpl implements FreeBoardService {

    private FreeBoardMapper freeBoardMapper;

    @Autowired
    public FreeBoardServiceImpl(FreeBoardMapper freeBoardMapper){
        this.freeBoardMapper = freeBoardMapper;
    }

    @Override
    public int getBoardListCount() {
        return freeBoardMapper.getBoardListCount();
    }

    @Override
    public ArrayList<Ctgry> getCtgryList(int type) {

        return freeBoardMapper.getCtgryList(type);
    }

    @Override
    public void addBoard(Board board) {
        freeBoardMapper.insertBoard(board);
    }

    @Override
    public List<Board> getBoardList(int page, int limit) {

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow=(page-1)*limit+1;
        int endrow=startrow+limit-1;
        map.put("start", startrow);
        map.put("end", endrow);
        return freeBoardMapper.getBoardList(map);
    }

    @Override
    public Board getBoardById(int id) {
        return freeBoardMapper.getBoardById(id);
    }

    @Override
    public void increaseReadCount(int id) {
        freeBoardMapper.increaseReadCount(id);
    }
    @Override
    public int likeBoard(int id) {
        // 게시글 추천 수 증가 로직
        freeBoardMapper.increaseLikes(id);
        return freeBoardMapper.getLikes(id);
    }

    @Override
    public int dislikeBoard(int id) {
        // 게시글 비추천 수 증가 로직
        freeBoardMapper.increaseDislikes(id);
        return freeBoardMapper.getDislikes(id);
    }

    @Override
    public void deleteBoard(int id) {
        freeBoardMapper.deleteBoard(id);
    }

    @Override
    public void updateBoard(int id, Board updatedBoard) {
        freeBoardMapper.updateBoard(id, updatedBoard);
    }
}

