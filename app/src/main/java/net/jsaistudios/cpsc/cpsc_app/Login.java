package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alec on 9/2/2018.
 */

public class Login extends Fragment {

    private EditText emailLogin, passwordLogin;
    private EditText adminPin;
    private View loginButton, adminLoginButton, signupButton;
    private String enteredEmail;
    private TextView login2;
    private VideoView vw;

    public Context context;
    public Activity activity;
    private FirebaseAuth firebaseAuth;
    public LoginListener loginListener;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View progressBar;
    View baseFragmentView;

    public interface LoginListener {
        void loggedIn(boolean isAdmin, boolean newUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseFragmentView = inflater.inflate(R.layout.login_page, container, false);

        emailLogin = (EditText) baseFragmentView.findViewById(R.id.loginEmailEditText);
        passwordLogin = (EditText) baseFragmentView.findViewById(R.id.loginPasswordEditText);
        login2 = baseFragmentView.findViewById(R.id.loginButton2);

        vw = baseFragmentView.findViewById(R.id.viewViewBg);
        vw.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/cpscdatabase.appspot.com/o/skiflip1.mp4?alt=media&token=e065ce9f-9e3a-402b-a38e-c23c52ac1fff"));
        vw.start();
        vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vw.start();
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) vw.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        vw.setLayoutParams(params);

        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = baseFragmentView.findViewById(R.id.loginButton);
        signupButton = baseFragmentView.findViewById(R.id.signupButton);
        progressBar = baseFragmentView.findViewById(R.id.progressBar);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://cpsconline.com/join/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailLogin.getText().toString().toLowerCase().trim();
                if (passwordLogin.getText().toString().equals("ski")) {
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snap: dataSnapshot.getChildren()) {
                                if(snap.child("email").getValue(String.class).toLowerCase().trim().equals(email)) {
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, snap.child("pass").getValue().toString())
                                            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        progressBar.setVisibility(View.GONE);
                                                        loginListener.loggedIn(false, false);
                                                    } else {
                                                        progressBar.setVisibility(View.GONE);
                                                        emailLogin.setBackgroundResource(R.drawable.edit_text_background_border);
                                                        emailLogin.setBackgroundTintList(null);

                                                    }
                                                }
                                            });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String email = emailLogin.getText().toString().toLowerCase();
               emailLogin.setBackgroundResource(R.drawable.edit_text_background);
               emailLogin.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray1));
               FirebaseDatabase database = FirebaseDatabase.getInstance();
               DatabaseReference myRef = database.getReference("users");
               progressBar.setVisibility(View.VISIBLE);
               myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       for(DataSnapshot snap :dataSnapshot.getChildren()) {
                           if (snap.child("email").getValue().toString().equals(email)) {
                               if (snap.child("clearance").getValue().toString().equals("admin")) {
                                   passwordLogin.setVisibility(View.VISIBLE);
                                   passwordLogin.requestFocus();
                                   login2.setVisibility(View.VISIBLE);
                                   progressBar.setVisibility(View.GONE);
                               } else {
                                   FirebaseAuth.getInstance().signInWithEmailAndPassword(email, "Skimania")
                                           .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if (task.isSuccessful()) {
                                                       loginListener.loggedIn(false, false);
                                                       progressBar.setVisibility(View.GONE);
                                                   } else {
                                                       progressBar.setVisibility(View.GONE);
                                                       emailLogin.setBackgroundResource(R.drawable.edit_text_background_border);
                                                       emailLogin.setBackgroundTintList(null);
                                                   }
                                               }
                                           });
                               }
                           }
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError error) {
                       // Failed to read value
                   }
               });
           }
        }) ;
        return baseFragmentView;

    }
}



