package com.example.krazyknight.techcards;

        import android.content.Context;
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
        import android.widget.ImageView;
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
    int[] images = {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6, R.drawable.img7,R.drawable.img8};
    MyAdapter adapter ;

    ArrayList<String> summery = new ArrayList<String>(4);
    ArrayList<String> links = new ArrayList<String>(4);
    Elements art;

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
    private class Fetch extends AsyncTask<String, Void, Elements> {

        @Override
        protected Elements doInBackground(String... params) {
            Elements articles = null;

            try {
                // Connect to the web site
                Document doc = Jsoup.connect(params[0]).get();
                Element main =doc.getElementById("content").getElementById("primary").
                        getElementById("main");
                articles = main.getElementsByClass("entry-summary");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO exception");
            }

            return articles;
        }



        protected void onPostExecute(Elements result) {

            art = result;

            try{
                for (int i =0;i<art.size() ;i++ ) {

                    Log.v("articles after post executive",art.get(i).toString());
                    links.add(i,art.get(i).getElementsByTag("a").toString());
                    Log.v("links",art.toString());
                    summery.add(i,art.get(i).getElementsByTag("p").text());

                }

            }catch(NullPointerException e){
                e.printStackTrace();
                System.out.println("art is null");
            }


            adapter = new MyAdapter(MainActivity.this,links,summery,images);
            listView1.setAdapter(adapter);
            listView1.setOnItemClickListener(MainActivity.this);


        }

    }

    public class MyAdapter extends ArrayAdapter<String>{

        int[] images;
        ArrayList<String>  links;
        ArrayList<String> summery;

        public MyAdapter(Context c,ArrayList<String>  links,ArrayList<String>  summery,int[]images){
            super(c,R.layout.single_row,R.id.textView1,links);
            this.images = images;
            this.links = links;
            this.summery = summery;
        }
        public View getView(int position,View convertView,ViewGroup parent){

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.single_row,listView1,false);

            TextView t1 = (TextView) v.findViewById(R.id.textView1);
            TextView t2 = (TextView) v.findViewById(R.id.textView2);
            ImageView v1 = (ImageView) v.findViewById(R.id.imageView1);
            t1.setText(links.get(position));
            t2.setText(summery.get(position));

            v1.setImageResource(images[position]);


            return v;
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        TextView t = (TextView) view.findViewById(R.id.textView2);
        String text =(String) t.getText();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();//this makeText is a static method
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
