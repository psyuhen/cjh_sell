/**
 * 
 */
package com.cjh.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 
 * @author ps
 *
 */
public class SocketUtil {
	private String ip = "192.168.1.127";
	private int port = 10000;

	public List<Object> getMsg() throws InterruptedException, ExecutionException{
		FutureTask<List<Object>> task = new FutureTask<List<Object>>(new Callable<List<Object>>() {
			@Override
			public List<Object> call() throws Exception {
				Socket socket = new Socket(ip, port);
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				Object obj = null;
				while(obj != null){
					
				}
				return null;
			}
		});
		
		new Thread(task).start();
		return task.get();
	}
}
