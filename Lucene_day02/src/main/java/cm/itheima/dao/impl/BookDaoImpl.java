package cm.itheima.dao.impl;

import cm.itheima.dao.BookDao;
import cm.itheima.po.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
//    查询所以图书列表
    public List<Book> findAllBooks() {
        List<Book> list = new ArrayList<Book>();

        Connection con =null;
        PreparedStatement psmt =null;
        ResultSet rs =null;
//        加载jdbc
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lucene","root","root");

            String sql = "select * from book";

            psmt = con.prepareStatement(sql);

            rs = psmt.executeQuery();
            while (rs.next()){
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setBookname(rs.getString("bookname"));
                book.setPrice(rs.getFloat("price"));
                book.setPic(rs.getString("pic"));
                book.setBookdesc(rs.getString("bookdesc"));
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                psmt.close();
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }


        return list;
    }
}
