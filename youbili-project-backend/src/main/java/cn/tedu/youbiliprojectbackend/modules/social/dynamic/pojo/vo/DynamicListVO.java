package cn.tedu.youbiliprojectbackend.modules.social.dynamic.pojo.vo;

import lombok.Data;

/**
 * 动态列表VO
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Data
public class DynamicListVO {
    /**
     * 视频ID
     */
    private Long videoID;
    /**
     * 作者头像
     */
    private String imgUrl;
    /**
     * 作者昵称
     */
    private String nickname;
    /**
     * 视频发布时间
     */
    private String uploadDate;
    /**
     * 视频描述
     */
    private String description;
    /**
     * 视频封面
     */
    private String videoImgUrl;
    /**
     * 视频时长
     */
    private String duration;
    /**
     * 视频标题
     */
    private String title;
    /**
     * 视频播放量
     */
    private Integer viewCount;
    /**
     * 点赞量
     */
    private Integer likeCount;
    /**
     * 平论量
     */
    private Integer commentCount;
    /**
     * 视频地址
     */
    private String videoSrcUrl;
}
