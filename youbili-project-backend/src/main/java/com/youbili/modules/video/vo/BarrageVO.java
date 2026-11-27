package com.youbili.modules.video.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarrageVO implements Serializable {

    private Float time;
    private String text;
    private String color;
    private String type;
    private Long userId;
}
