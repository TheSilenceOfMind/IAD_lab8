package com;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

@ManagedBean (name="PointsData")
@SessionScoped
public class PointsData implements Serializable {
    private String x = "0";
    private String y;
    private String r;
    ArrayList<Point> points;
    private static Connection dbConnection = null;

    private static int count = 0;

    public PointsData() {

    }

    public String getX() {
        return this.x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return this.y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getR() {
        return this.r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public boolean checkIsInside(double x, double y, double r) {
        return  (x >= 0 && y >= 0 && (x*x + y*y <= r*r / 4)) ||
                (x <= 0 && y >= 0 && (y <= r / 2 + x / 2)) ||
                (x >= 0 && y <= 0 && y >= -r && x <= r / 2);
    }

    private Connection getDbConnection() {
        if (dbConnection == null) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String URL = "jdbc:oracle:thin:@localhost:1521:XE";
                String USER = "Kirill";
                String PASS = "0797";
                dbConnection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Connection has been established");
            } catch (Exception e) {
                System.err.println("Can't get connection! Exception occured: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return dbConnection;
    }

    private void addPointToDB(Point p) throws Exception {
        try {
            Connection conn = getDbConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO RESULTS VALUES(?,?,?,?)");
            statement.setFloat(1, p.getX());
            statement.setFloat(2, p.getY());
            statement.setFloat(3, p.getR());
            statement.setInt(4, p.getIsInside() ? 1 : 0);
            System.out.println("Sending db update: " + statement);
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error: SQL exception " + e.getMessage());
            e.printStackTrace();
        } finally {
            dbConnection = null;
        }
    }

    public ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        try {
            Connection connection = getDbConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT X, Y, R, INSIDE FROM RESULTS");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Point point = new Point(rs.getFloat(1), rs.getFloat(2), rs.getFloat(3), rs.getInt(4) != 0);
                points.add(point);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error: SQL exception" + e.getMessage());
        } finally {
            dbConnection = null;
        }
        return points;
    }

    public String parseRequestAndUpdateDB () {
        try {
            float x = Float.parseFloat(getX());
            float y = Float.parseFloat(getY());
            float r = Float.parseFloat(getR());
            Point point = new Point(x, y, r, checkIsInside((double) x, (double) y, (double) r));
            addPointToDB(point);
        } catch (Exception e) {
            System.err.println("Exception occured in method parseRequestAndUpdateDB" + e.getMessage());
        }
        return "toMainPage";
    }

}
