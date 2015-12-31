package waterfall;

import javax.swing.JOptionPane;



public class mainmain {
	static main m;
	static int[] wins = {0,0};
	public static void main(String[] args) {
		while(true){
			m = new main();
			m.init();
			if(m.getGameState() != gameState.TIE){
				wins[m.winner]++;
			}
			int playAgain = JOptionPane.showConfirmDialog(null, main.names[0]+" has "+Integer.toString(wins[0])+" "
						+ (wins[0]==1? "win":"wins")+".\n"
					+ main.names[1]+" has "+Integer.toString(wins[1])+" "
						+ (wins[1]==1?"win":"wins")+".\n"
					+ "Do you want to play again?", "Play Again",
					JOptionPane.YES_NO_OPTION);
			if(playAgain == JOptionPane.NO_OPTION){
				break;
			}
			new myPlayer(0,"Coin_Drop");
		}
		System.exit(0);
	}
}
