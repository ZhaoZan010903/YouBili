package cn.tedu.youbiliprojectbackend.common.consts;

/**
 * JWT缓存相关常量
 *
 * @author java@tedu.cn
 * @version 1.0
 */
public interface UserCacheConsts {

    /**
     * 缓存的JWT前缀
     */
    String USER_JWT_PREFIX = "Account:Login:jwt:";
    /**
     * 用户启用状态的KEY的前缀
     */
    String USER_ENABLE_PREFIX = "Account:Login:enable:";

}
