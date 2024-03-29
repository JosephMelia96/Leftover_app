package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.bcs421_leftoversapp.DataBase.UsersContract;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

/*home activity to use swipe-in nav bar from left, change between home (search), account info,
and saved recipes, and to sign out*/
public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    View headerView;
    GoogleSignInClient mGoogleSignInClient;
    UsersContract mUsersContract;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //lock screen to portrait

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        initViews();
        showUserInfo(acct);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);// add listener for navigation drawer
        toggle.syncState();

        //open home fragment on start
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();

            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    private void initViews() {
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
    }

    private void createUserInDatabase(String firstName, String lastName, String email) {
        mUsersContract.createUser(firstName, lastName, email, "");
    }


    //update labels in nav header with user info
    private void showUserInfo(GoogleSignInAccount acct) {

        if (acct != null) {
            TextView nav_username = headerView.findViewById(R.id.nav_userName); //access item by through navHeader
            TextView nav_userEmail = headerView.findViewById(R.id.nav_userEmail);
            ImageView nav_userPicture = headerView.findViewById(R.id.nav_userPicture);

            nav_username.setText(acct.getDisplayName());
            nav_userEmail.setText(acct.getEmail());
            Glide.with(this).load(String.valueOf(acct.getPhotoUrl())).into(nav_userPicture);

            this.mUsersContract = new UsersContract(this); //initialize UsersContract
            String firstName = acct.getGivenName();
            String lastName = acct.getFamilyName();
            String email = acct.getEmail();
            if (mUsersContract.checkForExistingUser(email))
                Log.d("EMAIL", "Email exists");
            else {
                createUserInDatabase(firstName, lastName, email);
                Log.d("EMAIL", "Email does not exist");
            }


        } else if (getIntent().getStringExtra("name") != null) {
            TextView nav_username = headerView.findViewById(R.id.nav_userName); //access item by through navHeader
            TextView nav_userEmail = headerView.findViewById(R.id.nav_userEmail);
            ImageView nav_userPicture = headerView.findViewById(R.id.nav_userPicture);

            nav_username.setText(getIntent().getStringExtra("name"));
            nav_userEmail.setText(getIntent().getStringExtra("email"));
            Glide.with(this).load(String.valueOf(getIntent().getStringExtra("image_url"))).into(nav_userPicture);

            this.mUsersContract = new UsersContract(this); //initialize UsersContract
            String firstName = getIntent().getStringExtra("firstName");
            String lastName = getIntent().getStringExtra("lastName");
            String email = getIntent().getStringExtra("email");
            if (mUsersContract.checkForExistingUser(email))
                Log.d("EMAIL", "Email exists");
            else {
                createUserInDatabase(firstName, lastName, email);
                Log.d("EMAIL", "Email does not exist");
            }

        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HomeActivity.this, "Sign out successful", Toast.LENGTH_LONG).show();
                    }

                });
        Intent mainScreen = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(mainScreen);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_signOut:
                signOut();
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) { //use switch to read what item is pressed and switch fragment accordingly
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_accountSettings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountSettingsFragment()).commit();
                break;
            case R.id.nav_savedRecipe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SavedRecipesFragment()).commit();
                break;
            case R.id.nav_signOut:
                signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); //close navigation drawer after selecting item
        return true;
    }

    //close nav drawer on back press if open
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }


}
