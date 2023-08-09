package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class videoDay implements Serializable {
    private Long videoID;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadDate;
}
