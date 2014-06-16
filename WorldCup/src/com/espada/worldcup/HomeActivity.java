package com.espada.worldcup;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.espada.common.SQLiteUtils;
import com.espada.common.Utils;
import com.espada.entity.GameInfo;
import com.espada.entity.TeamInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends Activity{
	private Context hContext;
	private String appPath = Environment.getExternalStorageDirectory()+"/worldcup/";
	Utils utils = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		hContext = this;
		utils = new Utils();
		
		homeHandler.sendEmptyMessage(11);
		
	}
	
	Handler homeHandler = new Handler(){
		
		public void handleMessage(Message msg){
			
			switch(msg.what){
			case 0:
				
				break;
			case 11:
				initHomeView();
				break;
			case 21:
				ArrayList<GameInfo> gameInfoList = (ArrayList<GameInfo>)msg.obj;
				ListView homeScheduleListView = (ListView)findViewById(R.id.home_schedule_list);
				BaseAdapter adapter = new GameInfoAdapter(gameInfoList);
				homeScheduleListView.setAdapter(adapter);
				break;
			case 51:
				
				break;
			}
			
		}
		
	};
	
	ExecutorService threadsPool = Executors.newCachedThreadPool();
	
	public void initHomeView(){
		
		ArrayList<Integer> homeNavigationViewIdList = new ArrayList<Integer>();
		homeNavigationViewIdList.add(R.id.home_navigation_0);
		homeNavigationViewIdList.add(R.id.home_navigation_1);
		homeNavigationViewIdList.add(R.id.home_navigation_2);
		
		TextView homeNavigationTxv0 = (TextView)findViewById(R.id.home_navigation_0);
		TextView homeNavigationTxv1 = (TextView)findViewById(R.id.home_navigation_1);
		TextView homeNavigationTxv2 = (TextView)findViewById(R.id.home_navigation_2);
		
		homeNavigationTxv0.setOnTouchListener(new HomeNavigationOnTouchListener(0));
		homeNavigationTxv1.setOnTouchListener(new HomeNavigationOnTouchListener(1));
		homeNavigationTxv2.setOnTouchListener(new HomeNavigationOnTouchListener(2));
		
		Runnable getAllGameInfoRunnable = new GetAllGameInfoRunnable(21);
		threadsPool.execute(getAllGameInfoRunnable);
		
	}
	
	public class GetAllGameInfoRunnable implements Runnable{
		private int handlerId;
		
		public GetAllGameInfoRunnable(int handler_id){
			this.handlerId = handler_id;
		}
		
		@Override
		public void run(){
			
			String dbPath = appPath+"worldcup.db";
			SQLiteUtils sqliteUtils = new SQLiteUtils(hContext,dbPath);
			ArrayList<GameInfo> gameInfoList = sqliteUtils.getGameInfoListBySql("");
			sqliteUtils.getGameMoreByDate();
			System.out.println(">>>>> debug home gameinfo size="+gameInfoList.size()+" bothsides="+gameInfoList.get(0).getBothSides()+" team0="+gameInfoList.get(0).getTeam0());
			
			for(int i=0;i<gameInfoList.size();i++){
				GameInfo gameInfo = gameInfoList.get(i);
				System.out.println(">>>>> debug home gameinfo"+i+" country0="+gameInfo.getCountry0()+" country1="+gameInfo.getCountry1()+" date="+gameInfo.getDate());
			}
			
			Message msg = new Message();
			msg.what = handlerId;
			msg.obj = gameInfoList;
			homeHandler.sendMessage(msg);
			sqliteUtils.updateGameInfo();
		}
		
	}
	
	public class GetDayGameInfoRunnable implements Runnable{
		
		private int handlerId;
		private int dayKind;
		
		public GetDayGameInfoRunnable(int handler_id,int day_kind){
			this.handlerId = handler_id;
			this.dayKind = day_kind;
		}
		
		@Override
		public void run(){
			
			String dbPath = appPath+"worldcup.db";
			SQLiteUtils sqliteUtils = new SQLiteUtils(hContext,dbPath);
			
			ArrayList<GameInfo> todayGameInfoList = sqliteUtils.getGameInfoOfDay(dayKind);
			
			System.out.println(">>>>> debug wc home todaygf size="+todayGameInfoList.size());
			
			Message msg = new Message();
			msg.what = handlerId;
			msg.obj = todayGameInfoList;
			homeHandler.sendMessage(msg);
		}
		
	}
	
	public class HomeNavigationOnTouchListener implements OnTouchListener{
		private int navId;
		
		public HomeNavigationOnTouchListener(int nav_id){
			this.navId = nav_id;
		}
		
		@Override
		public boolean onTouch(View view,MotionEvent event){
			
			if(event.getAction()==MotionEvent.ACTION_UP){
				System.out.println(">>>>> debug remark navtouchid="+navId);
				switch(navId){
				case 0:
					view.setBackgroundResource(R.drawable.navigation_left_focus);
					Runnable getAllGameInfoRunnable = new GetAllGameInfoRunnable(21);
					threadsPool.execute(getAllGameInfoRunnable);
					break;
				case 1:
					view.setBackgroundResource(R.drawable.navigation_center_focus);
					GetDayGameInfoRunnable getTodayGameInfo = new GetDayGameInfoRunnable(21,0);
					threadsPool.execute(getTodayGameInfo);
					break;
				case 2:
					view.setBackgroundResource(R.drawable.navigation_right_focus);
					GetDayGameInfoRunnable getTomorrowGameInfo = new GetDayGameInfoRunnable(21,1);
					threadsPool.execute(getTomorrowGameInfo);
					break;
				}
				
				
				
			}
			
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				TextView homeNavigationTxv0 = (TextView)findViewById(R.id.home_navigation_0);
				TextView homeNavigationTxv1 = (TextView)findViewById(R.id.home_navigation_1);
				TextView homeNavigationTxv2 = (TextView)findViewById(R.id.home_navigation_2);
				homeNavigationTxv0.setBackgroundResource(R.drawable.navigation_left);
				homeNavigationTxv1.setBackgroundResource(R.drawable.navigation_center);
				homeNavigationTxv2.setBackgroundResource(R.drawable.navigation_right);
			}
			
			
			return true;
		}
		
	}
	
	public class GameInfoAdapter extends BaseAdapter{
		private ArrayList<GameInfo> gameInfoList;
		private GameInfo gameInfo;
		
		public GameInfoAdapter(ArrayList<GameInfo> game_info_list){
			this.gameInfoList = game_info_list;
		}
		
		@Override
		public int getCount(){
			return gameInfoList.size();
		}
		
		@Override
		public Object getItem(int position){
			return null;
		}
		
		@Override
		public long getItemId(int position){
			return 0;
		}
		
		@Override
		public View getView(int position,View convertView,ViewGroup parent){
			
			GameInfoViewHolder gameInfoViewHolder = null;
			
			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				convertView = inflater.inflate(R.layout.schedule_item, null);
				
				
				gameInfoViewHolder = new GameInfoViewHolder();
				gameInfoViewHolder.scheduleCardGameSessionTxv = (TextView)convertView.findViewById(R.id.schedule_card_game_session);
				gameInfoViewHolder.scheduleCardGameTimeTxv = (TextView)convertView.findViewById(R.id.schedule_card_game_time);
				gameInfoViewHolder.scheduleCardTeamFlagView0 = (ImageView)convertView.findViewById(R.id.schedule_card_team_flag0);
				gameInfoViewHolder.scheduleCardTeamFlagView1 = (ImageView)convertView.findViewById(R.id.schedule_card_team_flag1);
				gameInfoViewHolder.scheduleCardTeamNameTxv0 = (TextView)convertView.findViewById(R.id.schedule_card_team_name0);
				gameInfoViewHolder.scheduleCardTeamNameTxv1 = (TextView)convertView.findViewById(R.id.schedule_card_team_name1);
				gameInfoViewHolder.scheduleCardShareTxv = (TextView)convertView.findViewById(R.id.schedule_card_share);
				gameInfoViewHolder.scheduleCardStoreTxv = (TextView)convertView.findViewById(R.id.schedule_card_store);
				
				convertView.setTag(gameInfoViewHolder);
				
			}else{
				gameInfoViewHolder = (GameInfoViewHolder)convertView.getTag();
			}
			
			int id = position;
			
			gameInfo = gameInfoList.get(id);
			
			String session = gameInfo.getSession()+"场";
			gameInfoViewHolder.scheduleCardGameSessionTxv.setText(session);
			
			String date = gameInfo.getDate();
			gameInfoViewHolder.scheduleCardGameTimeTxv.setText(date);
			
			String country0 = gameInfo.getCountry0();
			String country1 = gameInfo.getCountry1();
			
			String flag0 = appPath+"Image/flagImage/baxi.png";
			if(country0!=null&&country0!=""){
				flag0 = appPath+"Image/flagImage/"+country0+".png";
			}
			
			String flag1 = appPath+"Image/flagImage/faguo.png";
			if(country1!=null&&country1!=""){
				flag1 = appPath+"Image/flagImage/"+country1+".png";
			}
			System.out.println(">>>>> debug haome flag0="+flag0);
			gameInfoViewHolder.scheduleCardTeamFlagView0.setImageBitmap(utils.getBitmapByPath(flag0));
			gameInfoViewHolder.scheduleCardTeamFlagView1.setImageBitmap(utils.getBitmapByPath(flag1));
			
			String team0 = gameInfo.getTeam0();
			String team1 = gameInfo.getTeam1();
			
			gameInfoViewHolder.scheduleCardTeamNameTxv0.setText(team0);
			gameInfoViewHolder.scheduleCardTeamNameTxv1.setText(team1);
			
			
			
			return convertView;
		}
		
		
	}
	
	public static class GameInfoViewHolder{
		TextView scheduleCardGameSessionTxv;
		TextView scheduleCardGameTimeTxv;
		ImageView scheduleCardTeamFlagView0;
		ImageView scheduleCardTeamFlagView1;
		TextView scheduleCardTeamNameTxv0;
		TextView scheduleCardTeamNameTxv1;
		TextView scheduleCardShareTxv;
		TextView scheduleCardStoreTxv;
	}
	
	public String[] getTeams(String both_sides){
		String[] result = null;
		String teams[] = new String[3];
		
		result = both_sides.split(" ");
		
		if(result.length==3){
			teams[0] = result[0];
			teams[1] = result[2];
			teams[2] = result[1];
		}
		
		return teams;
	}

}
