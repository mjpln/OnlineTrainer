
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
import org.apache.log4j.Logger;
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
import org.w3c.dom.Document;   

import com.knowology.km.dal.Database;
/**  
 * @author: Chembo Huang  
 * @since: May 3, 2012  
 * @modified: May 3, 2012  
 * @version:  
 */    
public class WordToHtml {   
	/*
	 * 方法测试
	 */
	public static Logger logger = Logger.getLogger(WordToHtml.class);
    
    public static void main(String argv[]) {    
        try {    
            convert2Html("D://test//123.doc","D://test//abc.html","D:/test/");    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }    
    
    public static void writeFile(String content, String path) {    
        FileOutputStream fos = null;    
        BufferedWriter bw = null;    
        try {    
            File file = new File(path);    
            fos = new FileOutputStream(file);    
//            bw = new BufferedWriter(new OutputStreamWriter(fos,"GB2312"));   
            bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));    
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
    
    public static void convert2Html(String fileName, String outPutFile,String picPath)    
            throws TransformerException, IOException,    
            ParserConfigurationException { 
    	logger.info("doc转html开始....");
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));//WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));    
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(    
                DocumentBuilderFactory.newInstance().newDocumentBuilder()    
                        .newDocument());    
         wordToHtmlConverter.setPicturesManager( new PicturesManager()    
         {    
             public String savePicture( byte[] content,    
                     PictureType pictureType, String suggestedName,    
                     float widthInches, float heightInches )    
             {    
                 return suggestedName;    
             }    
         } );    
        wordToHtmlConverter.processDocument(wordDocument);    
        //save pictures    
        List pics=wordDocument.getPicturesTable().getAllPictures();    
        if(pics!=null){    
            for(int i=0;i<pics.size();i++){    
                Picture pic = (Picture)pics.get(i);    
                System.out.println();    
                try {    
                    pic.writeImageContent(new FileOutputStream(picPath+"/"  
                            + pic.suggestFullFileName()));    
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
//        serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");   
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");    
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");    
        serializer.setOutputProperty(OutputKeys.METHOD, "html");    
        serializer.transform(domSource, streamResult);    
        out.close();    
        writeFile(new String(out.toByteArray()), outPutFile);   
        logger.info("doc转html成功!");
    }    
}    