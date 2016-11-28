package com.ustc.yolk.model;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分享内容对象 一次分享可以分享多个图片或者文本
 * Created by Administrator on 2016/11/28.
 */
public class ShareContent extends ToString {

    /*包含的多个图片或者文字*/
    private List<SingleContent> contents = Lists.newArrayList();
    /*上传时间*/
    private Date createTime;
    /*上传的用户的username*/
    private String sharedByUsername;

    /**
     * Instantiates a new Share content.
     */
    public ShareContent() {
    }

    /**
     * Gets create time.
     *
     * @return the create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     *
     * @param createTime the create time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets shared by username.
     *
     * @return the shared by username
     */
    public String getSharedByUsername() {
        return sharedByUsername;
    }

    /**
     * Sets shared by username.
     *
     * @param sharedByUsername the shared by username
     */
    public void setSharedByUsername(String sharedByUsername) {
        this.sharedByUsername = sharedByUsername;
    }

    /**
     * Gets contents.
     *
     * @return the contents
     */
    public List<SingleContent> getContents() {
        return contents == null ? new ArrayList<SingleContent>() : contents;
    }

    /**
     * Sets contents.
     *
     * @param contents the contents
     */
    public void setContents(List<SingleContent> contents) {
        this.contents = contents;
    }
}
