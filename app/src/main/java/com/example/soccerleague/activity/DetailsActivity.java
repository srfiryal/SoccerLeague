package com.example.soccerleague.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.soccerleague.R;
import com.example.soccerleague.model.TeamModel;
import com.example.soccerleague.utils.RealmHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_details)
    MaterialToolbar toolbar;

    @BindView(R.id.img_badge_details)
    ImageView img_badge;

    @BindView(R.id.tv_team_details)
    TextView tv_team;
    @BindView(R.id.tv_stadium_details)
    TextView tv_stadium;
    @BindView(R.id.tv_formedYear_details)
    TextView tv_formedYear;
    @BindView(R.id.tv_teamDesc_details)
    TextView tv_description;

    @BindView(R.id.background_layout)
    LinearLayout linearLayout;

    private Integer id;
    private String badgeUrl, team, stadium, formedYear, description;
    private boolean isFavorite, isFromFavoriteFragment;
    private int listIndex;
    private Realm realm;
    private RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Realm.init(DetailsActivity.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
            badgeUrl = bundle.getString("badgeUrl");
            team = bundle.getString("team");
            stadium = bundle.getString("stadium");
            formedYear = bundle.getString("formedYear");
            description = bundle.getString("description");
            isFavorite = bundle.getBoolean("isFavorite");
            isFromFavoriteFragment = bundle.getBoolean("isFromFavoriteFragment");

            getSupportActionBar().setTitle(team);
        }

        tv_team.setText(team);
        tv_stadium.setText(stadium);
        tv_formedYear.setText("Since " + formedYear);
        tv_description.setText(description);
        Picasso.get()
                .load(badgeUrl)
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .into(img_badge);

        setDominantColor();
    }

    private void setDominantColor() {
        Picasso.get()
                .load(badgeUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(@Nullable Palette palette) {
                                        Palette.Swatch vibrant = palette.getDarkVibrantSwatch();

                                        if (vibrant != null) {
                                            int titleColor = vibrant.getRgb();
                                            linearLayout.setBackgroundColor(titleColor);

                                            toolbar.setBackgroundColor(titleColor);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                getWindow().setStatusBarColor(titleColor);
                                            }
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isFavorite)
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24));

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_favorite:
                toggleFavorites();
                setToolbarIcon(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setToolbarIcon(MenuItem item) {
        if (isFavorite) item.setIcon(R.drawable.ic_baseline_favorite_24);
        else item.setIcon(R.drawable.ic_baseline_favorite_border_24);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (!isFromFavoriteFragment) {
            intent.putExtra("id", id);
            intent.putExtra("isFavorite", isFavorite);
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void toggleFavorites() {
        if (isFavorite) {
            Log.d("Favorite", "Delete");
            isFavorite = false;
            realmHelper.delete(id);
        } else {
            Log.d("Favorite", "Add");
            isFavorite = true;
            TeamModel teamModel = new TeamModel(badgeUrl, team, stadium, formedYear, description, true);
            id = realmHelper.save(teamModel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}