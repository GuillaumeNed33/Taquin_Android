package com.example.guillaume.projettaquin_nedelec;

/**
 * Created by Guillaume on 04/03/2017.
 */

public class Score {
    private String position;
    private String name;
    private String time;

    public Score(String position, String name, String time) {
        this.position = position;
        this.name = name;
        this.time = time;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}