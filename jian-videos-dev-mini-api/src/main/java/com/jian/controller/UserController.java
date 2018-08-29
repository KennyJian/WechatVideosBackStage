package com.jian.controller;

import com.jian.model.Users;
import com.jian.service.UserService;
import com.jian.util.JianJSONResult;
import com.jian.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(value = "用户相关业务的接口",tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户上传头像",notes = "用户上传头像的接口")
    @ApiImplicitParam(name="userId",value="用户id",required = true,dataType = "String",paramType = "query")
    @PostMapping("/uploadFace")
    public JianJSONResult uploadFace(String userId,@RequestParam("file") MultipartFile[] files) throws IOException {

        if(StringUtils.isBlank(userId)){
            return JianJSONResult.errorMsg("用户id不能为空");
        }

        //文件保存的命名空间
        String fileSpace="E:/code/workspace_wxxcx/jian_video_users_dev";
        //保存到数据库中的相对路径
        String uploadPathDB="/"+userId+"/face";

        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        try {
            if(files!=null&&files.length>0){
                String fileName=files[0].getOriginalFilename();
                if(StringUtils.isNotBlank(fileName)){
                    //文件上传的最终保存路径
                    String finalFacePath=fileSpace+uploadPathDB+"/"+fileName;
                    //设置数据库保存的路径
                    uploadPathDB+=("/"+fileName);

                    File outFile=new File(finalFacePath);
                    if(outFile.getParentFile()!=null||!outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=files[0].getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            }else{
                return JianJSONResult.errorMsg("上传出错");
            }
        }catch (Exception e){
            e.printStackTrace();
            return JianJSONResult.errorMsg("上传出错");
        }finally {
            if(fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        Users users=new Users();
        users.setId(userId);
        users.setFaceImage(uploadPathDB);
        userService.updateUserInfo(users);
        return JianJSONResult.ok(uploadPathDB);
    }

    @ApiOperation(value = "查询用户信息",notes = "查询用户信息的接口")
    @ApiImplicitParam(name="userId",value="用户id",required = true,dataType = "String",paramType = "query")
    @PostMapping("/query")
    public JianJSONResult query(String userId){
        if(StringUtils.isBlank(userId)){
            return JianJSONResult.errorMsg("用户名不能为空");
        }

        Users userInfo=userService.queryUserInfo(userId);
        if(userInfo==null){
            return JianJSONResult.errorMsg("用户id错误");
        }
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(userInfo,usersVO);
        return JianJSONResult.ok(usersVO);
    }
}
