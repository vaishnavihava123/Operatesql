package com.example.operatesql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddMovieActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_MOVIE_NAME = "movie_name";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_YEAR = "year";
    private static final String KEY_RATING = "rating";
    private static final String BASE_URL = "http://urmilacreation.orgfree.com/test_data/";
    private static String STRING_EMPTY = "";
    private EditText movieNameEditText, genreEditText, yearEditText, ratingEditText;
    private String movieName,genre, year, rating;
    private Button addButton;
    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        movieNameEditText = findViewById(R.id.txtMovieNameAdd);
        genreEditText =  findViewById(R.id.txtGenreAdd);
        yearEditText = findViewById(R.id.txtYearAdd);
        ratingEditText = findViewById(R.id.txtRatingAdd);
        addButton =  findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check internet permission..here we gives validation for running apps
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addMovie();
                } else {
                    Toast.makeText(AddMovieActivity.this,"Unable to connect to internet", Toast.LENGTH_LONG).show();


                }

            }
        });

    }

    /**
     * Checks whether all files are filled. If so then calls AddMovieAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    private void addMovie() {
        if (!STRING_EMPTY.equals(movieNameEditText.getText().toString()) &&
                !STRING_EMPTY.equals(genreEditText.getText().toString()) &&
                !STRING_EMPTY.equals(yearEditText.getText().toString()) &&
                !STRING_EMPTY.equals(ratingEditText.getText().toString())) {

            movieName = movieNameEditText.getText().toString();
            genre = genreEditText.getText().toString();
            year = yearEditText.getText().toString();
            rating = ratingEditText.getText().toString();
            new AddMovieAsyncTask().execute();
        } else {
            Toast.makeText(AddMovieActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }


    }

    /**
     * AsyncTask for adding a movie
     */
    private class AddMovieAsyncTask extends AsyncTask<String, String, String> {
        //before execution show to user some process doing in background
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(AddMovieActivity.this);
            pDialog.setMessage("Adding Movie. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        //add data into table in background
        @Override
        protected String doInBackground(String... params) {   //when parameters are change dyanamically

            //map the values through key and value pairs
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_MOVIE_NAME, movieName);
            httpParams.put(KEY_GENRE, genre);
            httpParams.put(KEY_YEAR, year);
            httpParams.put(KEY_RATING, rating);
            //added data into table

            //JSONObject: A modifiable set of name/value mappings. Names are unique, non-null strings. Values may be any mix of JSONObject , JSONArray , Strings, Booleans, Integers, Longs, Doubles or NULL . Values may not be null, Double#isNaN() , Double#isInfinite() , or of any type not listed here.
            //This class can coerce values to another type when requested.

            //make HttpRequest of JSON Parser
           JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_movie.php", "POST", httpParams);






            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(AddMovieActivity.this,
                                "Movie Added", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        finish();

                    } else {
                        Toast.makeText(AddMovieActivity.this,
                                "Some error occurred while adding movie",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}