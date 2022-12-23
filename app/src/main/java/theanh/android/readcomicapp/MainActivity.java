package theanh.android.readcomicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import theanh.android.readcomicapp.Fragment.ExploreFragment;
import theanh.android.readcomicapp.Fragment.HomeFragment;
import theanh.android.readcomicapp.Fragment.MineFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ExploreFragment exploreFragment = new ExploreFragment();
    MineFragment mineFragment  = new MineFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.mainNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeFragment).commit();
                        return true;
                    case R.id.menu_explore:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,exploreFragment).commit();
                        return true;
                    case R.id.menu_mine:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,mineFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}