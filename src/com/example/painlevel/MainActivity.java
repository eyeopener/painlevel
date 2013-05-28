package com.example.painlevel;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.LayoutInflater;
import android.view.View;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.GestureDetector;  
import android.view.GestureDetector.OnGestureListener; 
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Button; 
import android.widget.LinearLayout;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.util.Date;
import android.content.res.Configuration;
import android.view.Gravity;

public class MainActivity extends Activity {
	private DrawLineView mMyView;
	private static LinearLayout ll;  
    private Button mBtnClean;  
    private Button mBtnGet; 
    private Button mBtnSave;
    private TextView mTV;
    private FileService fileService;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mMyView = new DrawLineView(this);
		//setContentView(mMyView);// ��view��ͼ�ŵ�Activity����ʾ
		setContentView(R.layout.activity_main);
		
		fileService = new FileService(this); 
		// ���û��沼��  
        ll = new LinearLayout(this.getBaseContext());  
        ll.setOrientation(LinearLayout.VERTICAL); 
        //ll.setGravity(Gravity.CENTER);
        
          
     // button  
        LinearLayout llBtn = new LinearLayout(this.getBaseContext());  
        llBtn.setOrientation(LinearLayout.HORIZONTAL); 
        llBtn.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        lpBtn.topMargin = 0;  
        lpBtn.leftMargin = 0; 
        
        mBtnClean = new Button(this.getBaseContext());  
        mBtnClean.setText("���");   
        mBtnClean.setTextColor(Color.BLACK);  
        mBtnClean.setTextSize(14);          
        mBtnClean.setWidth(120);
        mBtnClean.setHeight(20);
        llBtn.addView(mBtnClean,lpBtn);  
        
        mBtnGet = new Button(this.getBaseContext());  
        mBtnGet.setText("��ȡ");   
        mBtnGet.setTextColor(Color.BLACK);  
        mBtnGet.setTextSize(14);          
        mBtnGet.setWidth(120);
        mBtnGet.setHeight(20);
        llBtn.addView(mBtnGet);  
        
        mBtnSave = new Button(this.getBaseContext());  
        mBtnSave.setText("����");   
        mBtnSave.setTextColor(Color.BLACK);  
        mBtnSave.setTextSize(14);   
        mBtnSave.setWidth(120);
        mBtnSave.setHeight(20);
        //llBtn.addView(mBtnSave);  
        
        //��ʾ�ı�
        LinearLayout llTv = new LinearLayout(this.getBaseContext());  
        llTv.setOrientation(LinearLayout.HORIZONTAL); 
        llTv.setGravity(Gravity.CENTER);
        
        LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        lpTv.topMargin = 0;  
        lpTv.leftMargin = 50;
        
        mTV = new TextView(this.getBaseContext());
        mTV.setText("0.0");    
        mTV.setTextColor(Color.RED);  
        mTV.setTextSize(14);   
        mTV.setWidth(100);
        llTv.addView(mTV,lpTv);     
        
        // ���û�ͼview  
        LinearLayout llView = new LinearLayout(this.getBaseContext());  
        llView.setOrientation(LinearLayout.HORIZONTAL); 
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(  
                LayoutParams.MATCH_PARENT, 180);  
        //lpView.weight = 1;  
        lpView.topMargin = 10;  
        lpView.leftMargin = 0;        
        mMyView = new DrawLineView(this);  
        llView.addView(mMyView,lpView);
                 
        ll.addView(llView);  
        ll.addView(llTv);
        ll.addView(llBtn);
     
        setContentView(ll); 
        
     // ���ü�����հ�ť����¼�  
        mBtnClean.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
            	mMyView.ReDraw();
            	mMyView.invalidate();
            	mTV.setText("0.0");
            	mMyView.ClearnPainLevel();
            }  
              
        }); 
        
     // ���ü�����ȡ�����ť����¼�  
        mBtnGet.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
            	//mMyView.ReDraw();
            	//mMyView.invalidate();
            	NumberFormat ddf1=NumberFormat.getNumberInstance() ;
            	ddf1.setMaximumFractionDigits(2); 
            	mTV.setText(String.valueOf(ddf1.format(mMyView.GetPainLevel())));
            }  
              
        });  
        
     // ���ü������水ť����¼�  
        mBtnSave.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) { 
            	try{
            		// TODO Auto-generated method stub 
            		String fileName = "PainLevel.txt"; 
            		String content = mTV.getText().toString(); 
            		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd-   HH:mm:ss     ");     
            		Date   curDate   =   new   Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
            		String   str   =   formatter.format(curDate);   
            		fileService.saveAppend(fileName, content +"   "+str+ "\n\r");  
            		//��ʾ����ɹ�  
            		Toast.makeText(MainActivity.this, R.string.success, 1).show();
            	}
            	catch(Exception ex)
            	{
            		Toast.makeText(MainActivity.this, R.string.fault, 1).show();
            		System.out.println(ex.getMessage());
            	}
            }  
              
        });  
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			//TO-DO
		}
		else if (this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
		{
			//TO-DO
		}
	}

}
