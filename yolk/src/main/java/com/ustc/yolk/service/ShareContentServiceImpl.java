package com.ustc.yolk.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ustc.yolk.dal.ShareContentDAO;
import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.ShareContentDO;
import com.ustc.yolk.model.SingleContent;
import com.ustc.yolk.model.User;
import com.ustc.yolk.utils.PicUploadUtil;
import com.ustc.yolk.utils.common.ParamChecker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
@Component
public class ShareContentServiceImpl implements ShareContentService {

    @Autowired
    private ShareContentDAO shareContentDAO;

    @Override
    public ShareContent queryById(long id) {
        ShareContentDO shareContentDO = shareContentDAO.queryById(id);
        ParamChecker.notNull(shareContentDO, "illegal shared content id!");
        return doToVO(shareContentDO);
    }

    @Override
    public List<ShareContent> queryRecentPubContents(int start, int counts) {
        List<ShareContent> shareContents = Lists.newArrayList();
        for (ShareContentDO shareContentDO : shareContentDAO.queryPub(start, counts)) {
            shareContents.add(doToVO(shareContentDO));
        }
        return shareContents;
    }

    @Override
    public List<ShareContent> queryMyRecentContents(String username, int start, int counts) {
        List<ShareContent> shareContents = Lists.newArrayList();
        for (ShareContentDO shareContentDO : shareContentDAO.queryByUsernameForPage(username, start, counts)) {
            shareContents.add(doToVO(shareContentDO));
        }
        return shareContents;
    }

    @Override
    public void share(long id, User user) {
//        ShareContent shareContent = queryById(id);
//        ParamChecker.notNull(shareContent, "illegal share content id!");
//        if (!StringUtils.equals(shareContent.getSharedByUsername(), user.getUsername())) {
//            throw new RuntimeException("illegal user!");
//        }
        //将isPublic设置为true
        if (shareContentDAO.setPublis(user.getUsername(), id, 1) != 1) {
            throw new RuntimeException("illegal share id or user");
        }
    }

    @Override
    public long add(ShareContent shareContent) {
        return shareContentDAO.insert(voToDO(shareContent));
    }

    @Override
    public void del(long id, User user) {
        ShareContentDO shareContentDO = shareContentDAO.queryById(id);
        if (shareContentDO == null || !shareContentDO.getUsername().equals(user.getUsername())) {
            throw new RuntimeException("illegal content id!");
        }
        shareContentDAO.del(shareContentDO);
        ShareContent content = doToVO(shareContentDO);
        for (SingleContent singleContent : content.getContents()) {
            if (StringUtils.isNoneBlank(singleContent.getPicName())) {
                //删除图片
                PicUploadUtil.delFile(content.getSharedByUsername(), singleContent.getPicName());
            }
        }
    }

    private ShareContent doToVO(ShareContentDO shareContentDO) {
        ShareContent content = JSON.parseObject(shareContentDO.getContent(), ShareContent.class);
        content.setPublic0(shareContentDO.getIsPublic() == 1);
        content.setCreateTime(shareContentDO.getCreateDate());
        content.setId(shareContentDO.getShareID());
        return content;
    }

    private ShareContentDO voToDO(ShareContent content) {
        ShareContentDO shareContentDO = new ShareContentDO();
        shareContentDO.setUsername(content.getSharedByUsername());
        shareContentDO.setCreateDate(content.getCreateTime());
        shareContentDO.setIsPublic(content.isPublic0() ? 1 : 0);
        shareContentDO.setContent(JSON.toJSONString(content));
        return shareContentDO;
    }
}
