package waterfall;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.sound.sampled.Clip;
import javax.swing.*;

enum state{BLANK,GREEN,BLUE,GRAY,DISCO}

enum gameState {OVER,TIE,ONGOING,PAUSED}

public class main{
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int BWIDTH = 38;
	public static final int BHEIGHT = 20;
	public static final int FRAMETIME = 100;
	public static int BLOCKSIZE = 20;
	JFrame frame = new JFrame("Waterfall");
	int[][] playerpos = {{0,0},{BWIDTH-1,0}};
	int[] score = {0,0};
	gameState gs = gameState.ONGOING;
	int count = 0;
	state[][] board;
	static String[] names= new String[] {"Blue","Red"};
	int winner = 0;
	myPlayer music = null;
	static int musicyesno = -1;
	boolean[] cheat = {false,false};
	URL url;
	BufferedImage image;
	boolean movable;
	int[][] playerPositions = {{0,0},{0,BWIDTH-1}};
	
	public void toggleSound(){
		if(myPlayer.play){
			myPlayer.play = false;
			music.stop();
		}else{
			myPlayer.play = true;
			music.start(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public void cheat(int p){
		cheat[p] = !cheat[p];
	}
	
	public boolean checkColumnForBlock(int column, state block){
		for(int i = 0; i < BHEIGHT; i++){
			if(board[i][column] == block){
				return true;
			}
		}
		return false;
	}
	
	public void init(){
		while((BLOCKSIZE*BWIDTH < screenSize.width && BLOCKSIZE*BHEIGHT < screenSize.height)){
			BLOCKSIZE++;
		}
		BLOCKSIZE--;
		myPanel.getImg("http://garrett.comze.com/advertisement/ad.png");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		URL iconURL = this.getClass().getClassLoader().getResource("resources/icon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());
		
		if(musicyesno != JOptionPane.YES_OPTION
				&& musicyesno != JOptionPane.NO_OPTION){
	        musicyesno = JOptionPane.showConfirmDialog(frame,
		        "Do you want epic music and sound effects?",
		        "Music",JOptionPane.YES_NO_OPTION);
	        if(musicyesno==JOptionPane.NO_OPTION){
	            myPlayer.play = false;
	        }
	        new myPlayer(0,"Coin_Drop");
			JOptionPane.showMessageDialog(frame, "Control scheme:\n"
					+ "WASD to move blue player.\n"
					+ "Arrows to move red player.\n"
					+ "Enter to pause.\n"
					+ "Press C to see credits.\n"
					+ "Press M to toggle music/sound effects.\n\n"
					+ "Whoever cleans up the most pollution wins.\n"
					+ "Disco pollution is worth extra points.\n"
					+ "The last block is worth even more points.\n\n"
					+ "Keep your eye on the bar at the top.\n"
					+ "When the bar is on your side, you're winning!",
					"Instructions", JOptionPane.INFORMATION_MESSAGE);
	        new myPlayer(0,"Coin_Drop");
			promptNames();
		}
		
		int[] heights = makeHeights();
		int width = heights.length;
		int height = BHEIGHT;
		board = new state[height][width];
		if(board[0][0] == null){
			for(int y = height-1; y >= 0; y--){
				for(int x = 0; x < width; x++){
					if(height-y <= heights[x]){
						board[y][x] = state.GREEN;
					}else{
						board[y][x] = state.BLANK;
					}
				}
			}
		}
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//finished initialization
        myPanel p = new myPanel();
		frame.getContentPane().removeAll();
		
		frame.add(p);
		frame.pack();
		draw();
		fill();
		gray();
		
		music = new myPlayer(Clip.LOOP_CONTINUOUSLY,"Call_to_Adventure");
		frame.addKeyListener(new myKeyListener());
		frame.addKeyListener(new myOtherKeyListener());
		frame.getContentPane().addMouseListener(new myMouseListener());
		frame.getContentPane().addMouseMotionListener(new myMouseMotionListener());
		keepRefreshing();
		new myPlayer(0,"Applause");
		gameOver();
	}
	
	public String getName(int index){
		return names[index];
	}
	
	void promptNames(){
		String response;
		for(int i = 0; i < 2; i++){
			response = JOptionPane.showInputDialog(frame,
					"Enter name for "+(i==0?"left":"right")+" player:",
					"Name",
					JOptionPane.QUESTION_MESSAGE);
            new myPlayer(0,(i==0? "Jump0": "Jump1"));
            try{
				if(!response.equals("")){
					names[i] = response;
				}
            }catch(NullPointerException e){
            	//whatever
            }
		}
	}
	
	public void credits(){
		new myPlayer(0,"Coin_Drop");
		String message = "";
		String[] myRoles = {"Executive producer",
				 "Director",
				 "Developed by",
				 "Head of programming",
				 "Head of engineering",
				 "Head of reverse engineering",
				 "Janitor",
				 "Head of art",
				 "Chief court defender",
				 "That guy",
				 "Head of animation",
				 "Remastered by",
				 "Head of marketing",
				 "Chief editor",
				 "Visual effects",
				 "Chief beta tester",
				 "Chief procrastinator",
				 "Chief barista",
				 "I'm not sure who he is but I think he works here"};
		for(String role:myRoles){
			message = message+role+" -- Garrett Gu\n";
		}
		JOptionPane.showMessageDialog(frame, message+"\n"
				+ "Epic music from incompetech.com:\n"
				+ "Call to Adventure by Kevin MacLeod\n\n"
				+ "Sound effects from soundbible.com:\n"
				+ "Coin Drop\n"
				+ "Jump (modified)\n"
				+ "Audience Applause\n\n"
				+ "All audio files under CC Atribution 3.0 licence "
				+ "or public domain. ","Credits",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void TogglePause(){
		if(gs == gameState.PAUSED){
			gs = gameState.ONGOING;
		}else if(gs == gameState.ONGOING){
			gs = gameState.PAUSED;
		}
	}
	
	int finalSeconds = -1;
	Thread finalCount = null;
	public boolean movePlayer(int dPosx, int dPosy, int p){
		if(gs == gameState.ONGOING && movable){
			int[] newpos = new int[] {playerpos[p][0]+dPosx,playerpos[p][1]+dPosy};
			if(newpos[0] < BWIDTH && newpos[0] >=0 &&
					newpos[1] < BHEIGHT && newpos[1] >=0){
				if(board[newpos[1]][newpos[0]] != state.GREEN){
					if((playerpos[1-p][1] == newpos[1] && playerpos[1-p][0] == newpos[0])){
						if(!movePlayer(dPosx,dPosy,1-p)){
							return false;
						}else{
							return movePlayer(dPosx,dPosy,p);
						}
					}
					
					playerpos[p][0]+=dPosx;
					playerpos[p][1]+=dPosy;
					if(board[playerpos[p][1]][playerpos[p][0]] == state.GRAY){
						count--;
						board[playerpos[p][1]][playerpos[p][0]] = state.BLUE;
						if(cheat[p]){
							score[p] = score[p]+5;
						}else{
							score[p]++;
						}
						if(count == 1){
							finalCount.interrupt();
							finalSeconds = -1;
						}
						if(count == 0){
							finalSeconds = 0;
							score[p]=score[p]+9;
						}
						if(p==0){
							new myPlayer(0,"Jump0");
						}else{
							new myPlayer(0,"Jump1");
						}
					}else if(board[playerpos[p][1]][playerpos[p][0]] == state.DISCO){
						count = count-5;
						board[playerpos[p][1]][playerpos[p][0]] = state.BLUE;
						score[p] = score[p]+5;
						if(count == 0){
							score[p]=score[p]+5;
						}
						new myPlayer(0,"Coin_Drop");
					}
					return true;
				}
			}
		}
		return false;
	}

	gameState getGameState(){
		if(score[0]>score[1]){
			winner = 0;
			return gameState.OVER;
		}
		else if(score[0]<score[1]){
			winner = 1;
			return gameState.OVER;
		}
		return gameState.TIE;
	}
	
	void gameOver(){
		gs=getGameState();
		
		draw();
		if(gs==gameState.OVER){
			JOptionPane.showMessageDialog(frame,
                	"Congratulations! The winner is " + names[winner] + ".",
                	"Game Over", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(frame,
                	"It's a tie.",
                	"Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
		new myPlayer(0,"Coin_Drop");
		frame.setVisible(false);
		frame.dispose();
		music.stop();
	}
	
	static int[] makeHeights(){
		int[] h = new int[BWIDTH];
		
		for(int i = 0; i < BWIDTH; i++){
			h[i] = (int)Math.ceil(Math.random()*(BHEIGHT-1));
		}
		return h;
	}
	
	
	
	void keepRefreshing(){
		while(count>0 && count != -100){
			try {
				Thread.sleep(FRAMETIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			draw();
		}
	}

	void gray(){
		Thread t =new Thread(){
			public void run(){
				mainmain.m.count = 1;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int count = 0;
				for(int z = 0; z < board.length; z++){
					for(int x = 0; x < board[0].length; x++){
						if(board[z][x] == state.BLUE){
							int rand = (int)Math.round(20*Math.random());
							if(rand>=10){
								board[z][x] = state.GRAY;
								count++;
							}else if(rand==7){
								board[z][x] = state.DISCO;
								count=count+5;
							}
						}
					}
					if(count!=0){
						mainmain.m.count = count;
						try {
							Thread.sleep(FRAMETIME/2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				movable = true;
			}
		};
		t.start();
	}
	
	public void draw(){
		if(count == 2 && finalCount == null){
			System.out.println("finalCount");
			finalCount = new Thread(){
				boolean good = true;
				public void run(){
					finalSeconds = 10;
					while(finalSeconds > 0){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							good = false;
						}
						finalSeconds --;
					}
					if(good){
						count = 0;
					}
				}
			};
			finalCount.start();
		}
		frame.revalidate();
		frame.repaint();
	}
	
	void fill(){
		boolean leftBound;
		boolean rightBound;
		for(int z = board.length-1; z>= 0; z--){
			for(int i = 0; i < board[z].length; i++){
				if(board[z][i] == state.BLANK){
					leftBound = false; rightBound = false;
					for(int j = 0; j < i; j++){
						if(board[z][j] == state.GREEN){
							leftBound = true;
							break;
						}
					}
					for(int j = board[z].length-1; j > i; j--){
						if(board[z][j] == state.GREEN){
							rightBound = true;
							break;
						}
					}
					if(rightBound && leftBound) {
						board[z][i] = state.BLUE;
						//count++;
					}
				}
			}
			try {
				Thread.sleep(FRAMETIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			draw();
		}
	}
}