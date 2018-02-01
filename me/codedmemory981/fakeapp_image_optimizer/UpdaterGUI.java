package me.codedmemory981.fakeapp_image_optimizer;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class UpdaterGUI {
	private static JFrame frmAnUpdateIs;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void init() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private static void initialize() {
		frmAnUpdateIs = new JFrame();
		frmAnUpdateIs.setType(Type.POPUP);
		frmAnUpdateIs.setTitle("An update is available!");
		frmAnUpdateIs.setBounds(100, 100, 491, 199);
		frmAnUpdateIs.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAnUpdateIs.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel("An update is available!");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 11, 455, 20);
		frmAnUpdateIs.getContentPane().add(lblTitle);

		JLabel lblVersionsBehind = new JLabel(
				"<html>You are <b>" + (Main.remoteVersionCode - Main.versionCode) + "</b> version(s) behind!</html>");
		lblVersionsBehind.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblVersionsBehind.setBounds(10, 42, 455, 20);
		frmAnUpdateIs.getContentPane().add(lblVersionsBehind);

		JLabel lblDownload = new JLabel("Download latest version here:");
		lblDownload.setBounds(10, 73, 414, 20);
		frmAnUpdateIs.getContentPane().add(lblDownload);

		JLabel lblURL = new JLabel("<html><a href=\"" + Main.downloadURL + "\">" + Main.downloadURL + "</a></html>");
		lblURL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Desktop.getDesktop().browse(new URL(Main.downloadURL).toURI());
					} catch (IOException | URISyntaxException ex) {
						ex.printStackTrace();
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				frmAnUpdateIs.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				frmAnUpdateIs.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		lblDownload.setLabelFor(lblURL);
		lblURL.setFont(new Font("Consolas", Font.PLAIN, 11));
		lblURL.setBounds(10, 88, 455, 20);
		frmAnUpdateIs.getContentPane().add(lblURL);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAnUpdateIs.dispose();
			}
		});
		btnClose.setBounds(376, 126, 89, 23);
		frmAnUpdateIs.getContentPane().add(btnClose);

		frmAnUpdateIs.setVisible(true);
	}
}