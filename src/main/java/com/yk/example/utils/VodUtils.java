package com.yk.example.utils;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.yk.example.dto.VodAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created by yk on 2018/3/29.
 */
public class VodUtils {

    private static final Logger logger = LoggerFactory.getLogger(VodUtils.class);


    /**
     * 账号AK信息请填写(必选)
     */
    private static final String accessKeyId = "LTAIBnXqKTOw0LYp";
    /**
     * 账号AK信息请填写(必选)
     */
    private static final String accessKeySecret = "BoiKZRrXvpVG05iIRLb37DEg93t637";

    private static final DefaultAcsClient aliyunClient = new DefaultAcsClient(
            DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret));


    public static void main(String[] args){
        DefaultAcsClient aliyunClient;
        aliyunClient = new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai",accessKeyId,accessKeySecret));
        String videoId = createUploadVideo(aliyunClient, "test867789.mp4", 1000);
        System.out.println("VideoId:"+videoId);
        //refreshUploadVideo(aliyunClient, videoId);
    }
    private static String createUploadVideo(DefaultAcsClient client,String fileName, long fileSize) {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        CreateUploadVideoResponse response = null;
        try {
            request.setFileName(fileName);
            request.setFileSize(Long.valueOf(fileSize));
            request.setTitle(fileName);

            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            System.out.println("CreateUploadVideoRequest Server Exception:");
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("CreateUploadVideoRequest Client Exception:");
            e.printStackTrace();
        }
        System.out.println("RequestId:"+response.getRequestId());
        System.out.println("UploadAuth:"+response.getUploadAuth());
        System.out.println("Address:"+response.getUploadAddress());
        return response.getVideoId();
    }

    /**
     * 获取上传凭证
     *
     * @param fileName
     * @param title
     * @return
     */
    public static VodAuth createUploadVideo(String fileName, String title) {

        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        CreateUploadVideoResponse response = null;
        try {
              /*必选，视频源文件名称（必须带后缀, 支持 ".3gp", ".asf", ".avi", ".dat", ".dv", ".flv", ".f4v", ".gif", ".m2t", ".m3u8", ".m4v", ".mj2", ".mjpeg", ".mkv", ".mov", ".mp4", ".mpe", ".mpg", ".mpeg", ".mts", ".ogg", ".qt", ".rm", ".rmvb", ".swf", ".ts", ".vob", ".wmv", ".webm"".aac", ".ac3", ".acm", ".amr", ".ape", ".caf", ".flac", ".m4a", ".mp3", ".ra", ".wav", ".wma"）*/
            request.setFileName("1111.mp4");
            //必选，视频标题
            request.setTitle("test");
            request.setFileSize(Long.valueOf(1000));
            response = aliyunClient.getAcsResponse(request);
        } catch (ServerException e) {
            logger.info("CreateUploadVideoRequest Server Exception:");
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            logger.info("CreateUploadVideoRequest Client Exception:");
            e.printStackTrace();
            return null;
        }
        return new VodAuth(response.getRequestId(), response.getUploadAuth(), response.getVideoId(), response.getUploadAddress());
    }

    /**
     * 刷新上传凭证
     *
     * @param videoId
     */
    public static void refreshUploadVideo(String videoId) {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        RefreshUploadVideoResponse response = null;
        try {
            request.setVideoId(videoId);
            response = aliyunClient.getAcsResponse(request);
        } catch (ServerException e) {
            System.out.println("RefreshUploadVideoRequest Server Exception:");
            e.printStackTrace();
            return;
        } catch (ClientException e) {
            System.out.println("RefreshUploadVideoRequest Client Exception:");
            e.printStackTrace();
            return;
        }
        System.out.println("RequestId:" + response.getRequestId());
        System.out.println("UploadAuth:" + response.getUploadAuth());
    }

}
