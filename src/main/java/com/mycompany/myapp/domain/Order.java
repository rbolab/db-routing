package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.sql.Date;

public class Order implements Serializable {

    public Order() {
    }

    private int id;

    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
