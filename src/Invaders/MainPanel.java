package Invaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final int move=6;
	public final int width=1200,height=800;
	private int highScore;
	//Player Vars
	private int score;
	private int lives;
	private int radius;
	private int level;
	public Point pt;
	private boolean gameOver;
	
	//ArrayLists to draw from
	ArrayList<Enemy> blocks;
	ArrayList<Enemy> deadBlocks;
	ArrayList<Projectile> shots;
	ArrayList<Projectile> otherShots;
	
	//Mothership vars
	public boolean isMotherShip;
	private MotherShip mother;
	
	//Boolean movement system
	private boolean right=false;
	private boolean left=false;
	
	//Pause Option
	private boolean paused;
	
	//Image Stuffs
	private Image block;
	private Image tank;
	private Image motherShip;
	private int imageNum;
	private String images[];
	
	//Score managing system
	Score scoreManager;
	private boolean submitted;
	
	public MainPanel(){
		pt=new Point();
		pt.move(width/2,height-20);
		setBackground(Color.black);
		setPreferredSize(new Dimension(width+2,height+20));
		images = new String[3];
		images[0]="alien.gif";images[1]="alien2.gif";images[2]="alien3.gif";
		imageNum=0;
        try {
        	URL url = this.getClass().getResource(images[imageNum]);
        	block = ImageIO.read(url);
        	Enemy.setSize(block.getWidth(null));
        	Enemy.setHeight(block.getHeight(null));
        	url = this.getClass().getResource("tank.gif");
        	tank = ImageIO.read(url);
        	radius=(int)(tank.getWidth(null)/2);
        	url = this.getClass().getResource("mother.gif");
        	motherShip = ImageIO.read(url);
        	MotherShip.setSize(motherShip.getWidth(null));
        	MotherShip.setHeight(motherShip.getHeight(null));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Add the key listener
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        gameOver=true;
        paused=true;
        highScore=0;
        score=0;
        lives=3;
        blocks=new ArrayList<Enemy>();
        deadBlocks=new ArrayList<Enemy>();
        shots=new ArrayList<Projectile>();
        otherShots=new ArrayList<Projectile>();
        //Add enemies
        for(int x=45;x<=650;x+=45){
        	Enemy cur=new Enemy(width,x,this);
        	cur.start();
        	blocks.add(cur);
        }
        mother=new MotherShip(width,5,this);
        mother.start();
        isMotherShip=false;
        level=1;
        scoreManager = new Score();
        scoreManager.processScores();
        submitted=true;
	}
	
	public void cycleImage(){
		imageNum++;
		if(imageNum>=images.length)
			imageNum=0;
    	try {
    		URL url = this.getClass().getResource(images[imageNum]);
			block = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Enemy.setSize(block.getWidth(null));
    	Enemy.setHeight(block.getHeight(null));
    	//Add mothership
    	mother.setX(width);
    	isMotherShip=true;
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	public void collisionDetection(){
		//Go through for each shot
		for(int x=0;x<shots.size();x++){
			Projectile cur=shots.get(x);
			if(cur.isAlive()){
				//Check for mothership
				if(isMotherShip){
					if(cur.isWithin(mother)){
						score+=100;
						isMotherShip=false;
						shots.remove(cur);
					}
				}
				//Check if its within an enemy
				for(int y=0;y<blocks.size();y++){
					if(cur.isWithin(blocks.get(y))){
						score+=10;
						Enemy temp=blocks.remove(y);
						temp.randomize();
						deadBlocks.add(temp);
						shots.remove(cur);
					}
				}
			}
		}
		//Each enemy shot
		for(int x=0;x<otherShots.size();x++){
			Projectile cur=otherShots.get(x);
			if(cur.isAlive()){
				//If its alive, check if its within the player
				if(cur.x>=pt.x-radius && cur.x+2<=pt.x+radius){
					if(cur.y>=pt.y-radius){
						lives--;
						otherShots.remove(cur);
						if(lives<=0){
							submitted=false;
							gameOver=true;
							paused=true;
						}
					}
				}
			}
		}
		
	}
	
	public void paintComponent(Graphics page){
		super.paintComponent(page);
		//Check if the game is over
		if(gameOver){
			if(!submitted){
				scoreManager.submitScore(score);
				submitted=true;
			}
			gameOverMessage(page);
			page.dispose();
			return;
		}
		//Move player
		if(right)
			pt.move(pt.x+move,pt.y);
		if(left)
			pt.move(pt.x-move,pt.y);
		//Keep within bounds
        if(pt.x<1+radius)
        	pt.move(1+radius,pt.y);
        if(pt.y<0+radius)
        	pt.move(pt.x,radius);
        if(pt.y>height-radius)
        	pt.move(pt.x, height-radius);
        if(pt.x>width-radius)
        	pt.move(width-radius, pt.y);
		//Collision Detection
        collisionDetection();
        //No enemies or shots, make the enemies come back
        if(blocks.size()==0 && shots.size()==0 && otherShots.size()==0){
        	level++;
        	if(level/5>=1){
        		level/=5;
        		Enemy.setMove(Enemy.getMove()+1);
        		MotherShip.setMove(MotherShip.getMove()+1);
        		if(Projectile.getMove()<10)
        			Projectile.setMove(Projectile.getMove()+1);
        	}
        	cycleImage();
        	blocks=deadBlocks;
        	deadBlocks=new ArrayList<Enemy>();
        }
        
        //Border
		page.setColor(Color.GRAY);
		page.drawRect(0, 0, width, height);
		//Player
		page.drawImage(tank,pt.x-radius,pt.y-15,null);
		//Enemies
		for(int x=0;x<blocks.size();x++){
			Enemy block=blocks.get(x);
			page.drawImage(this.block,block.getX(), block.getY(),null);
		}
		//Mother ship
		if(isMotherShip){
			page.drawImage(motherShip,mother.getX(),mother.getY(),null);
		}
		
		//Shots
		page.setColor(Color.green);
		for(int x=0;x<shots.size();x++){
			Projectile cur=shots.get(x);
			if(cur.isAlive())
				page.fillRect(cur.x,cur.y,Projectile.width,Projectile.height);
			else
				shots.remove(cur);
		}
		//Enemy fire
		page.setColor(Color.white);
		for(int x=0;x<otherShots.size();x++){
			Projectile cur=otherShots.get(x);
			if(cur.isAlive())
				page.fillRect(cur.x,cur.y,Projectile.width,Projectile.height);
			else
				otherShots.remove(cur);
		}
		//Stats
		page.setColor(Color.orange);
		page.drawString("Score: "+score+"     Lives: "+lives, 5, height+15);
		if(paused){
			page.setFont(new Font("Times New Roman", Font.PLAIN, 18));
			page.drawString("Paused - Press P or Enter to resume.",width/2 -100,height/2);
		}
		
		//Dispose
		page.dispose();
	}
	
	public void gameOverMessage(Graphics page){
		page.setColor(Color.white);
		page.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		page.drawString("Game Over, press Enter to start new game.",width/2 -125,height/2);
		page.drawString("High Score:",width/2-30,height/2+20);
		HighScore high = scoreManager.getHighest();
		highScore=high.getScore();
		page.drawString(""+highScore,width/2,height/2+40);	
	}
	
	public void resetGame(){
		paused=false;
		gameOver=false;
		lives=3;
		score=0;
		level=0;
		imageNum=-1;
		cycleImage();
		shots.clear();
		otherShots.clear();
		pt.x=width/2;
		Enemy.setMove(1);
		MotherShip.setMove(1);
		Projectile.setMove(6);
		for(int x=0;x<deadBlocks.size();x++){
			blocks.add(deadBlocks.remove(x));
		}
		isMotherShip=false;
	}

    public void formKeyPressed(java.awt.event.KeyEvent evt) {
        //38=up,40=down,37=left,39=right
    	//32=space
        //System.out.println(evt.getKeyCode());
        if(evt.getKeyCode()==37)
            left=true;
        if(evt.getKeyCode()==39)
            right=true;
        
        //Listen for Enter/P
        if(evt.getKeyCode()==80||evt.getKeyCode()==10){
        	if(paused && gameOver){
        		resetGame();
        	}else if(paused){
        		paused=false;
        	}else
        		paused=true;
        }
        
        if(evt.getKeyCode()==32 && !paused){
        	if(shots.size()<=10 && blocks.size()>0){
	        	Projectile temp=new Projectile(pt.x-(Projectile.width/2),pt.y-15,this);
	        	temp.start();
	        	shots.add(temp);
	        }
        }
        
        //  Cheap cheating way :) 
        if(false && evt.getKeyCode()==17 && !paused){
        	for(int x=0;x<36;x++){
	        	Projectile temp=new Projectile(pt.x-(x*20),pt.y-2*radius,this);
	        	temp.start();
	        	shots.add(temp);
	        	temp=new Projectile(pt.x+(x*20),pt.y-2*radius,this);
	        	temp.start();
	        	shots.add(temp);
        	}
        }
        
    }
    
    public void formKeyReleased(java.awt.event.KeyEvent evt) {
        if(evt.getKeyCode()==37)
            left=false;
        if(evt.getKeyCode()==39)
            right=false;
    }
	
}
