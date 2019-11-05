package com.knowology.km.util;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
/**
 * ͨ�õ��ķ���д��
 * @author dell-pc
 *
 */
public class CommonReadAndWrite {
	/**
	 * ��ȡһ���ļ���һ��list�� ���д洢
	 * @param readPath ��ȡ·��
	 * @param readResult �洢����
	 */
	public static void read(String readPath,ArrayList<String> readResult){
		String currLine = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					readPath), "utf-8"));
			while ((currLine = br.readLine()) != null) {
				readResult.add(currLine);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ȡһ���ļ���һ��hashser�� ���д洢 ע��set���Զ�ȥ��
	 * @param readPath ��ȡ·��
	 * @param readResult �洢����
	 */
	public static void readHashSet(String readPath,HashSet<String> readResult){
		String currLine = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					readPath), "GBK"));
			while ((currLine = br.readLine()) != null) {
				readResult.add(currLine);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��arraylist�е�Ԫ�طֱ�һ��д�뵽�ı��ĵ���
	 * @param writePath д��·��
	 * @param writeResult �洢Ԫ�صı���
	 */
	public static void writeArraylist(String writePath,ArrayList<String> writeResult){
		PrintWriter pws=null;
		try {
			pws= new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(writePath), "GBK"), true);
			for(int i=0;i<writeResult.size();i++){
				pws.println(writeResult.get(i));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pws!=null)
				pws.close();
		}
	}
	
	/**
	 * ��hashset�е�Ԫ�طֱ�һ��д�뵽�ı��ĵ���
	 * @param writePath д��·��
	 * @param writeResult �洢Ԫ�صı���
	 */
	public static void writeHashset(String writePath,HashSet<String> writeResult){
		PrintWriter pws=null;
		try {
			pws= new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(writePath), "GBK"), true);
			for (Iterator iterator = writeResult.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				pws.println(string);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pws!=null)
				pws.close();
		}
	}
	
	/**
	 * ��ȡһ���ĵ��е����ݵõ�hashmap�� ָ���ָ��� �ĵ����ǰ��д洢��
	 * @param readPath ��ȡ·��
	 * @param seperator �ָ��
	 * @param hashMap �洢map
	 */
	public static void readHashMap(String readPath,String seperator,HashMap<String, String> hashMap){
		String currLine = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					readPath), "utf-8"));
			while ((currLine = br.readLine()) != null) {
				String []temp=currLine.split(seperator);
				if (temp.length==2) {
					hashMap.put(temp[0], temp[1]);
				}else {
					hashMap.put(currLine.trim(),"");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Iterator iter = cateToGraNameMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String category =(String)entry.getKey();
				HashSet<String> hashSet =(HashSet<String>) entry.getValue();
	 * @param writePath
	 * @param hashMap
	 */
	
	/**
	 * ��hashmapд�뵽�ĵ��� ӳ�����tab����� ÿ�д洢һ��ӳ��
	 * @param writePath д��·��
	 * @param hashMap �洢��map
	 */
	public static void writeHashMap(String writePath,HashMap<String, String> hashMap){
		PrintWriter pws=null;
		try {
			pws= new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(writePath), "GBK"), true);
			Object[] key = hashMap.keySet().toArray();
			Arrays.sort(key);
			for (int i = 0; i < key.length; i++) {
				pws.println(key[i]+"	"+hashMap.get(key[i]));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pws!=null)
				pws.close();
		}
	}
}
