package com.bnd.dailyband.service.Calendar;

import com.bnd.dailyband.domain.Calendar;

import java.util.List;

public interface CalendarService {
    void addCalendar(Calendar calendar);
    List<Calendar> getAllCalendars(int bbs_sn);
    void deleteCalendar(int cal_id);
    void updateCalendar(Calendar calendar);
}
