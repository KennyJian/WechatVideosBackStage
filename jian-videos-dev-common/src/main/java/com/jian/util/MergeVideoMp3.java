package com.jian.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {

    private String ffmpegEXE;

    public MergeVideoMp3(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath,String BgmInputPath,String videoOutputPath,double second) throws IOException {
        // ffmpeg.exe -i test.mp4 -i 苏打绿-无与伦比的美丽.mp3 -t 7 -y 新的视频.mp4
        List<String> command=new ArrayList<>();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-i");
        command.add(BgmInputPath);
        command.add("-t");
        command.add(String.valueOf(second));
        command.add("-y");
        command.add(videoOutputPath);

        for(String c:command){
            System.out.println(c);
        }
        ProcessBuilder processBuilder=new ProcessBuilder(command);
        Process process=processBuilder.start();
        InputStream errorStream=process.getErrorStream();
        InputStreamReader inputStreamReader=new InputStreamReader(errorStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

        String line="";
        while((line=bufferedReader.readLine())!=null){
            System.out.println(line);
        }

        if(bufferedReader!=null){
            bufferedReader.close();
        }
        if(inputStreamReader!=null){
            inputStreamReader.close();
        }
        if(errorStream!=null){
            errorStream.close();
        }
    }

    public static void main(String[] args){
        MergeVideoMp3 ffmpet =new MergeVideoMp3("E:/ffmpeg/ffmpeg/bin/ffmpeg.exe");
        try {
            ffmpet.convertor("E:/ffmpeg/ffmpeg/bin/test.mp4","E:/ffmpeg/ffmpeg/bin/苏打绿-无与伦比的美丽.mp3","E:/ffmpeg/ffmpeg/bin/整合后的视频.avi",7);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
