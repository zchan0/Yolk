package com.ustc.yolk.service;

import com.alibaba.fastjson.JSON;
import com.ustc.yolk.dal.ShareContentDAO;
import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.ShareContentDO;
import com.ustc.yolk.model.User;
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
        return null;
    }

    @Override
    public void share(long id, User user) {
        ShareContent shareContent = queryById(id);
        ParamChecker.notNull(shareContent, "illegal share content id!");
        if (!StringUtils.equals(shareContent.getSharedByUsername(), user.getUsername())) {
            throw new RuntimeException("illegal user!");
        }
        //将isPublic设置为true
    }

    @Override
    public long add(ShareContent shareContent) {
        return shareContentDAO.insert(voToDO(shareContent));
    }

    private ShareContent doToVO(ShareContentDO shareContentDO) {
        ShareContent content = JSON.parseObject(shareContentDO.getContent(), ShareContent.class);
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
