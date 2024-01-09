package com.example.mariadbtestcontainersspringboot.util;

import com.example.mariadbtestcontainersspringboot.Customer;
import com.example.mariadbtestcontainersspringboot.DBConnection;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class TestUtils {
    private static final String TABLE = "Customer";
    private final DBConnection dbConnection;

    public TestUtils(DBConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public void setUpData() throws SQLException {
        String createUserSql = "create table if not exists " + TABLE + " (id int primary key, name varchar(20) null)";
        String insertUserSql = "insert into " + TABLE + " (id, name) values(1, 'Wagner')";
        try (Connection connection = this.dbConnection.getConnection()) {
            Statement createStatement = connection.createStatement();
            Statement insertStatement = connection.createStatement();
            createStatement.execute(createUserSql);
            insertStatement.execute(insertUserSql);
         }
    }

    public Customer getRandomCustomer() throws SQLException {
        String query = "SELECT id, name FROM " + TABLE + " ORDER by rand() limit 1";
        try (Connection connection = this.dbConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            return new Customer(resultSet.getInt("id"), resultSet.getString("name"));
        }
    }

    public Customer getCustomerById(int id) throws SQLException {
        try (Connection connection = this.dbConnection.getConnection()) {
            String query = "SELECT id, name FROM " + TABLE + " WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new Customer(rs.getInt("id"), rs.getString("name"));
        }
    }

    public String getRandomString(int length){
        return RandomStringUtils.randomAlphabetic(length);
    }
}
