package com.jian.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegTest {

    private String ffmpegEXE;

    public FFMpegTest(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath,String videoOutputPath) throws IOException {
        // ffmpeg -i input.mp4 output.avi
        List<String> command=new ArrayList<>();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
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
        FFMpegTest ffmpet =new FFMpegTest("E:/ffmpeg/ffmpeg/bin/ffmpeg.exe");
        try {
            ffmpet.convertor("E:/ffmpeg/ffmpeg/bin/test.mp4","E:/ffmpeg/ffmpeg/bin/test.avi");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
