package com.youbili.modules.video.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.ServiceCode;
import com.youbili.config.StorageProperties;
import com.youbili.modules.video.vo.UploadResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private static final Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "webm", "mov", "m3u8");
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    private final StorageProperties storageProperties;

    public UploadResultVO storeVideo(Long userId, MultipartFile file) {
        String ext = validateAndGetExtension(file, VIDEO_EXTENSIONS, "仅支持上传 MP4 / WebM / MOV 视频文件");
        String fileName = userId + "-" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
        return store("videos", fileName, file, true);
    }

    public UploadResultVO storeCover(Long userId, MultipartFile file) {
        String ext = validateAndGetExtension(file, IMAGE_EXTENSIONS, "仅支持上传 JPG / PNG / WebP 封面图片");
        String fileName = userId + "-" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
        return store("covers", fileName, file, false);
    }

    private UploadResultVO store(String subDir, String fileName, MultipartFile file, boolean isVideo) {
        if ("oss".equalsIgnoreCase(storageProperties.getType())) {
            return storeToOss(subDir, fileName, file, isVideo);
        }
        return storeLocally(subDir, fileName, file);
    }

    private UploadResultVO storeLocally(String subDir, String fileName, MultipartFile file) {
        try {
            Path targetDir = Paths.get(storageProperties.getLocalDir(), subDir).toAbsolutePath().normalize();
            Files.createDirectories(targetDir);
            Path targetFile = targetDir.resolve(fileName).normalize();
            if (!targetFile.startsWith(targetDir)) {
                throw new ServiceException(ServiceCode.BAD_REQUEST, "非法文件路径");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
            String baseUrl = storageProperties.getPublicBaseUrl().replaceAll("/+$", "");
            return UploadResultVO.builder()
                    .fileName(fileName)
                    .size(file.getSize())
                    .url(baseUrl + "/" + subDir + "/" + fileName)
                    .build();
        } catch (IOException e) {
            log.error("Failed to save uploaded file locally", e);
            throw new ServiceException(ServiceCode.STORAGE_ERROR, "文件保存失败");
        }
    }

    private UploadResultVO storeToOss(String subDir, String fileName, MultipartFile file, boolean isVideo) {
        StorageProperties.Oss ossConfig = storageProperties.getOss();
        if (!StringUtils.hasText(ossConfig.getAccessKeyId()) || !StringUtils.hasText(ossConfig.getAccessKeySecret())) {
            throw new ServiceException(ServiceCode.STORAGE_ERROR, "未配置阿里云 OSS 密钥，请先配置环境变量或切换为 local 存储");
        }
        String bucket = isVideo ? ossConfig.getVideoBucket() : ossConfig.getImageBucket();
        String objectKey = subDir + "/" + fileName;
        OSS ossClient = new OSSClientBuilder().build(
                ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try (InputStream inputStream = file.getInputStream()) {
            ossClient.putObject(bucket, objectKey, inputStream);
            String endpointHost = ossConfig.getEndpoint().replaceFirst("^https?://", "");
            String url = "https://" + bucket + "." + endpointHost + "/" + objectKey;
            return UploadResultVO.builder()
                    .fileName(fileName)
                    .size(file.getSize())
                    .url(url)
                    .build();
        } catch (Exception e) {
            log.error("Failed to upload file to Aliyun OSS", e);
            throw new ServiceException(ServiceCode.STORAGE_ERROR, "OSS 文件上传失败");
        } finally {
            ossClient.shutdown();
        }
    }

    private String validateAndGetExtension(MultipartFile file, Set<String> allowed, String errorMsg) {
        if (file == null || file.isEmpty()) {
            throw new ServiceException(ServiceCode.BAD_REQUEST, "上传文件不能为空");
        }
        String original = file.getOriginalFilename();
        if (!StringUtils.hasText(original) || !original.contains(".")) {
            throw new ServiceException(ServiceCode.BAD_REQUEST, errorMsg);
        }
        String ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        if (!allowed.contains(ext)) {
            throw new ServiceException(ServiceCode.BAD_REQUEST, errorMsg);
        }
        return ext;
    }
}
