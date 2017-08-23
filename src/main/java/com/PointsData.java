package com;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@ManagedBean (name="PointsData")
@SessionScoped
public class PointsData implements Serializable {
    private static Connection dbConnection = null;
    private String x = "0";
    private String y;
    private String r;
    List<Point> points = new ArrayList<Point>();

    public PointsData() {
    }

    public List<Point> getPoints() {
        try {
            points.add(new Point(1,2,3,true));
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return points;
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

    public boolean check(double x, double y, double r) {
        return  (x >= 0 && y >= 0 && (x*x + y*y <= r*r / 4)) ||
                (x <= 0 && y >= 0 && (y <= r / 2 + x / 2)) ||
                (x >= 0 && y <= 0 && y >= -r && x <= r / 2);
    }

    public String addPoint() throws Exception {
        //System.out.println("Request: " + this.x + " " + this.y + " " + this.r);

        try {
            points.add(new Point(-1,-1,-1,false));
        } catch (Exception e) {
            System.out.println("Error while adding new point!: " + e.getMessage());
        }

        return "toStartPage";
    }

}
