package com.bnd.dailyband.service.rboard;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.mybatis.mapper.RboardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class RboardServiceImpl implements RboardService {

    private RboardMapper dao;

    @Autowired
    public RboardServiceImpl(RboardMapper dao) {
        this.dao = dao;
    }


    @Override
    public List<Rboard> getRboardList(int page, int limit) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow =(page -1) * limit+1;
        int endrow = startrow + limit -1;
        map.put("start", startrow);
        map.put("end", endrow);
        return dao.getBoardList(map);
    }

    @Override
    public ArrayList<Ctgry> getCtgryList(int type) {
        return dao.getCtgryList(type);
    }

    @Override
    public void insertRboard(Rboard rboard) {
        dao.insertRboard(rboard);
    }

    @Override
    public int setReadCountUpdate(int num) {
        return dao.setReadCountUpdate(num);
    }

    @Override
    public Rboard getDetail(int num) {
        return dao.getDetail(num);
    }

}

