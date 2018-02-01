package me.codedmemory981.fakeapp_image_optimizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class Main {
	public static final String versionName = "1.1";
	public static final int versionCode = getLocalVersionCode();
	public static final int remoteVersionCode = getRemoteVersionCode();

	public static final String downloadURL = "https://github.com/CodedMemory981/FakeApp-ImageOptimizer/releases/latest";

	public static void main(String[] args) {
		System.out.println("Starting FakeApp-ImageOptimizer by CodedMemory981 v" + versionName);

		if (remoteVersionCode > versionCode) {
			System.out.println("You are running an outdated Version! Download the latest release at " + downloadURL);
		} else {
			System.out.println("You are running the latest release!");
		}

		boolean nogui = false, showVersion = false, showHelp = false;

		for (String arg : args) {
			if (arg.equalsIgnoreCase("-nogui")) {
				nogui = true;
			} else if (arg.equalsIgnoreCase("-version") || arg.equalsIgnoreCase("-ver")) {
				showVersion = true;
			} else if (arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("-?")) {
				showHelp = true;
			}

			if (nogui && showVersion && showHelp) {
				break;
			}
		}

		if (showVersion) {
			showVersion();
		} else if (showHelp) {
			showHelp();
		} else {
			if (nogui || args.length > 0) {
				File src = null, dest = null;

				for (String arg : args) {
					if (arg.toLowerCase().startsWith("-src:") || arg.toLowerCase().startsWith("-src=")) {
						src = new File(arg.substring(4));
					} else if (arg.toLowerCase().startsWith("-dest:") || arg.toLowerCase().startsWith("-dest=")) {
						dest = new File(arg.substring(5));
					}
				}

				if (src == null || dest == null) {
					showHelp();
				} else if (!src.exists()) {
					System.err.println("'src' does not exist!");
				} else if (!src.isDirectory()) {
					System.err.println("'src' is not a directory!");
				} else if (dest.exists() && !dest.isDirectory()) {
					System.err.println("'dest' is not a valid directory!");
				} else {
					try {
						Utils.startOptimizing(src, dest, new ProgressInterface() {
							@Override
							public void update(int currentFile, int fileCount) {
								if (fileCount == -1) {
									System.out.println("Status: Initialising");
								} else if (currentFile == -1) {
									System.out.println("Status: 0 / " + fileCount);
								} else if (currentFile >= fileCount) {
									System.out.println("Status: Success! (" + currentFile + " / " + fileCount + ")");
								} else {
									System.out.println("Status: " + currentFile + " / " + fileCount);
								}
							}
						});
					} catch (Exception ex) {
						ex.printStackTrace();
						System.err.println("Status: Failed [Unkown reason]");
					}
				}
			} else {
				GUI.init();
			}
		}
	}

	private static void showHelp() {
		System.out.println(
				"With this small tool, you can 'optimize' images to prevent some exceptions when using FakeApp");
		System.out.println("Valid arguments:");
		System.out.println("-src:PATH - A path to the directory with all the images");
		System.out.println(
				"-dest:PATH - A path to the directory where all optimized images will be stored [Should be empty - Overrides existing files!]");
		System.out.println(" ");
		System.out.println("-ver - Shows you the current version of this tool");
		System.out.println("-? - Prints these beautiful lines");
	}

	private static void showVersion() {
		System.out.println("VersionName: " + versionName);

		if (versionCode == -1) {
			System.out.println("VersionCode: Unknown");
		} else {
			System.out.println("VersionCode: " + versionCode);

			if (remoteVersionCode == -1) {
				System.out.println("Update: Could not check for Updates!");
			} else {
				if (remoteVersionCode <= versionCode) {
					System.out.println("Update: You are using the latest version!");
				} else {
					System.out.println("An update is available! - Current VersionCode: " + versionCode
							+ " | Latest VersionCode: " + remoteVersionCode);
					System.out.println("Download at " + downloadURL);
				}
			}
		}
	}

	private static int getLocalVersionCode() {
		int result = -1;

		Scanner s = null;
		try {
			s = new Scanner(new InputStreamReader(Main.class.getResourceAsStream("/versionCode")));

			result = s.nextInt();
		} catch (Exception ex) {
			System.err.println("Could not get versionCode. Seems like 'versionCode' is missing...");

			ex.printStackTrace();
		} finally {
			if (s != null) {
				s.close();
			}
		}

		return result;
	}

	private static int getRemoteVersionCode() {
		int result = -1;

		Scanner s = null;
		try {
			s = new Scanner(new URL(
					"https://raw.githubusercontent.com/CodedMemory981/FakeApp-ImageOptimizer/master/versionCode")
							.openStream());

			result = s.nextInt();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (s != null) {
				s.close();
			}
		}

		return result;
	}
}