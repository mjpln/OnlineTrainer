package com.knowology.km.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;



/**
 * 
 * @author Mafx
 *
 */
public class Word2Html {

	private final static String  tempPath = "D:/test/";
	
	public static void main(String argv[]) {
		try {
			doc2Html("D://test//123.doc", "D://test//abc.html");
//			docx2Html("D://test//123.docx", "D://test//abc.html");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * doc转换为html
	 * 
	 * @param fileName
	 * @param outPutFile
	 * @throws TransformerException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void doc2Html(String fileName, String outPutFile) throws TransformerException, IOException, ParserConfigurationException {
		long startTime = System.currentTimeMillis();
		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
				return "test/" + suggestedName;
			}
		});
		wordToHtmlConverter.processDocument(wordDocument);
		// 保存图片
		List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = (Picture) pics.get(i);
				System.out.println();
				try {
					pic.writeImageContent(new FileOutputStream(tempPath + pic.suggestFullFileName()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		out.close();
		writeFile(new String(out.toByteArray()), outPutFile);
		System.out.println("Generate " + outPutFile + " with " + (System.currentTimeMillis() - startTime) + " ms.");
	}

	/**
	 * 写文件
	 * 
	 * @param content
	 * @param path
	 */
	public static void writeFile(String content, String path) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
			bw.write(content);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}

	/**
	 * docx格式word转换为html
	 * 
	 * @param fileName
	 *            docx文件路径
	 * @param outPutFile
	 *            html输出文件路径
	 * @throws TransformerException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void docx2Html(String fileName, String outPutFile) throws TransformerException, IOException, ParserConfigurationException {
		String fileOutName = outPutFile;
		long startTime = System.currentTimeMillis();
		XWPFDocument document = new XWPFDocument(new FileInputStream(fileName));
		XHTMLOptions options = XHTMLOptions.create().indent(4);
		// 导出图片
		File imageFolder = new File(tempPath);
		options.setExtractor(new FileImageExtractor(imageFolder));
		// URI resolver
		options.URIResolver(new FileURIResolver(imageFolder));
		File outFile = new File(fileOutName);
		outFile.getParentFile().mkdirs();
		OutputStream out = new FileOutputStream(outFile);
		XHTMLConverter.getInstance().convert(document, out, options);
		System.out.println("Generate " + fileOutName + " with " + (System.currentTimeMillis() - startTime) + " ms.");

	}

}
