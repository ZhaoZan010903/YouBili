package cn.tedu.youbiliprojectbackend.common.ex;


import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Getter
public class ServiceException extends RuntimeException {

    private ServiceCode serviceCode;

    public ServiceException(ServiceCode serviceCode, String message) {
        super(message);
        this.serviceCode = serviceCode;
    }

}
