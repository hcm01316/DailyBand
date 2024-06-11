package com.bnd.dailyband.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

    public void chatinsert(String id, String cn, int num);

    public String myprofl(String id);
}
