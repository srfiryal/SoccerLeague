package com.example.soccerleague.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TeamModel extends RealmObject {
    @PrimaryKey
    private Integer id;
    private String badgeUrl, team, stadium, formedYear, description;
    private boolean isFavorite;

    public TeamModel() {}

    public TeamModel(String badgeUrl, String team, String stadium, String formedYear, String description, boolean isFavorite) {
        this.badgeUrl = badgeUrl;
        this.team = team;
        this.stadium = stadium;
        this.formedYear = formedYear;
        this.description = description;
        this.isFavorite = isFavorite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getFormedYear() {
        return formedYear;
    }

    public void setFormedYear(String formedYear) {
        this.formedYear = formedYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
