package cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 取消收藏视频的计数
 */
@Data
public class FavoriteParam {
    /**
     * 用户ID
     */
    private Long userID;
    /**
     * 视频ID
     */
    private Long videoID;
    /**
     * 启用状态
     */
    private Integer enable;
    /**
     * 收藏总数
     */
    private Integer favoriteCount;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
}
