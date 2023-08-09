package cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = "barrage")
public class Barrage implements Serializable {

    private Long videoID;
    private Float time;
    private String text;
    private String color;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    private String name;
    private String type;
    private Boolean isMe;
    private Boolean force;
}
