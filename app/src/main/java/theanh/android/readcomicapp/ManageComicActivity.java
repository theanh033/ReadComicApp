package theanh.android.readcomicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import theanh.android.readcomicapp.Adapter.ComicsUpdateAdapter;
import theanh.android.readcomicapp.Object.Comics;

public class ManageComicActivity extends AppCompatActivity {
    private EditText edtComicName, edtComicImage, edtComicCategory;
    private Button btnAddComic, btnDisplayComic, btnLogOut;
    private RecyclerView rcvDisplayComic;
    long id = 0;
    private ComicsUpdateAdapter comicsUpdateAdapter;
    private List<Comics> comicsList;
    private DatabaseReference comicRef;
//    private DatabaseReference marvelRef;
//    private DatabaseReference dcRef;
//    private DatabaseReference otherRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_comic);
        getSupportActionBar().setTitle("Manage Comic");

        edtComicName = findViewById(R.id.addComicName);
        edtComicImage = findViewById(R.id.addComicImageUrl);
        edtComicCategory = findViewById(R.id.addComicCategory);

        rcvDisplayComic = findViewById(R.id.rcvDisplayComic);

        comicRef = FirebaseDatabase.getInstance().getReference("Comics");
        comicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    id = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageComicActivity.this, "Empty data", Toast.LENGTH_SHORT).show();
            }
        });

//        marvelRef = FirebaseDatabase.getInstance().getReference("MarvelComics");
//        dcRef = FirebaseDatabase.getInstance().getReference("DCComics");
//        otherRef = FirebaseDatabase.getInstance().getReference("OtherComics");

        btnAddComic = findViewById(R.id.addComic);
        btnAddComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comicId = String.valueOf(id+1);
                String comicName = edtComicName.getText().toString().trim();
                String comicImage = edtComicImage.getText().toString().trim();
                String comicCategory = edtComicCategory.getText().toString().trim();

                Comics comics = new Comics(comicId, comicName, comicImage, comicCategory);
                addComic(comics);
            }
        });

        comicsList = new ArrayList<>();
        comicsUpdateAdapter = new ComicsUpdateAdapter(ManageComicActivity.this, comicsList, new ComicsUpdateAdapter.onClick() {
            @Override
            public void onClickUpdate(Comics comics) {
                updateComic(comics);
            }

            @Override
            public void onClickDelete(Comics comics) {
                deleteComic(comics);
            }

//            @Override
//            public void onClickDelete(Comics comics, MarvelComics marvelComics, DcComics dcComics, OtherComic otherComic) {
//                deleteComic(comics, marvelComics, dcComics, otherComic);
//            }
        });
        rcvDisplayComic.setAdapter(comicsUpdateAdapter);
        btnDisplayComic = findViewById(R.id.displayComic);
        btnDisplayComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayComic();
                btnDisplayComic.setEnabled(false);
                btnDisplayComic.setBackgroundTintList(ContextCompat.getColorStateList(ManageComicActivity.this, R.color.gray));
            }
        });

        btnLogOut = findViewById(R.id.logOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    private void addComic(Comics comics) {
        comicRef.child(String.valueOf(id+1)).setValue(comics, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(ManageComicActivity.this, "Add successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void addMarvelComic(MarvelComics marvelComics) {
//        marvelRef.child(String.valueOf(id+1)).setValue(marvelComics);
//    }
//
//    private void addDCComic(DcComics dcComics) {
//        dcRef.child(String.valueOf(id+1)).setValue(dcComics);
//    }
//
//    private void addOtherComic(OtherComic otherComic) {
//        otherRef.child(String.valueOf(id+1)).setValue(otherComic);
//    }

    private void displayComic() {
        rcvDisplayComic.setLayoutManager(new GridLayoutManager(ManageComicActivity.this, 3, LinearLayoutManager.HORIZONTAL, false));
        comicRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comics comics = snapshot.getValue(Comics.class);
                if (comics != null) {
                    comicsList.add(comics);
                }
                comicsUpdateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comics comics = snapshot.getValue(Comics.class);
                if (comics == null || comicsList == null || comicsList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < comicsList.size(); i++) {
                    if (comics.getId() == comicsList.get(i).getId()) {
                        comicsList.set(i, comics);
                    }
                }
                comicsUpdateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Comics comics = snapshot.getValue(Comics.class);
                if (comics == null || comicsList == null || comicsList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < comicsList.size(); i++) {
                    if (comics.getId() == comicsList.get(i).getId()) {
                        comicsList.remove(comicsList.get(i));
                        break;
                    }
                }
                comicsUpdateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateComic(Comics comics) {
        final Dialog dialog = new Dialog(ManageComicActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextView txvComicName = dialog.findViewById(R.id.txvComicName);
        EditText edtUpdateName = dialog.findViewById(R.id.updateName);
        EditText edtUpdateCategory = dialog.findViewById(R.id.updateCategory);
        EditText edtUpdateImage = dialog.findViewById(R.id.updateImageUrl);

        txvComicName.setText(comics.getName());
        edtUpdateName.setText(comics.getName());
        edtUpdateImage.setText(comics.getImage());
        edtUpdateCategory.setText(comics.getCategory());

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edtUpdateName.getText().toString().trim();
                String newImage = edtUpdateImage.getText().toString().trim();
                String newCategory = edtUpdateCategory.getText().toString().trim();

                if (!TextUtils.isEmpty(newName)) {
                    comics.setName(newName);
                }
                if (!TextUtils.isEmpty(newImage)) {
                    comics.setImage(newImage);
                }
                if (!TextUtils.isEmpty(newCategory)) {
                    comics.setCategory(newCategory);
                }

                comicRef.child(String.valueOf(comics.getId())).updateChildren(comics.map(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ManageComicActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void deleteComic(Comics comics) {
        new AlertDialog.Builder(ManageComicActivity.this)
                .setTitle("Delete Comic")
                .setMessage("You sure ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        comicRef.child(String.valueOf(comics.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(ManageComicActivity.this, "Delete successful", Toast.LENGTH_SHORT).show();
                            }
                        });
//                        if (comics.getCategory().equals("Marvel Comic")) {
//                            marvelRef.removeValue();
//                        }
//                        if (comics.getCategory().equals("Marvel Comic")) {
//                            dcRef.removeValue();
//                        }
//                        if (comics.getCategory().equals("Marvel Comic")) {
//                            otherRef.removeValue();
//                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logOut() {
        new AlertDialog.Builder(ManageComicActivity.this)
                .setTitle("Log out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Toast.makeText(ManageComicActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ManageComicActivity.this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}