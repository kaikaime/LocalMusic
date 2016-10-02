package com.example.administrator.good.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/23.
 */
public class MusicEntity implements Serializable {
    public String musicId;
    public String musicRating;
    public String id;
    public String musicPath;
    public String musicName;
     public String musicAlbum;
     public String musicArtist;
     public long musicTime;
     public String path;
     public String displayName;
     public String musicAlbumImage;

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public boolean isFcu() {
        return isFcu;
    }

    public void setIsFcu(boolean isFcu) {
        this.isFcu = isFcu;
    }

    public boolean isFcu=false;

    public String getMusicRating() {
        return musicRating;
    }

    public void setMusicRating(String musicRating) {
        this.musicRating = musicRating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicAlbum() {
        return musicAlbum;
    }

    public void setMusicAlbum(String musicAlbum) {
        this.musicAlbum = musicAlbum;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public long getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(long musicTime) {
        this.musicTime = musicTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMusicAlbumImage() {
        return musicAlbumImage;
    }

    public void setMusicAlbumImage(String musicAlbumImage) {
        this.musicAlbumImage = musicAlbumImage;
    }

    @Override
    public String toString() {
        return "MusicEntity{" +
                "musicId='" + musicId + '\'' +
                ", musicRating='" + musicRating + '\'' +
                ", id='" + id + '\'' +
                ", musicPath='" + musicPath + '\'' +
                ", musicName='" + musicName + '\'' +
                ", musicAlbum='" + musicAlbum + '\'' +
                ", musicArtist='" + musicArtist + '\'' +
                ", musicTime=" + musicTime +
                ", path='" + path + '\'' +
                ", displayName='" + displayName + '\'' +
                ", musicAlbumImage='" + musicAlbumImage + '\'' +
                ", isFcu=" + isFcu +
                '}';
    }
}
