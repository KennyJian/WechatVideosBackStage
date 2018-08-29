package com.jian.controller;

import com.jian.enums.VideoStatusEnum;
import com.jian.model.Bgm;
import com.jian.model.Users;
import com.jian.model.Videos;
import com.jian.service.BgmService;
import com.jian.service.VideoService;
import com.jian.util.FetchVideoCover;
import com.jian.util.JianJSONResult;
import com.jian.util.MergeVideoMp3;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Date;
import java.util.UUID;

@Api(value = "视频相关业务的接口",tags = {"视频相关业务的controller"})
@RestController
@RequestMapping(value = "/video",headers = "content-type=multipart/form-data")
public class VideoController extends BasicController{


    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "上传视频",notes = "上传视频的接口")
    @ApiImplicitParams({
       @ApiImplicitParam(name="userId",value="用户id",required = true,dataType = "String",paramType = "form"),
       @ApiImplicitParam(name="bgmId",value="背景音乐id",required = false,dataType = "String",paramType = "form"),
       @ApiImplicitParam(name="videoSeconds",value="视频秒数",required = true,dataType = "String",paramType = "form"),
       @ApiImplicitParam(name="videoWidth",value="视频宽度",required = true,dataType = "String",paramType = "form"),
       @ApiImplicitParam(name="videoHeight",value="视频高度",required = true,dataType = "String",paramType = "form"),
       @ApiImplicitParam(name="description",value="描述",required = false,dataType = "String",paramType = "form")

    })
    @PostMapping(value = "/uploadVideo",headers="content-type=multipart/form-data")
    public JianJSONResult uploadFace(String userId,
                                     String bgmId,
                                     double videoSeconds,
                                     int videoWidth,
                                     int videoHeight,
                                     String description,
                                     @ApiParam(value = "短视频",required = true) MultipartFile file) throws IOException, InterruptedException {

        if(StringUtils.isBlank(userId)){
            return JianJSONResult.errorMsg("用户id不能为空");
        }

        //文件保存的命名空间
        //String fileSpace="E:/code/workspace_wxxcx/jian_video_users_dev";
        //保存到数据库中的相对路径
        String uploadPathDB="/"+userId+"/video";
        String coverPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        //文件上传最终保存路径
        String finalVideoPath="";
        try {
            if(file!=null){
                String fileName=file.getOriginalFilename();
                //abc.mp4
                String arrayFilenameItem[]=fileName.split("\\.");
                String fileNamePrefix="";
                for(int i=0;i<arrayFilenameItem.length-1;i++){
                    fileNamePrefix+=arrayFilenameItem[i];
                }
                // fix bug: 解决小程序端OK，PC端不OK的bug，原因：PC端和小程序端对临时视频的命名不同
//				String fileNamePrefix = fileName.split("\\.")[0];
                if(StringUtils.isNotBlank(fileName)){
                    //文件上传的最终保存路径
                    finalVideoPath=FILE_SPACE+uploadPathDB+"/"+fileName;
                    //设置数据库保存的路径
                    uploadPathDB+=("/"+fileName);
                    coverPathDB=coverPathDB+"/"+fileNamePrefix+".jpg";
                    File outFile=new File(finalVideoPath);
                    if(outFile.getParentFile()!=null||!outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=file.getInputStream();
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

        if(StringUtils.isNotBlank(bgmId)){
            Bgm bgm=bgmService.queryBgmById(bgmId);
            String mp3InputPath=FILE_SPACE+bgm.getPath();
            MergeVideoMp3 mergeVideoMp3=new MergeVideoMp3(FFMPEG_EXE);
            String videoInputPath=finalVideoPath;
            String videoOutputName= UUID.randomUUID().toString()+".mp4";
            uploadPathDB="/"+userId+"/video"+"/"+videoOutputName;
            finalVideoPath=FILE_SPACE+uploadPathDB;
            mergeVideoMp3.convertor(videoInputPath,mp3InputPath,finalVideoPath,videoSeconds);
        }
        System.out.println("uploadPathDB=" + uploadPathDB);
        System.out.println("finalVideoPath=" + finalVideoPath);

        //对视频进行截图
        FetchVideoCover fetchVideoCover=new FetchVideoCover(FFMPEG_EXE);
        fetchVideoCover.getCover(finalVideoPath,FILE_SPACE+coverPathDB);

        Videos videos=new Videos();
        videos.setAudioId(bgmId);
        videos.setUserId(userId);
        videos.setVideoSeconds((float)videoSeconds);
        videos.setVideoHeight(videoHeight);
        videos.setVideoWidth(videoWidth);
        videos.setVideoDesc(description);
        videos.setVideoPath(uploadPathDB);
        videos.setCoverPath(coverPathDB);
        videos.setStatus(VideoStatusEnum.SUCCESS.value);
        videos.setCreateTime(new Date());
        String videoId=videoService.saveVideo(videos);


        return JianJSONResult.ok(videoId);
    }

    @ApiOperation(value = "上传封面",notes = "上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value="用户主键的id",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(name="videoId",value="视频主键的id",required = true,dataType = "String",paramType = "form")

    })
    @PostMapping(value = "/uploadCover",headers = "content-type=multipart/form-data")
    public JianJSONResult uploadCover(String userId,
                                      String videoId,
                                     @ApiParam(value = "视频封面",required = true) MultipartFile file)  throws IOException {

        if(StringUtils.isBlank(videoId)||StringUtils.isBlank(userId)){
            return JianJSONResult.errorMsg("视频主键id和用户主键id不能为空");
        }

        //文件保存的命名空间
        //String fileSpace="E:/code/workspace_wxxcx/jian_video_users_dev";
        //保存到数据库中的相对路径
        String uploadPathDB="/"+userId+"/video";

        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        //文件上传最终保存路径
        String finalCoverPath="";
        try {
            if(file!=null){
                String fileName=file.getOriginalFilename();
                if(StringUtils.isNotBlank(fileName)){
                    //文件上传的最终保存路径
                    finalCoverPath=FILE_SPACE+uploadPathDB+"/"+fileName;
                    //设置数据库保存的路径
                    uploadPathDB+=("/"+fileName);
                    File outFile=new File(finalCoverPath);
                    if(outFile.getParentFile()!=null||!outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=file.getInputStream();
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

        videoService.updataVideo(videoId,uploadPathDB);
        return JianJSONResult.ok();
    }
}
