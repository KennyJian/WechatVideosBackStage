package com.jian.controller;

import com.jian.service.BgmService;
import com.jian.util.JianJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "背景音乐的接口",tags = {"背景音乐的controller"})
@RestController
@RequestMapping("/bgm")
public class BgmController {

    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "获取背景音乐列表",notes="获取背景音乐列表的接口")
    @PostMapping("/list")
    public JianJSONResult list(){
        return JianJSONResult.ok(bgmService.queryBgmList());
    }
}
