/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.dataobject.computervision;

/**
 *
 * @author Suranga
 */
public class DOFace {

    private int age;
    private String gender;
    private DOFaceRectangle faceRectangle;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public DOFaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(DOFaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

}
