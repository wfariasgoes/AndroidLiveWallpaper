package com.br.livewallpaper.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.br.livewallpaper.R;
import com.br.livewallpaper.view.Common.Common;
import com.br.livewallpaper.view.WLPActivity;
import com.br.livewallpaper.view.adapter.MyFragmentAdapter;
import com.br.livewallpaper.databinding.ActivityHomeBinding;
import com.br.livewallpaper.view.fragment.CategoryFragment;
import com.br.livewallpaper.view.fragment.TrendingFragment;
import com.br.livewallpaper.view.fragment.RecenteFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends WLPActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHomeBinding binding;
    private MyFragmentAdapter adapter;
//    private PrincipalAdapter adapter;;
    private int[] tabIcons = {
            R.drawable.ic_event_note_black_24dp,
            R.drawable.ic_explore_black_24dp,
            R.drawable.ic_file_upload_black_24dp
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Common.PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Você precisa aceitar a permissão para baixar a imagem", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.toolbar.setTitle("Papeis de parede");
        setSupportActionBar(binding.toolbar);

        //Request runtime permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
        }

        initView();
    }

    private void initView() {
        adapter = new MyFragmentAdapter(getSupportFragmentManager(), this);
//        adapter = new PrincipalAdapter(getSupportFragmentManager(), this);
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setupViewPager(binding.viewPager);
        setupTabIcons();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupTabIcons() {
//        binding.tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        binding.tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        binding.tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    }

    public void setupViewPager(ViewPager viewPager){
        adapter = new MyFragmentAdapter(getSupportFragmentManager(), this);
        adapter.addFrag(CategoryFragment.getInstance(),"Categoria");
        adapter.addFrag(RecenteFragment.getInstance(this), "Recente");
        adapter.addFrag(TrendingFragment.getInstance(), "Tendências");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText(this, "",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
