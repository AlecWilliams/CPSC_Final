package net.jsaistudios.cpsc.cpsc_app;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.jsaistudios.cpsc.cpsc_app.Dialogs.BasicAlertDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alec on 9/2/2018.
 */

public class Login extends Fragment {

    private EditText emailLogin, passwordLogin;
    private EditText adminPin;
    private View loginButton, adminLoginButton, signupButton;
    private String enteredEmail;
    private TextView login2, forgotEmail;
    private VideoView vw;
    private boolean paused=false;

    public Context context;
    public Activity activity;
    private FirebaseAuth firebaseAuth;
    public LoginListener loginListener;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View progressBar;
    View baseFragmentView;
    public FragmentManager fragmentManager;
    private EmailHolder emailHolder = new EmailHolder();

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
        forgotEmail = baseFragmentView.findViewById(R.id.forgot_email);

        forgotEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "webmaster@cspsconline.com");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Forgot CPSC Email :(");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear almighty and extremely handsome webmaster,\n" +
                        "\n" +
                        "I don't know what my registered email is, which is ironic considering I'm sending you an email right now.\n" +
                        "\n" +
                        "Love,\n" +
                        "Helpless, lonely soul\n" +
                        "\n" +
                        "P.S. Did I mention that you're extremely handsome?");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                }
            }
        });

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

        emailLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailLogin.setBackgroundResource(R.drawable.edit_text_background);
                emailLogin.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray1));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://cpsconline.com/join/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference fbRef = database.getReference("users");

        emailHolder.readyObserver = new Observer2<Boolean>() {
            @Override
            public void update(Boolean x) {
                if (emailHolder.isReady()) {
                    ArrayList<String> unAddedAccounts = new ArrayList<>(emailHolder.wordPressEmails);
//                    unAddedAccounts.removeAll(emailHolder.firebaseEmails);
//                    for(String email : unAddedAccounts) {
//                        DatabaseReference newFbUser = fbRef.push();
//                        newFbUser.child("email").setValue(email);
//                        newFbUser.child("name").setValue("New User");
//                        newFbUser.child("clearance").setValue("member");
//                        newFbUser.child("pass").setValue("skiski");
//                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, "skiski");
//                    }
                    if(unAddedAccounts.size()>0)
                        addAllEmailsAuth(unAddedAccounts);
                }
            }
        };
        fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for(DataSnapshot snap :dataSnapshot.getChildren()) {
                     emailHolder.addFirebaseEmail(snap.child("email").getValue().toString().toLowerCase().trim());
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

        final DatabaseReference wpRef = database.getReference("wpusersmasterSheet");
        wpRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap :dataSnapshot.getChildren()) {
                    emailHolder.addWordPressEmail(snap.child("1").getValue().toString().toLowerCase().trim());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
        });
        return baseFragmentView;
    }
    private void addAllEmailsAuth(final ArrayList<String> list) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(list.get(0), "skiski").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().signOut();
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    addAllEmailsAuth(new ArrayList<>(list.subList(1, list.size())));
                }
            }
        });
    }
    class EmailHolder {
        private ArrayList<String> firebaseEmails = new ArrayList<>(), wordPressEmails = new ArrayList<>();
        private boolean ready=false;
        public Observer2<Boolean> readyObserver;
        public ArrayList<String> getFirebaseEmails() {
            return firebaseEmails;
        }

        public void setFirebaseEmails(ArrayList<String> firebaseEmails) {
            this.firebaseEmails = firebaseEmails;
        }

        public void addFirebaseEmail(String firebaseEmail) {
            if(firebaseEmails.size()>0&&wordPressEmails.size()>0) {
                setReady(true);
            }
            this.firebaseEmails.add(firebaseEmail);
        }

        public ArrayList<String> getWordPressEmails() {
            return wordPressEmails;
        }

        public void setWordPressEmails(ArrayList<String> wordPressEmails) {
            this.wordPressEmails = wordPressEmails;
        }

        public void addWordPressEmail(String wordPressEmail) {
            if(firebaseEmails.size()>0&&wordPressEmails.size()>0) {
                setReady(true);
            }
            this.wordPressEmails.add(wordPressEmail);
        }

        public boolean isReady() {
            return ready;
        }

        public void setReady(boolean ready) {
            if(readyObserver!=null) {
                readyObserver.update(ready);
            }
            this.ready = ready;
        }
    }
    private void createUser(String enteredEmail) {
        try {
            final String myEmail = enteredEmail.toLowerCase();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("users");
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(myEmail, "skiski").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(myEmail);
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue("New User");
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("clearance").setValue("member");
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pass").setValue("skiski");
                    loginListener.loggedIn(false, false);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {

        }
    }

    private void updateUserList() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference wpRef = database.getReference("wpusersmasterSheet");
        wpRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot wpSnap) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("users");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot fbSnap) {
                        String em = "";
                        ArrayList<String> listOfWpUsers = new ArrayList<>();
                        Outside:
                        for(DataSnapshot snapshot : wpSnap.getChildren()) {
                            boolean found =false;
                            listOfWpUsers.add(snapshot.child("email").getValue().toString());
                        }
                        for(DataSnapshot fbSnapshot: fbSnap.getChildren()) {
                            if(listOfWpUsers.contains(fbSnapshot.child("email").getValue().toString())) {
                                em = fbSnapshot.child("email").getValue().toString();
                            }
                        }
//                        if(!found) {
//                            found=false;
//                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            final DatabaseReference myRef = database.getReference("users").push();
//                            myRef.child("email").setValue(em);
//                            myRef.child("name").setValue("New User");
//                            myRef.child("clearance").setValue("member");
//                            myRef.child("pass").setValue("skiski");
//                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(em, "skiski");
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



