package cn.tedu.youbiliprojectbackend.modules.video.uplaod.controller;


import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.mapper.AddCategoryMapper;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.mapper.AddTagMapper;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.UploadContentDTO;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.UploadTextDTO;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.VideoCategoryDTO;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.VideoTagDTO;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.service.IVideoUploadService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/video")
public class VideoUploadController {

    @Value("${youbili.OSS.accessKeyId}")
    String accessKeyId;
    @Value("${youbili.OSS.accessKeySecret}")
    String accessKeySecret;
    @Value("${youbili.OSS.endpoint}")
    String endpoint;


    //声明一个全局变量用于保存上传进度
    private static double uploadProgress = 0;
    @Autowired
    private IVideoUploadService iVideoUploadService;
    @Autowired
    private AddCategoryMapper addCategoryMapper;
    @Autowired
    private AddTagMapper addTagMapper;

    VideoUploadController() {
        log.debug("创建控制器类:VideoUploadController");
    }

    // 上传视频
    @PostMapping("/upload")
    public RestBean<Object> uploadVideo(@AuthenticationPrincipal CurrentPrincipal principal,
                                        @RequestParam("file") MultipartFile file) throws EncoderException, IOException {
        log.debug("开始处理上传视频请求: /video/upload");
        // 获取当事人id
        Long id = principal.getUserID();
        log.debug("file{}", file);

        if (file.isEmpty() && file.getOriginalFilename().split("\\.")[1] != "mp4") {
            // 返回请求参数异常
            return RestBean.failure(ServiceCode.ERROR_BAD_REQUEST);
        }
        UUID randomUUID = UUID.randomUUID();
        String fileName = id + "-" + randomUUID.toString().replace("-", "") + ".mp4";
        //上传视频
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() +
                "\\src\\main\\resources\\videos\\";
        String path = pre + fileName;
        File file1 = new File(path);
        //获取文件总大小
        long fileSize = file.getSize();
        long uploadedBytes = 0;

        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = new FileOutputStream(file1)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                //写入文件
                outputStream.write(buffer, 0, bytesRead);
                //更新已上传的字节数
                uploadedBytes += bytesRead;
                //计算上传进度
                uploadProgress = (double) uploadedBytes / fileSize * 100;
//                System.out.println("上传进度: " + uploadProgress + "%");
            }
        }
        //获取视频信息
        MultimediaObject instance = new MultimediaObject(file1);
        MultimediaInfo result = instance.getInfo();
//        String videoTime = String.format("视频时长(秒) :%s", result.getDuration() / 1000);
//        System.out.println(videoTime);
        int width = result.getVideo().getSize().getWidth();
        int height = result.getVideo().getSize().getHeight();
        System.out.println(width);
        System.out.println(height);

        if (width < 1920 || height < 1080) {
            //要删除文件的路径
            String filePath = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() +
                    "\\src\\main\\resources\\videos\\" + fileName;
            //创建file对象
            File deleteFile = new File(filePath);
            //检查文件是否存在
            if (deleteFile.exists()) {
                //调用delete()方法删除文件
                boolean deleted = deleteFile.delete();
                System.out.println(deleted);
                //检查文件是否删除成功
                if (deleted) {
                    return RestBean.failure(ServiceCode.ERROR_BAD_REQUEST);
                } else {
                    return RestBean.failure(ServiceCode.ERROR_BAD_REQUEST);
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("videoSrcUrl", path);
        map.put("userID", principal.getUserID());
        return RestBean.success(map);
    }

    // 上传封面
    @PostMapping("/uploadCover")
    public RestBean<Object> uploadFile(@AuthenticationPrincipal CurrentPrincipal principal,
                                       @RequestParam("file") MultipartFile file) {
        Long id = principal.getUserID();

        String bucketName = "youbili-image";
        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            //上传文件
            String objectName = file.getOriginalFilename();//上传到OSS后的文件名
            objectName = id + "-" + UUID.randomUUID().toString().replace("-", "") + ".jpg";
            ossClient.putObject(bucketName, objectName, file.getInputStream());
            //返回上传成功的消息
            log.debug("objectName{}", objectName);
            return RestBean.success(objectName);
        } catch (Exception e) {
            e.printStackTrace();
            //返回上传失败的消息
            return RestBean.failure(ServiceCode.ERROR_BAD_REQUEST);
        } finally {
            //关闭OSSClient
            ossClient.shutdown();
        }
    }

    // 上传信息
    @PostMapping("/uploadContent")
    public RestBean<Object> uploadContent(@AuthenticationPrincipal CurrentPrincipal principal,
                                          UploadContentDTO video) throws MQClientException {
        log.debug("video{}", video);
        UploadTextDTO uploadTextDTO = new UploadTextDTO();
        uploadTextDTO.setUserID(video.getUserID());
        uploadTextDTO.setTitle(video.getTitle());
        uploadTextDTO.setDescription(video.getDescription());
        uploadTextDTO.setVideoImgUrl(video.getVideoImgUrl());
        uploadTextDTO.setVideoSrcUrl(video.getVideoSrcUrl());
        iVideoUploadService.videoUpload(uploadTextDTO);
        Long videoID = uploadTextDTO.getVideoID();
        log.debug("videoID{}", videoID);

        // 添加到标签关联表
        VideoCategoryDTO videoCategoryDTO = new VideoCategoryDTO();
        videoCategoryDTO.setVideoID(videoID);
        videoCategoryDTO.setCategoryID(video.getType());
        addCategoryMapper.insertCategory(videoCategoryDTO);

        for (String tag : video.getTag()) {
            VideoTagDTO videoTagDTO = new VideoTagDTO();
            videoTagDTO.setVideoID(videoID);
            videoTagDTO.setTagID(Long.parseLong(tag));
            addTagMapper.insertTag(videoTagDTO);
        }
        DefaultMQProducer producer = new DefaultMQProducer("asyncproducer_group22");
        producer.setSendMsgTimeout(5000);
        producer.setNamesrvAddr("114.116.107.63:9876");
        producer.start();
        //发送消息
        try {
            byte[] msgByte = ("Hello world").getBytes(StandardCharsets.UTF_8);
            String vId = String.valueOf(videoID);
            String uId = String.valueOf(video.getUserID());
            Message msg = new Message("TopicTest-ZZ", "TagZZ", msgByte);
            msg.putUserProperty("videoID", vId);
            msg.putUserProperty("userID", uId);
            msg.putUserProperty("videoSrcUrl", video.getVideoSrcUrl());
            msg.putUserProperty("videoImgUrl", video.getVideoImgUrl());

            int maxRetryTimes = 3; // 最大重试次数 boolean
            Boolean sendSuccess = false;
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送成功" + sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("发送失败" + throwable);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestBean.success();
    }


}





