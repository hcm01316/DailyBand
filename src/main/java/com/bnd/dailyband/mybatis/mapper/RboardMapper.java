package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Rboard;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface RboardMapper  {

    // 글의 갯수 구하기
    public int getListCount();

    // 모집 게시판 리시트 구하기
    public List<Rboard> getBoardList(HashMap<String, Integer> map);

    // 모집 게시판 글 내용 보기
    public Rboard getDetail(int num);

    //글 수정
    public int boardModify(Rboard modifyboard);

    //글 삭제
    public int boardDelete(Rboard rboard);

    // 조회수 업데이트
    public int setReadCountUpdate(int num);

    // 글쓴이인지 확인
    public Rboard isBoardWriter(HashMap<String, Object> map);

    // 글 등록하기
    public void insertRboard(Rboard rboard);

    public ArrayList<Ctgry> getCtgryList(int type);

}
