package com.espada.common;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Environment;

public class HttpUtils {
	
	private int TIMEOUT = 5000;
	private Context context;
	private String appPath = Environment.getExternalStorageDirectory()+"/worldcup/";
	
	public HttpUtils(Context context){
		this.context = context;
	}
	
	public void getGroupGameList(String url){
		
		String dbPath = appPath+"worldcup.db";
		SQLiteUtils sqliteUtils = new SQLiteUtils(context,dbPath);
		Document doc = null;
		
		try{
			doc = Jsoup.parse(new URL(url), TIMEOUT);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(doc==null)
			return;
		
		Elements content = doc.getElementsByClass("wctable");
		System.out.println(">>>>> wct debug httputils content="+content.toString());
		
		int count0 = content.size();
		for(int i=0;i<count0;i++){
			
			Elements tbody = content.get(i).getElementsByTag("tbody");
//			System.out.println(">>>>> wct debug httputils gl tbody"+i+"="+tbody.text());
			
			if(tbody!=null){
//				System.out.println(">>>>> wct debug httputils gl tbody="+tbody.toString());
				
				int count1 = tbody.size();
				for(int j=0;j<count1;j++){
					Elements trs = tbody.get(j).getElementsByTag("tr");
					
					if(trs!=null){
						int count2 = trs.size();
						for(int k=0;k<count2;k++){
							Elements tds = trs.get(k).getElementsByTag("td");
							
							if(tds!=null){
								int session = Integer.valueOf(getNumbers(tds.get(1).text()));
								String date = tds.get(2).text();
								String bothSides = tds.get(4).text();
								String location = tds.get(5).text();
								Elements others = tds.get(7).getElementsByTag("a");
								String other = "http://worldcup.2014.163.com";
								if(others!=null&&others.size()>0){
									other = other+others.get(0).attr("href");
								}
								
								System.out.println(">>>>> wct debug httputils gl td session="+session+" snum="+session+" date="+date+" bothsides="+bothSides+" location="+location+" other="+other);
//								for(int l=0;l<tds.size();l++){
//									System.out.println(">>>>> wct debug httputils gl td"+l+"="+tds.get(l).text());
//								}
								
								String[] teams = getTeams(bothSides);
								String team0 = "";
								String team1 = "";
								
								if(teams!=null){
									team0 = teams[0];
									team1 = teams[1];
								}
								
								String country0 = "";
								if(sqliteUtils.getTeamInfo(team0)!=null){
									country0 = sqliteUtils.getTeamInfo(team0).getCountry();
								}
								
								String country1 = "";
								if(sqliteUtils.getTeamInfo(team1)!=null){
									country1 = sqliteUtils.getTeamInfo(team1).getCountry();
								}
								
								sqliteUtils.insertGameInfo(session, date, bothSides, location, "", "", 0, 0, i, other,team0,team1,country0,country1);
								
							}
						}
					}
					
				}
				
			}
			
		}
		
		
	}
	
	public String getNumbers(String content){
		
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()){
			return matcher.group(0);
		}
		
		return "";
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
