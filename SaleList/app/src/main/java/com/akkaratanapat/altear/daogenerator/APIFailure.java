package com.akkaratanapat.altear.daogenerator;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "APIFAILURE".
 */
@Entity
public class APIFailure {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String url;
    private String param;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public APIFailure() {
    }

    public APIFailure(Long id) {
        this.id = id;
    }

    @Generated
    public APIFailure(Long id, String name, String url, String param) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.param = param;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
