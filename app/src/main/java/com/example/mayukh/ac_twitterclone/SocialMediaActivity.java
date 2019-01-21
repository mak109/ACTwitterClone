package com.example.mayukh.ac_twitterclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;
    String followedUser = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        setTitle("Twitter Users");
        //List View to contain lists of users
        listView = findViewById(R.id.listView);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(SocialMediaActivity.this,android.R.layout.simple_list_item_checked, tUsers);
        //This line of code is important and is required to retain the following and un following
        // users when current user restart the app
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(SocialMediaActivity.this);


        try {
            //Query of all connected users except the current user
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseUser twitterUser : objects) {
                            tUsers.add(twitterUser.getUsername());
                        }
                        listView.setAdapter(adapter);

                        //Iterating over the users
                        // which are connected and checking those users which are in the fanOf list of the current list i.e.
                        //those users which are being followed by the current user
                        for(String twitterUser : tUsers){
                            //the app will crash if we don't add this line
                            if(ParseUser.getCurrentUser().getList("fanOf") != null) {
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                    followedUser = followedUser + twitterUser + "\n";
                                    listView.setItemChecked(tUsers.indexOf(twitterUser), true);

                                    FancyToast.makeText(SocialMediaActivity.this,ParseUser.getCurrentUser().getUsername()+
                                    " is following "+followedUser,FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                                }

                            }
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//        if(ParseUser.getCurrentUser()!=null)
//            ParseUser.getCurrentUser().logOut();
//            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOutUserItem:
                ParseUser.getCurrentUser().logOut();
                finish();
                Intent intent = new Intent(SocialMediaActivity.this,SignUp.class);
                startActivity(intent);
                break;
            case R.id.sendTweet:
                //Transit to send tweet activity
                Intent intent1 = new Intent(SocialMediaActivity.this,SendTweetActivity.class);
                startActivity(intent1);
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    //following and un following users
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView)view;
        if(checkedTextView.isChecked()){
            FancyToast.makeText(SocialMediaActivity.this,tUsers.get(position)+" is followed! ",
                    Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
            //adding the user to fanOf list
            ParseUser.getCurrentUser().add("fanOf",tUsers.get(position));
        }
        else {
            FancyToast.makeText(SocialMediaActivity.this,tUsers.get(position)+" is un-followed! ",
                    Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
            //Removing the user from fanOf list is not as easy as adding
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOfList);

        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    FancyToast.makeText(SocialMediaActivity.this,"Saved Successfully",
                            Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
                else {
                    FancyToast.makeText(SocialMediaActivity.this,e.getMessage(),
                            Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
            }
        });

    }
}
