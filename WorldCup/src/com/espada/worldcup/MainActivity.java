package com.espada.worldcup;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.espada.common.HttpUtils;
import com.espada.common.SQLiteUtils;
import com.espada.common.Utils;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	
	private Context mContext;
	
    private TabHost tabHost;
	
	private Intent homeIntent;
	private Intent storeIntent;
	private Intent settingIntent;
	
	private static final String HOME = "赛程";
	private static final String STORE = "收藏";
	private static final String SETTING = "设置";
	private int homeImgId = R.drawable.home,storeImgId = R.drawable.store,settingImgId = R.drawable.setting;
	
	private String appPath = Environment.getExternalStorageDirectory()+"/worldcup/";
	
	Utils utils = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mContext = this;
        utils = new Utils();
        
        tabHost = this.getTabHost();
        tabHost.setFocusable(true);
        
        
        mainHandler.sendEmptyMessage(11);
//        mainHandler.sendEmptyMessage(41);
        
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	Runnable  initRunnable = new InitRunnable();
    	pool.execute(initRunnable);
    }
    
    Handler mainHandler = new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case 0:
    			break;
    		case 11:
    			prepareIntent();
    	        setupIntent();
    			break;
    		case 41:
    			Runnable getDateRunnable = new GetDateRunnable();
    			pool.execute(getDateRunnable);
    			break;
    		}
    	}
    };
    
    ExecutorService pool = Executors.newCachedThreadPool();
    
    private void prepareIntent(){
    	
    	homeIntent = new Intent(this,HomeActivity.class);
    	storeIntent = new Intent(this,StoreActivity.class);
    	settingIntent = new Intent(this,SettingActivity.class);
    	
    }
    
    private void setupIntent(){
    	tabHost.addTab(buildTabSpec(HOME,homeImgId,homeIntent));
    	tabHost.addTab(buildTabSpec(STORE,storeImgId,storeIntent));
    	tabHost.addTab(buildTabSpec(SETTING,settingImgId,settingIntent));
    	tabHost.setCurrentTab(0);
    	tabHost.setOnTabChangedListener(new TabChangeListener(mContext,tabHost));
    	tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(mContext.getResources().getColor(R.color.green));
    }
    
    private TabSpec buildTabSpec(String tag,int imgId,Intent intent){
    	View view = View.inflate(mContext, R.layout.tab, null);
    	((TextView)view.findViewById(R.id.tab_tv_txt)).setText(tag);
    	((ImageView)view.findViewById(R.id.tab_tv_img)).setImageResource(imgId);
    	TabSpec tabSpec = tabHost.newTabSpec(tag).setIndicator(view).setContent(intent);
    	return tabSpec;
    }
    
    public class InitRunnable implements Runnable{
    	
    	public InitRunnable(){
    		
    	}
    	
    	@Override
    	public void run(){
    		String outputPath = appPath+"Image/flagImage/";
    		
    		if(!new File(outputPath).exists()){
    			try {
    				utils.copyAssetsFile(mContext, "flags", outputPath);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		
    	}
    	
    }
    
    public class GetDateRunnable implements Runnable{
    	public GetDateRunnable(){
    		
    	}
    	
    	@Override
    	public void run(){
    		HttpUtils httpUtils = new HttpUtils(mContext);
    		
    		String url0 = "http://worldcup.2014.163.com/schedule/calendar/?bdsc";
    		httpUtils.getGroupGameList(url0);
    		
//    		String dbPath = appPath+"worldcup.db";
//			SQLiteUtils sqliteUtils = new SQLiteUtils(mContext,dbPath);
//			sqliteUtils.initTeam();
    		
    	}
    	
    }
    
    class TabChangeListener implements OnTabChangeListener{
    	private Context context;
    	private TabHost tabHost;
    	
    	TabChangeListener(Context context,TabHost tabHost){
    		this.context = context;
    		this.tabHost = tabHost;
    	}
    	
    	@Override
    	public void onTabChanged(String tagId){
    		TabWidget tabWidget = tabHost.getTabWidget();
    		View view = null;
    		
    		for(int i=0;i<tabWidget.getChildCount();i++){
    			System.out.println(">>>>> debug drivetext count="+tabWidget.getChildCount()+" cur="+i+" height="+tabWidget.getChildTabViewAt(i).getHeight());
    			tabWidget.getChildTabViewAt(i).setBackgroundColor(context.getResources().getColor(R.color.calm_green));
    		}
    		
    		if(tagId.equals(HOME)){
    			view = tabWidget.getChildTabViewAt(0);
    		}else if(tagId.equals(STORE)){
    			view = tabWidget.getChildTabViewAt(1);
    		}else if(tagId.equals(SETTING)){
    			view = tabWidget.getChildTabViewAt(2);
    		}
    		
    		view.setBackgroundColor(context.getResources().getColor(R.color.green));
    	}
    	
    }
    
}