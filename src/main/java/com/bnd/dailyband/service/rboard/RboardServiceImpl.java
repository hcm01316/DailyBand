package com.bnd.dailyband.service.rboard;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Bandhr;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.domain.Reservation;
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
    public List<Bandhr> getbandList(int bbs_sn) {
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
    public int bandaccept(String id,int num) {
        return dao.bandaccept(id,num);
    }

    @Override
    public int isjoin(String id) {
        return dao.isjoin(id);
    }

    @Override
    public void join(String id, int num) {
        dao.join(id,num);
    }

    @Override
    public void joinwatingdel(int num) {
        dao.joinwatingdel(num);
    }

    @Override
    public List<Bandhr> getjoinlist(int bbs_sn) {
        return dao.getjoinlist(bbs_sn);
    }

    @Override
    public int myband(String id) {
        return dao.myband(id);
    }

    @Override
    public int resign(String id, int num) {
        return dao.resign(id,num);
    }

    @Override
    public int refuse(String id, int num) {
        return dao.refuse(id,num);
    }

    @Override
    public int breakup(int num) {
        return dao.breakup(num);
    }

    @Override
    public void insertBand(String id, int num) {
        dao.insertBand(id, num);
    }

    @Override
    public int getaddnum() {
        return dao.getaddnum();
    }

    @Override
    public int leave(String id, int num) {
        return dao.leave(id,num);
    }

    @Override
    public int bandacceptcnt(int num) {
        return dao.bandacceptcnt(num);
    }

    @Override
    public int getrenope(int num) {
        return dao.getrenope(num);
    }

    @Override
    public void teamstclose(int num) {
        dao.teamstclose(num);
    }

    @Override
    public void teamstopen(int num) {
        dao.teamstopen(num);
    }

    @Override
    public int updateRboard(Rboard rboard) {
        return dao.updateRboard(rboard);
    }

    @Override
    public void BandChatRoomCreate(String chatname, int hc) {
        dao.BandChatRoomCreate(chatname,hc);
    }

    @Override
    public int getChatNum() {
        return dao.getChatNum();
    }

    @Override
    public String BandTeamName(int num) {
        return dao.BandTeamName(num);
    }

    @Override
    public int MyBandChat(String name) {
        return dao.MyBandChat(name);
    }

    @Override
    public void BandChatJoin(int chat,String id) {
        dao.BandChatJoin(chat,id);
    }

    @Override
    public int JoinCk(int num, String id) {
        return dao.JoinCk(num,id);
    }

    @Override
    public int JoinCancel(int num, String id) {
        return dao.JoinCancel(num,id);
    }

    @Override
    public String getleader(int num) {
        return dao.getleader(num);
    }

    @Override
    public List<String> bandlist(int num) {
        return dao.bandlist(num);
    }

    //합주실 예약 현황
    @Override
    public List<Reservation> getRoomResList(int bandck) {
        return dao.getRoomResList(bandck);
    }


}