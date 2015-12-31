package waterfall;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class myKeyListener implements KeyListener{
	final static int[] konamiCode = {KeyEvent.VK_UP,KeyEvent.VK_UP,
			KeyEvent.VK_DOWN,KeyEvent.VK_DOWN,
			KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,
			KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,
			KeyEvent.VK_B,KeyEvent.VK_A,
	};
	private int step = 0;
	private boolean keyHeld = false;
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(!(keyHeld)){
			int code = arg0.getKeyCode();
			switch(code){
				case KeyEvent.VK_RIGHT:
					mainmain.m.movePlayer(1,0,1);
                    keyHeld = true; break;
				case KeyEvent.VK_LEFT:
					mainmain.m.movePlayer(-1,0,1);
                    keyHeld = true; break;
				case KeyEvent.VK_UP:
					mainmain.m.movePlayer(0, -1,1);
                    keyHeld = true; break;
				case KeyEvent.VK_DOWN:
					mainmain.m.movePlayer(0, 1,1);
                    keyHeld = true; break;
				case KeyEvent.VK_ENTER:
					mainmain.m.TogglePause(); break;
			}
			if(konamiCode[step]==code){
				step++;
				if(step==10){
					new myPlayer(0,"Coin_Drop");
					mainmain.m.cheat(1);
					step=0;
				}
			}else{
				switch(code){
					case KeyEvent.VK_W:
					case KeyEvent.VK_A:
					case KeyEvent.VK_S:
					case KeyEvent.VK_D: break;
					default: step = 0;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		keyHeld = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}
}
class myOtherKeyListener implements KeyListener{
	final static int[] konamiCode = {KeyEvent.VK_W,KeyEvent.VK_W,
			KeyEvent.VK_S,KeyEvent.VK_S,
			KeyEvent.VK_A,KeyEvent.VK_D,
			KeyEvent.VK_A,KeyEvent.VK_D,
			KeyEvent.VK_B,KeyEvent.VK_A,
	};
	private int step = 0;
	private boolean keyheld = false;
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!(keyheld)){
			int code = e.getKeyCode();
			switch(code){
				case KeyEvent.VK_W:
					mainmain.m.movePlayer(0, -1,0);keyheld = true; break;
				case KeyEvent.VK_S:
					mainmain.m.movePlayer(0, 1,0);keyheld = true; break;
				case KeyEvent.VK_A:
					mainmain.m.movePlayer(-1, 0,0);keyheld = true; break;
				case KeyEvent.VK_D:
					mainmain.m.movePlayer(1, 0,0);keyheld = true; break;
			}
			if(konamiCode[step]==code){
				step++;
				if(step==10){
					new myPlayer(0,"Coin_Drop");
					mainmain.m.cheat(0);
					step=0;
				}
			}else{
				switch(code){
					case KeyEvent.VK_UP:
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_RIGHT: break;
					default: step = 0;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyheld = false;
		int code = e.getKeyCode();
		if(code==KeyEvent.VK_C){
			mainmain.m.credits();
		}else if(code==KeyEvent.VK_M){
			mainmain.m.toggleSound();
		}
	}
}