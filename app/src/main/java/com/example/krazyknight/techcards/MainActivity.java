package com.example.krazyknight.techcards;

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;
        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.*;
        import org.jsoup.select.Elements;

        import java.io.IOException;
        import java.util.ArrayList;

        import static android.widget.Toast.*;
        import static android.widget.Toast.makeText;


public class MainActivity extends ActionBarActivity implements OnItemClickListener {
    ListView listView1;
    MyAdapter adapter ;

    ArrayList<String> summery = new ArrayList<String>(4);
    ArrayList<String> links = new ArrayList<String>(4);
    ArrayList<String> titles = new ArrayList<String>(4);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView1 = (ListView) findViewById(R.id.listView1);

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


            adapter = new MyAdapter(MainActivity.this);
            listView1.setAdapter(adapter);
            listView1.setOnItemClickListener(MainActivity.this);


        }

    }

    public class MyAdapter extends ArrayAdapter<String>{
        //constructor..should call the super class constructor with one of its valid constructors
        public MyAdapter(Context c){
            super(c,R.layout.single_row,R.id.textView1,links);
        }
        //override this method to make a custom view
        public View getView(final int position,View convertView,ViewGroup parent){
            final int pos = position;


            //why should we use convertView,can't we instead create our own View v,and return it at the end instead of convertView???
            if(convertView == null)
            {

                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.single_row,listView1,false);
            }




            TextView t1 = (TextView) convertView.findViewById(R.id.textView1);
            TextView t2 = (TextView) convertView.findViewById(R.id.textView2);
            Button b1 = (Button) convertView.findViewById(R.id.button1);

            t1.setText(titles.get(position));
            t2.setText(summery.get(position));
            b1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(MainActivity.this,Read.class);
                    i.putExtra("title",titles.get(pos));//or we can create a bundle with the key value pair and put that bundle in extra
                    startActivity(i);

                }

            });



            return convertView;
        }
        /*
        private class ClickHandler implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                //new Toast().makeText(this, "hi", LENGTH_SHORT).show();
            }
        }
        */

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        TextView t = (TextView) view.findViewById(R.id.textView1);
        String text =(String) t.getText();
        Toast.makeText(this, text, LENGTH_SHORT).show();//this makeText is a static method
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
        return super.onOptionsItemSelected(item);
    }



}
