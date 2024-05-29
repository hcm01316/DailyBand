package com.bnd.dailyband.service.rboard;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Mgmt;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.domain.Rlist;
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
    public int getListCount() {
        return dao.getListCount();
    }

    @Override
    public List<Rlist> getRboardList(int page, int limit) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int startrow =(page -1) * limit+1;
        int endrow = startrow + limit -1;
        map.put("start", startrow);
        map.put("end", endrow);
        return dao.getRboardList(map);
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

    @Override
    public List<Mgmt> getbandList(int bbs_sn) {
        return dao.getbandList(bbs_sn);
    }

    @Override
    public int bandck(String id) {

        int result = 0;
        if (dao.bandck(id) == null)
        {
            result = -1;
        }
        return result;
    }

    @Override
    public int leaderck(String id) {
        return dao.leaderck(id);
    }

    @Override
    public void bandaccept(String id) {
        dao.bandaccept(id);
    }
}

