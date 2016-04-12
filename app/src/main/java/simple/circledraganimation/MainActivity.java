package simple.circledraganimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import simple.circledrag.CircleDrag;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        final int radius = 200;
        final View view = findViewById(R.id.view_circle);
        final CircleDrag circleDrag = new CircleDrag();
        circleDrag.init(view, radius);
    }
}
