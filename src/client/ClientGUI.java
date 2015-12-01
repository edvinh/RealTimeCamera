package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import util.Logger;

// Pax Robin 
public class ClientGUI extends JFrame {
	public static final String TITLE = "Real-Time Camera System";
	protected ClientMonitor monitor;
	private static final long serialVersionUID = 1L;
	private int currDelay;
	private Logger log = Logger.getInstance();

	public ClientGUI(ClientMonitor monitor) {
		super();
		currDelay = 0;
		this.monitor = monitor;
		this.setTitle(TITLE);
		this.setLayout(new BorderLayout());

		this.add(new ImagePanel(monitor), BorderLayout.NORTH);

		// Buttons for IDLE and MOVIE
		JRadioButton idle = new JRadioButton("Idle", true);
		JRadioButton movie = new JRadioButton("Movie", false);
		ItemListener item = new IdleAndMovieHandler(monitor);
		idle.addItemListener(item);
		movie.addItemListener(item);
		ButtonGroup modes = new ButtonGroup();
		modes.add(idle);
		modes.add(movie);

		// Buttons for synchronized and asynchronized
		JRadioButton sync = new JRadioButton("Synchronized", true);
		JRadioButton async = new JRadioButton("Asynchronized", false);
		ButtonGroup syncSet = new ButtonGroup();
		syncSet.add(sync);
		syncSet.add(async);

		// Label for displaying the current delay
		JLabel delay = new JLabel("Delay: " + Integer.toString(currDelay));

		// Auto button
		JRadioButton auto = new JRadioButton("Auto", true);

		JPanel settings = new JPanel();
		settings.setLayout(new FlowLayout());
		settings.add(auto);
		settings.add(idle);
		settings.add(movie);
		settings.add(sync);
		settings.add(async);
		settings.add(delay);
		this.add(settings, BorderLayout.SOUTH);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

}

class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	ClientMonitor monitor;
	Logger log = Logger.getInstance();

	ImagePanel(final ClientMonitor monitor) {
		super();
		this.monitor = monitor;
		icon = new ImageIcon();
		JLabel label = new JLabel(icon);
		add(label, BorderLayout.NORTH);
		this.setSize(200, 200);

	}

	void refresh(final byte[] data) {
		if (data == null) {
			log.debug("ClientGUI - received null image");
			return;
		}
		final JPanel self = this;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Image theImage = getToolkit().createImage(data);
				getToolkit().prepareImage(theImage, -1, -1, null);
				icon.setImage(theImage);
				icon.paintIcon(self, self.getGraphics(), 5, 5);
			}

		});
	}
}

class IdleAndMovieHandler implements ItemListener {
	private ClientMonitor mon;

	IdleAndMovieHandler(ClientMonitor mon) {
		this.mon = mon;
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		JRadioButton pressed = (JRadioButton) event.getSource();
		if (pressed.getText().equals("Movie")) {
			// Update period
		} else {
			// Update period
		}

	}
}
