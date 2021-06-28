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
public class DOImageAnalyze {

    private ArrayList<DOCategory> categories = new ArrayList<>();
    private DOAdult adult;
    private ArrayList<DOTag> tags = new ArrayList<>();
    private DODescription description;
    private String requestId;
    private DOMetadata metadata;
    private ArrayList<DOFace> faces = new ArrayList<>();
    private DOColor color;
    private DOImageType imageType;
    private ArrayList<DOObject> objects = new ArrayList<>();
    private boolean imageResized;

    public ArrayList<DOCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<DOCategory> categories) {
        this.categories = categories;
    }

    public DOAdult getAdult() {
        return adult;
    }

    public void setAdult(DOAdult adult) {
        this.adult = adult;
    }

    public ArrayList<DOTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<DOTag> tags) {
        this.tags = tags;
    }

    public DODescription getDescription() {
        return description;
    }

    public void setDescription(DODescription description) {
        this.description = description;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public DOMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(DOMetadata metadata) {
        this.metadata = metadata;
    }

    public ArrayList<DOFace> getFaces() {
        return faces;
    }

    public void setFaces(ArrayList<DOFace> faces) {
        this.faces = faces;
    }

    public DOColor getColor() {
        return color;
    }

    public void setColor(DOColor color) {
        this.color = color;
    }

    public DOImageType getImageType() {
        return imageType;
    }

    public void setImageType(DOImageType imageType) {
        this.imageType = imageType;
    }

    public ArrayList<DOObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<DOObject> objects) {
        this.objects = objects;
    }

    public boolean isImageResized() {
        return imageResized;
    }

    public void setImageResized(boolean imageResized) {
        this.imageResized = imageResized;
    }

}
