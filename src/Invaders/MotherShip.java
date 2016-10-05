package Invaders;

public class MotherShip extends Enemy {
	private static int size,height,move;
	private MainPanel panel;
	
	MotherShip(int range, int pos, MainPanel tmp) {
		super(range, pos, tmp);
		move=1;
		panel=tmp;
	}
	
	public void move(){
		if(getX()>0-size){
			setX(getX()-move);
		}
		else{
			panel.isMotherShip=false;
		}
	}
	
	public int getSize(){
		return size;
	}
	
	public int getHeight(){
		return height;
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
