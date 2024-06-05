package com.bnd.dailyband.service.Calendar;

import com.bnd.dailyband.domain.Calendar;
import com.bnd.dailyband.mybatis.mapper.CalendarMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarMapper calendarMapper;

    @Autowired
    public CalendarServiceImpl(CalendarMapper calendarMapper) {
        this.calendarMapper = calendarMapper;
    }

    @Transactional
    @Override
    public void addCalendar(Calendar calendar) {
        calendarMapper.insertCalendar(calendar);
        log.info(calendar.getCal_id()+"cal_id");
    }

    @Override
    public List<Calendar> getAllCalendars(int bbs_sn) {
        return calendarMapper.getAllCalendars(bbs_sn);
    }

    @Override
    public void deleteCalendar(int cal_id) {
        calendarMapper.deleteCalendar(cal_id);
    }

    @Override
    public void updateCalendar(Calendar calendar) {
        calendarMapper.updateCalendar(calendar);
    }

}
