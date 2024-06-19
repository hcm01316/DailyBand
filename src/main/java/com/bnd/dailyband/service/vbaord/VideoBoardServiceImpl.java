// BoardServiceImpl.java
package com.bnd.dailyband.service.vbaord;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.mybatis.mapper.VideoBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class VideoBoardServiceImpl implements VideoBoardService {

    private VideoBoardMapper videoBoardMapper;

    @Autowired
    public VideoBoardServiceImpl(VideoBoardMapper videoBoardMapper){
        this.videoBoardMapper = videoBoardMapper;
    }

    @Override
    public int getBoardListCount() {
        return videoBoardMapper.getBoardListCount();
    }

    @Override
    public ArrayList<Ctgry> getCtgryList(int type) {

        return videoBoardMapper.getCtgryList(type);
    }

    @Override
    public void addBoard(Board board) {
        videoBoardMapper.insertBoard(board);
    }

    @Override
    public List<Board> getBoardList(int page, int limit) {

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow=(page-1)*limit+1;
        int endrow=startrow+limit-1;
        map.put("start", startrow);
        map.put("end", endrow);
        return videoBoardMapper.getBoardList(map);
    }

    @Override
    public Board getBoardById(int id) {
        return videoBoardMapper.getBoardById(id);
    }

    @Override
    public void increaseReadCount(int id) {
        videoBoardMapper.increaseReadCount(id);
    }
    @Override
    public int likeBoard(int id) {
        // 게시글 추천 수 증가 로직
        videoBoardMapper.increaseLikes(id);
        return videoBoardMapper.getLikes(id);
    }

    @Override
    public int dislikeBoard(int id) {
        // 게시글 비추천 수 증가 로직
        videoBoardMapper.increaseDislikes(id);
        return videoBoardMapper.getDislikes(id);
    }

    @Override
    public void deleteBoard(int id) {
        videoBoardMapper.deleteBoard(id);
    }

    @Override
    public void updateBoard(int id, Board updatedBoard) {
        videoBoardMapper.updateBoard(id, updatedBoard);
    }
}

