package Invaders;

import javax.swing.JFrame;

public class Runner {
	public static void main(String[] args){
		JFrame dots = new JFrame("Invaders");
		dots.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Thread to update UI
		final MainPanel temp=new MainPanel();
		updaterThread painter=new updaterThread(temp);
		painter.start();
		dots.getContentPane().add(temp);
		//Key Listener
        dots.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                temp.formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                temp.formKeyReleased(evt);
            }
        });
		dots.pack();
		dots.setVisible(true);
	}
}