package cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryTitleVO implements Serializable {

    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Long categoryID;
    /**
     * 分类名称
     */
    private String categoryName;
}
