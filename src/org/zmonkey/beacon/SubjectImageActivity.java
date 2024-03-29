package org.zmonkey.beacon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import org.zmonkey.beacon.data.Subject;

/**
 * User: corey
 * Date: 3/25/12
 * Time: 8:52 PM
 */
public class SubjectImageActivity extends Activity {
    private Subject subject;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjectimage);

        Bundle b = getIntent().getExtras();
        if (b != null){
            subject = (Subject) b.getSerializable("subject");
        }

        ImageView image = (ImageView) findViewById(R.id.subjectImageImage);
        if (subject != null){
            if (subject.image != null){
                //scale the image to an appropriate size
                int width = image.getWidth();
                Bitmap bitmap = Bitmap.createScaledBitmap(subject.image, width, subject.image.getHeight(), true);
                image.setImageBitmap(bitmap);
            }
            
            TextView t;
            if (subject.name != null){
                t = (TextView) findViewById(R.id.subjectImageName);
                t.setText(subject.bio());
            }
        }

    }
}