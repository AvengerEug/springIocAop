package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dao.UserDao;
import com.model.User;
import com.util.DBUtil;

public class UserDaoImpl implements UserDao {

    @Override
    public List<User> showUsers() {
        Connection con = DBUtil.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<User>();

        try {
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            User user = null;
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSex(rs.getBoolean("sex"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
