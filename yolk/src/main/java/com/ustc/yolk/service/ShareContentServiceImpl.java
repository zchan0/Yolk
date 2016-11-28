package com.ustc.yolk.service;

import com.ustc.yolk.model.ShareContent;
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
}
