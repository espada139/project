package com.espada.entity;

public class GameInfo {
	private int session;
	private String date;
	private String bothSides;
	private String location;
	private String winner;
	private String score;
	private int stored;
	private int over;
	private int gameKind;
	private String other;
	private String team0;
	private String team1;
	private String country0;
	private String country1;
	
	public GameInfo(int session,String date,String both_sides,String location,String winner,String score,int stored,int over,int game_kind,String other,String team0,String team1,String country0,String country1){
		setSession(session);
		setDate(date);
		setBothSides(both_sides);
		setLocation(location);
		setWinner(winner);
		setScore(score);
		setStored(stored);
		setOver(over);
		setGameKind(game_kind);
		setOther(other);
	}
	
	public void setSession(int session){
		this.session = session;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public void setBothSides(String both_sides){
		this.bothSides = both_sides;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public void setWinner(String winner){
		this.winner = winner;
	}
	
	public void setScore(String score){
		this.score = score;
	}
	
	public void setStored(int stored){
		this.stored = stored;
	}
	
	public void setOver(int over){
		this.over = over;
	}
	
	public void setGameKind(int game_kind){
		this.gameKind = game_kind;
	}
	
	public void setOther(String other){
		this.other = other;
	}
	
	public void setTeam0(String team_0){
		this.team0 = team_0;
	}
	
	public void setTeam1(String team_1){
		this.team1 = team_1;
	}
	
	public void setCountry0(String country_0){
		this.country0 = country_0;
	}
	
	public void setCountry1(String country_1){
		this.country1 = country_1;
	}
	
	public int getSession(){
		return this.session;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getBothSides(){
		return this.bothSides;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	public String getWinner(){
		return this.winner;
	}
	
	public String getScore(){
		return this.score;
	}
	
	public int getStored(){
		return this.stored;
	}
	
	public int getOver(){
		return this.over;
	}
	
	public int getGameKind(){
		return this.gameKind;
	}
	
	public String getOther(){
		return this.other;
	}
	
	public String getTeam0(){
		return this.team0;
	}
	
	public String getTeam1(){
		return this.team1;
	}
	
	public String getCountry0(){
		return this.country0;
	}
	
	public String getCountry1(){
		return this.country1;
	}

}
