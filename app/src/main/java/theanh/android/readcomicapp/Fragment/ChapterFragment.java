package theanh.android.readcomicapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import theanh.android.readcomicapp.Adapter.ChapterAdapter;
import theanh.android.readcomicapp.ChapterInfoActivity;
import theanh.android.readcomicapp.Object.Chapters;
import theanh.android.readcomicapp.Object.Comics;
import theanh.android.readcomicapp.R;

public class ChapterFragment extends Fragment {

    private TextView chapterName;
    private RecyclerView rcvChapter;
    private ChapterAdapter chapterAdapter;
    private List<Chapters> chaptersList;

    private DatabaseReference comicRef;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);

        chapterName = view.findViewById(R.id.chapterName);
        rcvChapter = view.findViewById(R.id.rcvChapters);
        comicRef = FirebaseDatabase.getInstance().getReference("Comics");
        rcvChapter.setHasFixedSize(true);
        rcvChapter.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rcvChapter.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        comicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chaptersList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comics comics = dataSnapshot.getValue(Comics.class);
                    DataSnapshot ds = dataSnapshot.child("chapters");
                    for (DataSnapshot dataChap : ds.getChildren()) {
                        Chapters chapters = dataChap.getValue(Chapters.class);
                        chaptersList.add(chapters);
                    }
                }
                chapterAdapter = new ChapterAdapter(requireContext(), chaptersList, new ChapterAdapter.onChapterClick() {
                    @Override
                    public void onItemClick(Chapters chapters) {
                        showChapterInfo(chapters);
                    }
                });
                rcvChapter.setAdapter(chapterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void showChapterInfo(Chapters chapters) {
        Intent intent = new Intent(requireContext(), ChapterInfoActivity.class);
        intent.putExtra("chaptername", chapters.getName());
        startActivity(intent);
    }
}
