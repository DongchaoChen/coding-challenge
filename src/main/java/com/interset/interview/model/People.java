package com.interset.interview.model;

/**
 * People model for json processing
 *
 * @author Dongchao Chen
 */
public class People {

    private String first_name;
    private String last_name;
    private int siblings;
    private String favourite_food;
    private String birth_timezone;
    private String birth_timestamp;

    // get / set methods
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getSiblings() {
        return siblings;
    }

    public void setSiblings(int siblings) {
        this.siblings = siblings;
    }

    public String getFavourite_food() {
        return favourite_food;
    }

    public void setFavourite_food(String favourite_food) {
        this.favourite_food = favourite_food;
    }

    public String getBirth_timezone() {
        return birth_timezone;
    }

    public void setBirth_timezone(String birth_timezone) {
        this.birth_timezone = birth_timezone;
    }

    public String getBirth_timestamp() {
        return birth_timestamp;
    }

    public void setBirth_timestamp(String birth_timestamp) {
        this.birth_timestamp = birth_timestamp;
    }
}
