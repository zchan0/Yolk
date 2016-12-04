package com.ustc.yolk.dal;

import com.ustc.yolk.model.ShareContentDO;
import com.ustc.yolk.utils.common.ParamChecker;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

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

    public List<ShareContentDO> queryByUsernameForPage(String username, int start, int pageSize) {
        try {
            ParamChecker.assertCondition(start >= 0, "illegal page id");
            return sqlMapClient.queryForList("queryByUsernameForPage", username, start, pageSize);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ShareContentDO> queryPub(int start, int pageSize) {
        try {
            ParamChecker.assertCondition(start >= 0, "illegal page id");
            return sqlMapClient.queryForList("queryForPage", null, start, pageSize);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*将内容设置为是否公开*/
    public long setPublis(String username, long id, int publis) {
        try {
            ShareContentDO shareContentDO = new ShareContentDO();
            shareContentDO.setShareID(id);
            shareContentDO.setUsername(username);
            shareContentDO.setIsPublic(publis);
            return sqlMapClient.update("updatePublic", shareContentDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void del(ShareContentDO shareContentDO) {
        try {
            sqlMapClient.delete("delContent", shareContentDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
