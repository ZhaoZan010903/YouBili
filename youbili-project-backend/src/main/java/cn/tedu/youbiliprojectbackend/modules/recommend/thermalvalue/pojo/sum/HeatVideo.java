package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum;

import lombok.Data;

import java.io.Serializable;

@Data
public class HeatVideo implements Serializable {
    private Long videoID;
    private Double HeatDay;
}
