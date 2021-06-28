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
public class DOAdult {

    private boolean isAdultContent;
    private boolean isRacyContent;
    private boolean isGoryContent;
    private double adultScore;
    private double racyScore;
    private double goreScore;

    public boolean isIsAdultContent() {
        return isAdultContent;
    }

    public void setIsAdultContent(boolean isAdultContent) {
        this.isAdultContent = isAdultContent;
    }

    public boolean isIsRacyContent() {
        return isRacyContent;
    }

    public void setIsRacyContent(boolean isRacyContent) {
        this.isRacyContent = isRacyContent;
    }

    public boolean isIsGoryContent() {
        return isGoryContent;
    }

    public void setIsGoryContent(boolean isGoryContent) {
        this.isGoryContent = isGoryContent;
    }

    public double getAdultScore() {
        return adultScore;
    }

    public void setAdultScore(double adultScore) {
        this.adultScore = adultScore;
    }

    public double getRacyScore() {
        return racyScore;
    }

    public void setRacyScore(double racyScore) {
        this.racyScore = racyScore;
    }

    public double getGoreScore() {
        return goreScore;
    }

    public void setGoreScore(double goreScore) {
        this.goreScore = goreScore;
    }

}
