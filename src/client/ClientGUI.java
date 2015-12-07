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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import util.Command.CMD;
import util.ImageFrame;

public class ClientGUI extends JFrame {
	public static final String TITLE = "Real-Time Camera System";
	protected ClientMonitor monitor;
	private static final long serialVersionUID = 1L;
	private ImagePanel imagePanel;
	private JLabel delayLabel;
	private final JRadioButton idle, movie;
	private JTextArea field;

	public ClientGUI(final ClientMonitor monitor) {
		super();
		this.monitor = monitor;
		this.setPreferredSize(new Dimension(840, 640));
		this.setTitle(TITLE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		imagePanel = new ImagePanel(monitor, this);
		imagePanel.setVisible(true);
		imagePanel.setPreferredSize(new Dimension(640, 480));
		this.add(imagePanel, BorderLayout.WEST);

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

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					monitor.setMode(CMD.IDLE);
					createNotification(CMD.IDLE + " entered");
				}
			}

		});
		movie.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					monitor.setMode(CMD.MOVIE);
					createNotification(CMD.MOVIE + " entered");
				}
			}
		});
		ButtonGroup modes = new ButtonGroup();
		modes.add(idle);
		modes.add(movie);

		// Buttons for synchronized and asynchronized
		final JRadioButton sync = new JRadioButton("Synchronous", true);
		final JRadioButton async = new JRadioButton("Asynchronous", false);
		sync.setMnemonic(KeyEvent.VK_S);
		async.setMnemonic(KeyEvent.VK_X);
		sync.setEnabled(false);
		async.setEnabled(false);
		sync.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					monitor.setSyncMode(CMD.SYNC);
					createNotification(CMD.SYNC + " entered");
				}
			}
		});
		async.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					monitor.setSyncMode(CMD.ASYNC);
					createNotification(CMD.ASYNC + " entered");
				}
			}
		});
		ButtonGroup syncSet = new ButtonGroup();
		syncSet.add(sync);
		syncSet.add(async);

		// Label for displaying the current delay
		delayLabel = new JLabel("Delay: 0 ms");

		// Auto button
		final JCheckBox auto = new JCheckBox("Auto", true);
		auto.setMnemonic(KeyEvent.VK_A);
		auto.addActionListener(new ActionListener() {
			// This is the itemlistener for the auto button
			@Override
			public void actionPerformed(ActionEvent e) {
				if (auto.isSelected()) {
					sync.setEnabled(false);
					async.setEnabled(false);
					idle.setEnabled(false);
					movie.setEnabled(false);
					createNotification(CMD.AUTO + " entered");
					monitor.setSyncMode(CMD.AUTO);
					monitor.setMode(CMD.IDLE);
				} else {
					/*
					 * if (idle.isSelected()) { idle.doClick(); } else {
					 * movie.doClick(); } if (sync.isSelected()) {
					 * sync.doClick(); } else { async.doClick(); }
					 */

					idle.doClick();
					sync.doClick();
					monitor.setSyncMode(CMD.SYNC);
					monitor.setMode(CMD.IDLE);
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
		settings.add(delayLabel);
		this.add(settings, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	public void createNotification(String not) {
		field.append(Time.getCurrentTime() + ": " + not + "\n");
	}

	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public void updateDelay(double delay) {
		delayLabel.setText("Delay: " + Double.toString(delay) + " ms");
	}

	public void setMovieMode() {
		movie.doClick();
		movie.setSelected(true);
	}
	
	public void updateRadioButtons(CMD mode) {
//		System.out.println("sync mode: " + monitor.getSyncMode() + " recv mode: " + mode);
		if (monitor.getSyncMode() == CMD.AUTO) {
			if (mode == CMD.MOTION) {
				movie.setSelected(true);
				idle.setSelected(false);
			} else {
				idle.setSelected(true);
				movie.setSelected(false);
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

	ImagePanel(ClientMonitor monitor, ClientGUI gui) {
		super();
		this.gui = gui;
		this.monitor = monitor;
		icon = new ImageIcon();
		JLabel label = new JLabel(icon);
		this.setMinimumSize(new Dimension(640, 480));
		add(label, BorderLayout.NORTH);
	}

	public void refresh(ImageFrame image) {
		if (image == null) {
			System.out.println("ClientGUI - received null image");
			return;
		}

		// System.out.println("ClientGUI: Refreshing... Timestamp: "
		// + image.getTimestamp() + " image size: " + image.getImage().length);

		theImage = getToolkit().createImage(image.getImage());
		getToolkit().prepareImage(theImage, -1, -1, null);
		icon.setImage(theImage);
		icon.paintIcon(this, this.getGraphics(), 5, 5);
		double delay = System.currentTimeMillis() - image.getTimestamp();
		gui.updateDelay(delay);
		gui.updateRadioButtons(image.getMode());
	}
}

class Time {
	static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(cal.getTime());
	}
}