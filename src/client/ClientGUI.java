package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import util.Command.CMD;
import util.ImageFrame;

import com.sun.glass.events.KeyEvent;

public class ClientGUI extends JFrame {
	public static final String TITLE = "Real-Time Camera System";
	protected ClientMonitor monitor;
	private static final long serialVersionUID = 1L;
	private ImagePanel imagePanel;
	private JLabel delayLabel;
	private double threshold;

	public ClientGUI(ClientMonitor monitor) {
		super();
		this.monitor = monitor;
		this.setMinimumSize(new Dimension(800, 600));
		this.setTitle(TITLE);
		this.setLayout(new BorderLayout());
		imagePanel = new ImagePanel(monitor, this);
		this.add(imagePanel, BorderLayout.NORTH);

		// Buttons for IDLE and MOVIE
		final JRadioButton idle = new JRadioButton("Idle", true);
		final JRadioButton movie = new JRadioButton("Movie", false);
		idle.setMnemonic(KeyEvent.VK_I);
		movie.setMnemonic(KeyEvent.VK_M);
		idle.setEnabled(false);
		movie.setEnabled(false);
		ItemListener item = new IdleAndMovieHandler(monitor);
		idle.addItemListener(item);
		movie.addItemListener(item);
		ButtonGroup modes = new ButtonGroup();
		modes.add(idle);
		modes.add(movie);

		// Buttons for synchronized and asynchronized
		final JRadioButton sync = new JRadioButton("Synchronous", true);
		final JRadioButton async = new JRadioButton("Asynchronous", false);
		sync.setMnemonic(KeyEvent.VK_S);
		async.setMnemonic(KeyEvent.VK_A);
		sync.setEnabled(false);
		async.setEnabled(false);
		SyncAndAsyncHandler hand = new SyncAndAsyncHandler(monitor);
		sync.addItemListener(hand);
		async.addItemListener(hand);
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
					sync.setSelected(true);
					idle.setSelected(true);
					sync.doClick();
					idle.doClick();
				} else {
					sync.setEnabled(true);
					async.setEnabled(true);
					idle.setEnabled(true);
					movie.setEnabled(true);
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

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public void updateDelay(double delay) {
		delayLabel.setText("Delay: " + Double.toString(delay) + " ms");
	}

}

class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	ClientMonitor monitor;
	ClientGUI gui;

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

		Image theImage = getToolkit().createImage(image.getImage());
		getToolkit().prepareImage(theImage, -1, -1, null);
		icon.setImage(theImage);
		icon.paintIcon(this, this.getGraphics(), 5, 5);
		double delay = System.currentTimeMillis() - image.getTimestamp();
		// System.out.println(delay);
		gui.updateDelay(delay);
	}
}

class SyncAndAsyncHandler implements ItemListener {
	private ClientMonitor mon;

	SyncAndAsyncHandler(ClientMonitor mon) {
		this.mon = mon;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		JRadioButton pressed = (JRadioButton) e.getSource();
		if (pressed.getText().equals("Synchronous")) {
			mon.setMode(CMD.SYNC);
		} else {
			mon.setMode(CMD.ASYNC);
		}
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
			mon.setMode(CMD.MOVIE);
		} else {
			mon.setMode(CMD.IDLE);
		}

	}
}
