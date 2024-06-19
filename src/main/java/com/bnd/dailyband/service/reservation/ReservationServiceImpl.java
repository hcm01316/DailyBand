package com.bnd.dailyband.service.reservation;

import com.bnd.dailyband.domain.Reservation;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.mybatis.mapper.ReservationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService{

        private final ReservationMapper reservationMapper;

        @Autowired
        public ReservationServiceImpl(ReservationMapper reservationMapper) {
            this.reservationMapper = reservationMapper;
        }

        @Transactional
        @Override
        public void addCalendar(Reservation calendar) {
            reservationMapper.insertCalendar(calendar);
            log.info(calendar.getCal_id()+"cal_id");
        }

        @Override
        public List<Reservation> getAllCalendars(int bbs_sn) {
            return reservationMapper.getAllCalendars(bbs_sn);
        }

        @Override
        public void deleteCalendar(int cal_id) {
            reservationMapper.deleteCalendar(cal_id);
        }

        @Override
        public void updateCalendar(Reservation calendar) {
            reservationMapper.updateCalendar(calendar);
        }

        @Override
        public Rboard bandck(String id) {
            return reservationMapper.bandck(id);

        }
}
