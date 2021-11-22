package com.example.quang.gallerytrain.models;


public class Albums {
    private  String path="";
    private  String albumName;
    private int numberOfPics = 0;
    private String firstPic;

    public Albums(){}

    public Albums(String path, String albumName){
        this.albumName=albumName;
        this.path=path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getNumberOfPics() {
        return numberOfPics;
    }
    public void addpics(){
        this.numberOfPics++;
    }
    public void setNumberOfPics(int numberOfPics) {
        this.numberOfPics = numberOfPics;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }
}
