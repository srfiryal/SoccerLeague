package com.example.soccerleague.utils;

import android.util.Log;

import com.example.soccerleague.model.TeamModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;
    private int nextId;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    public int save(final TeamModel TeamModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null) {
                    Log.e("Realm", "Database was created");
                    Number currentIdNum = realm.where(TeamModel.class).max("id");
                    if (currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    TeamModel.setId(nextId);
                    TeamModel model = realm.copyToRealm(TeamModel);
                } else {
                    Log.e("Realm", "execute: Database not Exist");
                }
            }
        });

        return nextId;
    }

    public List<TeamModel> getFavorites() {
        RealmResults<TeamModel> results = realm.where(TeamModel.class).findAll();
        results = results.sort("team");
        return results;
    }

    public void update(final Integer id, final String badgeUrl, final String team, final String stadium, final String formedYear, final String description, final boolean isFavorite) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TeamModel model = realm.where(TeamModel.class)
                        .equalTo("id", id)
                        .findFirst();
                model.setBadgeUrl(badgeUrl);
                model.setTeam(team);
                model.setStadium(stadium);
                model.setFormedYear(formedYear);
                model.setDescription(description);
                model.setIsFavorite(isFavorite);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Realm", "onSuccess: Update Successfully");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    public void delete(Integer id) {
        final RealmResults<TeamModel> model = realm.where(TeamModel.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteFromRealm(0);
            }
        });
    }
}