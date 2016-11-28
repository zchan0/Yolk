package com.ustc.yolk.service;

import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.User;
import com.ustc.yolk.utils.common.ParamChecker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
@Component
public class ShareContentServiceImpl implements ShareContentService {


    @Override
    public ShareContent queryById(long id) {
        return null;
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
}
