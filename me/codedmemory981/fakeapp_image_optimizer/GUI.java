package me.codedmemory981.fakeapp_image_optimizer;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI {
	private static JFrame frmfakeappimageoptimizerV;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void init() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();

					frmfakeappimageoptimizerV.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private static void initialize() {
		frmfakeappimageoptimizerV = new JFrame();
		frmfakeappimageoptimizerV.setTitle("FakeApp-ImageOptimizer v" + Main.versionName);
		frmfakeappimageoptimizerV.setBounds(100, 100, 450, 246);
		frmfakeappimageoptimizerV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmfakeappimageoptimizerV.getContentPane().setLayout(null);

		JLabel lblSrcDir = new JLabel("Source-Directory");
		lblSrcDir.setBounds(10, 11, 414, 15);
		frmfakeappimageoptimizerV.getContentPane().add(lblSrcDir);

		JTextField srcTextField = new JTextField();
		lblSrcDir.setLabelFor(srcTextField);
		srcTextField.setFont(new Font("Consolas", Font.PLAIN, 11));
		srcTextField.setToolTipText("A path to directory with images that should be used by FakeApp for extracting");
		srcTextField.setBounds(10, 37, 291, 25);
		frmfakeappimageoptimizerV.getContentPane().add(srcTextField);
		srcTextField.setColumns(10);

		JButton srcBrowseButton = new JButton("Browse");
		srcBrowseButton.setToolTipText("Not yet available");
		srcBrowseButton.setEnabled(false);
		srcBrowseButton.setBounds(335, 37, 89, 25);
		frmfakeappimageoptimizerV.getContentPane().add(srcBrowseButton);

		JLabel lblDestDir = new JLabel("Destination-Directory");
		lblDestDir.setBounds(10, 73, 414, 15);
		frmfakeappimageoptimizerV.getContentPane().add(lblDestDir);

		JTextField destTextField = new JTextField();
		destTextField.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblDestDir.setLabelFor(destTextField);
		destTextField.setToolTipText(
				"<html>A path to the directory where all optimized images should be saved. <b>Overrides existing files!</b></html>");
		destTextField.setBounds(10, 99, 291, 25);
		frmfakeappimageoptimizerV.getContentPane().add(destTextField);
		destTextField.setColumns(10);

		JButton destBrowseButton = new JButton("Browse");
		destBrowseButton.setToolTipText("Not yet available");
		destBrowseButton.setEnabled(false);
		destBrowseButton.setBounds(335, 99, 89, 23);
		frmfakeappimageoptimizerV.getContentPane().add(destBrowseButton);

		JLabel lblStatus = new JLabel("<html><b>Status</b>: Not started yet</html>");
		lblStatus.setBounds(10, 135, 414, 15);
		frmfakeappimageoptimizerV.getContentPane().add(lblStatus);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (srcTextField.getText().trim().isEmpty() || !new File(srcTextField.getText()).exists()) {
					lblStatus
							.setText("<html><b>Status</b>: Failed [Invalid Source-Directory! (Does it exist?)]</html>");
				} else if (destTextField.getText().trim().isEmpty()) {
					lblStatus.setText("<html><b>Status</b>: Failed [Invalid Destination-Directory!]</html>");
				} else if (destTextField.getText().trim().equalsIgnoreCase(srcTextField.getText().trim())) {
					lblStatus.setText(
							"<html><b>Status</b>: Failed [Source- and Destination-Directory are identical!]</html>");
				} else {
					btnStart.setEnabled(false);

					try {
						Utils.startOptimizing(new File(srcTextField.getText()), new File(destTextField.getText()),
								new ProgressInterface() {
									@Override
									public void update(int currentFile, int fileCount) {
										if (fileCount == -1) {
											lblStatus.setText("<html><b>Status</b>: Initialising</html>");
										} else if (currentFile == -1) {
											lblStatus.setText("<html><b>Status</b>: 0 / " + fileCount + "</html>");
										} else if (currentFile >= fileCount) {
											lblStatus.setText("<html><b>Status</b>: <i>Success!</i> (" + currentFile
													+ " / " + fileCount + ")</html>");
											btnStart.setEnabled(true);
										} else {
											lblStatus.setText("<html><b>Status</b>: " + currentFile + " / " + fileCount
													+ "</html>");
										}
									}
								});
					} catch (Exception ex) {
						ex.printStackTrace();

						lblStatus.setText("<html><b>Status</b>: Failed [Unkown reason]</html>");
					}
				}
			}
		});
		btnStart.setBounds(173, 173, 89, 23);
		frmfakeappimageoptimizerV.getContentPane().add(btnStart);
	}
}