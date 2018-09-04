package com.jian.service;

import com.jian.model.Comments;
import com.jian.model.Videos;
import com.jian.util.PagedResult;

import java.util.List;

public interface VideoService {

    /**
     * 保存视频
     * @param videos
     */
    public String saveVideo(Videos videos);

    /**
     * 修改视频的封面
     * @param videoId
     * @param coverPath
     * @return
     */
    public void updataVideo(String videoId,String coverPath);

    /**
     * 分页和搜索查询视频列表
     * @param video
     * @param isSaveRecord 1:需要保存 0:不需要保存,或者为空的时候
     * @param page
     * @return
     */
    public PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page,Integer pageSize);

    /**
     * 获取热门搜索的列表
     * @return
     */
    public List<String> getHotwords();

    /**
     * 保存评论
     * @param comments
     */
    void saveComment(Comments comments);
}
