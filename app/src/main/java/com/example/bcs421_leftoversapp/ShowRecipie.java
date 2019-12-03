package com.example.bcs421_leftoversapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bcs421_leftoversapp.DataBase.RecipesContract;
import com.example.bcs421_leftoversapp.DataBase.UsersContract;
import com.example.bcs421_leftoversapp.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ShowRecipie extends AppCompatActivity implements View.OnClickListener {

    TextView ingredients, title;
    ImageView img;
    GoogleSignInClient mGoogleSignInClient;
    UsersContract mUsersContract;
    RecipesContract mRecipesContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipie);
        ingredients=findViewById(R.id.ingredients);
        title = findViewById(R.id.title);
        img = findViewById(R.id.img);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        String recIngr = getIntent().getStringExtra("ingr");
        String recTitle = getIntent().getStringExtra("title");
        String recImg = getIntent().getStringExtra("img");
        Glide.with(img).load(String.valueOf(recImg)).placeholder(R.drawable.noimg).into(img);
        if(recIngr.indexOf(",") != -1){
            recIngr = recIngr.replaceAll(",","\n");
        }
        title.setText(recTitle);
        ingredients.setText(recIngr);
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
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareSubject = "Check This Delicious Recipe Out!";
                String shareBody = getIntent().getStringExtra("title") +
                        "\n\n" + getIntent().getStringExtra("href");
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent,"Share Using"));
                break;
            case R.id.btn_save:
                saveRecipeIntoDatabase();
                break;
        }
    }

    public void saveRecipeIntoDatabase() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        this.mUsersContract = new UsersContract(this); //initialize UsersContract
        this.mRecipesContract = new RecipesContract(this); //initialize RecipesContract
        User user = mUsersContract.getParentIdByEmail(acct.getEmail());

        mRecipesContract.addRecipe(getIntent().getStringExtra("title"),getIntent().getStringExtra("ingr"),
                getIntent().getStringExtra("img"),getIntent().getStringExtra("href"),user.getID());

        Toast.makeText(this, "Recipe Saved", Toast.LENGTH_SHORT).show();
    }

}
