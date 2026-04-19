package com.budaos.utils.tool;

import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 视频工具类
 * @author zhuq
 *
 */
public class VideoUtils {


	private static Logger logger = LoggerFactory.getLogger(VideoUtils.class);
	
	/**
	 * 获取指定视频的第1帧图片
	 * @param videoFileFullName 视频所在绝对路径
	 * @param savePath 图片保存路径
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(String videoFileFullName, String savePath) {
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFileFullName), savePath, 1, null, null, null);
	}
	
	/**
	 * 获取指定视频的第1帧图片
	 * @param videoFileFullName 视频所在绝对路径
	 * @param savePath 图片保存路径
	 * @param picName 图片保存名称
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(String videoFileFullName, String savePath, String picName) {
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFileFullName), savePath, 1, null, null, picName);
	}
	
	/**
	 * 获取指定视频的指定帧图片
	 * @param videoFileFullName 视频所在绝对路径
	 * @param savePath 图片保存路径
	 * @param frameIndex 要截取的视频帧数
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(String videoFileFullName, String savePath, Integer frameIndex) {
		frameIndex = frameIndex == null ? 1 : frameIndex;
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFileFullName), savePath, frameIndex, null, null, null);
	}
	
	/**
	 * 获取指定视频的指定帧图片
	 * @param videoFileFullName 视频所在绝对路径
	 * @param savePath 图片保存路径
	 * @param frameIndex 要截取的视频帧数
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(String videoFileFullName, String savePath, Integer frameIndex, Integer width, Integer height) {
		frameIndex = frameIndex == null ? 1 : frameIndex;
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFileFullName), savePath, frameIndex, width, height, null);
	}
	
	/**
	 * 获取指定视频的第1帧图片
	 * @param videoFileFullName 视频文件对象
	 * @param savePath 图片保存路径
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(File videoFile, String savePath) {
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFile), savePath, 1, null, null, null);
	}
	/**
	 * 获取指定视频的第1帧图片
	 * @param videoFileFullName 视频文件对象
	 * @param savePath 图片保存路径
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(File videoFile, String savePath, String picName) {
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFile), savePath, 1, null, null, picName);
	}
	/**
	 * 获取指定视频的指定帧图片
	 * @param videoFileFullName 视频文件对象
	 * @param savePath 图片保存路径
	 * @param frameIndex 要截取的视频帧数
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(File videoFile, String savePath, Integer frameIndex) {
		frameIndex = frameIndex == null ? 1 : frameIndex;
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFile), savePath, frameIndex, null, null, null);
	}

	/**
	 * 获取指定视频的指定帧图片
	 * @param videoFileFullName 视频文件对象
	 * @param savePath 图片保存路径
	 * @param frameIndex 要截取的视频帧数
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(File videoFile, String savePath, Integer frameIndex, Integer width, Integer height) {
		frameIndex = frameIndex == null ? 1 : frameIndex;
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFile), savePath, frameIndex, width, height, null);
	}
	
	/**
	 * 获取指定视频的第1帧图片
	 * @param videoFileFullName 视频流
	 * @param savePath 图片保存路径
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(InputStream videoFileIS, String savePath) {
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFileIS), savePath, 1, null, null, null);
	}
	
	/**
	 * 获取指定视频的指定帧图片
	 * @param videoFileFullName 视频流
	 * @param savePath 图片保存路径
	 * @param frameIndex 要截取的视频帧数
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(InputStream videoFileIS, String savePath, Integer frameIndex) {
		frameIndex = frameIndex == null ? 1 : frameIndex;
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFileIS), savePath, frameIndex, null, null, null);
	}
	
	/**
	 * 获取指定视频的指定帧图片
	 * @param videoFileFullName 视频流
	 * @param savePath 图片保存路径
	 * @param frameIndex 要截取的视频帧数
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @return 图片名称
	 */
	public static String getVideoImageByFrame(InputStream videoFileIS, String savePath, Integer frameIndex, Integer width, Integer height) {
		frameIndex = frameIndex == null ? 1 : frameIndex;
		return grabberVideoFramer(new FFmpegFrameGrabber(videoFileIS), savePath, frameIndex, width, height, null);
	}
	
	/**
	 * 将视频文件帧处理并以“jpg”格式进行存储
	 *
	 * @param videoFileName
	 */
	private static String grabberVideoFramer(FFmpegFrameGrabber fFmpegFrameGrabber, String savePath, Integer frameIndex, Integer width, Integer height, String picName) {
		// 最后获取到的视频的图片的路径
		String videPicture = "";
		// Frame对象
		Frame frame = null;
		// 标识
		int flag = 0;
		try {
			fFmpegFrameGrabber.start();
			// 获取视频总帧数
			int ftp = fFmpegFrameGrabber.getLengthInFrames();
			while (flag <= ftp) {
				frame = fFmpegFrameGrabber.grabImage();
				//对视频的指定帧进行处理
				if (frame != null && flag == frameIndex) {
					videPicture = StrUtils.isEmpty(picName) ? UUID.randomUUID().toString() + "_" + String.valueOf(flag) + ".jpg" : picName;
					// 文件绝对路径+名字
					String fileName = savePath + (savePath.endsWith("/") || savePath.endsWith("\\") ? "" : File.separator) + videPicture;
					// 文件储存对象
					File outPut = new File(fileName);
					BufferedImage frameToBufferedImage = FrameToBufferedImage(frame);
					if(width != null && height != null) {
						frameToBufferedImage = Thumbnails.of(frameToBufferedImage).size(width, height).keepAspectRatio(false).asBufferedImage();
					}
					ImageIO.write(frameToBufferedImage, "jpg", outPut);
					break;
				}
				flag++;
			}
			fFmpegFrameGrabber.stop();
			fFmpegFrameGrabber.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return videPicture;
	}

	private static BufferedImage FrameToBufferedImage(Frame frame) {
		// 创建BufferedImage对象
		Java2DFrameConverter converter = new Java2DFrameConverter();
		BufferedImage bufferedImage = converter.getBufferedImage(frame);
		return bufferedImage;
	}
	
	/**
	 * 获取音频/视频时长
	 * @param filePath 音频视频绝对路径
	 * @return 时长(单位秒)
	 */
	public static Long getDuration(String filePath) {
		return getDuration(new FFmpegFrameGrabber(filePath));
	}
	
	/**
	 * 获取音频/视频时长
	 * @param file 音频/视频文件对象
	 * @return 时长(单位秒)
	 */
	public static Long getDuration(File file) {
		return getDuration(new FFmpegFrameGrabber(file));
	}
	
	/**
	 * 获取音频/视频时长
	 * @param is 音频/视频流
	 * @return 时长(单位秒)
	 */
	public static Long getDuration(InputStream is) {
		return getDuration(new FFmpegFrameGrabber(is));
	}
	
	private static Long getDuration(FFmpegFrameGrabber ff) {
		Long dura = 0L;
		try {
			ff.start();
			dura = ff.getLengthInTime() / 100000;
			ff.stop();
			ff.close();
		} catch (Exception e) {
			e.printStackTrace();
			return dura;
		}
		if(dura % 10 >= 7) {
			return dura / 10 + 1;
		}
		return dura / 10;
	}
	
	/**
	 * @param inputFile:需要转换的视频绝对路径
	 * @param outputFile：转换后的视频绝对路径
	 * @return
	 */
	public static boolean convertVideo(String inputFile, String outputFile) {
		if (!checkfile(inputFile)) {
			return false;
		}
		if (process(inputFile, outputFile)) {
			return true;
		}
		return false;
	}
 
	// 检查文件是否存在
	private static boolean checkfile(String path) {
		File file = new File(path);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}
 
	/**
	 * @param inputFile
	 * @param outputFile
	 * @return
	 * 转换视频文件
	 */
	private static boolean process(String inputFile, String outputFile) {
		int type = checkContentType(inputFile);
		boolean status = false;
		if (type == 0) {
			status = processFLV(inputFile, outputFile);// 直接将文件转为flv文件
		} else if (type == 1) {
			String avifilepath = processAVI(type, inputFile);
			if (avifilepath == null)
				return false;// avi文件没有得到
			status = processFLV(avifilepath, outputFile);// 将avi转为flv
		}
		return status;
	}
 
	private static int checkContentType(String inputFile) {
		String type = inputFile.substring(inputFile.lastIndexOf(".") + 1,
				inputFile.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		if (type.equals("avi")) {
			return 0;
		} else if (type.equals("mpg")) {
			return 0;
		} else if (type.equals("wmv")) {
			return 0;
		} else if (type.equals("3gp")) {
			return 0;
		} else if (type.equals("mov")) {
			return 0;
		} else if (type.equals("mp4")) {
			return 0;
		} else if (type.equals("asf")) {
			return 0;
		} else if (type.equals("asx")) {
			return 0;
		} else if (type.equals("flv")) {
			return 0;
		}
		// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
		// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		else if (type.equals("wmv9")) {
			return 1;
		} else if (type.equals("rm")) {
			return 1;
		} else if (type.equals("rmvb")) {
			return 1;
		}
		return 9;
	}
	// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）直接转换为目标视频
	private static boolean processFLV(String inputFile, String outputFile) {
		if (!checkfile(inputFile)) {
			return false;
		}
		List<String> commend = new ArrayList<String>();
		commend.add("ffmpeg");
		commend.add("-i");
		commend.add(inputFile);
		commend.add("-ab");
		commend.add("128");
		commend.add("-acodec");
		commend.add("libmp3lame");
		commend.add("-ac");
		commend.add("1");
		commend.add("-ar");
		commend.add("22050");
		commend.add("-r");
		commend.add("29.97");
		//高品质 
		commend.add("-qscale");
		commend.add("6");
		//低品质
//		commend.add("-b");
//		commend.add("512");
		commend.add("-y");
		
		commend.add(outputFile);
		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++) {
			test.append(commend.get(i) + " ");
		}
		logger.debug(test.toString());
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process start = builder.start();
			long startTime = new Date().getTime();
			final InputStream is1 = start.getInputStream();
			final InputStream is2 = start.getErrorStream();
			new Thread() {
				public void run() {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is1));
					try {
						String lineB = null;
						while ((lineB = br.readLine()) != null) {
							if (lineB != null)
								logger.debug(lineB);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(is2));
					try {
						String lineC = null;
						while ((lineC = br2.readLine()) != null) {
							if (lineC != null)
								logger.debug(lineC);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			start.waitFor();
			long endTime = new Date().getTime();
			logger.debug("共耗时:" + (endTime - startTime));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
	// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
	private static String processAVI(int type, String inputFile) {
		String avifilepath = inputFile.substring(0, inputFile.lastIndexOf(".")) + ".avi";
		File file = new File(avifilepath);
		if (file.exists())
			file.delete();
		List<String> commend = new ArrayList<String>();
		commend.add("mencoder");
		commend.add(inputFile);
		commend.add("-oac");
		commend.add("mp3lame");
		commend.add("-lameopts");
		commend.add("preset=64");
		commend.add("-ovc");
		commend.add("xvid");
		commend.add("-xvidencopts");
		commend.add("bitrate=600");
		commend.add("-of");
		commend.add("avi");
		commend.add("-o");
		commend.add(avifilepath);
		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++) {
			test.append(commend.get(i) + " ");
		}
		logger.debug(test.toString());
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process p = builder.start();
 
			final InputStream is1 = p.getInputStream();
			final InputStream is2 = p.getErrorStream();
			new Thread() {
				public void run() {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is1));
					try {
						String lineB = null;
						while ((lineB = br.readLine()) != null) {
							if (lineB != null)
								logger.debug(lineB);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(is2));
					try {
						String lineC = null;
						while ((lineC = br2.readLine()) != null) {
							if (lineC != null)
								logger.debug(lineC);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			// 等Mencoder进程转换结束，再调用ffmepg进程
			p.waitFor();
			return avifilepath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws Exception{
		//视频按帧截图
		//VideoUtils.getVideoImageByFrame("D:\\test\\video\\3.mp4", "D:\\test\\img");
		//调用摄像头
		/*
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        grabber.start();   //开始获取摄像头数据
        CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        int ex = 0;
        while(true){
            if(!canvas.isDisplayable()){//窗口是否关闭
                grabber.stop();//停止抓取
                System.exit(2);//退出
                break;
            }    
            canvas.showImage(grabber.grab());//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
            opencv_core.Mat mat = converter.convertToMat(grabber.grabFrame());
            ex++;
            opencv_imgcodecs.imwrite("d:\\test\\img\\" + ex + ".png", mat);
            Thread.sleep(200);//50毫秒刷新一次图像
        }
        */
	}
}
