package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Board;
import com.bnd.dailyband.domain.Ctgry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface VideoBoardMapper {
    int getBoardListCount();
    public ArrayList<Ctgry> getCtgryList(int type);
    void insertBoard(Board board);
    List<Board> getBoardList(HashMap<String, Integer> map);
    Board getBoardById(int id);
    void increaseReadCount(@Param("id") int id);
    void increaseLikes(int id);
    void increaseDislikes(int id);
    int getLikes(int id);
    int getDislikes(int id);
    void deleteBoard(int id);
    void updateBoard(@Param("id") int id,Board updatedBoard);
}

