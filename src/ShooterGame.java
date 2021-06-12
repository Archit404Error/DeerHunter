import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.*;

public class ShooterGame {
	JFrame game;
	int[] direction = { 0, 0 };
	boolean paused = false;

	public ShooterGame() throws Exception {
		game = new JFrame("Shooter");
		game.setSize(1000, 800);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ShooterComponent s = new ShooterComponent(1000, 800, this);

		game.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					direction[0] = 1;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					direction[0] = -1;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					direction[1] = -1;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					direction[1] = 1;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
					try {
						s.kill();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_R) {
					s.reload();
				}

				if (e.getKeyCode() == KeyEvent.VK_P) {
					paused = !paused;
				}
				if (isPaused() && e.getKeyCode() == KeyEvent.VK_B) {
					s.buyBullet();
				}
				if (isPaused() && e.getKeyCode() == KeyEvent.VK_M) {
					s.buyMovement();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_UP && direction[0] == 1) {
					direction[0] = 0;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN && direction[0] == -1) {
					direction[0] = 0;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT && direction[1] == -1) {
					direction[1] = 0;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction[1] == 1) {
					direction[1] = 0;
				}
			}

		});
		game.add(s);
		game.setVisible(true);
	}

	public int[] getMovementDir() {
		return direction;
	}
	
	public boolean isPaused() {
		return paused;
	}

	public static void main(String[] args) throws Exception {
		ShooterGame p = new ShooterGame();
	}
}
