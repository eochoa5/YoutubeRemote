package com.example.edwin.youtuberemote;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Video> videoList = new ArrayList<Video>();
    private ListAdapter adapter;
    private String urlToScrape;
    private boolean searching = false;
    private TextView mTextView;
    private ListView mListView;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextView = (TextView) findViewById(R.id.textView);
        adapter = new ListAdapter(MainActivity.this, videoList);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(adapter);

        String content = "1. Search and choose a YouTube video." +
                " \n2. Go to https://youtr.herokuapp.com on your computer and enter the given key in the app." +
                " \n3. You can now control the video from your phone";

        SpannableStringBuilder str = new SpannableStringBuilder(content);
        int startPos = content.indexOf("https://youtr.herokuapp.com");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                startPos, startPos+27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.setText(str);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem searchViewMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchViewMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searching) {
                    mTextView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    videoList.clear();
                    adapter.notifyDataSetChanged();
                    search(query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){

            @Override
            public boolean onClose() {
                mListView.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        return true;
    }

    private void search(String q){
        String query = q.replace(' ', '+');
        urlToScrape = "https://www.youtube.com/results?search_query="+query;
        searching = true;
        new Scraper().execute();

    }

    // Scraper AsyncTask

    private class Scraper extends AsyncTask<Void, Void, Void> {
        Document document;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Please wait");
            mProgressDialog.setMessage("Searching YouTube...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                document = Jsoup.connect(urlToScrape).get();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            for (Element row : document.select("div.yt-lockup-content")) {

                final String duration = row.select("h3.yt-lockup-title span").text();

                if (duration.contains("Duration")) {
                    final String title = row.select("h3.yt-lockup-title a").text();
                    final String url = row.select("h3.yt-lockup-title a").attr("href");
                    final String uploader = row.select("div.yt-lockup-byline a").text();
                    final String views = row.select("ul.yt-lockup-meta-info li").size() > 0 ?
                            row.select("ul.yt-lockup-meta-info li").get(1).text() : "0";

                    Video vid = new Video(title, url, views, uploader, duration);

                    videoList.add(vid);
                    adapter.notifyDataSetChanged();

                }

            }
            mProgressDialog.dismiss();
            searching = false;

        }
    }

}




