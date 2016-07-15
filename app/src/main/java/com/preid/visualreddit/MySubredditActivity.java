package com.preid.visualreddit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySubredditActivity extends AppCompatActivity {
    private List<String> mySubredditNames;
    private SharedPreferences mSharedPrefs;
    private SubListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subreddit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mySubredditNames = new ArrayList<>();
        mSharedPrefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        Set subredditSet = mSharedPrefs.getStringSet(MainActivity.LIST_NAME, null);
        mySubredditNames.addAll(subredditSet);
        Collections.sort(mySubredditNames);

        ListView listView = (ListView) findViewById(R.id.mysub_list_view);
        mAdapter = new SubListViewAdapter(this, mySubredditNames);
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Set<String> newSubredditSet = new HashSet<>();
        newSubredditSet.addAll(mySubredditNames);

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.clear();
        editor.putStringSet(MainActivity.LIST_NAME, newSubredditSet);
        editor.apply();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_subreddit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sub_add) {
            showAddSubDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddSubDialog() {
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);

        Dialog dialog = new AlertDialog.Builder(this)
                .setView(editText)
                .setTitle("Add new subreddit")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String subreddit = String.valueOf(editText.getText());
                        mySubredditNames.add(subreddit);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        dialog.show();
    }

    private class SubListViewAdapter extends ArrayAdapter<String>{

        public SubListViewAdapter(Context context, List<String> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String subredditName = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_subreddit, parent, false);
            }

            final TextView textView = (TextView) convertView.findViewById(R.id.subreddit_name_text);
            textView.setText(subredditName);

            ImageButton button = (ImageButton) convertView.findViewById(R.id.subreddit_delete_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String toRemove = String.valueOf(textView.getText());
                    //remove(toRemove);

                    mySubredditNames.remove(toRemove);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}
