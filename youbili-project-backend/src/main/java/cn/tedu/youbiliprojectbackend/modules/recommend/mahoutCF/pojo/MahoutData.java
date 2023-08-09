package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class MahoutData implements Serializable {
    private Integer mahoutID;

    private Long userID;

    private Long videoID;

    private Integer type;

    @TableField(exist = false)
    private Integer value;

}
