/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.dataobject;

import java.util.ArrayList;

/**
 *
 * @author Suranga
 */
public class DOImageAnalyzeRequest {

    private String url;
    private double confidenceThreshold;
    private ArrayList<DOImageTag> tags = new ArrayList<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getConfidenceThreshold() {
        return confidenceThreshold;
    }

    public void setConfidenceThreshold(double confidenceThreshold) {
        this.confidenceThreshold = confidenceThreshold;
    }

    public ArrayList<DOImageTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<DOImageTag> tags) {
        this.tags = tags;
    }

}
