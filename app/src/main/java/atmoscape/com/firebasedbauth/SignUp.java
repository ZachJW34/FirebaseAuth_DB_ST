package atmoscape.com.firebasedbauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    Button createacctBT;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        createacctBT = (Button) findViewById(R.id.createnewacct);

        createacctBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createacctwithEP();
            }
        });
    }

    private void createacctwithEP(){
        EditText emailET = (EditText) findViewById(R.id.newemail);
        String email = emailET.getText().toString();
        EditText passwordET = (EditText) findViewById(R.id.newpassword);
        String password = passwordET.getText().toString();
        EditText passwordET2 = (EditText) findViewById(R.id.newpassword2);
        String password2 = passwordET2.getText().toString();
        if (email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(SignUp.this, "Error: necessary Fields Were Left Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!Objects.equals(password,password2)){
            Toast.makeText(SignUp.this, "Error: Passwords Did Not Match", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d("TAG", "CreateUserWithEmail: Success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignUp.this, SecondOne.class));
                        }
                        else {
                            Log.w("Tag", "CreateUserWithEmail: Failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }



}
