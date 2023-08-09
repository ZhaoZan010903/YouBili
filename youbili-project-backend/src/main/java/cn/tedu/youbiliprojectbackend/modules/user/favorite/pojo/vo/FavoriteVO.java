package cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 赵錾
 * 收藏列表实体类
 */
@Data
public class FavoriteVO implements Serializable {

    /**
     * 收藏ID
     */
    private Long favoriteID;
    /**
     * 视频ID
     */
    private Long videoID;
    /**
     * 昵称(视频作者昵称)
     */
    private String nickname;
    /**
     * 收藏日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date favoriteDate;
    /**
     * 视频标题
     */
    private String title;
    /**
     * 头像(视频作者头像)
     */
    private String imgUrl;
    /**
     * 视频封面地址
     */
    private String videoImgUrl;


}
