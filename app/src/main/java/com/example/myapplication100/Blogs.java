package com.example.myapplication100;

import com.google.firebase.database.PropertyName;

public class Blogs {
private String blogImageurl;
    private String authorName;
    private String content;
    private String title;
    private String name;

    // Default constructor (required for Firebase)
    public Blogs() {
    }

    // Parameterized constructor
    public Blogs(String authorName, String blogImageurl,String content, String title) {
        this.authorName = authorName;
        this.content = content;
        this.title = title;
    }

    // Getter and Setter for AuthorName
    @PropertyName("AuthorName")
    public String getAuthorName() {
        return authorName;
    }

    @PropertyName("AuthorName")
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    // Getter and Setter for Content
    @PropertyName("BlogImageUrl")
    public void setBlogImageurl(String blogImageurl){
        this.blogImageurl=blogImageurl;
    }
    @PropertyName("BlogImageUrl")
    public String getBlogImageurl(){
        return blogImageurl;
    }
    @PropertyName("Content")
    public String getContent() {
        return content;
    }

    @PropertyName("Content")
    public void setContent(String content) {
        this.content = content;
    }


    // Getter and Setter for Title
    @PropertyName("BlogTitle")
    public String getTitle() {
        return title;
    }

    @PropertyName("BlogTitle")
    public void setTitle(String title) {
        this.title = title;
    }


}
