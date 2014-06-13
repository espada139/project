package com.espada.entity;

public class TeamInfo {
	
	private String teamName;
	private String country;
	private String flag;
	private String group;
	private int level;
	
	public TeamInfo(String team_name,String country,String flag,String group,int level){
		setTeamName(team_name);
		setCountry(country);
		setFlag(flag);
	    setGroup(group);
	    setLevel(level);
	}
	
	public void setTeamName(String team_name){
		this.teamName = team_name;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public void setFlag(String flag){
		this.flag = flag;
	}
	
	public void setGroup(String group){
		this.group = group;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public String getTeamName(){
		return teamName;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getFlag(){
		return flag;
	}
	
	public String getGroup(){
		return group;
	}
	
	public int getLevel(){
		return level;
	}

}
