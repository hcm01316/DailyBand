package com.bnd.dailyband.service.rboard;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Rboard;

import java.util.ArrayList;
import java.util.List;

public interface RboardService {
    public List<Rboard> getRboardList(int page, int limit);

    public ArrayList<Ctgry> getCtgryList(int type);

    public void insertRboard(Rboard rboard);

    public int setReadCountUpdate(int num);
    // 글 내용 보기
    public Rboard getDetail(int num);

}
