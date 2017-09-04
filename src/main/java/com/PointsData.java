package com;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

@ManagedBean (name="PointsData")
@SessionScoped
public class PointsData implements Serializable {
    private String x;
    private String y;
    private String r;
    ArrayList<Point> points;
    private static Connection dbConnection = null;

    public PointsData() {

    }

    @PostConstruct
    public void init() {
        r = "3";
    }

    public String getX() {
        return this.x;
    }

    public void setX(String x) {
        System.out.println(x);
        this.x = x;
    }

    public String getY() {
        return this.y;
    }

    public void setY(String y) {
        this.y = y.replace(",", ".");
    }

    public String getR() {
        return this.r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public boolean checkIsInside(double x, double y, double r) {
        return  ((x >= 0 && y >= 0 && (x*x + y*y <= r*r / 4)) ||
                (x <= 0 && y >= 0 && (y <= r / 2 + x / 2)) ||
                (x >= 0 && y <= 0 && y >= -r && x <= r / 2));
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

    public void addPointToDB(Point p) throws Exception {
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
            float xx = Float.parseFloat(getX());
            float yy = Float.parseFloat(getY());
            float rr = Float.parseFloat(getR());
            Point point = new Point(xx, yy, rr, checkIsInside((double) xx, (double) yy, (double) rr));
            x = "";
            y = "";
            r = "3";
            addPointToDB(point);
        } catch (Exception e) {
            System.err.println("Exception occured in method parseRequestAndUpdateDB: " + e.getMessage());
        }
        return "toMainPage";
    }

    public ArrayList<Boolean> changeRadius(float r) {
        ArrayList<Boolean> listOfInsideInformation = new ArrayList<Boolean>();
        ArrayList<Point> points = new ArrayList<Point>();
        try {
            float x, y;
            Connection connection = getDbConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE RESULTS SET R=(?), INSIDE=(?) WHERE X=(?) AND Y=(?)");
            PreparedStatement statementGet = connection.prepareStatement("SELECT X, Y, R, INSIDE FROM RESULTS");
            ResultSet rs = statementGet.executeQuery();
            while (rs.next()) {
                Point point = new Point(rs.getFloat(1), rs.getFloat(2), rs.getFloat(3), rs.getInt(4) != 0);
                points.add(point);
            }
            for (int i = 0; i < points.size(); i++) {
                x = points.get(i).getX();
                y = points.get(i).getY();
                listOfInsideInformation.add(checkIsInside(x, y, r));
                statement.setFloat(1, r);
                statement.setInt(2, listOfInsideInformation.get(i) ? 1 : 0);
                statement.setFloat(3, x);
                statement.setFloat(4, y);
                statement.executeUpdate();
            }
            System.out.println("Sending DB update: " + statement);
            statement.close();
            connection.close();
            dbConnection = null;
        } catch (SQLException var4) {
            System.err.println("Error: SQL exception " + var4.getMessage());
            var4.printStackTrace();
        }
        return listOfInsideInformation;
    }

}