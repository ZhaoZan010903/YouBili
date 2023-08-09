package cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 类别的vo类
 *
 * @author 万佳兴
 */
@Data
public class CategoryListVO implements Serializable {
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Long categoryID;
    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 视频ID
     */
    private Long videoID;
}
