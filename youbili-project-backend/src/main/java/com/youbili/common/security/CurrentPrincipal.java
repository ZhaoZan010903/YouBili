package com.youbili.common.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentPrincipal implements Serializable {

    private Long userId;
    private String username;
    private String tokenId;
}
