package Invaders;

public class HighScore {
	String name;
	int score;
	
	HighScore(int a){
		name="";
		score=a;
	}
	
	HighScore(int a,String str){
		name=str;
		score=a;
	}
	
	public String toString(){
		return ""+score;
	}
	
	public int getScore(){
		return score;
	}
}
