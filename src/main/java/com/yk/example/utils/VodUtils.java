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
     * 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
     */
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";

    /**
     * 当前 STS API 版本
     */
    public static final String STS_API_VERSION = "2015-04-01";

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


/*    public static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,
                                                String roleArn, String roleSessionName, String policy,
                                                ProtocolType protocolType) {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return response;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }*/

/*    public static void main(String[] args) throws ClientException {
        String roleArn = "acs:ram::1358121793615316:root";
        String roleSessionName = "RamUpload";
        String policy = "{\n" +
                "  \"Version\": \"1\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Action\": \"vod:CreateUploadVideo\",\n" +
                "      \"Resource\": \"*\",\n" +
                "      \"Effect\": \"Allow\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        // 此处必须为 HTTPS
        ProtocolType protocolType = ProtocolType.HTTPS;
        final AssumeRoleResponse response = assumeRole(accessKeyId, accessKeySecret,
                roleArn, roleSessionName, policy, protocolType);
        System.out.println("Expiration: " + response.getCredentials().getExpiration());
        System.out.println("Access Key Id: " + response.getCredentials().getAccessKeyId());
        System.out.println("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
        System.out.println("Security Token: " + response.getCredentials().getSecurityToken());
    }*/

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
            request.setFileName(fileName);
            //必选，视频标题
            request.setTitle(title);
      /*      //可选，分类ID
            request.setCateId(0);
            //可选，视频标签，多个用逗号分隔
            request.setTags("标签1,标签2");
            //可选，视频描述
            request.setDescription("视频描述");*/
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
