// BoardServiceImpl.java
package com.bnd.dailyband.service.mboard;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.mybatis.mapper.MusicBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class MusicBoardServiceImpl implements MusicBoardService {

    private MusicBoardMapper musicBoardMapper;

    @Autowired
    public MusicBoardServiceImpl(MusicBoardMapper musicBoardMapper){
        this.musicBoardMapper = musicBoardMapper;
    }

    @Override
    public int getBoardListCount() {
        return musicBoardMapper.getBoardListCount();
    }

    @Override
    public ArrayList<Ctgry> getCtgryList(int type) {

        return musicBoardMapper.getCtgryList(type);
    }

    @Override
    public void addBoard(Board board) {
        musicBoardMapper.insertBoard(board);
    }

    @Override
    public List<Board> getBoardList(int page, int limit) {

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow=(page-1)*limit+1;
        int endrow=startrow+limit-1;
        map.put("start", startrow);
        map.put("end", endrow);
        return musicBoardMapper.getBoardList(map);
    }

    @Override
    public Board getBoardById(int id) {
        return musicBoardMapper.getBoardById(id);
    }

    @Override
    public void increaseReadCount(int id) {
        musicBoardMapper.increaseReadCount(id);
    }
    @Override
    public int likeBoard(int id) {
        // 게시글 추천 수 증가 로직
        musicBoardMapper.increaseLikes(id);
        return musicBoardMapper.getLikes(id);
    }

    @Override
    public int dislikeBoard(int id) {
        // 게시글 비추천 수 증가 로직
        musicBoardMapper.increaseDislikes(id);
        return musicBoardMapper.getDislikes(id);
    }

    @Override
    public void deleteBoard(int id) {
        musicBoardMapper.deleteBoard(id);
    }

    @Override
    public void updateBoard(int id, Board updatedBoard) {
        musicBoardMapper.updateBoard(id, updatedBoard);
    }

    @Override
    public int getLikesCount(int id) {
        return musicBoardMapper.getLikes(id);
    }

    @Override
    public int getDislikesCount(int id) {
        return musicBoardMapper.getDislikes(id);
    }

    @Override
    public void unlikeBoard(int id) {
        musicBoardMapper.decreaseLikes(id);
    }

    @Override
    public void undislikeBoard(int id) {
        musicBoardMapper.decreaseDislikes(id);
    }
}

