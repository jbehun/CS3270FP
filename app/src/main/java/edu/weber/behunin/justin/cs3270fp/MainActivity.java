package edu.weber.behunin.justin.cs3270fp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().replace(R.id.frag1, new SignInFragement(), "FragSignIn")
                .addToBackStack(null).commit();
    }
}
