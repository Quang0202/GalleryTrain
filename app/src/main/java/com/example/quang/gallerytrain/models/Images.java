package com.example.quang.gallerytrain.models;

public class Images {
    private String imageName;
    private String imagePath;
    private  String imageSize;
    private long timeTamp;
    private String address;

    public long getTimeTamp() {
        return timeTamp;
    }

    public void setTimeTamp(long timeTamp) {
        this.timeTamp = timeTamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private Boolean selected = false;
    public Images(){}
    public Images(String imageName, String imagePath, String imageSize, String imageUri){
        this.imageName=imageName;
        this.imagePath=imagePath;
        this.imageSize=imageSize;
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

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
