package cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class BarrageVO implements Serializable {
    private Float time;
    private String text;
    private String color;
    private String type;
    private Boolean force;
}
