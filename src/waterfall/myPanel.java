package waterfall;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class myPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public static final int bs = main.BLOCKSIZE;
	public static final int SCREENWIDTH = bs*main.BWIDTH;
	public static final int SCREENHEIGHT = bs*main.BHEIGHT;
	public static final int METRICSFONTSIZE = bs/5*3;
	static int waterPoint = 0;
	static int[] cloudxy = {(int)Math.floor(Math.random()*main.BWIDTH)*30,
			(int)Math.floor(Math.random()*3)};
	static URL url; 
	static BufferedImage image;
	static final String credit = "Created by Garrett Gu with audio files from Incompetech.com and Soundbible.com. Press C to view credits.";
	static final Font pausedFont = new Font("Helvetica",Font.BOLD,bs*2);
	static final Font metricsFont = new Font("Calibri",Font.PLAIN,METRICSFONTSIZE);
	static final Font creditFont = new Font("Calibri",Font.PLAIN,bs/3);
	int finalSeconds = -1;
	
	public static void getImg(String urls){
		Thread t = new Thread(){
			public void run(){
				try {
					url = new URL(urls);
					image = ImageIO.read(url);
				} catch (IOException e) {
					//nah
				}
			}
		};
		t.start();
	}
	
	public static Rectangle getImageRegion(){
		try{
			return new Rectangle((SCREENWIDTH/2)-(image.getWidth()/2),SCREENHEIGHT-image.getHeight(),
				image.getWidth(),image.getHeight());
		}catch(NullPointerException e){
			
		}
		return null;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(mainmain.m.board[0].length*bs,mainmain.m.board.length*bs);
	}
	
	public void paint(Graphics g) {
		state[][] b = mainmain.m.board;
		int[][] playerpos = mainmain.m.playerpos;
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(metricsFont);
		for(int i = 0; i < b.length; i++){
			for(int j = b[0].length-1; j >= 0; j--){
				switch(b[i][j]){
					case BLANK: 
						if(Math.abs(i-cloudxy[1])<=1 && Math.abs(j-(cloudxy[0]/30))<=2
							&& (Math.abs(i-cloudxy[1])!=1 || Math.abs(j-(cloudxy[0]/30))!=2)){
							g2.setColor(new Color(204,255,255));
						}else{
							g2.setColor(new Color(0,255-(80*i/b.length),255-(80*i/b.length)));
						}
						break;
					case BLUE: g2.setColor(new Color(0,0,
							225-(160*i/b.length)+((int)(Math.sin((j-waterPoint)*Math.PI/b[0].length*5/2)*30)))); break;
					case GRAY: int n = 200- (int)(Math.random()*35)-(160*i/b.length);
					g2.setColor(new Color(n,n,n)); break;
					case DISCO:
						g2.setColor(new Color(155+(int)(Math.random()*100)-(140*i/b.length),
								155+(int)(Math.random()*100)-(140*i/b.length),
								155+(int)(Math.random()*100)-(140*i/b.length)));break;
					case GREEN: g2.setColor(new Color(0,150+(60*i/b.length),0)); break;
				}
				if((playerpos[0][0] == j && playerpos[0][1] == i) || (playerpos[1][0] == j && playerpos[1][1] == i)){
					g2.setColor(Color.WHITE);
				}
				g2.fillRect(j*bs, i*bs, bs, bs);
				if(b[i][j] == state.GREEN){
//					g2.setColor(new Color(0,180+(3*i),0));
//					g2.fillRect(j*bs+5, i*bs+5, bs-10, bs-10);
					g2.setColor(new Color(0,130+(60*i/b.length),0));
					try{
						if(j==0 || b[i][j-1]!=state.GREEN){
							g2.fillRect(j*bs,i*bs,bs/5,bs);
						}
						if(b[i-1][j]!=state.GREEN){
							g2.fillRect(j*bs, i*bs, bs, bs/5);
						}
						if(j==b[0].length-1 || b[i][j+1]!=state.GREEN){
							g2.fillRect((j+1)*bs-bs/5, i*bs, bs/5, bs);
						}
						if(j==0 || b[i-1][j-1]!=state.GREEN){
							g2.fillRect(j*bs, i*bs, bs/5, bs/5);
						}
						if(j==b[0].length-1 || b[i-1][j+1]!=state.GREEN){
							g2.fillRect((j+1)*bs-bs/5, i*bs, bs/5, bs/5);
						}
					}catch(ArrayIndexOutOfBoundsException e){
						//blah blah
					}
				}else if((playerpos[0][0] == j && playerpos[0][1] == i)){
					g2.setColor(Color.BLUE);
					g2.fillRect(j*bs+bs/10, i*bs+bs/10, bs-bs/5, bs-bs/5);
				}else if((playerpos[1][0] == j && playerpos[1][1] == i)){
					g2.setColor(Color.RED);
					g2.fillRect(j*bs+bs/10, i*bs+bs/10, bs-bs/5, bs-bs/5);
				}else if((b[i][j] == state.GRAY || b[i][j] == state.DISCO) && (mainmain.m.count == 1)){
					g2.setColor(new Color(155+(int)(Math.random()*100)-(140*i/b.length),
							155+(int)(Math.random()*100)-(140*i/b.length),
							155+(int)(Math.random()*100)-(140*i/b.length)));
					g2.fillRect(j*bs+bs/10, i*bs+bs/10, bs-bs/5, bs-bs/5);
				}
			}
		}
		g2.setColor(Color.BLUE);
		for(int k = 0; k < mainmain.wins[0]; k++){
			g2.fillRect(bs/4+bs*k, bs*(b.length-1)+bs/4, bs-bs/2, bs-bs/2);
		}
		g2.setColor(Color.RED);
		for(int k = 0; k < mainmain.wins[1]; k++){
			g2.fillRect(bs*(b[0].length-1)-bs*k+bs/4, bs*(b.length-1)+bs/4, bs-bs/2, bs-bs/2);
		}
		if(mainmain.m.score[0]!=0 && mainmain.m.score[1]!=0){
			int totalscore = mainmain.m.score[0]+mainmain.m.score[1];
			int location = (int)(((double)mainmain.m.score[1])/totalscore*SCREENWIDTH);
			g2.setColor((mainmain.m.score[1]>mainmain.m.score[0]? Color.RED : 
				(mainmain.m.score[1]!=mainmain.m.score[0]? Color.BLUE:Color.BLACK)));
			g2.fillRect(location-2, 0, 4, bs/5*3);
			g2.setColor(Color.BLACK);
			g2.fillRect(SCREENWIDTH/2-2, 0, 4, bs/5);
		}
		g2.setColor(Color.BLACK);
		g2.drawString(mainmain.m.getName(0)+"'s score: "+Integer.toString(mainmain.m.score[0]), 
				bs/5, g.getFontMetrics().getHeight()/2+bs/5);
		String m = mainmain.m.getName(1)+"'s score: "+Integer.toString(mainmain.m.score[1]);
		g2.drawString(m,SCREENWIDTH-g.getFontMetrics().stringWidth(m)-bs/5,
				g.getFontMetrics().getHeight()/2+bs/5);
		m = "Points remaining: "+Integer.toString((mainmain.m.count!= -1? mainmain.m.count: 0));
		g2.drawString(m, SCREENWIDTH/2-g.getFontMetrics().stringWidth(m)/2, 
				g.getFontMetrics().getHeight()/2+bs/5);
		if(mainmain.m.gs== gameState.PAUSED){
			g2.setColor(Color.RED);
			g2.setFont(pausedFont);
			g2.drawString("PAUSED", SCREENWIDTH/2-(g.getFontMetrics().stringWidth("PAUSED")/2), 
					SCREENHEIGHT/2+(g.getFontMetrics().getHeight()/2));
		}
		if(waterPoint<b[0].length/5*4){
			waterPoint++;
		}else{
			waterPoint = 0;
		}
		if(cloudxy[0] <1200){
			cloudxy[0]++;
		}else{
			cloudxy[0]=-120;
			cloudxy[1]=(int)Math.floor(Math.random()*3);
		}
		try{
			g2.drawImage(image,(SCREENWIDTH/2)-(image.getWidth()/2),SCREENHEIGHT-image.getHeight(),null);
		}catch(NullPointerException e){
			//meh
		}
		if(finalSeconds != mainmain.m.finalSeconds){
			finalSeconds = mainmain.m.finalSeconds;
		}
		if(finalSeconds > 0 && finalSeconds <= 5){
			g2.setColor(Color.RED);
			g2.setFont(pausedFont);
			g2.drawString(Integer.toString(mainmain.m.finalSeconds), 
					SCREENWIDTH/2-(g.getFontMetrics().stringWidth(Integer.toString(mainmain.m.finalSeconds))/2), 
					SCREENHEIGHT/2+(g.getFontMetrics().getHeight()/2));
		}
		g2.setColor(Color.BLACK);
		g2.setFont(creditFont);
		g2.drawString(credit, SCREENWIDTH-g.getFontMetrics().stringWidth(credit)-bs/6,
				SCREENHEIGHT-(g.getFontMetrics().getHeight()/2)-bs/6);
	}
}