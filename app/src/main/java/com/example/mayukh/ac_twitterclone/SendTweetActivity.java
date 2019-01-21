package com.example.mayukh.ac_twitterclone;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSendTweet;
    private Button btnSendTweet,btnViewTweets;
    private ListView listViewTweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
        setTitle("Tweets");
        edtSendTweet = findViewById(R.id.edtSendTweet);
        btnSendTweet = findViewById(R.id.btnSendTweet);
        listViewTweets = findViewById(R.id.listViewTweets);
        btnViewTweets = findViewById(R.id.btnViewTweets);

        btnViewTweets.setOnClickListener(SendTweetActivity.this);


    }
    public void sendTweet(View view){
        ParseObject myTweets = new ParseObject("MyTweets");
        myTweets.put("tweet",edtSendTweet.getText().toString());
        myTweets.put("user",ParseUser.getCurrentUser().getUsername());
        final ProgressDialog progressDialog = new ProgressDialog(SendTweetActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        myTweets.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    FancyToast.makeText(SendTweetActivity.this,ParseUser.getCurrentUser().getUsername()
                            +"'s tweets (" +edtSendTweet.getText().toString()+ ") are send successfully",FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS,true).show();
                }
                else{
                    FancyToast.makeText(SendTweetActivity.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
            final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
            final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this,tweetList,android.R.layout.simple_list_item_2,new String[]{"tweetUserName","tweetValue"},new int[]{android.R.id.text1,android.R.id.text2});
            try{
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweets");
                parseQuery.whereContainedIn("user",ParseUser.getCurrentUser().getList("fanOf"));
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(objects.size() > 0 && e == null){
                        for(ParseObject tweetObject : objects){
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName",tweetObject.getString("user"));
                            userTweet.put("tweetValue",tweetObject.getString("tweet"));
                            tweetList.add(userTweet);

                        }
                        listViewTweets.setAdapter(adapter);
                        }
                    }
                });
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }
}
