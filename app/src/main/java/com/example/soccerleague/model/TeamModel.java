package com.example.soccerleague.model;

public class TeamModel {
    private String badgeUrl, team, stadium, formedYear, description;

    public TeamModel(String badgeUrl, String team, String stadium, String formedYear, String description) {
        this.badgeUrl = badgeUrl;
        this.team = team;
        this.stadium = stadium;
        this.formedYear = formedYear;
        this.description = description;
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
}
