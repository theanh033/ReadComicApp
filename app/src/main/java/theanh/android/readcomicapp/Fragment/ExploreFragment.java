package theanh.android.readcomicapp.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import theanh.android.readcomicapp.Adapter.ComicsAdapter;
import theanh.android.readcomicapp.ChapterActivity;
import theanh.android.readcomicapp.Object.Comics;
import theanh.android.readcomicapp.R;
import theanh.android.readcomicapp.SearchActivity;

public class ExploreFragment extends Fragment {

    private RelativeLayout searchLayout;

    private RecyclerView rcvMarvelComic, rcvDcComic, rcvOtherComic;
    private DatabaseReference comicRef;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        searchLayout = view.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearch();
            }
        });
        rcvMarvelComic = view.findViewById(R.id.rcvMarvelComic);
        rcvDcComic = view.findViewById(R.id.rcvDcComic);
        rcvOtherComic = view.findViewById(R.id.rcvOtherComic);

        getMarvelList();
        getDcList();
        getOtherList();
        return view;
    }

    private void openSearch() {
        Intent intent = new Intent(requireContext(), SearchActivity.class);
        startActivity(intent);
    }

    private void getMarvelList() {
        rcvMarvelComic.setLayoutManager(new GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false));
        comicRef = FirebaseDatabase.getInstance().getReference("Comics");
        List<Comics> comicsList = new ArrayList<>();
        comicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (comicsList != null) {
                    comicsList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comics comics = dataSnapshot.getValue(Comics.class);
                    if (comics.getCategory().equals("Marvel Comic")){
                        comicsList.add(comics);
                    }
                }
                ComicsAdapter comicsAdapter = new ComicsAdapter(requireContext(), comicsList, new ComicsAdapter.onComicClick() {
                    @Override
                    public void onItemComicClick(Comics comics, ImageView comicImg) {
                        showChapter(comics, comicImg);
                    }
                });
                rcvMarvelComic.setAdapter(comicsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Lost connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDcList() {
        rcvDcComic.setLayoutManager(new GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false));
        comicRef = FirebaseDatabase.getInstance().getReference("Comics");
        List<Comics> comicsList = new ArrayList<>();
        comicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (comicsList != null) {
                    comicsList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comics comics = dataSnapshot.getValue(Comics.class);
                    if (comics.getCategory().equals("DC Comic")){
                        comicsList.add(comics);
                    }
                }
                ComicsAdapter comicsAdapter = new ComicsAdapter(requireContext(), comicsList, new ComicsAdapter.onComicClick() {
                    @Override
                    public void onItemComicClick(Comics comics, ImageView comicImg) {
                        showChapter(comics, comicImg);
                    }
                });
                rcvDcComic.setAdapter(comicsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Lost connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOtherList() {
        rcvOtherComic.setLayoutManager(new GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false));
        comicRef = FirebaseDatabase.getInstance().getReference("Comics");
        List<Comics> comicsList = new ArrayList<>();
        comicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (comicsList != null) {
                    comicsList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comics comics = dataSnapshot.getValue(Comics.class);
                    if (comics.getCategory().equals("Other Comic")){
                        comicsList.add(comics);
                    }
                }
                ComicsAdapter comicsAdapter = new ComicsAdapter(requireContext(), comicsList, new ComicsAdapter.onComicClick() {
                    @Override
                    public void onItemComicClick(Comics comics, ImageView comicImg) {
                        showChapter(comics, comicImg);
                    }
                });
                rcvOtherComic.setAdapter(comicsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Lost connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChapter(Comics comics, ImageView comicImg) {
        Intent intent = new Intent(requireContext(), ChapterActivity.class);
        intent.putExtra("name", comics.getName());
        intent.putExtra("image", comics.getImage());
        intent.putExtra("category", comics.getCategory());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) requireContext(), comicImg, "share");
        startActivity(intent, options.toBundle());
    }
}
