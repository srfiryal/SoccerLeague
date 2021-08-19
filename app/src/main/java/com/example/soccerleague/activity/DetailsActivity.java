package com.example.soccerleague.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.soccerleague.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private String badgeUrl, team, stadium, formedYear, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            badgeUrl = bundle.getString("badgeUrl");
            team = bundle.getString("team");
            stadium = bundle.getString("stadium");
            formedYear = bundle.getString("formedYear");
            description = bundle.getString("description");

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}