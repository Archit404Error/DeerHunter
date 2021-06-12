import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;

public class ShooterComponent extends JComponent {
	int frameWidth, frameLength;
	ArrayList<Enemy> platforms;
	BufferedImage player, crosshair, ammo, background;
	ShooterGame mainGame;
	int crossX, crossY, crossInc;
	int bulletsLeft;
	Image deer, duck, boar;
	int points, bulletAmount;

	public ShooterComponent(int fw, int fl, ShooterGame game) throws Exception {
		super();
		crossX = 450;
		crossY = 300;
		crossInc = 8;

		bulletAmount = 5;
		bulletsLeft = 5;
		points = 0;

		platforms = new ArrayList<Enemy>();
		player = ImageIO.read(new File("images/rifleleft.png"));
		crosshair = ImageIO.read(new File("images/crosshair.png"));
		ammo = ImageIO.read(new File("images/bullet.png"));

		//deer = new ImageIcon(new URL("https://i.pinimg.com/originals/8f/50/55/8f50551d648807a057264708f02831ea.gif")).getImage();
		duck = new ImageIcon(new URL("https://i.pinimg.com/originals/5c/f0/bd/5cf0bd14cca425f0bd3783987484d143.gif")).getImage();
		deer = new ImageIcon(new URL("https://i.gifer.com/origin/e3/e3ee7f259f3cbc0c04e7b4c0df9caadf_w200.gif")).getImage();
		boar = new ImageIcon(new URL("https://media0.giphy.com/media/JTQ0y8egFwsv4eHZwd/source.gif")).getImage();

		background = ImageIO.read(new File("images/bg.jpg"));

		mainGame = game;

		Random rand = new Random();
		int height = rand.nextInt(700);
		platforms.add(new Enemy(rand.nextInt(1000), height, 100, 100, false, height > 400 ? "deer" : "duck"));

		frameWidth = fw;
		frameLength = fl;
		Thread animate = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (!mainGame.isPaused()) {
						int[] current = mainGame.getMovementDir();
						if (current[0] == 1 && crossY >= crossInc) {
							crossY -= crossInc;
						}
						if (current[0] == -1 && crossY <= 700 - crossInc) {
							crossY += crossInc;
						}
						if (current[1] == 1 && crossX <= 900 - crossInc) {
							crossX += crossInc;
						}
						if (current[1] == -1 && crossX >= crossInc) {
							crossX -= crossInc;
						}
					}
					repaint();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		animate.start();
	}

	public void kill() throws Exception {
		if (bulletsLeft > 0) {

			AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("audio/gun.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();

			for (int i = 0; i < platforms.size(); i++) {
				Enemy r = platforms.get(i);
				if (r.contains(crossX + 50, crossY + 50)) {
					if (r.getType().equals("duck")) {
						points += 10;
					} else if (r.getType().equals("deer")) {
						points += 25;
					}
					platforms.remove(r);
				}
			}
			bulletsLeft--;
		}
	}

	public void reload() {
		bulletsLeft = bulletAmount;
	}

	public void buyBullet() {
		if (points >= 50) {
			points -= 50;
			bulletAmount++;
		}
	}
	
	public void buyMovement() {
		if (points >= 100) {
			points -= 100;
			crossInc++;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D paint = (Graphics2D) g;
		if (!mainGame.isPaused()) {
			paint.setColor(Color.white);
			paint.drawImage(background, 0, 0, 1000, 800, null);
			for (int i = 0; i < platforms.size(); i++) {
				Enemy r = platforms.get(i);
				if (r.getX() < 0) {
					platforms.remove(r);
				} else {
					platforms.remove(r);
					r = new Enemy((int) r.getX() - 6, (int) r.getY(), (int) r.getWidth(), (int) r.getHeight(),
							r.hasCrossed(), r.getType());
					platforms.add(i, r);
					if (!r.hasCrossed() && r.getX() < 800) {
						Random rand = new Random();
						r.cross();
						int height = rand.nextInt(700);
						platforms.add(new Enemy(1000, height, 100, 100, false, height > 400 ? "deer" : "duck"));
					}
				}
				if (r.getY() > 400)
					paint.drawImage(deer, (int) r.getX(), (int) r.getY(), (int) (r.getWidth() - (700 - r.getY()) / 20),
							(int) (r.getHeight() - (700 - r.getY()) / 20), null);
				else
					paint.drawImage(duck, (int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight(),
							null);
			}
			if (platforms.size() == 0) {
				Random rand = new Random();
				int height = rand.nextInt(700);
				platforms.add(new Enemy(rand.nextInt(1000), height, 100, 100, false, height > 400 ? "deer" : "duck"));
			}
			paint.drawImage(crosshair, crossX, crossY, 100, 100, null);
			if (crossX < 500) {
				try {
					player = ImageIO.read(new File("images/rifleleft.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					player = ImageIO.read(new File("images/rifleright.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			paint.drawImage(player, 400, 625, 200, 150, null);
			for (int i = 0; i < bulletsLeft; i++) {
				paint.drawImage(ammo, 0 + i * 25, 0, 20, 60, null);
			}
			paint.setFont(new Font("Courier", Font.PLAIN, 60));
			paint.drawString("$" + points, 500, 60);
		} else {
			paint.setFont(new Font("Courier", Font.PLAIN, 60));
			paint.drawString("$" + points, 500, 60);
			paint.setFont(new Font("Courier", Font.PLAIN, 30));
			paint.drawString("Press \'m\' to increase movement speed ($100)", 0, 100);
			paint.drawString("Press \'b\' to increase bullets ($50)", 0, 150);
		}
	}
}
