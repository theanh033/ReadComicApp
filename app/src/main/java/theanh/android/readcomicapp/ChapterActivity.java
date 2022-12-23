package theanh.android.readcomicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import theanh.android.readcomicapp.Adapter.ViewPagerChapterAdapter;

public class ChapterActivity extends AppCompatActivity {

    private ImageView comicChapterImg;
    private TextView comicChapterName, comicChapterCategory;

    private TabLayout tabLayout;
    private ViewPager2 viewPagerChapter;
    private ViewPagerChapterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        comicChapterImg = findViewById(R.id.comicChapterImg);
        comicChapterName = findViewById(R.id.comicChapterName);
        comicChapterCategory = findViewById(R.id.comicChapterCategory);

        tabLayout = findViewById(R.id.tabLayout);
        viewPagerChapter = findViewById(R.id.viewPagerChapter);

        getDataFromHome();
        setUpTabLayout();
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
}