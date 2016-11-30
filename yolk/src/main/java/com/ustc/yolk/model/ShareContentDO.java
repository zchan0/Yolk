package com.ustc.yolk.model;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/30.
 */
public class ShareContentDO {

    private long shareID;
    private String username;
    private Date createDate;
    private String content;
    private int isPublic;

    public long getShareID() {
        return shareID;
    }

    public void setShareID(long shareID) {
        this.shareID = shareID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }
}
