package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于查询计算 计算字段值
 */
@Data
@Accessors(chain = true)
public class videoSum implements Serializable {
    private Long videoID;
    private Double heatValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadDate;
}
