package com.example.soccerleague.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.soccerleague.activity.DetailsActivity;
import com.example.soccerleague.R;
import com.example.soccerleague.adapter.TeamAdapter;
import com.example.soccerleague.model.TeamModel;
import com.example.soccerleague.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PremierLeagueFragment extends Fragment {

    @BindView(R.id.rv_premierleague)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private final int REQUEST_CODE = 101;

    private final String BASE_URL = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League";
    private TeamAdapter adapter;
    private List<TeamModel> teamList;
    private MainActivity mainActivity;
    private int listIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_premierleague, container, false);
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
        AndroidNetworking.initialize(getActivity());

        mainActivity = (MainActivity) getActivity();
        teamList = mainActivity.getPremiereList();

        if (teamList == null) addData();
        else setAdapter();
    }

    private void addData() {
        progressBar.setVisibility(View.VISIBLE);
        teamList = new ArrayList<>();

        AndroidNetworking.get(BASE_URL)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultArray = response.getJSONArray("teams");

                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject resultObj = resultArray.getJSONObject(i);
                        String badge = resultObj.getString("strTeamBadge");
                        String team = resultObj.getString("strTeam");
                        String stadium = resultObj.getString("strStadium");
                        String formedYear = resultObj.getString("intFormedYear");
                        String description = resultObj.getString("strDescriptionEN");
                        teamList.add(new TeamModel(badge, team, stadium, formedYear, description, false));
                    }

                    mainActivity.setPremiereList(teamList);

                    setAdapter();
                    progressBar.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onError(ANError anError) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("error", anError.toString());
            }
        });
    }

    private void setAdapter() {
        progressBar.setVisibility(View.INVISIBLE);

        adapter = new TeamAdapter(getActivity(), teamList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                listIndex = position;

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("id", teamList.get(position).getId());
                intent.putExtra("badgeUrl", teamList.get(position).getBadgeUrl());
                intent.putExtra("team", teamList.get(position).getTeam());
                intent.putExtra("stadium", teamList.get(position).getStadium());
                intent.putExtra("formedYear", teamList.get(position).getFormedYear());
                intent.putExtra("description", teamList.get(position).getDescription());
                intent.putExtra("isFavorite", teamList.get(position).getIsFavorite());
                intent.putExtra("isFromFavoriteFragment", false);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            teamList.get(listIndex).setId(data.getIntExtra("id", 1));
            teamList.get(listIndex).setIsFavorite(data.getBooleanExtra("isFavorite", false));
            listIndex = 0;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.btn_search);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    adapter.getFilter().filter(s);
                    } catch (Exception e) {
                    Log.d("error", "" + e.toString());
                }
                return false;
            }
        });
    }
}
