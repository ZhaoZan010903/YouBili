package cn.tedu.youbiliprojectbackend.modules.video.uplaod.controller;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.mapper.VideoUploadMapper;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.VideoUpdateDTO;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.bytedeco.ffmpeg.avcodec.AVCodecParameters;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Repository
@Slf4j
public class Consumer {
    @Autowired
    private VideoUploadMapper videoUploadMapper;

    @Value("${youbili.OSS.accessKeyId}")
    String accessKeyId;
    @Value("${youbili.OSS.accessKeySecret}")
    String accessKeySecret;
    @Value("${youbili.OSS.endpoint}")
    String endpoint;


    public void consumer() throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_a33_group");
//        consumer.setNamesrvAddr("114.116.107.63:9876");
        consumer.setNamesrvAddr("114.116.107.63:9876");
        consumer.subscribe("TopicTest-ZZ", "*");
        log.debug("");
        //消费模式:广播模式消费 OR 集群模式消费
        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                byte[] body = msg.getBody();
                //视频信息
                String videoID = msg.getUserProperty("videoID");
                String userID = msg.getUserProperty("userID");
                String inputFile = msg.getUserProperty("videoSrcUrl");
                String videoImgUrl = msg.getUserProperty("videoImgUrl");
                //获取视频时长
                File file = new File(inputFile);
                MultimediaObject instance = new MultimediaObject(file);
                MultimediaInfo result = null;
                try {
                    result = instance.getInfo();
                } catch (EncoderException e) {
                    throw new RuntimeException(e);
                }
                Long videoTime = result.getDuration() / 1000;
                Long hours = videoTime / 3600;
                Long minutes = (videoTime % 3600) / 60;
                Long seconds = videoTime % 60;
                String duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                Path path = Paths.get(inputFile);
                String fileName = path.getFileName().toString();
                fileName = userID + "-" + videoID;
                //转码参数
                String outputFile = "D:\\下载文件\\转码后";
                String videoFormat = "mp4";
                int[] videoWidth = {1920, 1080, 720};
                int[] videoHeight = {1080, 720, 480};
                //配置OSS连接参数
                String end = "oss-cn-beijing.aliyuncs.com";
                String bucketName = "youbili-test";
                String bucketNameImg = "youbili-image";
                String folderName = fileName + "/";
                //构建OSS客户端
                OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                for (int j = 0; j < 3; j++) {
                    String resultPath = Joiner.on(File.separator).join(Arrays.asList(outputFile, fileName + "-" + videoHeight[j] + ".mp4"));
                    FFmpegLogCallback.set();
                    Frame frame;
                    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
                    grabber.setNumBuffers(100);
                    FFmpegFrameRecorder recorder = null;
                    try {
                        //初始化帧抓取器,例如数据结构(时间戳/编码器上下文/帧对象等),
                        //如果入参等于true,还会调用avformat_find_stream_info方法获取流的信息,放入AVFormatContext类型的成员变量oc中
                        grabber.start(true);
                        System.out.println(grabber.getNumBuffers());
                        // grabber.start方法中,初始化的解码器信息存放在grabber的成员变量oc中
                        AVFormatContext avFormatContext = grabber.getFormatContext();
                        //文件内有几个媒体流(一般是测试流 + 音频流)
                        int streamNum = avFormatContext.nb_streams();
                        //没有媒体流就不用了
                        if (streamNum < 1) {
                            log.info("视频文件格式不对");
                            return null;
                        }
                        //取得视频的帧率
                        int framerate = (int) grabber.getVideoFrameRate();
                        log.info("视频帧率:{},视频时长:{}秒,媒体流两束:{}", framerate, avFormatContext.duration() / 10000000,
                                streamNum);
                        //遍历每一个流,检查其类型
                        for (int i = 0; i < streamNum; i++) {
                            AVStream avStream = avFormatContext.streams(i);
                            AVCodecParameters avCodecParameters = avStream.codecpar();
                            log.info("流的索引:{},编码器类型:{},编码器ID:{}", i, avCodecParameters.codec_type(),
                                    avCodecParameters.codec_id());
                        }
                        //源视频宽度
                        int frameWidth = grabber.getImageWidth();
                        //源视频高度
                        int frameHeight = grabber.getImageHeight();
                        //源音频通道数量
                        int audioChannels = grabber.getAudioChannels();
                        log.info("源视频宽度:{}, 源视频高度:{}, 音频通道数:{}", frameWidth, frameHeight, audioChannels);
                        recorder = new FFmpegFrameRecorder(resultPath, videoWidth[j], videoHeight[j], audioChannels);
                        //H264编码
                        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                        //设置视频格式
                        recorder.setFormat(videoFormat);
                        //使用原始视频的码率,若需要则自行修改码率
                        recorder.setVideoBitrate(grabber.getVideoBitrate());
                        //一秒内的帧数,帧率
                        recorder.setFrameRate(framerate);
                        //两个关键帧之间的帧数
                        recorder.setGopSize(framerate);
                        //设置音频通道数,与视频源的通道数相等
                        recorder.setAudioChannels(grabber.getAudioChannels());
                        recorder.start();
                        int videoFrameNum = 0;
                        int audioFrameNum = 0;
                        int dataFrameNum = 0;
                        //持续从视频源取帧
                        while (null != (frame = grabber.grab())) {
                            //有图像,就把视频帧加一
                            if (null != frame.image) {
                                videoFrameNum++;
                                //取出的每一帧,都保存到视频
                                recorder.record(frame);
                            }
                            //有声音,就把音频帧加一
                            if (null != frame.samples) {
                                audioFrameNum++;
                                //取出的每一帧,都保存到视频
                                recorder.record(frame);
                            }
                            //有数据,就把数据帧加一
                            if (null != frame.data) {
                                dataFrameNum++;
                            }
                        }
                        log.info("转码完成,视频帧:{}, 音频帧:{}, 数据帧:{}", videoFrameNum, audioFrameNum, dataFrameNum);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return null;
                    } finally {
                        if (recorder != null) {
                            try {
                                recorder.close();
                            } catch (FrameRecorder.Exception e) {
                                log.info("recoder.close异常" + e);
                            }
                        }
                        try {
                            grabber.close();
                        } catch (FrameGrabber.Exception e) {
                            log.info("frameGrabber.close异常" + e);
                        }
                    }
                    String ossName = folderName + fileName + "-";
                    log.info("更新视频表信息: duration,videoSrcUrl");
                    VideoUpdateDTO videoUpdateDTO = new VideoUpdateDTO();
                    videoUpdateDTO.setVideoID(Long.valueOf(videoID));
                    videoUpdateDTO.setVideoSrcUrl("https://" + bucketName + "." + end + "/" + ossName);
//                    http:// + bucketName + "." + end + "/" +
                    videoUpdateDTO.setVideoImgUrl("https://" + bucketNameImg + "." + end + "/" + videoImgUrl);
                    videoUpdateDTO.setDuration(duration);
                    videoUploadMapper.changeVideoContent(videoUpdateDTO);

                    String objectName = folderName + fileName + "-" + videoHeight[j] + ".mp4";
                    //设置上传的文件路径
                    String filePath = outputFile + "\\" + fileName + "-" + videoHeight[j] + ".mp4";
                    //创建PutObjectRequest对象,设置上传的Bucket名称.唯一文件名和文件
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(filePath));
                    //执行文件上传
                    PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
                    //输出上传结果
                    System.out.println("文件上传成功,返回ETag: " + putObjectResult.getETag());
                    //开始删除上传后的文件
                    File deleteFile = new File(filePath);
                    //检查文件是否存在
                    if (deleteFile.exists()) {
                        boolean deleted = deleteFile.delete();
                        System.out.println(deleted);
                        //检查是否删除成功
                        if (deleted) {
                            log.info("上传成功");
                        } else {
                            log.info("上传失败");
                        }
                    }
                }
                //关闭客户端
                ossClient.shutdown();
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        //调用启动
        consumer.start();
        log.debug("--------消息队列启动成功-----------");

    }

}
