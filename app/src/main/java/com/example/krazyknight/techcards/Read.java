package com.example.krazyknight.techcards;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Read extends ActionBarActivity {
    TextView t ;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        t = (TextView) findViewById(R.id.textView);
        Bundle b = getIntent().getExtras();
        String s = (String) b.get("title");
        t.setText(s);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Toast t = new Toast(Read.this);
                                          LayoutInflater inflater = getLayoutInflater();
                                          View toastView = inflater.inflate(R.layout.toast_read , (android.view.ViewGroup) findViewById(R.id.toast_root));
                                          t.setView(toastView);
                                          t.setDuration(Toast.LENGTH_SHORT);
                                          t.show();




                                      }
                                  }

        );




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
