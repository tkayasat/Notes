package com.example.notes;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.notes.observer.Publisher;
import com.example.notes.ui.SocialNetworkFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    private DrawerLayout drawer;
    private final Publisher publisher = new Publisher();
    private Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = new Navigation(getSupportFragmentManager());

        initToolbar();
        getNavigation().addFragment(SocialNetworkFragment.newInstance(), false);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_about) {
                Snackbar.make(findViewById(R.id.drawer_layout), "About", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.drawer_layout), "Settings", Snackbar.LENGTH_SHORT).show();
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
        toggle.syncState();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Snackbar.make(findViewById(R.id.drawer_layout), item.getTitle().toString(), Snackbar.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public Publisher getPublisher() {
        return publisher;
    }
}