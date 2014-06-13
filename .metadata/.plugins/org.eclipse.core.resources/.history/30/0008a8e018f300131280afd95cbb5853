package com.espada.worldcup;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.espada.common.SQLiteUtils;
import com.espada.entity.GameInfo;
import com.espada.entity.TeamInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends Activity{
	private Context hContext;
	private String appPath = Environment.getExternalStorageDirectory()+"/worldcup/";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
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
			String[] teams1 = getTeams(gameInfoList.get(0).getBothSides());
			for(int i=0;i<teams1.length;i++){
				System.out.println(">>>>> a="+teams1[i]);
			}
			String[] teams2 = getTeams(gameInfoList.get(1).getBothSides());
			for(int i=0;i<teams2.length;i++){
				System.out.println(">>>>> b="+teams2[i]);
			}
			System.out.println(">>>>> debug home gameinfo size="+gameInfoList.size()+" bothsides="+gameInfoList.get(0).getBothSides()+" "+gameInfoList.get(1).getBothSides());
			
			Message msg = new Message();
			msg.what = handlerId;
			msg.obj = gameInfoList;
			homeHandler.sendMessage(msg);
			
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
