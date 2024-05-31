package com.bnd.dailyband.service.Calendar;

import com.bnd.dailyband.domain.Calendar;
import com.bnd.dailyband.mybatis.mapper.CalendarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarMapper calendarMapper;

    @Autowired
    public CalendarServiceImpl(CalendarMapper calendarMapper) {
        this.calendarMapper = calendarMapper;
    }

    @Override
    public void addCalendar(Calendar calendar) {
        calendarMapper.insertCalendar(calendar);
    }

    @Override
    public List<Calendar> getAllCalendars(int bbs_sn) {
        return calendarMapper.getAllCalendars(bbs_sn);
    }

    @Override
    public void deleteCalendar(int cal_id) {
        calendarMapper.deleteCalendar(cal_id);
    }

}
