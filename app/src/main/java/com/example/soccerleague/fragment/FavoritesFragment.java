package com.example.soccerleague.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soccerleague.R;
import com.example.soccerleague.activity.DetailsActivity;
import com.example.soccerleague.adapter.TeamAdapter;
import com.example.soccerleague.model.TeamModel;
import com.example.soccerleague.utils.RealmHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FavoritesFragment extends Fragment {
    @BindView(R.id.rv_favorites)
    RecyclerView recyclerView;

    private  final int REQUEST_CODE = 101;

    private TeamAdapter adapter;
    private List<TeamModel> teamList;
    private Realm realm;
    private RealmHelper realmHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);

        addData();
    }

    private void addData() {
        teamList = new ArrayList<>();
        teamList = realmHelper.getFavorites();

        adapter = new TeamAdapter(getActivity(), teamList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("id", teamList.get(position).getId());
                intent.putExtra("badgeUrl", teamList.get(position).getBadgeUrl());
                intent.putExtra("team", teamList.get(position).getTeam());
                intent.putExtra("stadium", teamList.get(position).getStadium());
                intent.putExtra("formedYear", teamList.get(position).getFormedYear());
                intent.putExtra("description", teamList.get(position).getDescription());
                intent.putExtra("isFavorite", teamList.get(position).getIsFavorite());
                intent.putExtra("isFromFavoriteFragment", true);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK) addData();
    }
}