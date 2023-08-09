package cn.tedu.youbiliprojectbackend.modules.user.history.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
//@TableName("history")
public class History implements Serializable {
    /**
     * 历史记录ID
     */
    private Long historyID;
    /**
     * 用户ID
     */
    private Long userID;
    /**
     * 视频ID
     */
    private Long VideoID;
    /**
     * 显示状态，0=不显示，1=显示
     */
    private Integer enable;
    /**
     * 观看日期
     */
    private Long watchDate;
    /**
     * 观看时长
     */
    private Long watchDuration;
    /**
     * 数据创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    /**
     * 数据最后修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
