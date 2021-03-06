package com.example.soccerleague.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.soccerleague.fragment.FavoritesFragment;
import com.example.soccerleague.fragment.LaLigaFragment;
import com.example.soccerleague.fragment.PremierLeagueFragment;
import com.example.soccerleague.R;
import com.example.soccerleague.model.TeamModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private List<TeamModel> premiereList, laLigaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PremierLeagueFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_premierLeague:
                            selectedFragment = new PremierLeagueFragment();
                            break;
                        case R.id.nav_laliga:
                            selectedFragment = new LaLigaFragment();
                            break;
                        case R.id.nav_favorite:
                            selectedFragment = new FavoritesFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    public void setPremiereList(List<TeamModel> list) {
        premiereList = list;
    }

    public List<TeamModel> getPremiereList() {
        return premiereList;
    }

    public void setLaLigaList(List<TeamModel> list) {
         laLigaList = list;
    }

    public List<TeamModel> getLaLigaList() {
        return laLigaList;
    }
}