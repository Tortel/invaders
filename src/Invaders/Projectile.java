package Invaders;

public class Projectile extends Thread {
	public int x,y;
	private static int move=6;
	private boolean down;
	private int max;
	public static int width=4,height=15;
	private MainPanel panel;
	
	Projectile(int xPos,int yPos,int height,boolean tmp,MainPanel temp){
		//Name thread
		super("Projectile");
		x=xPos;
		y=yPos;
		down=tmp;
		max=height-10;
		panel=temp;
	}
	
	Projectile(int xPos,int yPos,MainPanel temp){
		//Name thread
		super("Projectile");
		x=xPos;
		y=yPos;
		down=false;
		panel=temp;
	}
	
	public boolean isWithin(Enemy block){
		//if its within the x boundries
		if(x>=block.getX() && x<=block.getX()+block.getSize())
			//Check if its within the y boundries
			if(y>=block.getY()&&y<=block.getY()+block.getHeight())
				return true;
		return false;
	}
	
	public void run(){
		int a =y;
		if(!down){
			while(a>=0){
				if(!panel.isPaused()){
					y=a;
					a-=move;
				}
				try {
					sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			while(a<=max){
				if(!panel.isPaused()){
					y=a;
					a+=move;
				}
				try {
					sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void setMove(int tmp){
		move=tmp;
	}
	
	public static int getMove(){
		return move;
	}
}
