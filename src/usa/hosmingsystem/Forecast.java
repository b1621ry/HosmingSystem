package usa.hosmingsystem;

import java.io.*;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;

import android.location.*;
import android.widget.*;


public class Forecast extends Activity {
	LocationManager manager;
	LocationListener listener;
	
	
	private Button button;
   
	private String lat;
	private String lon;
	
	private String requestURL; 
    private int clickFlag;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forecast);
		
		
		button      = (Button)findViewById(R.id.button);
        
		clickFlag = 0;
		
		manager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		listener = new LocationListener() {
			public void onLocationChanged(Location location){
				lat = Double.toString(location.getLatitude());
				lon = Double.toString(location.getLongitude());
				requestURL = "http://api.openweathermap.org/data/2.5/find?lat=" + lat + "&lon=" +  lon + "&cnt=1";
				
				if(lat != null && lon != null){
					clickFlag = 1;
				}
		}
		
		public void onProviderEnabled(String provider){}
		public void onProviderDisabled(String provider){}
		public void onStatusChanged(String provider,int status,Bundle extras){}
		
		};
		
		
        button.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	
                if(clickFlag == 1 ){
                	
                	new AsyncTask<String, Void, JSONObject> (){

  
                	    
                	    @Override
                	    protected JSONObject doInBackground(String... params) {
                	        if (params == null || params.length < 1) {
                	            return null;
                	        }
                	        
                	        URL url;
                	        try {
                	            url = new URL(params[0]);
                	        } catch (MalformedURLException e) {
                	           
                	            return null;
                	        }

                	        HttpURLConnection conn = null;
                	        try {
                	            conn = (HttpURLConnection) url.openConnection();
                	            conn.setRequestMethod("GET");
                	            conn.setRequestProperty("Connection", "close");
                	            conn.setFixedLengthStreamingMode(0);

                	            conn.connect();

                	            int code = conn.getResponseCode();
                	           

                	            if (code != 200) {
                	               
                	                return null;
                	            }

                	            return new JSONObject(readContent(conn));
                	        } catch (IOException e) {
                	         
                	            return null;
                	        } catch (JSONException e) {
                	           
                	            return null;
                	        } finally {
                	            if (conn != null) {
                	                try {
                	                    conn.disconnect();
                	                } catch (Exception ignore) {
                	                }
                	            }
                	        }
                	    }

                	    private String readContent(HttpURLConnection conn) throws IOException {
                	        String charsetName;

                	        String contentType = conn.getContentType();
                	        if (! TextUtils.isEmpty(contentType)) {
                	            int idx = contentType.indexOf("charset=");
                	            if (idx != -1) {
                	                charsetName = contentType.substring(idx + "charset=".length());
                	            } else {
                	                charsetName = "UTF-8";
                	            }
                	        } else {
                	            charsetName = "UTF-8";
                	        }

                	        InputStream is = new BufferedInputStream(conn.getInputStream());

                	        int length = conn.getContentLength();
                	        ByteArrayOutputStream os = length > 0 ? new ByteArrayOutputStream(length) : new ByteArrayOutputStream();

                	        byte[] buff = new byte[10240];
                	        int readLen;
                	        while ((readLen = is.read(buff)) != -1) {
                	            if (readLen > 0) {
                	                os.write(buff, 0, readLen);
                	            }
                	        }

                	        return new String(os.toByteArray(), charsetName);
                	    }

                	    @Override
                	    protected void onPostExecute(JSONObject result) {
                	        
                	    	try {
                	    		JSONArray listArray = result.getJSONArray("list");
                	    		JSONObject obj = listArray.getJSONObject(0);
                	    		JSONObject rain = obj.getJSONObject("rain");
                	    	
                	    		 Log.v("yes", String.valueOf(rain.getString("3h")));
                	    		if(rain.getString("3h") == "0"){
                	    			Toast.makeText(Forecast.this, "êÙëÛÇµÇƒëÂè‰ïvÇ≈Ç∑", Toast.LENGTH_LONG).show();
                	    			ImageView imageView = (ImageView)findViewById(R.id.imageView2);
                	    			imageView.setImageResource(R.drawable.icon_1r_192);
                	    		}else{
                	    			Toast.makeText(Forecast.this, "êÙëÛÇ∑ÇÈÇÃÇÕå„Ç…ÇµÇ‹ÇµÇÂÇ§", Toast.LENGTH_LONG).show();
                	    			ImageView imageView = (ImageView)findViewById(R.id.imageView2);
                	    			imageView.setImageResource(R.drawable.icon_5p_192);
                	    		}
                			
                				
                				
                			} catch (JSONException e) {
                				
                				e.printStackTrace();
                			}
                	    }

                		
                	}.execute(requestURL);
                
                }

        
                
       
                
           }
         });
        
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		manager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,0,0,listener);
	}
	@Override
	protected void onPause(){
		super.onPause();
		manager.removeUpdates(listener);
	}
}


