package com.youbili.modules.video.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "barrage")
public class BarrageDocument {

    @Id
    private String id;

    @Indexed
    private Long videoId;

    private Long userId;
    private Float time;
    private String text;
    private String color;
    private String type;
    private LocalDateTime gmtCreate;
}
