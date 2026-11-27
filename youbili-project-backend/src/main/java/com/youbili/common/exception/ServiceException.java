package com.youbili.common.exception;

import com.youbili.common.response.ServiceCode;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final ServiceCode serviceCode;

    public ServiceException(ServiceCode serviceCode) {
        super(serviceCode.getMessage());
        this.serviceCode = serviceCode;
    }

    public ServiceException(ServiceCode serviceCode, String message) {
        super(message);
        this.serviceCode = serviceCode;
    }
}
