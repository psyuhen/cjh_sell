package com.bgpublish.socket;

import java.io.Serializable;

/**
 * 
 * @author ps
 *
 */
public class FileSerial implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private  String fileName;// 文件名称
	private  long fileLength; // 文件长度
	private  byte[] fileContent; // 文件内容
	private  int type;
	private  String toUser;    //接收用户 (手机号码)
	private  String fromUser;  //发送用户 （手机号码）
	
	public static final int TYPE_ONLINE = -1;// 上线（用户登录上线）
	public static final int TYPE_OFFLINE = -2;// 下线（用户退出下线）
	public static final int TYPE_OFFLINE_MSG = -3;// 离线信息（默认打开聊天窗口时加载）
	
	/** name保存在filename字段里面  **/  
    public static final int TYPE_NAME = 0;// 连接成功后，向对方发出自己的名字
    
    /** text保存在filename字段里面   */  
    public static final int TYPE_TEXT = 1;// 文字  
    public static final int TYPE_AUDIO = 2;// 音频  
    public static final int TYPE_PICTURE = 3;// 图片
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
    
    
}
