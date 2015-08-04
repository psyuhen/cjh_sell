/**
 * 
 */
package com.cjh.qiniu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.net.CallRet;
import com.qiniu.api.resumableio.ResumeableIoApi;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;

/**
 * <pre>
 * 上传相关文件到7牛服务器
 * 
 * PutPolicy中的相关参数说明：
 * scope: <Bucket string>, 用于指定文件所上传的目标资源空间（Bucket）和资源名（Key）。格式为：<bucket name>[:<key>]。若只指定Bucket名，表示文件上传至该Bucket。若同时指定了Bucket和Key（<bucket name>:<key>），表示上传文件限制在指定的Key上。两种形式的差别在于，前者是“新增”操作：如果所上传文件的Key在Bucket中已存在，上传操作将失败。而后者则是“新增或覆盖”操作：如果Key在Bucket中已经存在，将会被覆盖；如不存在，则将文件新增至Bucket中。注意：资源名必须采用utf8编码，非utf8编码的资源名在访问七牛云存储将会反馈错误。
 * deadline: <UnixTimestamp int64>, 定义上传请求的失效时间，Unix时间戳，单位为秒。
 * endUser: <EndUserId string>, 给上传的文件添加唯一属主标识，特殊场景下非常有用，比如根据App-Client标识给图片或视频打水印
 * returnUrl: <RedirectURL string>, 设置用于浏览器端文件上传成功后，浏览器执行301跳转的URL，一般为HTML Form上传时使用。文件上传成功后会跳转到returnUrl?query_string, query_string会包含 returnBody 内容。
 * returnBody: <ResponseBodyForAppClient string>, 文件上传成功后，自定义从七牛云存储最终返回給终端 App-Client 的数据。支持 魔法变量和自定义变量。
 * callbackBody: <RequestBodyForAppServer string> 文件上传成功后，七牛云存储向 App-Server 发送POST请求的数据。支持 魔法变量 和 自定义变量。
 * callbackUrl: <RequestUrlForAppServer string>, 文件上传成功后，七牛云存储向 App-Server 发送POST请求的URL，必须是公网上可以正常进行POST请求并能响应 HTTP Status 200 OK 的有效 URL
 * persistentOps: <persistentOpsCmds string>, 音/视频文件上传成功后异步地执行的预转码持久化指令。每个预转指令是一个API规格字符串，多个预转指令可以使用分号;隔开。具体指令请参考fop
 * persistentNotifyUrl: <persistentNotifyUrl string> 七牛云存储向 App-Server 发送转码持久化结果的URL，必须是公网上可以正常进行POST请求并能响应 HTTP Status 200 OK 的有效 URL。
 *  
 * 注意：
 * •callbackUrl 与 returnUrl 不可同时指定，两者只可选其一。
 * •callbackBody 与 returnBody 不可同时指定，两者只可选其一。
 * 
 * PutExtra的参数说明：
 * extra 自定义变量，必须以 x: 开头命名，不限个数。里面的内容将在 callbackBody 参数中的 $(x:custom_field_name) 求值时使用。
 * 
 * 引用包：
 * httpclient-4.2.1.jar
 * httpmime-4.2.1.jar
 * httpcore-4.2.1.jar
 * org.restlet.lib.org.json-2.0.jar
 * qiniu-java-sdk-6.1.7.1.jar
 * </pre>
 * @author ps
 * @see PutPolicy
 */
public class UploadFile2QiNiu {
	private static UploadFile2QiNiu uploadFile2QiNiu;
	//访问key
	private String accessKey="-fEnz8U73A56AiBnDAxDca7zQKOhXqKW3ewg5Dnt";
	//密钥key
	private String secretKey="B1tvmnT882rHUThFDnvDN8T4nTq7aciXFzWxUSuz";
	//空间名称和资源名称
	private String bucketName="ingbbtest";
	//凭证算法
	private Mac mac;
	//生成文件上传的凭证
	private PutPolicy putPolicy;
	//自定义变量
	private PutExtra extra;
	
	private final String baseUrl = "http://7rf2l9.com1.z0.glb.clouddn.com";
	
	
	protected UploadFile2QiNiu(){
		
	}
	
	public synchronized static UploadFile2QiNiu getInstance(){
		if(uploadFile2QiNiu == null){
			uploadFile2QiNiu = new UploadFile2QiNiu();
		}
		
		return uploadFile2QiNiu;
	}
	
	/**
	 * 批量文件上传，不支持断点断传，建议上传小文件时用，key默认与hash值一样。
	 * @param localFiles 文件列表
	 * @return 返回文件上传后的相关信息，上传失败时，list里面为空，或者是在response里面有错误信息
	 */
	public List<PutRet> uploadFile(List<File> localFiles){
		List<PutRet> list  = new ArrayList<PutRet>(localFiles.size());
		for (File file : localFiles) {
			list.add(uploadFile(null, file));
		}
		return list;
	}
	
	/**
	 * 单个文件上传，不支持断点断传，建议上传小文件时用
	 * @param key 
	 * 资源名，所在的资源空间内唯一。key可包含/。若不指定 key，七牛云存储将使用文件的 etag（即上传成功后返回的hash值）作为key，
	 * 并在返回结果中传递给客户端。资源名必须采用 utf8编码 ，非utf8编码的资源名在访问七牛云存储将会反馈错误。
	 * @param localFile 本地文件
	 * @return 授权错误或者json转换出错时，返回null,其他返回PutRet对象
	 * @see PutRet
	 */
	public PutRet uploadFile(String key, File localFile){
		String uptoken;
		try {
			uptoken = uploadToken();
			//文件上传到bucketName中
			PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
			return ret;
		} catch (AuthException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 单个文件上传，不支持断点断传，建议上传小文件时用，key默认与hash值一样。
	 * @param localFile 本地文件
	 * @return 授权错误或者json转换出错时，返回null,其他返回PutRet对象
	 * @see PutRet
	 */
	public PutRet uploadFile(File localFile){
		return uploadFile(null, localFile);
	}
	
	/**
	 * 单个文件上传，支持断点断传，key默认与hash值一样。
	 * @param localFile 本地文件
	 * @return 授权错误或者json转换出错时，返回null,其他返回PutRet对象
	 * @see PutRet
	 */
	public PutRet resumeUploadFile(File localFile){
		return resumeUploadFile(null, localFile);
	}
	
	/**
	 * 单个文件上传，支持断点断传
	 * @param key 
	 * 资源名，所在的资源空间内唯一。key可包含/。若不指定 key，七牛云存储将使用文件的 etag（即上传成功后返回的hash值）作为key，
	 * 并在返回结果中传递给客户端。资源名必须采用 utf8编码 ，非utf8编码的资源名在访问七牛云存储将会反馈错误。
	 * @param localFile 本地文件
	 * @return 授权错误或者json转换出错时，返回null,其他返回PutRet对象
	 * @see PutRet
	 */
	public PutRet resumeUploadFile(String key, File localFile){
		String uptoken;
		try {
			uptoken = uploadToken();
			//文件上传到bucketName中
			PutRet ret = ResumeableIoApi.put(localFile, uptoken, key, null);
			return ret;
		} catch (AuthException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 单个文件上传(使用文件流上传)，支持断点断传，key默认与hash值一样。
	 * @param localFile 本地文件
	 * @return 授权错误或者json转换出错时，返回null,其他返回PutRet对象
	 * @see PutRet
	 */
	public PutRet resumeUploadStream(File localFile){
		return resumeUploadStream(null, localFile);
	}
	
	/**
	 * 单个文件上传(使用文件流上传)，支持断点断传
	 * @param key 
	 * 资源名，所在的资源空间内唯一。key可包含/。若不指定 key，七牛云存储将使用文件的 etag（即上传成功后返回的hash值）作为key，
	 * 并在返回结果中传递给客户端。资源名必须采用 utf8编码 ，非utf8编码的资源名在访问七牛云存储将会反馈错误。
	 * @param localFile 本地文件
	 * @return 授权错误或者json转换出错时，返回null,其他返回PutRet对象
	 * @see PutRet
	 */
	public PutRet resumeUploadStream(String key, File localFile){
		FileInputStream fis = null;
		try {
			String uptoken = uploadToken();
			//文件上传到bucketName中
			fis = new FileInputStream(localFile);
			PutRet ret = ResumeableIoApi.put(fis, uptoken, key, null);
			return ret;
		} catch (AuthException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	protected String uploadToken() throws AuthException, JSONException{
		//构造凭证
		if(mac == null){
			//TestCase.assertNotNull(accessKey);
			//TestCase.assertNotNull(secretKey);
			mac = new Mac(accessKey, secretKey);
		}
		
		//生成密钥
		if(putPolicy == null){
			//TestCase.assertNotNull(bucketName);
			putPolicy = new PutPolicy(bucketName);
		}
		
		if(extra == null){
			extra = new PutExtra();
		}
		
		return putPolicy.token(mac);
	}
	
	/**
	 * 删除7牛上的单个文件
	 * @param key 在7牛空间里面的文件名
	 * @return true-删除成功,false-删除失败
	 */
	public boolean deleteFile(String key){
		if(mac == null){
			mac = new Mac(accessKey, secretKey);
		}
		RSClient client = new RSClient(mac);
		CallRet callRet = client.delete(bucketName, key);
		return callRet.ok();
	}
	
	/**
	 * 获取7牛的文件名
	 * @param imageName
	 * @return
	 */
	public String getImageUrl(String imageName){
		String url = baseUrl+"/"+imageName;
		GetPolicy getPolicy = new GetPolicy();
		try {
			if(mac == null){
				mac = new Mac(accessKey, secretKey);
			}
			url = getPolicy.makeRequest(url, mac);
			return url;
		} catch (AuthException e) {
			return null;
		}
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public Mac getMac() {
		return mac;
	}

	public void setMac(Mac mac) {
		this.mac = mac;
	}

	public PutPolicy getPutPolicy() {
		return putPolicy;
	}

	public void setPutPolicy(PutPolicy putPolicy) {
		this.putPolicy = putPolicy;
	}

	public PutExtra getExtra() {
		return extra;
	}

	public void setExtra(PutExtra extra) {
		this.extra = extra;
	}
}
