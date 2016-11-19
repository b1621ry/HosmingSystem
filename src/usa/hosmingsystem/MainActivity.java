package usa.hosmingsystem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.content.Intent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;




public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button button1 = (Button)findViewById(R.id.measureStart);
        Button button2 = (Button)findViewById(R.id.button2);
        
        button1.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,usa.hosmingsystem.Forecast.class);
                startActivity(intent);
           }
         });
         button2.setOnClickListener(new OnClickListener(){
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this,usa.hosmingsystem.Measure.class);
                 startActivity(intent);
            }
          });
         
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
