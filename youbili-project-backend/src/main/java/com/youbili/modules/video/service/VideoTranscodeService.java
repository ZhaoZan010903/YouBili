package com.youbili.modules.video.service;

import com.youbili.config.TranscodeProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 可选的异步视频转码服务：仅在 youbili.transcode.enabled=true 且本机已安装 ffmpeg 时运行。
 * 替代旧版硬编码 Windows 路径和 3GB 重复 FFmpeg 依赖。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoTranscodeService {

    private final TranscodeProperties transcodeProperties;

    @Async
    public void transcodeIfEnabled(Long videoId, String localVideoPath) {
        if (!transcodeProperties.isEnabled() || localVideoPath == null) {
            return;
        }
        Path source = Paths.get(localVideoPath);
        if (!Files.exists(source)) {
            log.warn("Skip transcoding for video {}: local source not found at {}", videoId, localVideoPath);
            return;
        }
        String baseName = source.getFileName().toString().replaceFirst("\\.[^.]+$", "");
        int[][] profiles = {{1280, 720}, {854, 480}};
        for (int[] profile : profiles) {
            int width = profile[0];
            int height = profile[1];
            Path output = source.getParent().resolve(baseName + "-" + height + "p.mp4");
            try {
                ProcessBuilder pb = new ProcessBuilder(List.of(
                        transcodeProperties.getFfmpegPath(),
                        "-y", "-i", source.toString(),
                        "-vf", "scale=" + width + ":" + height + ":force_original_aspect_ratio=decrease",
                        "-c:v", "libx264", "-preset", "fast", "-crf", "23",
                        "-c:a", "aac", "-movflags", "+faststart",
                        output.toString()
                ));
                pb.redirectErrorStream(true);
                Process process = pb.start();
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    log.info("Transcoded video {} to {}p: {}", videoId, height, output);
                } else {
                    log.warn("FFmpeg exited with code {} for video {} ({}p)", exitCode, videoId, height);
                }
            } catch (Exception e) {
                log.warn("Transcoding skipped for video {} ({}p): {}", videoId, height, e.getMessage());
            }
        }
    }
}
