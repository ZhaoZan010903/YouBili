package cn.tedu.youbiliprojectbackend.modules.user.personal.service.impl;

import cn.tedu.youbiliprojectbackend.modules.user.personal.dao.persist.repository.IPersonalRepository;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.CenterVideoInfoVO;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.PersonalCenterInfoVO;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.PersonalCenterUserInfoVO;
import cn.tedu.youbiliprojectbackend.modules.user.personal.service.IPersonalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PersonalService implements IPersonalService {
    @Autowired
    IPersonalRepository repository;

    /*
     * 获取个人中心展示内容功能
     * */
    @Override
    public PersonalCenterInfoVO getCenterInfoById(Long id) {

        //响应VO
        PersonalCenterInfoVO infoVO = new PersonalCenterInfoVO();
        //用户信息
        PersonalCenterUserInfoVO userInfo = repository.selectUserInfoByUserId(id);
        //用户发布视频信息
        List<CenterVideoInfoVO> videoList = repository.selectVideoInfoByUserId(id);
        //用户收藏视频信息
        List<CenterVideoInfoVO> favoriteInfoByUserId = repository.selectFavoriteInfoByUserId(id);
        //用户历史记录信息
        List<CenterVideoInfoVO> historyInfoByUserId = repository.selectHistoryInfoByUserId(id);


        //解决视频个数<3,展示错位的问题
        List<CenterVideoInfoVO> list = new ArrayList<>(9);
        list.addAll(videoList);
        for (int i = videoList.size(); i < 3; i++) {
            list.add(null);
        }
        list.addAll(favoriteInfoByUserId);
        for (int i = favoriteInfoByUserId.size(); i < 3; i++) {
            list.add(null);
        }
        list.addAll(historyInfoByUserId);
        for (int i = historyInfoByUserId.size(); i < 3; i++) {
            list.add(null);
        }
        // set响应VO
        BeanUtils.copyProperties(userInfo, infoVO);
        infoVO.setVideoList(list);
        return infoVO;
    }
}
