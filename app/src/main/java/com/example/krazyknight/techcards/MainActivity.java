package com.example.krazyknight.techcards;

        import android.app.ProgressDialog;
        import android.os.AsyncTask;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;
        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.*;
        import org.jsoup.select.Elements;

        import java.io.IOException;
        import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements OnItemClickListener {
    ListView listView1;
    String[] data = {"sunday","monday","tuesday","wednesday","thursday","friday","saturday","sunday","monday","tuesday",
            "wednesday","thursday","friday","saturday","sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
    ArrayAdapter<String> adapter ;
    ArrayList<String> summery;
    ArrayList<String> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.single_row,listView1,false);

        Fetch fetch = new Fetch();
        fetch.execute();

        listView1 = (ListView) findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this,R.layout.single_row,R.id.textView2,data);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        TextView t = (TextView) view.findViewById(R.id.textView2);
        String text =(String) t.getText();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();//this makeText is a static method
    }

    // Fetch AsyncTask
    private class Fetch extends AsyncTask<Void, Void, Void> {
        Elements articles;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Android Basic JSoup Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document doc = Jsoup.connect("http://techcards.wordpress.com").get();
                Element main =doc.getElementById("content").getElementById("primary").
                        getElementById("main");

                Log.v("main",main.text());

                articles = main.getElementsByClass("entry-summary");
                Log.v("articles",articles.text());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int i =0;i < articles.size() ;i++ ){
                //links.add(i,articles.get(i).getElementsByTag("a").toString());
                summery.add(i,articles.get(i).getElementsByTag("p").text());

            }

            mProgressDialog.dismiss();
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
        return super.onOptionsItemSelected(item);
    }


}
