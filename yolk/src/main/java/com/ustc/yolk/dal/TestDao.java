package com.ustc.yolk.dal;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ustc.yolk.model.TestDo;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/7/31.
 */

@Component
public class TestDao {

    private static SqlMapClient sqlMapClient = null;

    //先去掉,不然启动的时候连接不上
    //    static {
    //        try {
    //            Reader reader = Resources
    //                    .getResourceAsReader("SqlMap.xml");
    //            sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
    //            reader.close();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }

    @SuppressWarnings("unchecked")
    public List<TestDo> selectAllDo() {

        List<TestDo> result = null;

        try {
            result = sqlMapClient.queryForList("selectAll", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<TestDo> selectRankWithLimit(int rank) {
        try {
            return sqlMapClient.queryForList("selectRankWithLimit", rank);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
