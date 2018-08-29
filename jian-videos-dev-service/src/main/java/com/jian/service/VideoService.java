package com.jian.service;

import com.jian.model.Videos;

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
}
