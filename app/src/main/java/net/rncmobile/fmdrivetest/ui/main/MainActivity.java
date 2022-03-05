package net.rncmobile.fmdrivetest.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.databinding.ActivityMainBinding;
import net.rncmobile.fmdrivetest.ui.base.BaseActivity;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.mainActivityTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void setUp() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}