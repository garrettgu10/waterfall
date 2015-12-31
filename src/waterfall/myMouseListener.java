package waterfall;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class myMouseListener implements MouseListener{
	static Rectangle imageRegion;
	
	public myMouseListener(){
		super();
		imageRegion = myPanel.getImageRegion();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();
		if(imageRegion != null && imageRegion.contains(x, y)){
			if(Desktop.isDesktopSupported())
			{
				try {
					Desktop.getDesktop().browse(new URI("http://garrett.comze.com/advertisement"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}

class myMouseMotionListener implements MouseMotionListener{
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		final int x = e.getX();
        final int y = e.getY();
        final Rectangle imageRegion = myMouseListener.imageRegion;
        if (imageRegion != null && imageRegion.contains(x, y)) {
            mainmain.m.frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            mainmain.m.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
	}
}