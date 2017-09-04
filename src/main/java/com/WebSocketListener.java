package com;

import com.Point;
import com.PointsData;

import javax.faces.bean.ApplicationScoped;
import javax.json.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@ServerEndpoint("/ping")
public class WebSocketListener {

    PointsData pointsData;
    private String type;
    private Double r;

    @OnMessage
    public void onMessage(Session session, String message) {
        Point curPoint = new Point();
        JsonReader jsonReader = Json.createReader(new StringReader(message));
        JsonObject data = jsonReader.readObject();
        type = data.getString("type");
        r = data.getJsonNumber("R").doubleValue();
        if (type.equals("Change radius and draw points")) {
            JsonArray xList, yList, hitList;
            // изменяем значения радиусов в БД на указанное;
            pointsData.changeRadius((float) (double) r);
            List<Point> pointList = pointsData.getPoints();
            JsonArrayBuilder xBuild = Json.createArrayBuilder();
            JsonArrayBuilder yBuild = Json.createArrayBuilder();
            JsonArrayBuilder pBuild = Json.createArrayBuilder();
            for (int i = 0; i < pointList.size(); i++) {
                curPoint = pointList.get(i);
                xBuild.add(curPoint.getX());
                yBuild.add(curPoint.getY());
                pBuild.add(curPoint.getIsInside());
            }
            xList = xBuild.build();
            yList = yBuild.build();
            hitList = pBuild.build();
            JsonObject retData = Json.createObjectBuilder()
                    .add("type", "Change radius and draw points")
                    .add("xList", xList)
                    .add("yList", yList)
                    .add("first", data.getInt("beginInd"))
                    .add("hitList", hitList)
                    .build();
            try {
                session.getBasicRemote().sendText(retData.toString());
            } catch (Throwable e) {
                System.err.println(e.getMessage());
            }
        } else if (type.equals("Add point to DB")) {
            Double x, y;
            x = data.getJsonNumber("x").doubleValue();
            y = data.getJsonNumber("y").doubleValue();
            curPoint.setR((float) (double) r);
            curPoint.setX((float) (double) x);
            curPoint.setY((float) (double) y);
            curPoint.setIsInside(pointsData.checkIsInside((double) curPoint.getX(), (double) curPoint.getY(), (double) curPoint.getR()));
            try {
                pointsData.addPointToDB(curPoint);
            } catch (Exception e) {

            }
            JsonObject retData = Json.createObjectBuilder()
                    .add("type", "Add point to DB")
                    .add("isInside", curPoint.getIsInside())
                    .add("R", r)
                    .add("ind", data.getInt("ind"))
                    .build();
            try {
                session.getBasicRemote().sendText(retData.toString());
            } catch (Throwable e) {

            }
        }
    }

    public void init() {
        pointsData = new PointsData();
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen: " + session.getId());
        init();
    }


}
