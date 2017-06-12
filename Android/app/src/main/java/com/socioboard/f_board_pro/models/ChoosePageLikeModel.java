package com.socioboard.f_board_pro.models;

/**
 * Created by GLB-122 on 5/8/2017.
 */

public class ChoosePageLikeModel
{

    //THIS IS SETTER GETTER METHOD TO ACCESS ADMIN PAGE OF FACEBOOK

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String access_token,category,name,id;

}
