package theanh.android.readcomicapp.Fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import theanh.android.readcomicapp.Adapter.ComicsAdapter;
import theanh.android.readcomicapp.Adapter.PreviewAdapter;
import theanh.android.readcomicapp.ChapterActivity;
import theanh.android.readcomicapp.Object.Comics;
import theanh.android.readcomicapp.R;

public class HomeFragment extends Fragment {

    private ViewPager viewPagerPreview;
    private CircleIndicator circleIndicator;
    private Timer timer;

    private DatabaseReference comicRef;
    private RecyclerView rcvUpdatingComic;
    private List<Comics> comicsList;
    private ComicsAdapter comicsAdapter;

    private DatabaseReference comicPreviewRef;
    private List<Comics> comicsPreviewList;
    private PreviewAdapter previewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPagerPreview = view.findViewById(R.id.vgPreview);
        circleIndicator = view.findViewById(R.id.ciPreview);
        rcvUpdatingComic = view.findViewById(R.id.rcvUpdatingComic);

        getListPreview();
        getListAllComic();

        return view;
    }

    private void getListPreview() {
        comicsPreviewList = new ArrayList<>();
        comicPreviewRef = FirebaseDatabase.getInstance().getReference("Comics");
        Query query = comicPreviewRef.limitToFirst(5);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comics comics = dataSnapshot.getValue(Comics.class);
                    comicsPreviewList.add(comics);
                }
                previewAdapter = new PreviewAdapter(requireContext(), comicsPreviewList);
                viewPagerPreview.setAdapter(previewAdapter);
                circleIndicator.setViewPager(viewPagerPreview);
                previewAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

                if (comicsPreviewList == null || comicsPreviewList.isEmpty() || viewPagerPreview == null) {
                    return;
                }

                if (timer == null) {
                    timer = new Timer();
                }
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                int currItem = viewPagerPreview.getCurrentItem();
                                int totalItem = comicsPreviewList.size() - 1;
                                if (currItem < totalItem) {
                                    currItem++;
                                    viewPagerPreview.setCurrentItem(currItem);
                                } else {
                                    viewPagerPreview.setCurrentItem(0);
                                }
                            }
                        });
                    }
                }, 500, 3000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Lost connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void getListAllComic() {
        rcvUpdatingComic.setLayoutManager(new GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false));
        comicRef = FirebaseDatabase.getInstance().getReference("Comics");
        comicsList = new ArrayList<>();
        comicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comics comics = dataSnapshot.getValue(Comics.class);
                    comicsList.add(comics);
                }
                comicsAdapter = new ComicsAdapter(requireContext(), comicsList, new ComicsAdapter.onComicClick() {
                    @Override
                    public void onItemComicClick(Comics comics, ImageView comicImg) {
                        showChapters(comics, comicImg);
                    }
                });
                rcvUpdatingComic.setAdapter(comicsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Lost connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChapters(Comics comics, ImageView comicImg) {
        Intent intent = new Intent(requireContext(), ChapterActivity.class);
        intent.putExtra("name", comics.getName());
        intent.putExtra("image", comics.getImage());
        intent.putExtra("category", comics.getCategory());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) requireContext(), comicImg, "share");
        startActivity(intent, options.toBundle());
    }
}
