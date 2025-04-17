package com.example.petit_reve;
import java.util.Date;

public class Story {
    private long id;
    private String title;
    private String content;
    private String heroName;
    private String ageRange;
    private String storyType;
    private String keywords;
    private String gender;
    private boolean isFavorite;
    private Date createdAt;

    // Constructeur par défaut
    public Story() {
        this.createdAt = new Date();
        this.isFavorite = false;
    }

    // Constructeur avec paramètres
    public Story(String title, String content, String heroName, String ageRange,
                 String storyType, String keywords, String gender) {
        this.title = title;
        this.content = content;
        this.heroName = heroName;
        this.ageRange = ageRange;
        this.storyType = storyType;
        this.keywords = keywords;
        this.gender = gender;
        this.createdAt = new Date();
        this.isFavorite = false;
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getStoryType() {
        return storyType;
    }

    public void setStoryType(String storyType) {
        this.storyType = storyType;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}