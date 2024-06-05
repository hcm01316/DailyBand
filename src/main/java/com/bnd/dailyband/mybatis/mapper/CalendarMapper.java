package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Calendar;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CalendarMapper {
    void insertCalendar(Calendar calendar);
    List<Calendar> getAllCalendars(int bbs_sn);
    void deleteCalendar(int cal_id);
    void updateCalendar(Calendar calendar);
}
