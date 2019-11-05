package com.knowology.km.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GetMultiMediaKnowledge {
	
	private static ArrayList<URLandPicNameMultiMedia> titleUrlPicList=new ArrayList<URLandPicNameMultiMedia>();
	
	public GetMultiMediaKnowledge(){
		if(titleUrlPicList.size()==0){
			ArrayList<String> knowledgeList=new ArrayList<String>();
			CommonReadAndWrite.read(getFilePath()+"multimediaknowledge", knowledgeList);
			for (Iterator iterator = knowledgeList.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				String []temp=string.trim().split("	");
				if(temp.length==3&&!"".equals(temp[2].trim())){
					URLandPicNameMultiMedia up=new URLandPicNameMultiMedia();
					up.title=temp[0].trim();
					up.urlString=temp[1].trim();
					up.picNameString=temp[2].trim();
					titleUrlPicList.add(up);
				}
			}
		}
	}
	
	public String getFilePath(){
		String path=this.getClass().getClassLoader().getResource("").getPath();
		try {
			path=URLDecoder.decode(path,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	public void returnMultiMediaKnowledge(String answerContent,String firstanswer,HashMap<String,String> position){
		String allContentString=answerContent+firstanswer;// 从钱磊返回+第一个问题的答案中，找多媒体
		
		//String allContentString=answerContent;//只从钱磊返回中找多媒体知识。
		
		int maxWordNum=0;
		URLandPicNameMultiMedia upMax = new URLandPicNameMultiMedia();
		for (Iterator iterator = titleUrlPicList.iterator(); iterator.hasNext();) {
			URLandPicNameMultiMedia up = (URLandPicNameMultiMedia) iterator.next();
			int count=0;
			String title=up.title;
			for (int i = 0; i < title.length(); i++) {//这里可以优化计算方法 这里只是一个简单的方法
				char word=title.charAt(i);
				if (allContentString.indexOf(word)!=-1) {
					count++;
				}
				
			}
			if (count>maxWordNum) {
				maxWordNum=count;
				upMax=up;
			}
		}
//		if(maxWordNum==0){
//			position.put("title", "");
//			position.put("url", "");
//			position.put("picName", "");
//		}else{
			position.put("title", upMax.title);
			position.put("url", upMax.urlString);
			position.put("picName", upMax.picNameString);
//		}
	}
}
