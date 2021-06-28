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
public class DODescription {

    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<DOCaption> captions = new ArrayList<>();

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<DOCaption> getCaptions() {
        return captions;
    }

    public void setCaptions(ArrayList<DOCaption> captions) {
        this.captions = captions;
    }

}
