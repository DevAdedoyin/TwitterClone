package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class Send_Tweet extends AppCompatActivity implements View.OnClickListener {

    private Button btnVout;
    private EditText edtTextTweet;
    private ListView Vout_ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtTextTweet = findViewById(R.id.edt_send_tweet);
        btnVout = findViewById(R.id.btn_vout);
        Vout_ListView = findViewById(R.id.user_tweets_listview);
        btnVout.setOnClickListener(this);
    }

    public void sendTweet(View view){
        ParseObject parseObject = new ParseObject("MyTweet");
        parseObject.put("tweet", edtTextTweet.getText().toString());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Tweet...");
        progressDialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    FancyToast.makeText(Send_Tweet.this, "Tweet sent successfully.", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                }else {
                    FancyToast.makeText(Send_Tweet.this, "Tweet not sent... \n Resend tweet...", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
            }
        });
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(Send_Tweet.this, tweetList, android.R.layout.simple_list_item_2, new String[]{"tweetUserName", "tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseObject tweetObject : objects){
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweetList.add(userTweet);
                        }
                        Vout_ListView.setAdapter(adapter);
                    } else{
                        FancyToast.makeText(Send_Tweet.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}