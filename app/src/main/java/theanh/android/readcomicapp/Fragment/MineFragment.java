package theanh.android.readcomicapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import theanh.android.readcomicapp.ChangePwdActivity;
import theanh.android.readcomicapp.Object.Reader;
import theanh.android.readcomicapp.R;
import theanh.android.readcomicapp.SignInActivity;
import theanh.android.readcomicapp.UploadAvatarActivity;

public class MineFragment extends Fragment {

    private CircleImageView imgMineAvatar;
    private ProgressBar progressBar;
    private TextView txvMineUserName, txvMineUserEmail, txvChangePwd, txvLogOut;
    private String username, email;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        txvMineUserName = view.findViewById(R.id.userName);
        txvMineUserEmail = view.findViewById(R.id.userEmail);
        txvChangePwd = view.findViewById(R.id.mineChangePwd);
        txvLogOut = view.findViewById(R.id.userLogOut);

        progressBar = view.findViewById(R.id.progessBar);

        imgMineAvatar = view.findViewById(R.id.userImage);
        imgMineAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), UploadAvatarActivity.class);
                startActivity(intent);
            }
        });

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(requireContext(), "Something went wrong, user's details are not available at the moment!", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            displayUserDetails(firebaseUser);
        }

        changePwd();
        logOut();
        return view;
    }

    private void displayUserDetails(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("User");
        profileRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Reader reader = snapshot.getValue(Reader.class);
                if (reader != null) {
                    username = reader.getUserName();
                    email = reader.getUserEmail();


                    txvMineUserName.setText(username);
                    txvMineUserEmail.setText(email);
                    Uri uri = firebaseUser.getPhotoUrl();
                    Glide.with(requireContext()).load(uri).into(imgMineAvatar);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePwd() {
        txvChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), ChangePwdActivity.class);
                startActivity(intent);
            }
        });
    }


    private void logOut() {
        txvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

}
