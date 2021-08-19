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

import butterknife.BindView;
import butterknife.ButterKnife;

public class LaLigaFragment extends Fragment {

    @BindView(R.id.rv_laliga)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    final String baseURL = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?s=Soccer&c=Spain";
    TeamAdapter adapter;
    ArrayList<TeamModel> arrayList;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_laliga, container, false);
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
        arrayList = mainActivity.getLaLigaList();

        if (arrayList == null) addData();
        else setAdapter();
    }

    private void addData() {
        progressBar.setVisibility(View.VISIBLE);
        arrayList = new ArrayList<>();

        AndroidNetworking.get(baseURL)
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
                        arrayList.add(new TeamModel(badge, team, stadium, formedYear, description));
                    }

                    mainActivity.setLaLigaList(arrayList);

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

        adapter = new TeamAdapter(getActivity(), arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("badgeUrl", arrayList.get(position).getBadgeUrl());
                intent.putExtra("team", arrayList.get(position).getTeam());
                intent.putExtra("stadium", arrayList.get(position).getStadium());
                intent.putExtra("formedYear", arrayList.get(position).getFormedYear());
                intent.putExtra("description", arrayList.get(position).getDescription());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.btn_search);
        SearchView searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        // MenuItemCompat.setShowAsAction(item, //MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | //MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        //  MenuItemCompat.setActionView(item, searchView);
        // These lines are deprecated in API 26 use instead
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);

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
