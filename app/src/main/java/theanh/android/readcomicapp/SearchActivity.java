package theanh.android.readcomicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import theanh.android.readcomicapp.Adapter.SearchAdapter;
import theanh.android.readcomicapp.Object.Comics;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView txvCancel;
    private RecyclerView rcvSearch;

    private ArrayList<Comics> comicsList;
    private SearchAdapter searchAdapter;
    private DatabaseReference comicRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");

        searchView = findViewById(R.id.searchView);
        rcvSearch = findViewById(R.id.rcvSearch);

        comicsList = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, new SearchAdapter.onComicClick() {
            @Override
            public void onComicClick(Comics comics, ImageView comicImg) {
                showChapter(comics, comicImg);
            }
        });

        rcvSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvSearch.setAdapter(searchAdapter);

        getListComic();

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchComic(newText);
                return false;
            }
        });

        txvCancel = findViewById(R.id.backToExplore);
        txvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getListComic() {
        comicRef = FirebaseDatabase.getInstance().getReference("Comics");
        comicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (comicsList != null) {
                    comicsList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comics comics = dataSnapshot.getValue(Comics.class);
                    comicsList.add(comics);
                }
                searchAdapter.setDisplayData(comicsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchComic(String newText) {
        ArrayList<Comics> filterList = new ArrayList<>();
        for (Comics comics : comicsList) {
            if (comics.getName().toLowerCase().contains(newText.toLowerCase())) {
                filterList.add(comics);
            }
            searchAdapter.setFilterList(filterList);
        }
    }

    private void showChapter(Comics comics, ImageView comicImg) {
        Intent intent = new Intent(SearchActivity.this, ChapterActivity.class);
        intent.putExtra("name", comics.getName());
        intent.putExtra("image", comics.getImage());
        intent.putExtra("category", comics.getCategory());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) SearchActivity.this, comicImg, "share");
        startActivity(intent, options.toBundle());
    }
}