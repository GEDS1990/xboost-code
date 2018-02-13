package com.xboost.pojo;

import java.io.Serializable;

public class Dimensions implements Serializable {

    private Integer id;
    private String dimension;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
}
