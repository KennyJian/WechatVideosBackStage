package com.jian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jian.mapper.VideosMapper;
import com.jian.mapper.VideosMapperCustom;
import com.jian.model.Videos;
import com.jian.service.VideoService;
import com.jian.util.PagedResult;
import com.jian.vo.VideosVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos videos) {
        String id=sid.nextShort();
        videos.setId(id);
        videosMapper.insertSelective(videos);
        return id;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updataVideo(String videoId, String coverPath) {
        Videos videos=new Videos();
        videos.setId(videoId);
        videos.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(videos);
    }

    @Override
    public PagedResult getAllVideos(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<VideosVO> list=videosMapperCustom.queryAllVideos();
        PageInfo<VideosVO> pageList=new PageInfo<>(list);
        PagedResult pagedResult=new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);
        return pagedResult;
    }
}
