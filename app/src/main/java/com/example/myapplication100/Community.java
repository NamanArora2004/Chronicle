package com.example.myapplication100;

import com.google.firebase.database.PropertyName;

public class Community {
    private String communityName;
    private String imageUrl;
    private String shopImageUrl;
    private String description;
    private String creatorId;  // Field to link the creator
    private String shopName;

    public Community() {
        // Default constructor required for calls to DataSnapshot.getValue(Community.class)
    }

    // Community Name
    @PropertyName("CommunityName")
    public String getCommunityName() {
        return communityName;
    }

    @PropertyName("CommunityName")
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    // Description
    @PropertyName("Description")
    public String getDescription() {
        return description;
    }

    @PropertyName("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    // Creator ID (Linking the creator)
    @PropertyName("CreatorId")
    public String getCreatorId() {
        return creatorId;
    }

    @PropertyName("CreatorId")
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    // Community Image URL
    @PropertyName("ImageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @PropertyName("ImageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Shop Image URL
    @PropertyName("ShopImageUrl")
    public String getShopImageUrl() {
        return shopImageUrl;
    }

    @PropertyName("ShopImageUrl")
    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    // Shop Name
    @PropertyName("ShopName")
    public String getShopName() {
        return shopName;
    }

    @PropertyName("ShopName")
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
