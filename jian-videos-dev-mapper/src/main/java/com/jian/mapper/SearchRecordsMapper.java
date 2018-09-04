package com.jian.mapper;

import com.jian.model.SearchRecords;

import java.util.List;

public interface SearchRecordsMapper {
    int deleteByPrimaryKey(String id);

    List<String> selectAll();

    int insert(SearchRecords record);

    int insertSelective(SearchRecords record);

    SearchRecords selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SearchRecords record);

    int updateByPrimaryKey(SearchRecords record);
}