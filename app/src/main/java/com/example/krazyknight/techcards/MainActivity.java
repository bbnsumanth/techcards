package com.example.krazyknight.techcards;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity  {
    RecyclerView recyclerView;
    Toolbar toolbar;

    ArrayList<String> summery = new ArrayList<String>(4);
    ArrayList<String> links = new ArrayList<String>(4);
    ArrayList<String> titles = new ArrayList<String>(4);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        String stringUrl = "https://techcards.wordpress.com";
        Fetch fetch = new Fetch();
        fetch.execute(stringUrl);
    }


    // Fetch AsyncTask
    private class Fetch extends AsyncTask<String, Void, Element> {

        @Override
        protected Element doInBackground(String... params) {
            Element mainContent = null;

            //TODO: before downloading ,check for network connection ,if not available respond pleasantly .
            try {
                // Connect to the web site
                Document doc = Jsoup.connect(params[0]).get();
                mainContent =doc.getElementById("content").getElementById("primary").
                        getElementById("main");

            } catch (IOException e) {
                e.printStackTrace();
            }

            return mainContent;
        }



        protected void onPostExecute(Element result) {

            Elements art = result.getElementsByClass("entry-summary");
            Elements title = result.getElementsByClass("entry-title");




            try{
                for (int i =0;i<art.size() ;i++ ) {

                    Log.v("articles after post executive",art.get(i).toString());
                    links.add(i,art.get(i).getElementsByTag("a").toString());
                    Log.v("links",art.toString());
                    summery.add(i,art.get(i).getElementsByTag("p").text());
                    titles.add(i,title.get(i).text());



                }

            }catch(NullPointerException e){
                e.printStackTrace();
                System.out.println("art is null");
            }

            List dataList = createViewDataList(titles,summery,links);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new MyAdapter(dataList));
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }

    }



    public List createViewDataList(ArrayList<String> titles,ArrayList<String> summery,ArrayList<String> links){
        List result = new ArrayList();

        for (int i=0; i < titles.size(); i++) {
            ViewData data = new ViewData(titles.get(i),summery.get(i),links.get(i));
            result.add(data);
        }

        return result;




    }

    public class ViewData{
        String title;
        String summery;
        String link;

        public ViewData(String title,String summery,String link){
            this.link=link;
            this.summery=summery;
            this.title=title;

        }

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<ViewData> items;


        public MyAdapter(List<ViewData> items) {
            this.items = items;

        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            final ViewData item = items.get(position);
            holder.title.setText(item.title);
            holder.summery.setText(item.summery);
            holder.readMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this,Read.class);
                    i.putExtra("title",item.title);
                    startActivity(i);
                }
            });
        }

        @Override public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView summery;
            public Button readMore;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.textView1);
                summery = (TextView) itemView.findViewById(R.id.textView2);
                readMore = (Button) itemView.findViewById(R.id.button1);

            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.refresh){
            Toast.makeText(this,"You are up to date",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



}
