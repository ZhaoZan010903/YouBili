package com.youbili.modules.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileVO {

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String avatarUrl;
    private LocalDate birthday;
    private Integer accountLevel;
    private Integer fans;
    private Integer follows;
    private Integer videoCount;
    private Integer totalLikes;
    private String membershipStatus;
    private LocalDateTime registrationDate;
}
