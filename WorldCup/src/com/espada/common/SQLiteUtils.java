package com.espada.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.espada.entity.GameInfo;
import com.espada.entity.TeamInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class SQLiteUtils {
	
	SQLiteDatabase sdb = null;
	private Context context;
	private String appPath = Environment.getExternalStorageDirectory()+"/worldcup/";
	
	public SQLiteUtils(Context context,String db_path){
		this.context = context;
		this.sdb = openDatabase(db_path);
		createGameInfoTable();
		initTeam();
	}
	
	public SQLiteDatabase openDatabase(String db_path){
        SQLiteDatabase database = null;
		
		File databasesDir = new File(db_path.substring(0, db_path.lastIndexOf("/")));
		
		if(!databasesDir.exists()){
			databasesDir.mkdirs();
		}
		
		try{
			database = SQLiteDatabase.openOrCreateDatabase(db_path, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return database;
	}
	
	public void createGameInfoTable(){
		SQLiteDatabase db = this.sdb;
		String crtSql = "create table if not exists wc_gameinfo(id integer primary key,session integer,date text,both_sides text,location text,winner text,score text,stored integer,over integer,game_kind integer,other text,team0,team1,country0,country1)";
		String createTeamTableSql = "create table if not exists wc_team(id integer primary key,team_name text,country text,flag text,game_group text,level integer)";
		String createVersionTableSql = "create table if not exists wc_version(id integer primary key,version_code integer,date text)";
		
		try{
			db.execSQL(crtSql);
			db.execSQL(createTeamTableSql);
			db.execSQL(createVersionTableSql);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void insertGameInfo(int session,String date,String both_sides,String location,String winner,String score,int stored,int over,int game_kind,String other,String team0,String team1,String country0,String country1){
		SQLiteDatabase db = this.sdb;
		String insertQuestionSql = "insert into wc_gameinfo(session,date,both_sides,location,winner,score,stored,over,game_kind,other,team0,team1,country0,country1) values("+session+",'"+date+"','"+both_sides+"','"+location+"','"+winner+"','"+score+"',"+stored+","+over+","+game_kind+",'"+other+"','"+team0+"','"+team1+"','"+country0+"','"+country1+"')";
		
		try{
			db.execSQL(insertQuestionSql);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void insertTeam(String team_name,String country,String flag,String game_group,int level){
		SQLiteDatabase db = this.sdb;
		String insert_sql = "insert into wc_team(team_name,country,flag,game_group,level) values('"+team_name+"','"+country+"','"+flag+"','"+game_group+"',"+level+")";
		
		try{
			db.execSQL(insert_sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void initTeam(){
				
		String[] teamNames = {"巴西","喀麦隆","克罗地亚","墨西哥","智利","澳大利亚","西班牙","荷兰","希腊","哥伦比亚","科特迪瓦","日本","哥斯达黎加","乌拉圭","意大利","英格兰","厄瓜多尔","法国","瑞士","洪都拉斯","阿根廷","波黑","尼日利亚","伊朗","美国","加纳","德国","葡萄牙","阿尔及利亚","比利时","韩国","俄罗斯"};
		String[] countryNames = {"baxi","kemailong","keluodiy","moxige","zhili","aodaliya","xibanya","helan","xila","gelunbiya","ketediwa","riben","gesidalijia","wulagui","yidali","yinggelan","eguduoer","faguo","ruishi","hongdulasi","agenting","bohei","niriliya","yilang","meiguo","jiana","deguo","putaoya","aerjiliya","bilishi","hanguo","eluosi"};
		
		for(int i=0;i<teamNames.length;i++){
			String flagPath = appPath+"Image/flagImage/"+countryNames[i]+".png";
			insertTeam(teamNames[i],countryNames[i],flagPath,"X",32);
		}
		
	}
	
	public TeamInfo getTeamInfo(String team_name){
		SQLiteDatabase db = this.sdb;
		TeamInfo teamInfo = null;
		
		String querySql = "select * from wc_team where team_name='"+team_name+"'";
		
		Cursor cursor = db.rawQuery(querySql, null);
		
		while(cursor.moveToNext()){
			String teamName = cursor.getString(cursor.getColumnIndex("team_name"));
			String country = cursor.getString(cursor.getColumnIndex("country"));
			String flag = cursor.getString(cursor.getColumnIndex("flag"));
			String group = cursor.getString(cursor.getColumnIndex("game_group"));
			int level = cursor.getInt(cursor.getColumnIndex("level"));
			
			teamInfo = new TeamInfo(teamName,country,flag,group,level);
			
		}
		
		return teamInfo;
	}
	
	
	public ArrayList<GameInfo> getGameInfoListBySql(String query_sql){
		
		SQLiteDatabase db = this.sdb;
		ArrayList<GameInfo> gameInfoList = new ArrayList<GameInfo>();
		
		String querySql = "select * from wc_gameinfo";
		
		if(query_sql!=null&&query_sql!=""){
			querySql = query_sql;
		}
		
		Cursor cursor = db.rawQuery(querySql, null);
		
		while(cursor.moveToNext()){
			
			int session = cursor.getInt(cursor.getColumnIndex("session"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String bothSides = cursor.getString(cursor.getColumnIndex("both_sides"));
			String location = cursor.getString(cursor.getColumnIndex("location"));
			String winner = cursor.getString(cursor.getColumnIndex("winner"));
			String score = cursor.getString(cursor.getColumnIndex("score"));
			int stored = cursor.getInt(cursor.getColumnIndex("stored"));
			int over = cursor.getInt(cursor.getColumnIndex("over"));
			int gameKind = cursor.getInt(cursor.getColumnIndex("game_kind"));
			String other = cursor.getString(cursor.getColumnIndex("other"));
			String team0 = cursor.getString(cursor.getColumnIndex("team0"));
			String team1 = cursor.getString(cursor.getColumnIndex("team1"));
			String country0 = cursor.getString(cursor.getColumnIndex("country0"));
			String country1 = cursor.getString(cursor.getColumnIndex("country1"));
			
			GameInfo gameInfo = new GameInfo(session,date,bothSides,location,winner,score,stored,over,gameKind,other,team0,team1,country0,country1);
			
			gameInfoList.add(gameInfo);
			
		}
		
		return gameInfoList;
		
	}
	
//	public ArrayList<QuestionRemark> getQuestionRemarkBySql(String query_sql,int offset,int limit_nums){
//		SQLiteDatabase db = this.sdb;
//		ArrayList<QuestionRemark> questionRemarkList = new ArrayList<QuestionRemark>();
//		
//		String querySql = "select * from question_remark limit "+offset+","+limit_nums;
//		
//		
//		if(query_sql!=""||query_sql!=null){
//			querySql = query_sql+" limit "+offset+","+limit_nums;
//		}
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			int questionId = cursor.getInt(cursor.getColumnIndex("question_id"));
//			int chapterId = cursor.getInt(cursor.getColumnIndex("chapter_id"));
//			int optionType = cursor.getInt(cursor.getColumnIndex("option_type"));
//			int wrongAnswer = cursor.getInt(cursor.getColumnIndex("wrong_answer"));
//			String time = cursor.getString(cursor.getColumnIndex("time"));
//			
//			System.out.println(">>>>> debug drivetest querybysql question questionid="+questionId+" chapterid="+chapterId+" optiontype="+optionType+" wronganswer="+wrongAnswer+" time="+time);
//			
//			QuestionRemark questionRemark = new QuestionRemark(questionId,chapterId,optionType,wrongAnswer,time);
//			questionRemarkList.add(questionRemark);
//		}
//		
//		return questionRemarkList;
//	}
//	
//	public ArrayList<String> getQuestionRemarkLateDate(int offset,int limits){
//		
//		SQLiteDatabase db = this.sdb;
//		ArrayList<String> lateDateList = new ArrayList<String>();
//		
//		String querySql = "select distinct time from question_remark order by time desc limit "+offset+","+limits;
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			String lastDate = cursor.getString(cursor.getColumnIndex("time"));
//			lateDateList.add(lastDate);
//		}
//		
//		return lateDateList;
//		
//	}
//	
//	public ArrayList<QuestionRemark> getQuestionRemarkByDateAndType(int option_type,String date,int offset,int limit_nums){
//		String querySql = "select * from question_remark where option_type="+option_type+" and time='"+date+"'";
//		
//		ArrayList<QuestionRemark> questionRemarkList = getQuestionRemarkBySql(querySql,offset,limit_nums);
//		
//		return questionRemarkList;
//		
//	}
//	
//	public ArrayList<QuestionRemark> getQuestionRemarkByChapterIdAndType(int option_type,int chapter_id,int offset,int limit_nums){
//		String querySql = "select * from question_remark where option_type="+option_type+" and chapter_id="+chapter_id;
//		
//		ArrayList<QuestionRemark> questionRemarkList = getQuestionRemarkBySql(querySql,offset,limit_nums);
//		
//		return questionRemarkList;
//		
//	}
//	
//	public int GetQuestionInfoListOfRemarkSize(int order_kind,int option_type,String tag){
//		String querySql = "";
//		int counts = 0;
//		
//		if(order_kind==0){
//			querySql = "select count(id) as counts from question_remark where option_type="+option_type+" and time='"+tag+"'";
//		}else if(order_kind==1){
//			querySql = "select count(id) as counts from question_remark where option_type="+option_type+" and chapter_id="+Integer.valueOf(tag);
//		}
//		
//		SQLiteDatabase db = this.sdb;
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			counts = cursor.getInt(cursor.getColumnIndex("counts"));
//		}
//		
//		return counts;
//		
//	}
//	
//	public ArrayList<QuestionInfo> getQuestionInfoListOfRemard(int order_kind,int option_type,String tag,int offset,int limit_nums){
//		ArrayList<QuestionInfo> questionInfoList = new ArrayList<QuestionInfo>();
//		ArrayList<QuestionRemark> questionRemarkList = new ArrayList<QuestionRemark>();
//		
//		if(order_kind==0){
//			questionRemarkList = getQuestionRemarkByDateAndType(option_type,tag,offset,limit_nums);
//		}else if(order_kind==1){
//			questionRemarkList = getQuestionRemarkByChapterIdAndType(option_type,Integer.valueOf(tag),offset,limit_nums);
//		}
//		
//		for(int i=0;i<questionRemarkList.size();i++){
//			QuestionRemark questionRemark = questionRemarkList.get(i);
//			QuestionInfo questionInfo = getQuestionById(questionRemark.getQuestionId());
//			questionInfoList.add(questionInfo);
//		}
//		
//		return questionInfoList;
//	}
//	
//	public ArrayList<Chapter> getQuestionRemarkByChapter(){
//		
//		SQLiteDatabase db = this.sdb;
//		ArrayList<Chapter> chapterList = new ArrayList<Chapter>();
//		String querySql = "select * from t_chapter where _id in (select distinct chapter_id from question_remark)";
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			
//			String chapterId = cursor.getString(cursor.getColumnIndex("_id"));
//			String title = cursor.getString(cursor.getColumnIndex("title"));
//			String chapterNum = cursor.getString(cursor.getColumnIndex("chapter"));
//			String section1 = cursor.getString(cursor.getColumnIndex("section1"));
//			String section2 = cursor.getString(cursor.getColumnIndex("section2"));
//			String section3 = cursor.getString(cursor.getColumnIndex("section3"));
//			String section4 = cursor.getString(cursor.getColumnIndex("section4"));
//			
//			System.out.println(">>>>> debug drivingtest getbynum chapter="+chapterNum+" title="+title);
//			Chapter chapter = new Chapter(chapterId,chapterNum,title);
//			chapterList.add(chapter);
//		}
//		
//		return chapterList;
//		
//	}
//	
//	public void getAllQuestion(){
//		SQLiteDatabase db = this.sdb;
//		
//		String querySql = "select * from t_question where media_type=2 limit 5";
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			String questionId = cursor.getString(cursor.getColumnIndex("question_id"));
//			String mediaType = cursor.getString(cursor.getColumnIndex("media_type"));
//			String chapterId = cursor.getString(cursor.getColumnIndex("chapter_id"));
//			String label = cursor.getString(cursor.getColumnIndex("label"));
//			String question = a(cursor.getBlob(cursor.getColumnIndex("question")));
//			byte[] mediaContent = cursor.getBlob(cursor.getColumnIndex("media_content"));
//			String mediaWidth = cursor.getString(cursor.getColumnIndex("media_width"));
//			String mediaHeight = cursor.getString(cursor.getColumnIndex("media_height"));
//			String answer = cursor.getString(cursor.getColumnIndex("answer"));
//			String optionA = cursor.getString(cursor.getColumnIndex("option_a"));
//			String optionB = cursor.getString(cursor.getColumnIndex("option_b"));
//			String optionC = cursor.getString(cursor.getColumnIndex("option_c"));
//			String optionD = cursor.getString(cursor.getColumnIndex("option_d"));
//			String explain = a(cursor.getBlob(cursor.getColumnIndex("explain")));
//			String difficulty = cursor.getString(cursor.getColumnIndex("difficulty"));
//			String optionType = cursor.getString(cursor.getColumnIndex("option_type"));
//			System.out.println(">>>>> debug drivetest question questionid="+questionId+" question="+question+" a="+optionA+" b="+optionB+" c="+optionC+" d="+optionD+" explain="+explain+" optiontype="+optionType+" answer="+answer);
//			
//		}
//		
//	}
//	
//	public QuestionInfo getQuestionById(int question_id){
//		SQLiteDatabase db = this.sdb;
//		QuestionInfo questionInfo = null;
//		String querySql = "select * from t_question where question_id="+question_id;
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			String questionId = cursor.getString(cursor.getColumnIndex("question_id"));
//			String mediaType = cursor.getString(cursor.getColumnIndex("media_type"));
//			String chapterId = cursor.getString(cursor.getColumnIndex("chapter_id"));
//			String label = cursor.getString(cursor.getColumnIndex("label"));
//			String question = a(cursor.getBlob(cursor.getColumnIndex("question")));
//			byte[] mediaContent = cursor.getBlob(cursor.getColumnIndex("media_content"));
//			String mediaWidth = cursor.getString(cursor.getColumnIndex("media_width"));
//			String mediaHeight = cursor.getString(cursor.getColumnIndex("media_height"));
//			String answer = cursor.getString(cursor.getColumnIndex("answer"));
//			String optionA = cursor.getString(cursor.getColumnIndex("option_a"));
//			String optionB = cursor.getString(cursor.getColumnIndex("option_b"));
//			String optionC = cursor.getString(cursor.getColumnIndex("option_c"));
//			String optionD = cursor.getString(cursor.getColumnIndex("option_d"));
//			String explain = a(cursor.getBlob(cursor.getColumnIndex("explain")));
//			String difficulty = cursor.getString(cursor.getColumnIndex("difficulty"));
//			String optionType = cursor.getString(cursor.getColumnIndex("option_type"));
//			
//			
//			
//			String mediaContentPath = "";
//			
//            if(mediaType.equals("1")){
//            	mediaContentPath = appPath+"qimg/"+questionId+".png";
//            	Runnable saveMediaToSdCard = new SaveMediaToSdCard(mediaContent,questionId,"png");
//            	pool.execute(saveMediaToSdCard);
//			}
//			
//			System.out.println(">>>>> debug drivetest querybysql question questionid="+questionId+" question="+question+" a="+optionA+" b="+optionB+" c="+optionC+" d="+optionD+" explain="+explain+" optiontype="+optionType+" answer="+answer+" mcpath="+mediaContentPath);
//			
//			questionInfo = new QuestionInfo(questionId,mediaType,chapterId,label,question,mediaContentPath,answer,optionA,optionB,optionC,optionD,explain,difficulty,optionType);
//			
//		}
//		
//		return questionInfo;
//	}
//	
//	public ArrayList<QuestionInfo> getQuestionBySql(String query_sql,int offset,int end){
//		SQLiteDatabase db = this.sdb;
//		ArrayList<QuestionInfo> questionInfoList = new ArrayList<QuestionInfo>();
//		
//		String querySql = "select * from t_question limit "+offset+","+end;
//		
//		
//		if(query_sql!=""||query_sql!=null){
//			querySql = query_sql+" limit "+offset+","+end;
//		}
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			String questionId = cursor.getString(cursor.getColumnIndex("question_id"));
//			String mediaType = cursor.getString(cursor.getColumnIndex("media_type"));
//			String chapterId = cursor.getString(cursor.getColumnIndex("chapter_id"));
//			String label = cursor.getString(cursor.getColumnIndex("label"));
//			String question = a(cursor.getBlob(cursor.getColumnIndex("question")));
//			byte[] mediaContent = cursor.getBlob(cursor.getColumnIndex("media_content"));
//			String mediaWidth = cursor.getString(cursor.getColumnIndex("media_width"));
//			String mediaHeight = cursor.getString(cursor.getColumnIndex("media_height"));
//			String answer = cursor.getString(cursor.getColumnIndex("answer"));
//			String optionA = cursor.getString(cursor.getColumnIndex("option_a"));
//			String optionB = cursor.getString(cursor.getColumnIndex("option_b"));
//			String optionC = cursor.getString(cursor.getColumnIndex("option_c"));
//			String optionD = cursor.getString(cursor.getColumnIndex("option_d"));
//			String explain = a(cursor.getBlob(cursor.getColumnIndex("explain")));
//			String difficulty = cursor.getString(cursor.getColumnIndex("difficulty"));
//			String optionType = cursor.getString(cursor.getColumnIndex("option_type"));
//			
//			
//			
//			String mediaContentPath = "";
//			
//            if(mediaType.equals("1")){
//            	mediaContentPath = appPath+"qimg/"+questionId+".png";
//            	Runnable saveMediaToSdCard = new SaveMediaToSdCard(mediaContent,questionId,"png");
//            	pool.execute(saveMediaToSdCard);
//			}
//			
//			System.out.println(">>>>> debug drivetest querybysql question questionid="+questionId+" question="+question+" a="+optionA+" b="+optionB+" c="+optionC+" d="+optionD+" explain="+explain+" optiontype="+optionType+" answer="+answer+" mcpath="+mediaContentPath);
//			
//			QuestionInfo questionInfo = new QuestionInfo(questionId,mediaType,chapterId,label,question,mediaContentPath,answer,optionA,optionB,optionC,optionD,explain,difficulty,optionType);
//			questionInfoList.add(questionInfo);
//		}
//		
//		return questionInfoList;
//	}
//	
//	public int getQuestionTotalCount(String query_sql){
//		SQLiteDatabase db = this.sdb;
//		
//		String querySql = "select count(_id) as count from t_question";
//		int count = 0;
//		
//		if(query_sql!=""||query_sql!=null){
//			querySql = query_sql;
//		}
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			String questionCount = cursor.getString(cursor.getColumnIndex("count"));
//			
//			System.out.println(">>>>> debug drivetest querybysql question questionCount="+questionCount);
//			count = Integer.valueOf(questionCount);
//		}
//		
//		return count;
//	}
//	
//	public void getAllChapter(){
//		SQLiteDatabase db = this.sdb;
//		
//		String querySql = "select * from t_chapter";
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			String title = cursor.getString(cursor.getColumnIndex("title"));
//			String chapter = cursor.getString(cursor.getColumnIndex("chapter"));
//			String section1 = cursor.getString(cursor.getColumnIndex("section1"));
//			String section2 = cursor.getString(cursor.getColumnIndex("section2"));
//			String section3 = cursor.getString(cursor.getColumnIndex("section3"));
//			String section4 = cursor.getString(cursor.getColumnIndex("section4"));
//			
//			System.out.println(">>>>> debug drivingtest chapter="+chapter+" title="+title);
//			
//		}
//		
//	}
//	
//	public Chapter getChapterByNum(int chapter_num){
//		SQLiteDatabase db = this.sdb;
//		Chapter chapter = null;
//		
//		String querySql = "select * from t_chapter where chapter='"+chapter_num+"'";
//		
//		Cursor cursor = db.rawQuery(querySql, null);
//		
//		while(cursor.moveToNext()){
//			String chapterId = cursor.getString(cursor.getColumnIndex("_id"));
//			String title = cursor.getString(cursor.getColumnIndex("title"));
//			String chapterNum = cursor.getString(cursor.getColumnIndex("chapter"));
//			String section1 = cursor.getString(cursor.getColumnIndex("section1"));
//			String section2 = cursor.getString(cursor.getColumnIndex("section2"));
//			String section3 = cursor.getString(cursor.getColumnIndex("section3"));
//			String section4 = cursor.getString(cursor.getColumnIndex("section4"));
//			
//			System.out.println(">>>>> debug drivingtest getbynum chapter="+chapterNum+" title="+title);
//			chapter = new Chapter(chapterId,chapterNum,title);
//		}
//		
//		if(cursor!=null){
//			cursor.close();
//		}
//		
//		return chapter;
//		
//	}
//	
//	public String a(byte[] paramArrayOfByte)
//	  {
//	    if (paramArrayOfByte != null){
//	      int i1 = 0;
//	      while (true)
//	      {
//	        byte[] arrayOfByte;
//	        
//	        try
//	        {
//	          if (paramArrayOfByte.length == 0)
//	            break;
//	          arrayOfByte = "_jiakaobaodian.com_".getBytes("utf8");
//	          if (i1 >= paramArrayOfByte.length)
//	          {
//	            String str = new String(paramArrayOfByte, "utf8");
//	            return str;
//	          }
//	        }
//	        catch (Exception localException)
//	        {
//	          localException.printStackTrace();
//	          return null;
//	        }
//	        paramArrayOfByte[i1] = ((byte)(paramArrayOfByte[i1] ^ arrayOfByte[(i1 % arrayOfByte.length)]));
//	        i1++;
//	      }
//	    }
//	    return null;
//	  }
//	
	public void closeDb(){
		if(sdb.isOpen()){
			sdb.close();
		}
	}
//	
//	ExecutorService pool = Executors.newCachedThreadPool();
//	
//	public class SaveMediaToSdCard implements Runnable{
//		
//		private byte[] blobByteArray;
//		private String saveName;
//		private String kindName;
//		
//		public SaveMediaToSdCard(byte[] blob_byteArray,String save_name,String kind_name){
//			this.blobByteArray = blob_byteArray;
//			this.saveName = save_name;
//			this.kindName = kind_name;
//		}
//		
//		@Override
//		public void run(){
//			Utils utils = new Utils(context);
//			utils.convertBlobToMedia(blobByteArray, saveName, kindName);
//		}
//		
//	}

}
