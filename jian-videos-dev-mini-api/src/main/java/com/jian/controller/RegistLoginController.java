package com.jian.controller;


import com.jian.model.Users;
import com.jian.service.UserService;
import com.jian.util.JianJSONResult;
import com.jian.util.MD5Utils;
import com.jian.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value = "用户注册登陆的接口",tags = {"注册和登陆的controller"})
public class RegistLoginController extends BasicController{

    @Autowired
    private UserService userService;

    @RequestMapping("/test")
    public String test(){
        int a=1;
        int b=2;
        return "testConnect";
    }

    @ApiOperation(value = "用户注册",notes="用户注册的接口")
    @PostMapping("/regist")
    public JianJSONResult regist(@RequestBody Users users) throws Exception {

        //1.判断用户名和密码必须不为空
        if(StringUtils.isBlank(users.getUsername())||StringUtils.isBlank(users.getPassword())){
            return JianJSONResult.errorMsg("用户名和密码不能为空");
        }

        //2.判断用户名是否存在
        boolean usernameIsExist=userService.queryUsernameIsExist(users.getUsername());

        if(usernameIsExist){
            //3.保存用户,注册信息
            return JianJSONResult.errorMsg("用户已经注册过了");
        }else{
            users.setNickname(users.getUsername());
            users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
            users.setFansCounts(0);
            users.setReceiveLikeCounts(0);
            users.setFollowCounts(0);
            userService.saveUser(users);
            users.setPassword("");
            return JianJSONResult.ok(setUserRedisSessionToken(users));
        }

    }

    @ApiOperation(value = "用户登录",notes = "用户登陆的接口")
    @PostMapping("/login")
    public JianJSONResult login(@RequestBody Users users) throws Exception {
        String username=users.getUsername();
        String password=users.getPassword();

        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return JianJSONResult.errorMsg("用户名和密码不能为空");
        }

        Users userResult=userService.queryUserForLogin(username,MD5Utils.getMD5Str(password));

        if (userResult!=null){
            userResult.setPassword("");
            return JianJSONResult.ok(setUserRedisSessionToken(userResult));
        }else {
            return JianJSONResult.errorMsg("用户名或密码不正确");
        }
    }

    @ApiOperation(value = "用户注销",notes = "用户登陆的接口")
    @PostMapping("/logout")
    public JianJSONResult logout(String userId) throws Exception {
        if (redis.del(USER_REDIS_SESSION+":"+userId)){
            return JianJSONResult.ok("注销成功");
        }
        return JianJSONResult.errorMsg("登陆已过期...注销失败");
    }


    public UsersVO setUserRedisSessionToken(Users users){
        String uniqueToken= UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION+":"+users.getId(),uniqueToken,1000*60*30);
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        usersVO.setUserToken(uniqueToken);
        return usersVO;
    }

}
