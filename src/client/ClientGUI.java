package client;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Pax Robin 
public class ClientGUI extends JFrame {
	private int height, width;
	public static final String TITLE = "Real-Time Camera System";
	private ClientMonitor mon;
	private static final long serialVersionUID = 1L;

	public ClientGUI(ClientMonitor mon) {
		super();
		this.mon = mon;
		this.setTitle(TITLE);
		this.setLayout(new BorderLayout());
		
		this.add(new ImagePanel());
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

}

class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;

	ImagePanel() {
		super();
		icon = new ImageIcon();
		Image edvin = null;
		try {
			URL url = new URL(
					"https://scontent-arn2-1.xx.fbcdn.net/hphotos-prn2/t31.0-8/10945063_10152636074767966_1813533636179366365_o.jpg");
			edvin = ImageIO.read(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		icon.setImage(edvin);
		JLabel label = new JLabel(icon);
		add(label, BorderLayout.NORTH);
		this.setSize(200, 200);
	}

	void refresh(byte[] data) {
		Image theImage = getToolkit().createImage(data);
		getToolkit().prepareImage(theImage, -1, -1, null);
		icon.setImage(theImage);
		icon.paintIcon(this, this.getGraphics(), 5, 5);
	}
}
