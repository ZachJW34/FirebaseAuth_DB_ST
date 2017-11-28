package atmoscape.com.firebasedbauth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewPic extends AppCompatActivity {

    Button backbutton;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pic);
        backbutton = (Button) findViewById(R.id.backBT);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewPic.this, Main2Activity.class));
            }
        });
        setPic();

    }

    private void setPic() {
        pic = (ImageView) findViewById(R.id.imageView);
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        StorageReference Imageref = storageRef.child(user.getUid()+"/images/test.jpg");
        final long ONE_MEGABYTE = 1024*1024;
        Imageref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bm = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);
                pic.setImageBitmap(bm);
            }
        });

    }

}
