package Invaders;

public class updaterThread extends Thread {
	private MainPanel panel;
	
	updaterThread(MainPanel temp){
		//Name thread
		super("GUI Updater");
		panel=temp;
	}
	public void run(){
		do{
			panel.repaint();
			try {
				sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(true);
	}
}
