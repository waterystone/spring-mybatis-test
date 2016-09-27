package com.adu.spring_test.mybatis.model;

import java.util.List;

import com.adu.spring_test.mybatis.util.Stringfy;

/**
 * @author yunjie.du
 * @date 2016/9/27 15:45
 */
public class ProfInfo extends Stringfy {
    private double height;
    private double weight;
    private List<String> interests;

    public ProfInfo() {
    }

    public ProfInfo(double height, double weight, List<String> interests) {
        this.height = height;
        this.weight = weight;
        this.interests = interests;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
