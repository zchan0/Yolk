package com.ustc.yolk.dal;

import com.ustc.yolk.model.ShareContentDO;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/11/30.
 */
@Component
public class ShareContentDAO extends BaseDAO {

    /*新增分享内容记录*/
    public long insert(ShareContentDO shareContentDO) {
        try {
            return (long) sqlMapClient.insert("insertShareContent", shareContentDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*查询分享内容记录*/
    public ShareContentDO queryById(long id) {
        try {
            return (ShareContentDO) sqlMapClient.queryForObject("queryById", id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
