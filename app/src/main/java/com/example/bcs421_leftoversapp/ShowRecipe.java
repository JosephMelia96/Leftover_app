package com.example.bcs421_leftoversapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bcs421_leftoversapp.DataBase.RecipesContract;
import com.example.bcs421_leftoversapp.DataBase.UsersContract;
import com.example.bcs421_leftoversapp.models.Recipe;
import com.example.bcs421_leftoversapp.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowRecipe extends AppCompatActivity implements View.OnClickListener {

    TextView ingredients, title;
    ImageView img;
    GoogleSignInClient mGoogleSignInClient;
    UsersContract mUsersContract;
    RecipesContract mRecipesContract;
    String currentPhotoPath;
    String recTitle;
    static final int REQUEST_TAKE_PHOTO = 1;
    public Uri imageUri;
    Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    final Intent intent = new Intent(Intent.ACTION_SEND);
    Button btn_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipie);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //lock screen to portrait


        ingredients=findViewById(R.id.ingredients);
        title = findViewById(R.id.title);
        img = findViewById(R.id.img);
        btn_share = findViewById(R.id.btn_share);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_home).setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        changeBookmark();

        String recIngr = getIntent().getStringExtra("ingr");
        recTitle = getIntent().getStringExtra("title");
        String recImg = getIntent().getStringExtra("img");
        Glide.with(img).load(String.valueOf(recImg)).placeholder(R.drawable.noimg).into(img);
        if(recIngr.indexOf(",") != -1){
            recIngr = recIngr.replaceAll(",","\n-");
        }
        recIngr = "- " + recIngr;
        title.setText(recTitle);
        ingredients.setText(recIngr);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeBookmark();
    }

    public void recipeSite(View view) {
        String recHref = getIntent().getStringExtra("href");
        Intent toSite = new Intent(Intent.ACTION_VIEW, Uri.parse(recHref));
        startActivity(toSite);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Would you like to share a photo with the recipe?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                //deletePhoto();
                break;
            case R.id.btn_save:
                saveRecipeIntoDatabase();
                    changeBookmark();
                break;
            case R.id.btn_home:
                startActivity(new Intent(this,HomeActivity.class));
        }
    }

    //Create a temp file that will hold the image for sharing
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //method to take a photo using google camera2 intent
    private void takePhoto() {
        // Ensure that there's a camera activity to handle the intent
        if (camIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Photo didn't save", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(camIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    //method to delete shared photo after it's taken
    private void deletePhoto() {
        File dir = new File("/sdcard/Android/data/com.example.bcs421_leftoversapp/files/Pictures");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String shareSubject = "Check This Delicious Recipe Out!";
        String shareBody = getIntent().getStringExtra("title") +
                "\n\n" + getIntent().getStringExtra("href");
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            Intent intentInner = new Intent(Intent.ACTION_SEND);
            intentInner.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(Intent.EXTRA_STREAM, imageUri).setType("image/*");
            intentInner.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intentInner.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentInner.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            intentInner.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intentInner, "Share Using"));
        }
    }

    public void saveRecipeIntoDatabase() {
    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
    this.mUsersContract = new UsersContract(this); //initialize UsersContract
    this.mRecipesContract = new RecipesContract(this); //initialize RecipesContract
    User user = mUsersContract.getParentIdByEmail(acct.getEmail());

    if (!checkForSavedRecipe(user)) {
        mRecipesContract.addRecipe(getIntent().getStringExtra("title"),getIntent().getStringExtra("ingr"),
                getIntent().getStringExtra("img"),getIntent().getStringExtra("href"),user.getID());

        Toast.makeText(this, "Recipe Saved", Toast.LENGTH_SHORT).show();
    }
}

    //check if recipe is saved for user...won't save recipe to prevent duplicates
    public boolean checkForSavedRecipe(User user) {
        findViewById(R.id.btn_saved).setVisibility(View.GONE);
        this.mRecipesContract = new RecipesContract(this); //initialize RecipesContract
        ArrayList<Recipe> savedRecipeList = mRecipesContract.getRecipesOfUser(user.getID());

        for (int i=0;i<savedRecipeList.size();i++) {
            if(savedRecipeList.get(i).getTitle().equals(recTitle)) {
                //Toast.makeText(this, "Recipe Already Saved", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void changeBookmark(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        this.mUsersContract = new UsersContract(this); //initialize UsersContract
        User user = mUsersContract.getParentIdByEmail(acct.getEmail());
        if(checkForSavedRecipe(user)) {
            LinearLayout nonSaved = (LinearLayout) findViewById(R.id.btn_save).getParent();
            nonSaved.removeView(findViewById(R.id.btn_save));
            findViewById(R.id.btn_saved).setVisibility(View.VISIBLE);
        }
    }

    //dialog function to check if user wants to include an image with their recipe share
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String shareSubject = "Check This Delicious Recipe Out!";
            String shareBody = getIntent().getStringExtra("title") +
                    "\n\n" + getIntent().getStringExtra("href");
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    takePhoto();
                    deletePhoto();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                    intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(intent,"Share Using"));
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
