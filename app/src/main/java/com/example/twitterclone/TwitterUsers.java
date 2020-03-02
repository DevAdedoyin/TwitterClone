package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList<String> tUsers;
    private ArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        mListView = findViewById(R.id.listViewId);
        tUsers = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, tUsers);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size() > 0 && e == null){
                    for (ParseUser twitterUser : objects){
                        tUsers.add(twitterUser.getUsername());
                    }
                    mListView.setAdapter(mArrayAdapter);

                    for (String twitterUser: tUsers){
                        if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                            if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                mListView.setItemChecked(tUsers.indexOf(twitterUser), true);
                            }
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menu) {
        if (menu.getItemId() == R.id.logout_user){
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
        } else if (menu.getItemId() == R.id.send_tweet){
            Intent intent = new Intent(getApplicationContext(), Send_Tweet.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this, tUsers.get(position) + " is now followed.", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().add("fanOf", tUsers.get(position));
        } else {
            FancyToast.makeText(TwitterUsers.this, tUsers.get(position) + " is now unfollowed.", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                FancyToast.makeText(TwitterUsers.this, "Saved", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
            }
        });
    }
}