package com.knowology.km.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class FileHandle {

	Logger log = Logger.getLogger(FileHandle.class);

	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            String 如D:/test.txt
	 * @return boolean
	 */
	public void newFolder(String folderPath) {
		try {
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			log.error("新建目录操作出错");
		}
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如D:/test.txt
	 * @param fileContent
	 *            String 文件内容
	 * @return boolean
	 */
	public void newFile(String filePathAndName, String fileContent) {

		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			resultFile.close();

		} catch (Exception e) {
			log.error("新建目录操作出错");
		}

	}

	/**
	 * 将Map中的内容写入到文件中,若文件不存在,则新建文件
	 * 
	 * @param map
	 *            存储明星信息
	 * @param filePathAndName
	 *            要写入信息的路径和文件名
	 * @param append
	 *            文件是否可扩展
	 */
	public void saveStarToFile(Map<String, String> starsMap,
			String filePathAndName, boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			fw.write("[" + "{" + "\"原称\"" + ":" + "\""
					+ starsMap.get("originalName") + "\"" + "," + "\"外文名称\""
					+ ":" + "\"" + starsMap.get("foreignName") + "\"" + ","
					+ "\"别称\"" + ":" + "\"" + starsMap.get("alias") + "\""
					+ "," + "\"渠道\"" + ":" + "\"" + "明星" + "\"" + "}" + "]"
					+ "\n");
			log.info("saving" + starsMap.get("originalName") + "success...");
			fw.close();
		} catch (IOException e) {
			log.info("保存明星文件出错" + e.getMessage());
		}
	}

	/**
	 * 将Map中的内容写入到文件中,若文件不存在,则新建文件
	 * 
	 * @param map
	 *            存储电影信息
	 * @param filePathAndName
	 *            要写入信息的路径和文件名
	 * @param append
	 *            文件是否可扩展
	 */
	public void saveMovieToFile(Map<String, String> movieMap,
			String filePathAndName, boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			String originalName = movieMap.get("originalName");
			String foreignName = movieMap.get("foreignName");
			String director = movieMap.get("director");
			String actor = movieMap.get("actor");
			String showTime = movieMap.get("showTime");

			Date now = new Date();
			DateFormat d = DateFormat.getDateTimeInstance();
			String time = d.format(now);
			fw.write("名称：" + originalName + "#外文名：" + foreignName + "#导演："
					+ director + "#演员：" + actor + "#上映时间：" + showTime
					+ "#取数据时间：" + time + "\n");
			log.info("saving" + movieMap.get("originalName") + "success...");
			fw.close();
		} catch (IOException e) {
			log.info("保存电影文件出错" + e.getMessage());
		}
	}
	
	
	/**
	 * 将Map中的内容写入到文件中,若文件不存在,则新建文件
	 * 
	 * @param map
	 *            存储电影信息
	 * @param filePathAndName
	 *            要写入信息的路径和文件名
	 * @param append
	 *            文件是否可扩展
	 */
	public void saveNewMovieToFile(Map<String, String> movieMap,
			String filePathAndName, boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			String originalName = movieMap.get("originalName");
			String foreignName = movieMap.get("foreignName");
			String director = movieMap.get("director");
			String actor = movieMap.get("actor");
			String showTime = movieMap.get("showTime");

			Date now = new Date();
			DateFormat d = DateFormat.getDateTimeInstance();
			String time = d.format(now);
//			fw.write("名称：" + originalName + "#外文名：" + foreignName + "#导演："
//					+ director + "#演员：" + actor + "#上映时间：" + showTime
//					+ "#取数据时间：" + time + "\n");
			fw.write("名称：" + originalName  + "\n");
			log.info("saving" + movieMap.get("originalName") + "success...");
			fw.close();
		} catch (IOException e) {
			log.info("保存电影文件出错" + e.getMessage());
		}
	}
	
	
	

	public Map<String, String> ReadExistName(String filePathAndName) {
		Map<String, String> existNameMap = new HashMap<String, String>();
		File file = new File(filePathAndName);
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			if (!file.exists())
				return existNameMap;
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
//				String movieName = tempString.substring(
//						tempString.indexOf("：") + 1, tempString.indexOf("#"));
//				existNameMap.put(movieName, null);
				existNameMap.put(tempString, null);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return existNameMap;
	}

	/**
	 * 读出电影href
	 * @return list
	 */
	public  List<String> ReadMovieHref(String filePathAndName){
		List<String> existHref = new ArrayList<String>();
		File file = new File(filePathAndName);
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			if (!file.exists())
				return existHref;
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
//				String movieName = tempString.substring(
//						tempString.indexOf("：") + 1, tempString.indexOf("#"));
//				existNameMap.put(movieName, null);
				existHref.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return existHref;
	}
	
	/**
	 * 
	 * @return list
	 */
	public  String ReadFile(String filePathAndName){
		List<String> content = new ArrayList<String>();
		StringBuffer  stringBuffer =  new StringBuffer();
		File file = new File(filePathAndName);
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			if (!file.exists())
				return "";
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
                 stringBuffer.append(tempString);
				
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return stringBuffer.toString();
	}
	
	
	/**
	 * 将Map中的内容写入到文件中,若文件不存在,则新建文件
	 * 
	 * @param map
	 *            存储电视剧信息
	 * @param filePathAndName
	 *            要写入信息的路径和文件名
	 * @param append
	 *            文件是否可扩展
	 */
	public void saveTeleplayToFile(Map<String, String> teleplayMap,
			String filePathAndName, boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			String originalName = teleplayMap.get("originalName");
			String foreignName = teleplayMap.get("foreignName");
			String director = teleplayMap.get("director");
			String actor = teleplayMap.get("actor");
			String showTime = teleplayMap.get("showTime");

			Date now = new Date();
			DateFormat d = DateFormat.getDateTimeInstance();
			String time = d.format(now);
			fw.write("名称：" + originalName + "#外文名：" + foreignName + "#导演："
					+ director + "#演员：" + actor + "#集数：" + showTime
					+ "#取数据时间：" + time + "\n");
			log.info("saving" + teleplayMap.get("originalName") + "success...");
			fw.close();
		} catch (IOException e) {
			log.info("保存电视剧文件出错" + e.getMessage());
		}
	}
	
	
	/**
	 * 将Map中的内容写入到文件中,若文件不存在,则新建文件
	 * 
	 * @param map
	 *            存储电视剧信息
	 * @param filePathAndName
	 *            要写入信息的路径和文件名
	 * @param append
	 *            文件是否可扩展
	 */
	public void saveNewTeleplayToFile(Map<String, String> teleplayMap,
			String filePathAndName, boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			String originalName = teleplayMap.get("originalName");
			String foreignName = teleplayMap.get("foreignName");
			String director = teleplayMap.get("director");
			String actor = teleplayMap.get("actor");
			String showTime = teleplayMap.get("showTime");

			Date now = new Date();
			DateFormat d = DateFormat.getDateTimeInstance();
			String time = d.format(now);
//			fw.write("名称：" + originalName + "#外文名：" + foreignName + "#导演："
//					+ director + "#演员：" + actor + "#集数：" + showTime
//					+ "#取数据时间：" + time + "\n");
			fw.write("名称：" + originalName  + "\n");
			log.info("saving" + teleplayMap.get("originalName") + "success...");
			fw.close();
		} catch (IOException e) {
			log.info("保存电视剧文件出错" + e.getMessage());
		}
	}
	
	
	
	/**
	 
	 */
	public boolean  saveName(String name,
			String filePathAndName, boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			fw.write( name  + "\n");
			fw.close();
			return true;
		} catch (IOException e) {
			log.info("保存电视剧文件出错" + e.getMessage());
			return false;
		}
	}
	
	
	

	/**
	 * 将Map中的内容写入到文件中,若文件不存在,则新建文件
	 * 
	 * @param map
	 *            存储 歌曲名称、艺人、风格、地域等信息
	 * @param filePathAndName
	 *            要写入信息的路径和文件名
	 * @param append
	 *            文件是否可扩展
	 */
	public void saveMusicToFile(Map<String, String> musicMap,
			String filePathAndName, boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			fw.write("[" + "{" + "\"歌名\"" + ":" + "\"" + musicMap.get("album")
					+ "\"" + "," + "\"艺人\"" + ":" + "\""
					+ musicMap.get("artist") + "\"" + "," + "\"风格\"" + ":"
					+ "\"" + musicMap.get("style") + "\"" + "," + "\"地域\""
					+ ":" + "\"" + musicMap.get("region") + "\"" + ","
					+ "\"渠道\"" + ":" + "\"" + "音乐" + "\"" + "}" + "]" + "\n");
			log.info("saving" + musicMap.get("album") + "success...");
			fw.close();
		} catch (IOException e) {
			log.info("保存音乐文件出错" + e.getMessage());
		}
	}

	/**
	 * 将Map中的内容写入到文件中,若文件不存在,则新建文件
	 * 
	 * @param list
	 *            存储歌曲名称信息
	 * @param filePathAndName
	 *            要写入信息的路径和文件名
	 * @param append
	 *            文件是否可扩展
	 */
	public void saveMusicNameToFile(String musicName, String filePathAndName,
			boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			if (musicName != null || !("".equals(musicName))) {
				fw.write("[" + "{" + "\"歌名\"" + ":" + "\"" + musicName + "\""
						+ "}" + "]" + "\n");
			}
			log.info("saving" + musicName + "success...");
			fw.close();
		} catch (IOException e) {
			log.info("保存歌曲名称文件出错" + e.getMessage());
		}
	}

	/**
	 * 将Map中的内容写入到文件中,若文件不存在,则新建文件
	 * 
	 * @param map
	 *            存储火车车次信息
	 * @param filePathAndName
	 *            要写入信息的路径和文件名
	 * @param append
	 *            文件是否可扩展
	 */
	public void saveTrainToFile(Map<String, String> trainMap,
			String filePathAndName, boolean append) {
		try {
			File myFilePath = new File(filePathAndName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			// 第二个参数 append 说明文件是重新新建或可扩充
			FileWriter fw = new FileWriter(myFilePath, append);
			fw.write("[" + "{" + "\"车次\"" + ":" + "\""
					+ trainMap.get("trainNumber") + "\"" + "," + "\"始发站\""
					+ ":" + "\"" + trainMap.get("departure") + "\"" + ","
					+ "\"终点站\"" + ":" + "\"" + trainMap.get("terminus") + "\""
					+ "," + "\"全程时间\"" + ":" + "\""
					+ trainMap.get("overallTravelTime") + "\"" + ","
					+ "\"总里程\"" + ":" + "\"" + trainMap.get("totalCourse")
					+ "\"" + "," + "\"渠道\"" + ":" + "\"" + "列车" + "\"" + "}"
					+ "]" + "\n");
			log.info("saving" + trainMap.get("trainNumber") + "success...");
			fw.close();
		} catch (IOException e) {
			log.info("保存列车文件出错" + e.getMessage());
		}
	}

	/**
	 * 操作流程：统计文件的总行数，用来读取文件中的数据用
	 * 
	 * @author Administrator
	 * @param fileName
	 * @return List 返回文件行数的list
	 */
	public int getLineSize(String fileName) {
		int count = 0;
		try {
			FileReader rd = new FileReader(fileName);
			// 带缓冲的字符输入流
			BufferedReader in = new BufferedReader(rd);
			while ((in.readLine()) != null) {
				count++;
			}
		} catch (Exception e) {
			log.error(fileName + "文件未找到");
		}
		log.info(fileName + "文件读取完成");
		return count;
	}

	/**
	 * 从文件中逐行读取数据并添加到list里面
	 * 
	 * @param fileName
	 */
	public List<String> getFileSourceByline(String fileName) {
		List<String> list = new ArrayList<String>();
		File file = new File(fileName);
		String s = null;
		if (file.exists() & file.isFile()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), "GBK");
				// 带缓冲的字符输入流
				BufferedReader in = new BufferedReader(read);
				while ((s = in.readLine()) != null) {
					String line = s.toString();
					if("".equals(line)){
						continue;
					}
					list.add(line);
				}
			} catch (Exception e) {
				log.error(fileName + "文件未找到");
			}
		}
		log.info(fileName + "文件处理完成");
		return list;
	}

	/**
	 * 将输入流转换成字节流
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static byte[] toBytes(InputStream input) throws Exception {
		byte[] data = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int read = 0;
			while ((read = input.read(b)) > 0) {
				byteOut.write(b, 0, read);
			}
			data = byteOut.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
		return data;
	}

	/**
	 * 将文件读取为一个字符串
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String toString(File file) throws Exception {
		return toString(new FileInputStream(file));
	}

	/**
	 * 将输入流转换为一个串
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String toString(InputStream input) throws Exception {
		return toStringWithLineBreak(input, null);
	}

	/**
	 * 转换为每行补充指定换行符(例如："/n"，"</br>")
	 * 
	 * @param input
	 * @param lineBreak
	 * @return
	 * @throws Exception
	 */
	public static String toStringWithLineBreak(InputStream input,
			String lineBreak) throws Exception {
		List<String> lines = toLines(input);
		StringBuilder sb = new StringBuilder(20480);
		for (String line : lines) {
			sb.append(line);
			if (lineBreak != null) {
				sb.append(lineBreak);
			}
		}
		return sb.toString();
	}

	/**
	 * 以GBK格式将输入流按行置入一个List<String>
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static List<String> toLines(InputStream input) throws Exception {
		return toLines(input, "GBK");
	}

	/**
	 * 以指定编码格式将输入流按行置入一个List<String>
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static List<String> toLines(InputStream input, String encoding)
			throws Exception {
		InputStreamReader insreader = new InputStreamReader(input, encoding);
		BufferedReader bin = new BufferedReader(insreader);
		List<String> lines = new ArrayList<String>();
		String line;
		while ((line = bin.readLine()) != null) {
			lines.add(line);
		}
		bin.close();
		insreader.close();
		return lines;
	}

	/**
	 * 将字符串转出到指定文件
	 * 
	 * @param saveFile
	 * @param content
	 */
	public static void toFile(File saveFile, String content) {
		File parent = saveFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(saveFile));
			out.print(content);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 将一组文件打zip包
	 * 
	 * @param srcFiles
	 * @param targetFileName
	 * @throws IOException
	 */
	public static void filesToZip(List<File> srcFiles, String targetFileName)
			throws IOException {
		String fileOutName = targetFileName + ".zip";
		byte[] buf = new byte[1024];
		FileInputStream in = null;
		FileOutputStream fos = null;
		ZipOutputStream out = null;
		try {
			fos = new FileOutputStream(fileOutName);
			out = new ZipOutputStream(fos);
			for (File file : srcFiles) {
				in = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(file.getName()));
				int len;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
			if (fos != null) {
				out.closeEntry();
				out.close();
				fos.close();
			}
		}
	}

	/**
	 * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	 * 
	 * @param fileName
	 *            文件的名
	 */
	public static void readFileByBytes(String fileName) {
		File file = new File(fileName);
		InputStream in = null;
		try {
			System.out.println("以字节为单位读取文件内容，一次读一个字节：");
			// 一次读一个字节
			in = new FileInputStream(file);
			int tempbyte;
			while ((tempbyte = in.read()) != -1) {
				System.out.write(tempbyte);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			// 一次读多个字节
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream(fileName);
			FileHandle.showAvailableBytes(in);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 * 
	 * @param fileName
	 *            文件名
	 */
	public static void readFileByChars(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		try {
			System.out.println("以字符为单位读取文件内容，一次读一个字节：");
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				// 对于windows下，rn这两个字符在一起时，表示一个换行。
				// 但如果这两个字符分开显示时，会换两次行。
				// 因此，屏蔽掉r，或者屏蔽n。否则，将会多出很多空行。
				if (((char) tempchar) != 'r') {
					System.out.print((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println("以字符为单位读取文件内容，一次读多个字节：");
			// 一次读多个字符
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(fileName));
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((charread = reader.read(tempchars)) != -1) {
				// 同样屏蔽掉r不显示
				if ((charread == tempchars.length)
						&& (tempchars[tempchars.length - 1] != 'r')) {
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == 'r') {
							continue;
						} else {
							System.out.print(tempchars[i]);
						}
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * 随机读取文件内容
	 * 
	 * @param fileName
	 *            文件名
	 */
	public static void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			System.out.println("随机读取一段文件内容：");
			// 打开一个随机访问文件流，按只读方式
			randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 读文件的起始位置
			int beginIndex = (fileLength > 4) ? 4 : 0;
			// 将读文件的开始位置移到beginIndex位置。
			randomFile.seek(beginIndex);
			byte[] bytes = new byte[10];
			int byteread = 0;
			// 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
			// 将一次读取的字节数赋给byteread
			while ((byteread = randomFile.read(bytes)) != -1) {
				System.out.write(bytes, 0, byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * 显示输入流中还剩的字节数
	 * 
	 * @param in
	 */
	private static void showAvailableBytes(InputStream in) {
		try {
			System.out.println("当前字节输入流中的字节数为:" + in.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 快速读取文本文件最后一行数据内容
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readLastLine(File file, String charset)
			throws IOException {
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			return null;
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			long len = raf.length();
			if (len == 0L) {
				return "";
			} else {
				long pos = len - 1;
				while (pos > 0) {
					pos--;
					raf.seek(pos);
					if (raf.readByte() == '\n') {
						break;
					}
				}
				if (pos == 0) {
					raf.seek(0);
				}
				byte[] bytes = new byte[(int) (len - pos)];
				raf.read(bytes);
				if (charset == null) {
					return new String(bytes);
				} else {
					return new String(bytes, charset);
				}
			}
		} catch (FileNotFoundException e) {
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}



}
