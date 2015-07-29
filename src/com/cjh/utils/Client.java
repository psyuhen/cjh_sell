/**
 * 
 */
package com.cjh.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.bgpublish.socket.FileSerial;

/**
 * 客户端
 * @author ps
 *
 */
public class Client {
	public static final String TAG = "Client";
	private String ip = "192.168.1.104";
	private int port = 11000;
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	
	public void connect(){
		if(socket != null){
			return ;
		}
		try {
			socket = new Socket(ip,port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			Log.e(TAG, "Ip地址错误",e);
		} catch (IOException e) {
			Log.e(TAG, "连接服务器失败",e);
		}
	}
	
	public void send(FileSerial fileSerial){
		connect();
		if(socket == null){
			return;
		}
		try {
			oos.writeObject(fileSerial);
			oos.flush();
		} catch (IOException e) {
			Log.e(TAG, "获取输出流失败",e);
		}
	}
	
	public void toOnLine(String fromUser){
		FileSerial fileSerial = new FileSerial();
		fileSerial.setType(FileSerial.TYPE_ONLINE);
		fileSerial.setFromUser(fromUser);
		send(fileSerial);
	}
	
	public void toOffLine(String fromUser){
		FileSerial fileSerial = new FileSerial();
		fileSerial.setType(FileSerial.TYPE_OFFLINE);
		fileSerial.setFromUser(fromUser);
		send(fileSerial);
	}
	
	public List<FileSerial> findOffLineMsg(String fromUser,String toUser){
		FileSerial fileSerial = new FileSerial();
		fileSerial.setType(FileSerial.TYPE_OFFLINE_MSG);
		fileSerial.setFromUser(fromUser);
		fileSerial.setToUser(toUser);
		send(fileSerial);
		
		FileSerial receive = receive();
		List<FileSerial> list = new ArrayList<FileSerial>();
		while(receive != null){
			list.add(receive);
			receive = receive();
		}
		
		return list;
	}
	
	public void sendMsg(String chatContent,String toUser,String fromUser){
		FileSerial fileSerial = new FileSerial();
		fileSerial.setType(FileSerial.TYPE_TEXT);
		fileSerial.setFromUser(fromUser);
		fileSerial.setToUser(toUser);
		fileSerial.setFileName(chatContent);
		
		send(fileSerial);
	}
	
	public FileSerial receive(){
		Object obj = null;
		connect();
		if(socket == null){
			return null;
		}
		try {
			obj = ois.readObject();
		} catch (StreamCorruptedException e) {
			Log.e(TAG, "获取输入流失败",e);
		} catch (IOException e) {
			Log.e(TAG, "获取输入流失败",e);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "找不到类",e);
		}
		if(obj == null){
			return null;
		}
		
		return (FileSerial)obj;
	}
	
	public void close(){
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, "关闭流失败",e);
			}
		}
	}
}
