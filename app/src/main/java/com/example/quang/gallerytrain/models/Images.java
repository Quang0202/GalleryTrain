package com.example.quang.gallerytrain.models;

public class Images {
    private String imageName;
    private String imagePath;
    private  String imageSize;
    private  String imageUri;
    private Boolean selected = false;
    public Images(){}
    public Images(String imageName, String imagePath, String imageSize, String imageUri){
        this.imageName=imageName;
        this.imagePath=imagePath;
        this.imageSize=imageSize;
        this.imageUri=imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
