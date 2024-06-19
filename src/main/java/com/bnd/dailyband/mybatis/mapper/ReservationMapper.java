package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Reservation;
import com.bnd.dailyband.domain.Rboard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    void insertCalendar(Reservation calendar);
    List<Reservation> getAllCalendars(int bbs_sn);
    void deleteCalendar(int cal_id);
    void updateCalendar(Reservation calendar);
    Rboard bandck (String id);
}
