package me.codedmemory981.fakeapp_image_optimizer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

public class Utils {
	private static ExecutorService pool;

	private static final int maxImageSizeX = 900, maxImageSizeY = 900;

	public static void startOptimizing(File src, File dest, ProgressInterface onProgress) throws Exception {
		if (pool == null && src.exists() && (!dest.exists() || dest.isDirectory())) {
			pool = Executors.newSingleThreadExecutor();

			onProgress.update(-1, -1);

			dest.mkdirs();

			pool.execute(new Runnable() {
				@Override
				public void run() {
					ArrayList<File> images = new ArrayList<>();

					for (File imgFile : src.listFiles()) {
						if (imgFile.isFile()) {
							try {
								ImageIO.read(imgFile).flush();

								images.add(imgFile);

								onProgress.update(-1, images.size());
							} catch (IOException ex) {
								System.out.println("Skipping file '" + imgFile.getName() + "' - Valid image? ("
										+ ex.getLocalizedMessage() + ")");
							}
						}
					}

					int i = 0;
					for (File imgFile : images) {
						String ext = FilenameUtils.getExtension(imgFile.getName()).toLowerCase();

						String format = "PNG";

						if (ext.equals("jpg") || ext.equals("jpeg")) {
							format = "JPG";
						}

						BufferedImage img = null;
						try {
							img = ImageIO.read(imgFile);
						} catch (IOException ex) {
							ex.printStackTrace();
						}

						if (img != null) {
							img = resizeImage(img);

							try {
								ImageIO.write(img, format, new File(dest, i + "." + format.toLowerCase()));
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}

						i++;

						onProgress.update(i, images.size());
					}
				}
			});
		}
	}

	private static BufferedImage resizeImage(BufferedImage org) {
		Dimension dim = getScaledDimension(new Dimension(org.getWidth(), org.getHeight()),
				new Dimension(maxImageSizeX, maxImageSizeY));

		BufferedImage result = new BufferedImage(Double.valueOf(dim.getWidth()).intValue(),
				Double.valueOf(dim.getHeight()).intValue(), 5);
		Graphics2D g2D = result.createGraphics();

		g2D.drawImage(org, 0, 0, result.getWidth(), result.getHeight(), null);
		g2D.dispose();

		return result;
	}

	private static Dimension getScaledDimension(Dimension imageSize, Dimension maxSize) {
		double ratio = Math.min(maxSize.getWidth() / imageSize.getWidth(), maxSize.getHeight() / imageSize.getHeight());

		return new Dimension((int) (imageSize.width * ratio), (int) (imageSize.height * ratio));
	}
}

interface ProgressInterface {
	void update(int currentFile, int fileCount);
}