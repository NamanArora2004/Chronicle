package com.example.myapplication100;

public class CommunityFollow {

    private String communityName;
    private String communityImageUrl;

    public CommunityFollow() {
        // Empty constructor required for Firebase
    }

    public CommunityFollow(String communityName, String communityImageUrl) {
        this.communityName = communityName;
        this.communityImageUrl = communityImageUrl;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityImageUrl() {
        return communityImageUrl;
    }

    public void setCommunityImageUrl(String communityImageUrl) {
        this.communityImageUrl = communityImageUrl;
    }
}
