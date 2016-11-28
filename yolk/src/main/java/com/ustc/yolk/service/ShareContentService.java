package com.ustc.yolk.service;

import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.User;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public interface ShareContentService {

    /*根据id查询分享内容*/
    ShareContent queryById(long id);

    /*查询最新的大家分享的内容*/
    List<ShareContent> queryRecentPubContents(int start, int counts);

    /*分享内容 这里会校验用户是否一致*/
    void share(long id, User user);
}
