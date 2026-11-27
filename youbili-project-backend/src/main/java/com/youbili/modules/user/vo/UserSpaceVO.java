package com.youbili.modules.user.vo;

import com.youbili.modules.video.vo.VideoCardVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSpaceVO {

    private UserProfileVO profile;
    private Boolean isSelf;
    private Boolean isFollowing;
    private List<VideoCardVO> videos;
    private List<VideoCardVO> favorites;
    private List<VideoCardVO> histories;
}
