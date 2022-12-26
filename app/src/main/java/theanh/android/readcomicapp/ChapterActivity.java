package theanh.android.readcomicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import theanh.android.readcomicapp.Adapter.ViewPagerChapterAdapter;
import theanh.android.readcomicapp.Fragment.ExploreFragment;
import theanh.android.readcomicapp.Object.Comics;

public class ChapterActivity extends AppCompatActivity {

    private ImageView comicChapterImg, favIcon;
    private TextView comicChapterName, comicChapterCategory, txvInfoTitle, txvInfo;
    private RelativeLayout addToFav;

    private TabLayout tabLayout;
    private ViewPager2 viewPagerChapter;
    private ViewPagerChapterAdapter adapter;

    private DatabaseReference favRef;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    int i = 0;
    int j = 0;
    long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        comicChapterImg = findViewById(R.id.comicChapterImg);
        comicChapterName = findViewById(R.id.comicChapterName);
        comicChapterCategory = findViewById(R.id.comicChapterCategory);

        txvInfoTitle = findViewById(R.id.infoTitle);
        txvInfo = findViewById(R.id.info);
        txvInfoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo();
            }
        });

        favIcon = findViewById(R.id.favIcon);
        addToFav = findViewById(R.id.addToFav);

        tabLayout = findViewById(R.id.tabLayout);
        viewPagerChapter = findViewById(R.id.viewPagerChapter);

        getDataFromHome();
        setUpTabLayout();
        addComicToFav();
    }

    private void getDataFromHome() {
        String comicName = getIntent().getExtras().getString("name");
        String comicImage = getIntent().getExtras().getString("image");
        String comicCategory = getIntent().getExtras().getString("category");

        getSupportActionBar().setTitle(comicName);
        comicChapterName.setText(comicName);
        comicChapterCategory.setText(comicCategory);
        Glide.with(this).load(comicImage).into(comicChapterImg);
    }

    private void setUpTabLayout() {
        adapter = new ViewPagerChapterAdapter(this);
        viewPagerChapter.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPagerChapter, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Description");
                    break;
                case 1:
                    tab.setText("Chapters");
                    break;
            }
        }).attach();
    }

    private void showInfo() {
        i++;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                i = 0;
            }
        };
        if (i%2==1) {
            txvInfo.setVisibility(View.VISIBLE);
        }
        if (i%2==0) {
            txvInfo.setVisibility(View.GONE);
        }

    }

    private void addComicToFav() {
        favIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j++;
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        j = 0;
                    }
                };
                if (j%2==1) {
                    favIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(ChapterActivity.this, "Add to my list", Toast.LENGTH_SHORT).show();
                }
                if (j%2==0) {
                    favIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    Toast.makeText(ChapterActivity.this, "Remove from my list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}