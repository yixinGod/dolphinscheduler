package org.apache.dolphinscheduler.plugin.task.sql;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.*;
import java.util.*;


/**
 * Created by meifangjian on 2019/12/2 18:04
 */
public class DruidJdbcUtil {

    public static DruidDataSource dataSource;


    /**
     * 获取DataSource
     * @param properties
     */
    public static void getDataSource(Properties properties){
        if (dataSource==null){
            createDataSource(properties);
        }
    }

    /**
     * 创建DataSource
     * @param properties
     */
    private static void createDataSource(Properties properties){
        try {
            dataSource = new DruidDataSource();
            //四个基本属性
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl(properties.getProperty("jdbc.url"));
            dataSource.setUsername(properties.getProperty("jdbc.username"));
            dataSource.setPassword(properties.getProperty("jdbc.password"));
            //其他属性
            //初始大小
            dataSource.setInitialSize(2);
            //最大大小
            dataSource.setMaxActive(5);
            //最小大小
            dataSource.setMinIdle(1);
            //检查时间
            dataSource.setMaxWait(5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String,Object>> executeSelect(Connection conn, String sql){
        List<Map<String,Object>> result = null;
        ResultSet rs = null;
        try {
            rs = conn.prepareStatement(sql).executeQuery();
            result = extractData(rs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll(conn,null,rs);
        }
        return result;
    }

    public static List<Map<String,Object>> executeSelect(Connection conn, String sql,List list){
        List<Map<String,Object>> result = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            for (int i =0;i<list.size();i++){
                preparedStatement.setObject(i+1,list.get(i));
            }
            rs = preparedStatement.executeQuery();

//            rs = conn.prepareStatement(sql).executeQuery();
            result = extractData(rs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll(conn,preparedStatement,rs);
        }
        return result;
    }

    public static void executeSql(Connection conn, String sql,List list){
        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = conn.prepareStatement(sql);
            for (int i =0;i<list.size();i++){
                preparedStatement.setObject(i+1,list.get(i));
            }
            preparedStatement.execute();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll(conn,preparedStatement,null);
        }
    }


    /**
     * 关闭连接
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void closeAll(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     */
    public static Connection getConnection(Properties properties) {
        getDataSource(properties);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Connection getConnection(){
        Properties properties = new Properties();
        properties.put("jdbc.driver","com.mysql.cj.jdbc.Driver");
        properties.put("jdbc.url","jdbc:mysql://master1:3306/dolphinscheduler?characterEncoding=UTF-8&allowMultiQueries=true");
        properties.put("jdbc.username","root");
        properties.put("jdbc.password","bookface06");
        return getConnection(properties);
    }


    public static List<Map<String,Object>> extractData(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int num = md.getColumnCount();
        List listOfRows = new ArrayList();
        while (rs.next()) {
            Map mapOfColValues = new LinkedHashMap(num);
            for (int i = 1; i <= num; i++) {
                mapOfColValues.put(md.getColumnLabel(i), rs.getObject(i));
            }
            listOfRows.add(mapOfColValues);
        }
        return listOfRows;
    }

    public static void main(String[] args)  {

        List<Map<String,Object>> list = executeSelect(getConnection(null),"desc db_stream_metric_computing.stream_metric_data_1_1");
        StringBuilder columns = new StringBuilder();
        StringBuilder metrics = new StringBuilder();
       list.forEach(v -> {
           columns.append(v.get("Field")).append(",");
           if (!v.get("Key").equals("PRI")){
               metrics.append(v.get("Field")).append(" = values(").append(v.get("Field")).append("),");
           }
       });
       System.out.println(columns.substring(0,columns.length()-1));
       System.out.println(metrics.substring(0,metrics.length()-1));
        List<Map<String,Object>> list1 = executeSelect(getConnection(null),"desc db_stream_metric_computing.stream_metric_data_1_1");
        System.out.println(list1);
//        Map map = list.get(0);
//        map.forEach((k,v) -> {
//            System.out.println(k+" --> "+v);
//        });
        /*Connection connection = null;
        PreparedStatement pstm = null;
        try {
            connection = getConnection(null);
            connection.setAutoCommit(false);



            List<List<String>> list = Lists.newArrayList();
//        connection.
            String sql = "insert into db_stream_metric_computing.stream_metric_data_1_1 (merchant_code ,manager_id ,iousAmtSum ,uinCount ,uinDistinctCount ,date) values (?,?,?,?,?,?) on duplicate key update iousAmtSum = values(iousAmtSum) ,uinCount = values(uinCount),uinDistinctCount = values(uinDistinctCount) ";
            pstm = connection.prepareStatement(sql);

            //结构
//            int count=list.get(0).size();
            for (int i = 0; i < 2; i++) {
                *//*pstm.setObject(1,"100");
                pstm.setObject(2,"100");
                pstm.setObject(3,"100");
                pstm.setObject(4,"100");
                pstm.setObject(5,"100");
                pstm.setObject(6,"100");*//*
                for (int j = 0; j < 5; j++) {
                    //区分字段类型,set
                    //pstm.setInt(j, 1);
                    pstm.setObject(j+1,"100"+i);
                }
                pstm.setObject(6,"2019-12-03");
                pstm.addBatch();
                if((i+1)%300==0){
                    pstm.executeUpdate();
                    pstm.clearBatch();
                }
            }

            if(2%300!=0){
                pstm.executeUpdate();
                pstm.clearBatch();
            }

            connection.commit();
        }catch (Exception e){
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);

        }finally {
            closeAll(connection,pstm,null);
            System.out.println(connection);
        }*/

//        conn.commit();


    }

}
