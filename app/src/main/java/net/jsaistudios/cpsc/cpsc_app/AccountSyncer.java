package net.jsaistudios.cpsc.cpsc_app;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AccountSyncer {
    private EmailHolder emailHolder = new EmailHolder();
    public AccountSyncer() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference fbRef = database.getReference("users");
        emailHolder.readyObserver = new Observer2<Boolean>() {
            @Override
            public void update(Boolean x) {
                if (emailHolder.isReady()) {
                    ArrayList<String> unAddedAccounts = new ArrayList<>(emailHolder.getWordPressEmails());
                    unAddedAccounts.removeAll(emailHolder.firebaseEmails);
                    for(String email : unAddedAccounts) {
                        DatabaseReference newFbUser = fbRef.push();
                        newFbUser.child("email").setValue(email);
                        newFbUser.child("name").setValue("New User");
                        newFbUser.child("clearance").setValue("member");
                        newFbUser.child("pass").setValue("skiski");
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, "skiski");
                    }
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
    }
    private void addAllEmailsAuth(final ArrayList<String> list) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(list.get(0), "skiski").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().signOut();
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
}
