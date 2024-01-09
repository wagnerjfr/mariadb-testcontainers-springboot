package com.example.mariadbtestcontainersspringboot;

import java.sql.Connection;

public interface DBConnection {
    Connection getConnection();
}
