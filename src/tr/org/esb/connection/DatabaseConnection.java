package tr.org.esb.connection;

import tr.org.esb.webpagecategorization.WebpageCategorization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DatabaseConnection {

    private Connection conn;
    private PreparedStatement stmt = null;

    public DatabaseConnection() throws SQLException{
        final String url = "jdbc:postgresql://localhost/testdb";
        Properties props = new Properties();
        props.setProperty("user", "emre");
        props.setProperty("password", "1E2M3R4E");
        props.setProperty("ssl", "false");

        conn = DriverManager.getConnection(url, props);
    }

    public void insertIntoDb(String url, WebpageCategorization wc) throws SQLException {
        if(url == null || wc == null) {
            throw new NullPointerException();
        } else {
            stmt = conn.prepareStatement("INSERT INTO test(url, text, img, video)" +
                    "VALUES (" +
                    "'"+url+"',"+
                    wc.getPercent("text")+","+
                    wc.getPercent("img")+","+
                    wc.getPercent("video")+
                    ")");
            stmt.executeUpdate();
        }
    }

	public static void main (String[] args) throws SQLException {

        DatabaseConnection dbconn = new DatabaseConnection();

        try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
            String url;
            while ((url = br.readLine()) != null) {
                try {
                    WebpageCategorization wc = new WebpageCategorization(url);
                    System.out.println("Text is " + wc.getPercent("text"));
                    System.out.println("Image is " + wc.getPercent("img"));
                    System.out.println("Video is " + wc.getPercent("video"));
                    //dbconn.insertIntoDb(url, wc);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
}
