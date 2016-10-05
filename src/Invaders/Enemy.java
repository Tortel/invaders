package Invaders;

import java.util.Random;

public class Enemy extends Thread {
	private int x,y;
	private int width;
	private static int size,height;
	private MainPanel panel;
	private static Random gen;
	private static int move;
	
	//Move left/right
	private boolean left;
	private boolean right;
	
	//When to return fire
	private int fire[];
	
	Enemy(int width,int yPos,MainPanel tmp){
		//Name Thread
		super("Enemy");
		if(gen==null)
			gen = new Random();
		panel=tmp;
		this.width=tmp.width;
		setX(gen.nextInt(width-getSize()));
		setY(yPos);
		int temp=gen.nextInt(2);
		if(temp==1){
			right=true;
			left=false;
		} else{
			setX(width-getSize());
			right=false;
			left=true;
		}
		fire=new int[6];
		genFire();
		move=1;
	}
	
	private void genFire(){
		Random gen = new Random();
		for(int x=0;x<fire.length;x++){
			//arg is the width of the window
			fire[x]=gen.nextInt(width);
		}
	}
	
	public void move(){
		if(right){
			setX(getX() + move);
			if(getX()>=width-getSize()){
				right=false;
				left=true;
				genFire();
			}
		}
		if(left){
			setX(getX() - move);
			if(getX()<=1){
				right=true;
				left=false;
				genFire();
			}
		}
		if(isFire(getX()) && panel.otherShots.size()<20
			&& panel.blocks.contains(this)){
        	Projectile temp=new Projectile(getX()+getSize()/2,getY()+getHeight(),panel.height,true,panel);
        	temp.start();
        	panel.otherShots.add(temp);
		}
	}
	
	private boolean isFire(int tmp){
		boolean temp=false;
		for(int x=0;x<fire.length;x++){
			if(tmp==fire[x])
				temp=true;
		}
		return temp;
	}
	
	public void randomize(){
		setX(gen.nextInt(width-getSize()));
	}
	
	public int getSize(){
		return size;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void run() {
		try {
			do{
				if(!panel.isPaused())
					move();
				sleep(5);
			}while(true);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public static void setSize(int tmp) {
		size = tmp;
	}

	public static void setHeight(int tmp) {
		height = tmp;
	}
	
	public static void setMove(int tmp){
		move=tmp;
	}
	
	public static int getMove(){
		return move;
	}
	
}
