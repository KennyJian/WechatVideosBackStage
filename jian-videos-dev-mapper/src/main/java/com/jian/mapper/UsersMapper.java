package com.jian.mapper;

import com.jian.model.Users;

public interface UsersMapper {
    int deleteByPrimaryKey(String id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(String id);

    Users selectByUserName(String userName);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
}