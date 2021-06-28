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
public class DOObject {

    private DORectangle rectangle;
    private String object;
    private double confidence;

    public DORectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(DORectangle rectangle) {
        this.rectangle = rectangle;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

}
