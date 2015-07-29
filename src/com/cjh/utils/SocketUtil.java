/**
 * 
 */
package com.cjh.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 
 * @author ps
 *
 */
public class SocketUtil {

	public static void send(final String chatContent,final String toUser,final String fromUser){
		FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				Client client = new Client();
				client.sendMsg(chatContent, toUser, fromUser);
				return null;
			}
		});
		
		new Thread(task).start();
	}
}
