package cn.tedu.youbiliprojectbackend.modules.social.comment.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.common.utils.pagedate.PageData;
import cn.tedu.youbiliprojectbackend.common.utils.pagedate.PageInfoToPageDataConverter;
import cn.tedu.youbiliprojectbackend.modules.social.comment.dao.persist.mapper.CommentMapper;
import cn.tedu.youbiliprojectbackend.modules.social.comment.dao.persist.mapper.CommentreplieMapper;
import cn.tedu.youbiliprojectbackend.modules.social.comment.dao.persist.repository.ICommentRepository;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity.Comment;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity.Commentreplie;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentFirstVO;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements ICommentRepository {
    @Autowired
    CommentreplieMapper commentreplieMapper;
    @Autowired
    private CommentMapper mapper;

    @Override
    public PageData<CommentFirstVO> selectByVideoID(Long videoID, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CommentFirstVO> commentFirstVO = mapper.selectFirstCommentByVideoID(videoID);
        PageInfo<CommentFirstVO> pageInfo = new PageInfo<>(commentFirstVO);
        PageData<CommentFirstVO> pageData = PageInfoToPageDataConverter.convert(pageInfo);
        return pageData;
    }

    @Override
    public List<CommentListVO> selectByCommentID(Long commentID) {
        List<CommentListVO> commentListVOS = mapper.selectCommentListByCommentID(commentID);
        return commentListVOS;
    }

    @Override
    public void insertComment(Comment comment) {
        mapper.insert(comment);
    }

    @Override
    public void insertCommentreplie(Commentreplie commentreplie) {
        commentreplieMapper.insert(commentreplie);
    }


}
