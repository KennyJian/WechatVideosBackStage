package com.jian.mapper;

import com.jian.model.Videos;
import com.jian.vo.VideosVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom {

    public List<VideosVO> queryAllVideos(@Param("videoDesc")String videoDesc);
}