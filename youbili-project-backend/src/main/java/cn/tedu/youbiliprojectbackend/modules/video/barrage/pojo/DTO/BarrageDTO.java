package cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class BarrageDTO implements Serializable {
    private Long videoID;
    private Float time;
    private String text;
    private String color;
    private String type;
    private Boolean isMe;
    private Boolean force;
}
