package com.example.psymood.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.psymood.Fragments.CameraFragment;
import com.example.psymood.Helpers.SnackbarHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.psymood.Fragments.AudioFragment;
import com.example.psymood.Fragments.HomeFragment;
import com.example.psymood.Fragments.StateFragment;
import com.example.psymood.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class NavigationHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, StateFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, CameraFragment.OnFragmentInteractionListener,AudioFragment.OnFragmentInteractionListener {


    private DrawerLayout drawer;
    private AppBarLayout appBarLayout;
    private BottomNavigationView bottomNav;
    private ImageView linkToSettings;

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_home);

        //Instanciamos el aplication preferences
        //ApplicationPreferences.init(getApplicationContext());

        //Firebase instace and obtain current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Firebase database instance and obtain reference of database.
        //database = FirebaseDatabase.getInstance();
        FirebaseInteractor.initUI();


        //Barra superior que engloba al toobar
        appBarLayout = findViewById(R.id.appBarLayout);


        //Barra con las opciones para abrir el menu y mas cosas
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Barra menu bottom
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return onNavigationBottomItemSelected(menuItem);
            }
        });


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        disableNavigationViewScrollbars(navigationView);
        View header = navigationView.getHeaderView(0);

        linkToSettings = header.findViewById(R.id.linkToSettings);
        linkToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });


        ActionBarDrawerToggle mDrawertoggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawertoggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.GreyTitle));
        drawer.addDrawerListener(mDrawertoggle);
        mDrawertoggle.syncState();


        // initViewNavigation marca en el menu la primera opcion , de forma que cuando lo abres ya esta un item seleccionado.
        initViewNavigation(savedInstanceState, navigationView);
        updateNavHeader();

        //Create method to upload info user in real time data base.
        //uploadInfoUserDataBase();
        FirebaseInteractor.createInfoUserInDatabase();

    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }


    private void initViewNavigation(Bundle savedInstanceState, NavigationView navigationView) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            appBarLayout.setElevation(0);
        }
    }

    //Update the info user in  NavHeader
    private void updateNavHeader() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_user_name);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        navUserName.setText(currentUser.getDisplayName());

        //Usremos Glide para cargar la photo.

        //Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
        Glide.with(this).load(currentUser.getPhotoUrl()).placeholder(R.drawable.ic_astronaut_profile_grey).into(navUserPhoto);
    }

    //flechita de android para atras para cerrar el menu lateral
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    //Menu principal
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_home, menu);
        return true;
    }

    //Menu de opciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //la sombra de la barra superior desaparece cuando iniciamos cada fragment.
        appBarLayout.setElevation(0);

        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, new HomeFragment()).commit();
                bottomNav.setSelectedItemId(item.getItemId());
                break;
            case R.id.nav_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, new StateFragment()).commit();
                bottomNav.setSelectedItemId(item.getItemId());
                break;
            case R.id.nav_camera:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, new CameraFragment()).commit();
                bottomNav.setSelectedItemId(item.getItemId());
                break;
            case R.id.nav_audio:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, new AudioFragment()).commit();
                bottomNav.setSelectedItemId(item.getItemId());
                break;
            case R.id.nav_singOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_terms:
                Intent intentTermsAndConditions = new Intent(getApplicationContext(), TermsAndConditions.class);
                startActivity(intentTermsAndConditions);
                break;
            case R.id.nav_politics:
                Intent intentPolitics = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                startActivity(intentPolitics);
                break;
            case R.id.nav_version:
                Intent intentVersion = new Intent(getApplicationContext(), VersionActivity.class);
                startActivity(intentVersion);
                break;
            default:
                Toast.makeText(this, "Other opcion", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean onNavigationBottomItemSelected(MenuItem menuItem) {
        appBarLayout.setElevation(0);
        Fragment selectFragment = selectFragmentById(menuItem.getItemId());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, selectFragment).commit();
        return true;
    }

    private Fragment selectFragmentById(int itemId) {
        Fragment selectFragment = null;

        switch (itemId) {
            case R.id.nav_home:
                selectFragment = new HomeFragment();
                break;
            case R.id.nav_add:
                selectFragment = new StateFragment();
                break;
            case R.id.nav_camera:
                selectFragment = new CameraFragment();
                break;
            case R.id.nav_audio:
                selectFragment = new AudioFragment();
                break;
        }
        return selectFragment;
    }

    @Override
    public void onFragmentInteraction(int elevation) {
        if (appBarLayout != null) {
            appBarLayout.setElevation(elevation);
        }
    }

    @Override
    public void onChangeFragment(int menuItem) {
        if (menuItem != -1) {
            Fragment selectFragment = selectFragmentById(menuItem);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, selectFragment).commit();
            bottomNav.setSelectedItemId(menuItem);
        }
    }

    @Override
    public void showMessageFragmentInHome(String message) {
        View view = this.findViewById(R.id.drawer_layout);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        SnackbarHelper.configSnackbar(view.getContext(), snackbar);
        snackbar.show();
    }
}
