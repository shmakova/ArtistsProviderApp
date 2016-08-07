package ru.shmakova.artistsapp.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import ru.shmakova.artistsapp.App;
import ru.shmakova.artistsapp.R;

public class MainActivity extends BaseActivity {
    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).applicationComponent().inject(this);
        setContentView(getLayoutInflater().inflate(R.layout.activity_main, null));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
