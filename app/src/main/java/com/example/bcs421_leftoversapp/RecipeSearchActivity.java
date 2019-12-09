package com.example.bcs421_leftoversapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bcs421_leftoversapp.adapters.RecipeSearchResultAdapter;
import com.example.bcs421_leftoversapp.service.recipe.RecipeService;
import com.example.bcs421_leftoversapp.service.recipe.recipepuppy.RecipePuppyService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Main application {@link android.app.Activity} which presents the user with a search view,
 * allowing them to find a recipe containing a specific term.
 */
public class RecipeSearchActivity extends AppCompatActivity
//        implements SearchView.OnQueryTextListener
{

}
