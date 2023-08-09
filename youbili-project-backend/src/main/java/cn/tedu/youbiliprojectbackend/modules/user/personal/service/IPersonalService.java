package cn.tedu.youbiliprojectbackend.modules.user.personal.service;

import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.PersonalCenterInfoVO;

public interface IPersonalService {
    PersonalCenterInfoVO getCenterInfoById(Long id);
}
