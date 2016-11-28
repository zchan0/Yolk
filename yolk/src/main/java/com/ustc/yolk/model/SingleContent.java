package com.ustc.yolk.model;

/**
 * Created by Administrator on 2016/11/28.
 */
public class SingleContent extends ToString {

    /*图片名*/
    private String picName;
    /*文本*/
    private String text;

    public SingleContent() {
    }

    /**
     * Gets pic name.
     *
     * @return the pic name
     */
    public String getPicName() {
        return picName;
    }

    /**
     * Sets pic name.
     *
     * @param picName the pic name
     */
    public void setPicName(String picName) {
        this.picName = picName;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }
}
