package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseUtil {

public static MongoClient mongoClient;
public static MongoDatabase database;
        public static void openConnection() throws SQLException{
        // Connect to MongoDB
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("JavaProject");
            System.out.println("Connected to MongoDB successfully");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
        }
            public static void closeConnection() {
                if (mongoClient != null) {
                    mongoClient.close();
                    System.out.println("Closed MongoDB connection");
                }
            }
    
            public static MongoClient getMongoClient() {
                return mongoClient;
            }
            public static MongoDatabase getDatabase() {
                return database;
            }
    }
