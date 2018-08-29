package com.jian.service;


import com.jian.model.Bgm;
import com.jian.model.Users;

import java.util.List;

public interface BgmService {

    /**
     * 查询背景音乐列表
     * @return
     */
    public List<Bgm> queryBgmList();

    public Bgm queryBgmById(String bgmId);
}
