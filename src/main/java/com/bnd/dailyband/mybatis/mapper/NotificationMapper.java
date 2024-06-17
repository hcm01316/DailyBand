package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {
    public int insert(Notification notification);

    List<Notification> getList(String userId);

    public String update(Notification notification);

    void delete(int num);
}
