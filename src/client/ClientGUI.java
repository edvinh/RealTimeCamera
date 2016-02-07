package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import util.Command.CMD;
import util.ImageFrame;

public class ClientGUI extends JFrame {
	public static final String TITLE = "Real-Time Camera System";
	protected ClientMonitor[] monitors;
	private static final long serialVersionUID = 1L;
	private ImagePanel[] imagePanels;
	private final JRadioButton idle, movie;
	private JTextArea field;
	private JRadioButton sync, async;
	private JLabel[] delays;

	// Checks if the previous image was synced or not.
	private boolean oldImageSync = true;

	public ClientGUI(ClientMonitor monitor1, ClientMonitor monitor2, int nbrOfServers) {
		super();
		monitors = new ClientMonitor[nbrOfServers];
		monitors[0] = monitor1;
		monitors[1] = monitor2;
		this.setPreferredSize(new Dimension(nbrOfServers * 840 - 170, 640));
		this.setTitle(TITLE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		imagePanels = new ImagePanel[nbrOfServers];

		// ImagePanel settings
		JPanel imageHolder = new JPanel();

		for (int i = 0; i < nbrOfServers; i++) {
			ImagePanel im = new ImagePanel(monitors[i], this);
			im.setVisible(true);
			im.setPreferredSize(new Dimension(640, 480));
			imageHolder.add(im);
			imagePanels[i] = im;
		}

		this.add(imageHolder, BorderLayout.WEST);

		// Notification field
		field = new JTextArea(Time.getCurrentTime() + ": System started \n");
		JScrollPane pane = new JScrollPane(field);
		pane.setPreferredSize(new Dimension(180, 430));
		this.add(pane, BorderLayout.EAST);
		field.setEditable(false);
		field.setVisible(true);
		field.setLineWrap(true);
		field.setWrapStyleWord(true);

		// Buttons for IDLE and MOVIE
		idle = new JRadioButton("Idle", true);
		movie = new JRadioButton("Movie", false);
		idle.setMnemonic(KeyEvent.VK_I);
		movie.setMnemonic(KeyEvent.VK_M);
		idle.setEnabled(false);
		movie.setEnabled(false);
		idle.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					monitors[0].setMode(CMD.IDLE);
					monitors[1].setMode(CMD.IDLE);
					createNotification(CMD.IDLE + " entered");
				}
			}

		});
		movie.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					monitors[0].setMode(CMD.MOVIE);
					monitors[1].setMode(CMD.MOVIE);
					createNotification(CMD.MOVIE + " entered");
				}
			}
		});
		ButtonGroup modes = new ButtonGroup();
		modes.add(idle);
		modes.add(movie);

		// Buttons for synchronized and asynchronized
		sync = new JRadioButton("Synchronous", true);
		async = new JRadioButton("Asynchronous", false);
		sync.setMnemonic(KeyEvent.VK_S);
		async.setMnemonic(KeyEvent.VK_X);
		sync.setEnabled(false);
		async.setEnabled(false);
		sync.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					monitors[0].setSyncMode(CMD.SYNC);
					monitors[1].setSyncMode(CMD.SYNC);
					createNotification(CMD.SYNC + " entered");
				}
			}
		});
		async.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					monitors[0].setSyncMode(CMD.ASYNC);
					monitors[1].setSyncMode(CMD.ASYNC);
					createNotification(CMD.ASYNC + " entered");
				}
			}
		});
		ButtonGroup syncSet = new ButtonGroup();
		syncSet.add(sync);
		syncSet.add(async);

		// Label for displaying the current delay
		// delayLabel = new JLabel("Delay: 0 ms");

		// Auto button
		final JCheckBox auto = new JCheckBox("Auto", true);
		auto.setMnemonic(KeyEvent.VK_A);
		auto.addActionListener(new ActionListener() {
			// This is the itemlistener for the auto button
			public void actionPerformed(ActionEvent e) {
				if (auto.isSelected()) {
					sync.setEnabled(false);
					async.setEnabled(false);
					idle.setEnabled(false);
					movie.setEnabled(false);
					createNotification(CMD.AUTO + " entered");
					monitors[0].setMode(CMD.IDLE);
					monitors[0].setSyncMode(CMD.AUTO);
					monitors[1].setMode(CMD.IDLE);
					monitors[1].setSyncMode(CMD.AUTO);
				} else {

					idle.doClick();
					sync.doClick();
					monitors[0].setSyncMode(CMD.SYNC);
					monitors[0].setMode(CMD.IDLE);
					monitors[1].setSyncMode(CMD.SYNC);
					monitors[1].setMode(CMD.IDLE);
					idle.setSelected(true);
					sync.setEnabled(true);
					async.setEnabled(true);
					idle.setEnabled(true);
					movie.setEnabled(true);
					createNotification(CMD.AUTO + " exited");
				}
			}
		});
		JPanel settings = new JPanel();
		settings.setLayout(new FlowLayout());
		settings.add(auto);
		settings.add(idle);
		settings.add(movie);
		settings.add(sync);
		settings.add(async);

		// Delay labels
		delays = new JLabel[nbrOfServers];
		for (int i = 0; i < nbrOfServers; i++) {
			delays[i] = new JLabel("Camera Delay #" + (i + 1) + ": 0 ms");
			settings.add(delays[i]);
		}

		this.add(settings, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	public void createNotification(String not) {
		field.append(Time.getCurrentTime() + ": " + not + "\n");
	}

	public ImagePanel[] getImagePanels() {
		return imagePanels;
	}

	public void updateDelay() {
		for (int i = 0; i < imagePanels.length; i++) {
			String delay = Long.toString(imagePanels[i].delay);
			delays[i].setText("Camera #" + (i + 1) + " Delay: " + delay);
		}
	}

	/* Update the radio buttons, mode for IDLE/MOVIE, syncMode for ASYNC/SYNC */
	public void updateRadioButtons(CMD mode) {
		if (monitors[0].getSyncMode() == CMD.AUTO) {
			if (mode == CMD.MOTION) {
				movie.setSelected(true);
				idle.setSelected(false);
			}
		}
	}
}

class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	ClientMonitor monitor;
	ClientGUI gui;
	Image theImage;
	JLabel imageLabel;
	long delay;

	ImagePanel(ClientMonitor monitor, ClientGUI gui) {
		super();
		this.gui = gui;
		this.monitor = monitor;
		icon = new ImageIcon();
		delay = 0;
		imageLabel = new JLabel(icon);
		this.setPreferredSize(new Dimension(640, 480));
		add(imageLabel, BorderLayout.NORTH);
	}

	/**
	 * Refreshes the panel with a new image. <b>syncedImage</b> determines if
	 * the image was updated in synchronized mode or not.
	 * 
	 * @param image
	 *            The image
	 * @param syncedImage
	 *            true if image was updated in synchronized mode, else false.
	 * 
	 */
	public void refresh(ImageFrame image) {
		if (image == null) {
			System.out.println("ClientGUI - received null image");
			return;
		}

		theImage = getToolkit().createImage(image.getImage());
		getToolkit().prepareImage(theImage, -1, -1, null);
		icon.setImage(theImage);
		icon.paintIcon(this, this.getGraphics(), 5, 5);
		delay = System.currentTimeMillis() - image.getTimestamp();
		gui.updateRadioButtons(image.getMode());
		gui.updateDelay();
	}
}

class Time {
	static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(cal.getTime());
	}
}