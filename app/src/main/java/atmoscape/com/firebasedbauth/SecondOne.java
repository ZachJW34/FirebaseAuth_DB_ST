package atmoscape.com.firebasedbauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SecondOne extends AppCompatActivity {
    Button logoutBt;
    Button cameraBT;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseFirestore mDocRef;
    TextView mQuoteTextView;
    FirebaseUser user;
    String nameofUser;
    public static final String AUTHOR_KEY = "author";
    public static final String QUOTE_KEY = "quote";
    public static final String TAG = "Inspiring Quote";


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_one);

        logoutBt = (Button) findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        mDocRef = FirebaseFirestore.getInstance();
        mQuoteTextView = (TextView) findViewById(R.id.fetchTV);
        user = mAuth.getCurrentUser();
        nameofUser = user.getUid();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(SecondOne.this, MainActivity.class));
                }
            }
        };
        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        cameraBT = (Button) findViewById(R.id.toCameraBT);
        cameraBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondOne.this, Main2Activity.class));
            }
        });
    }

    public void saveQuote(View view){
        EditText quoteView = (EditText) findViewById(R.id.quoteET);
        EditText authorView = (EditText) findViewById(R.id.nameET);
        String quoteText = quoteView.getText().toString();
        String authorText = authorView.getText().toString();
        user = mAuth.getCurrentUser();

        if (quoteText.isEmpty() || authorText.isEmpty()){return;}
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(QUOTE_KEY, quoteText);
        dataToSave.put(AUTHOR_KEY, authorText);
        mDocRef.collection(nameofUser)
                .document("InspiringQuote").set(dataToSave);
    }

    public void fetchQuote(View view){
        mDocRef.collection(nameofUser).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String, Object> printmess = document.getData();
                                String data = "Quote= " + printmess.get("quote").toString() +
                                        " :  Author= " + printmess.get("author").toString();
                                mQuoteTextView.setText(data);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
