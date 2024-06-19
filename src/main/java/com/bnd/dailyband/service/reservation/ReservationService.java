package com.bnd.dailyband.service.reservation;

import com.bnd.dailyband.domain.Reservation;
import com.bnd.dailyband.domain.Rboard;

import java.util.List;

public interface ReservationService {
    void addCalendar(Reservation calendar);
    List<Reservation> getAllCalendars(int bbs_sn);
    void deleteCalendar(int cal_id);
    void updateCalendar(Reservation calendar);
    public Rboard bandck(String id);
}
