package cn.ithiema.dao.impl;

import cn.ithiema.dao.BookDao;
import cn.ithiema.po.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoimpl implements BookDao {
//    查询图书全部列表
    public List<Book> findAllBooks() {
//        创建结果集集合
        List<Book>list = new ArrayList<Book>();
        Connection connection =null;
        PreparedStatement ps =null;
        ResultSet rs =null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
           connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Lucene", "root", "root");
            String sql = "select * from book;";

            ps = connection.prepareStatement(sql);
//            执行
           rs = ps.executeQuery();
//            处理结果集
            while (rs.next()){
                Book book = new Book();
//                图书id
                book.setId(rs.getInt("id"));
//                图书名称
                book.setBookname(rs.getString("bookname"));
//                图书价格
                book.setPrice(rs.getFloat("price"));
//                图书照片
                book.setPic(rs.getString("pic"));
//                图书详情
                book.setBookdesc(rs.getString("bookdesc"));
                list.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs!=null)rs.close();
                if (ps!=null)ps.close();
                if (connection!=null)connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
