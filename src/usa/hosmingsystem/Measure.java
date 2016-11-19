package usa.hosmingsystem;


import java.util.List;

import android.media.*;
import android.media.MediaPlayer.OnCompletionListener;

import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.widget.Button;

import android.text.format.Time;



public class Measure extends Activity implements SensorEventListener{
	 private SensorManager manager;
	 private Time time = new Time("Asia/Tokyo");
	 private GetTime gettime;
	 
	 private int measureStartFlag;
	 private int finishFlg;
	 private int mediaFlag;
	 
	 
	 private MediaPlayer mp;
	 
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure);
		
		gettime = new GetTime();
		manager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
		measureStartFlag = 0;
		mediaFlag = 0;
		finishFlg = 0;
		
		final Button button1 = (Button)findViewById(R.id.measureStart);
		
		mp = MediaPlayer.create(this,R.raw.ifudoudou);
        mp.setOnCompletionListener(
        		new OnCompletionListener(){
        			public void onCompletion(MediaPlayer mp){
        				mp.start();
        			}
        		}
        );
        
        button1.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                
                if(measureStartFlag == 0){	
                	measureStartFlag = 1;
                	button1.setText("�ꎞ��~");
                }else{
                	measureStartFlag = 0;
                	button1.setText("�v���J�n");
                }
                if(finishFlg == 1){
                	mediaFlag += 1;
                	if(mediaFlag > 10 ){
                		mp.stop();
                	}
                	
                }
           }
         });
         
		
	}

	
	@Override
	   protected void onResume() {
		   
		   super.onResume();
		   List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		   if(sensors.size() > 0) {
			  Sensor s = sensors.get(0);
			  manager.registerListener(this,s,SensorManager.SENSOR_DELAY_NORMAL )   ;
			  }
			 }


	   @Override
	   public void onAccuracyChanged(Sensor sensor, int accuracy) {
		   
	   }

	   @Override
	   public void onSensorChanged(SensorEvent event) {
		   
		   if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && measureStartFlag == 1) {
			   
			   
			   time.setToNow();
			   gettime.acc_x = event.values[0];
			   gettime.acc_y = event.values[1];
			   gettime.acc_z = event.values[2];
			   
			   if(gettime.getNowTime() < 10){
				   
				   gettime.hoge += 1;
				   //10�b��506��@�P�b50.6��@0.02�b�ň��
			   }
			   
			   if(gettime.getNowTime() > 2100){ //�R�T���o�߂�����
				 if(gettime.x_af == 0 && gettime.y_af == 0 && gettime.z_af == 0 ){
					 gettime.x_af = event.values[0];
					 gettime.y_af = event.values[1];
					 gettime.z_af = event.values[2];
				 }else{
					 if(gettime.accJudge()){
						 gettime.thresholdCount += 1;
					 }
					 gettime.x_af = event.values[0];
					 gettime.y_af = event.values[1];
					 gettime.z_af = event.values[2]; 
				 }
				 
				 if(gettime.thresholdCount > 6000){//�����x�����܂�u���Ȃ��Ȃ遨����@���~�܂���
					 finishFlg = 1;
					 mp.start();
					 
				 }
				 
			   }
			   
		   }
	   }
	   
	 
	   
	   
	   
		class GetTime{//�����x���� & �o�ߎ��Ԍv�����W���[��
			 long startTimeMillis = System.currentTimeMillis();
			 long lastTimeMillis;
			 long diffTime;
			 
			 float acc_x,acc_y,acc_z;
			 float x_af,y_af,z_af;
			 
			 int thresholdCount;
			 int hoge;
			 
			 public GetTime (){ //�R���X�g���N�^
				 startTimeMillis = System.currentTimeMillis();	 
				 lastTimeMillis = 0;
				 diffTime = 0;
				 
				 acc_x = 0;
				 acc_y = 0;
				 acc_z = 0;
				 
				 x_af = 0;
				 y_af = 0;
				 z_af = 0;
				 
				 thresholdCount = 0;
			 }
			 
			 public long getNowTime(){
				 long lastTimeMillis = System.currentTimeMillis();
				 diffTime = Math.round((lastTimeMillis - startTimeMillis) / 1000);
				 return diffTime;
			 }
			
		     public boolean accJudge(){
		    	 if(Math.abs(x_af - acc_x) < 0.13 && Math.abs(y_af - acc_y) < 0.13 && Math.abs(z_af - acc_z) < 0.13){
		    		 return true;
		    	 }else{
		    		 return false;
		    	 }
		     } 
		     
			 
		}
	   
}
