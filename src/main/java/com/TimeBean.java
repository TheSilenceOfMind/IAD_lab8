package com;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.Date;

/**
 * Created by Kirill on 06-Aug-17.
 */
@ManagedBean(
        name="timeBean",
        eager=true
)
@ApplicationScoped
public class TimeBean {
    private String time;

    public String getTime() {
        if (time == null) {
            update();
        }
        return time;
    }

    public void update() {
        time = (new Date()).toString();
    }
}
