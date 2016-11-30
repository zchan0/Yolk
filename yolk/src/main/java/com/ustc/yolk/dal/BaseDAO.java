package com.ustc.yolk.dal;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Administrator on 2016/11/30.
 */
public class BaseDAO {

    protected static SqlMapClient sqlMapClient = null;

    static {
        try {
            Reader reader = Resources
                    .getResourceAsReader("SqlMap.xml");
            sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
