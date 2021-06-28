/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.dataobject.computervision;

import java.util.ArrayList;

/**
 *
 * @author Suranga
 */
public class DOColor {

    private String dominantColorForeground;
    private String dominantColorBackground;
    private ArrayList<String> dominantColors = new ArrayList<>();
    private String accentColor;
    private boolean isBwImg;
    private boolean isBWImg;

    public String getDominantColorForeground() {
        return dominantColorForeground;
    }

    public void setDominantColorForeground(String dominantColorForeground) {
        this.dominantColorForeground = dominantColorForeground;
    }

    public String getDominantColorBackground() {
        return dominantColorBackground;
    }

    public void setDominantColorBackground(String dominantColorBackground) {
        this.dominantColorBackground = dominantColorBackground;
    }

    public ArrayList<String> getDominantColors() {
        return dominantColors;
    }

    public void setDominantColors(ArrayList<String> dominantColors) {
        this.dominantColors = dominantColors;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public boolean isIsBwImg() {
        return isBwImg;
    }

    public void setIsBwImg(boolean isBwImg) {
        this.isBwImg = isBwImg;
    }

    public boolean isIsBWImg() {
        return isBWImg;
    }

    public void setIsBWImg(boolean isBWImg) {
        this.isBWImg = isBWImg;
    }

}
