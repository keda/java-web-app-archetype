package ${package}.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleHttpClient {
	
	public final Logger log = LoggerFactory.getLogger(getClass());
	public final String PATH_SEPARATOR = "/";
	public final String DOT_SEPARATOR = ".";
	public final static String TIMESTAMP = "timestamp";
	public final static String PUBLICKEY = "publickey";
	public final static String SECRET = "secret";
	
	private CloseableHttpClient client;
	private String url;
	private String publicKey;
	private String privateKey;
	private ObjectMapper om = new ObjectMapper();

	public String getUrl() {
		return url;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public CloseableHttpClient getClient() {
		return client;
	}
	
	public ObjectMapper getObjectMapper() {
		return om;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.om = objectMapper;
	}

	public SimpleHttpClient(String url, CloseableHttpClient client) {
		this.url = url;
		this.client = client;
	}
	
	public SimpleHttpClient(String url, String publicKey, String privateKey,
			CloseableHttpClient client) {
		super();
		this.url = url;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.client = client;
	}

	/**描述:get请求
	 * @param apiMethod 请求路径
	 * @param params 请求参数
	 * @return 服务器返回的原始数据
	 * @throws AmssyApiException
	 * @author jinzhou.shao
	 * @date 2014-11-14 下午1:37:50
	 */
	public String executeGet(String apiMethod, Map<String, Object> params) throws AmssyApiException {
		URIBuilder b = buildUri(apiMethod);
		
		List<NameValuePair> list = buildParams(params);
		
		String md5 = sign(list, privateKey);
		list.add(new BasicNameValuePair(SECRET, md5));
		
		try {
			URI uri = b.addParameters(list).build();
			return execute(new HttpGet(uri));
		} catch (URISyntaxException e) {
			String str = reqParamsToString(params);
			
			throw new AmssyApiException("构建GET请求参数失败![uri="+b.toString()+", params="+str+"]", e);
		}
	}
	
	/**描述:get请求
	 * @param apiMethod 请求路径
	 * @param params 请求参数
	 * @param clz 将json数据转成类型为T的javabean <strong>默认返回的类型是<code>ResponseUtil</code></strong>
	 * @return 类型T的实例
	 * @throws AmssyApiException
	 * @author jinzhou.shao
	 * @date 2014-11-14 下午1:37:50
	 */
	@SuppressWarnings("unchecked")
	public <T extends ResponseUtil> T executeGet(String apiMethod, Map<String, Object> params, Class<T> clz) throws Exception {
		String resp = this.executeGet(apiMethod, params);
		
		if(clz == null) clz = (Class<T>) ResponseUtil.class;
		
		return convertToJavaBean(resp, clz);
	}

	public <T> T convertToJavaBean(String resp, Class<T> clz) throws JsonParseException, JsonMappingException, IOException {
		if(resp == null) { return null;}
		
		return om.readValue(resp, clz);
		
	}
	/**描述:post请求
	 * @param apiMethod 请求路径
	 * @param params 请求参数
	 * @param clz 将json数据转成类型为T的javabean <strong>默认返回的类型是<code>ResponseUtil</code></strong>
	 * @return 类型T的实例
	 * @throws AmssyApiException
	 * @author jinzhou.shao
	 * @date 2014-11-14 下午1:37:50
	 */
	@SuppressWarnings("unchecked")
	public <T extends ResponseUtil> T executePost(String apiMethod, Map<String, Object> params, Class<T> clz) throws Exception {
		String resp = this.executePost(apiMethod, params);
		
		if(clz == null) clz = (Class<T>) ResponseUtil.class;
		
		return convertToJavaBean(resp, clz);
	}
	/**描述:post请求
	 * @param apiMethod 请求路径
	 * @param params 请求参数
	 * @return 服务器返回的原始数据
	 * @throws AmssyApiException
	 * @author jinzhou.shao
	 * @date 2014-11-14 下午1:37:50
	 */
	public String executePost(String apiMethod, Map<String, Object> params) throws AmssyApiException {
		URIBuilder b = buildUri(apiMethod);
		
		List<NameValuePair> list = buildParams(params);
		
		String md5 = sign(list, privateKey);
		list.add(new BasicNameValuePair(SECRET, md5));
		
		try {
			HttpPost post = new HttpPost(b.build());
			
			post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			
			return execute(post);
		} catch (URISyntaxException e) {
			String str = reqParamsToString(params);
			
			throw new AmssyApiException("构建POST请求参数失败![uri="+b.toString()+", params="+str+"]", e);
		} catch (UnsupportedEncodingException e) {
			throw new AmssyApiException("构建POST请求参数失败!", e);
		} catch (Exception re){
			String str = reqParamsToString(params);
			
			throw new AmssyApiException("POST请求失败![uri="+b.toString()+", params="+str+"]", re);
		}
	}

	/**描述:
	 * @param params
	 * @return
	 * @author jinzhou.shao
	 * @date 2015-1-20 下午2:55:17
	 */
	private String reqParamsToString(Map<String, Object> params) {
		String str = "";
		try {
			str = om.writeValueAsString(params);
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return str;
	}
	
	/**描述:对发送请求的参数进行加密,默认使用MD5加密算法
	 * @param list 按照参数名字字母进行升序排序
	 * @param key
	 * @return 32位小写
	 * @author jinzhou.shao
	 * @date 2015-1-16 下午4:53:59
	 */
	public String sign(List<NameValuePair> list, String key) {
		
		Collections.sort(list, new Comparator<NameValuePair>() {
			@Override
			public int compare(NameValuePair o1, NameValuePair o2) {
				if(o1 == null) return -1;
				if(o2 == null) return 1;
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		StringBuffer sb = new StringBuffer();
		for (NameValuePair nv : list) {
			sb.append(nv.toString());
		}
		String md5 = sign(sb.toString(), key, new DefaultGenerateSign());
		log.info(sb.toString() + "....." + md5);
		
		return md5;
	}
	
	public String sign(String forSign, String key, GenerateSign sign) {
		return sign.sign(forSign, key);
	}

	private String execute(HttpUriRequest req) throws AmssyApiException {
		// Create a custom response handler
	    ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

	        public String handleResponse(
	                final HttpResponse response) throws ClientProtocolException, IOException {
	            int status = response.getStatusLine().getStatusCode();
	            if (status >= 200 && status < 300) {
	                HttpEntity entity = response.getEntity();
	                return entity != null ? EntityUtils.toString(entity) : null;
	            } else {
	                throw new ClientProtocolException("Unexpected response status: " + status);
	            }
	        }

	    };
	    String resp = null;
		try {
			log.info("url==>" + req.getURI().toString());
			resp = this.client.execute(req, responseHandler);
		} catch (ClientProtocolException e) {
			throw new AmssyApiException(e);
		} catch (IOException e) {
			throw new AmssyApiException(e);
		}
		
		return resp;
	}

	private List<NameValuePair> buildParams(Map<String, Object> params) {
		
		Map<String, Object> p = new HashMap<String, Object>();
		if(params != null) {
			p.putAll(params);
		}
		
		p.put(TIMESTAMP, System.currentTimeMillis());
		p.put(PUBLICKEY, this.publicKey);
		
		List<NameValuePair> plist = new ArrayList<NameValuePair>();
		
		Iterator<String> keys = p.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			
			plist.add(new BasicNameValuePair(key, String.valueOf(p.get(key))));
		}
		
		return plist;
	}

	private URIBuilder buildUri(String apiMethod) {
		URIBuilder b = new URIBuilder();
		
		String method = StringUtils.trimToNull(apiMethod);
		
		if(method == null) throw new IllegalArgumentException("apiMethod not null!");
		
		method = StringUtils.replaceChars(method, DOT_SEPARATOR, PATH_SEPARATOR);
		
		if(url.endsWith(PATH_SEPARATOR)) {
			b.setPath(url.concat(method));
		}else {
			b.setPath(url.concat(PATH_SEPARATOR).concat(method));
		}
		
		return b;
		
	}
	
	class DefaultGenerateSign implements GenerateSign {

		@Override
		public String sign(String params, String key) {
			String s = StringUtils.trimToEmpty(params) + StringUtils.trimToEmpty(key);
			
			return lower32MD5(s, "UTF-8");
		}
		
		public String lower32MD5(String s, String encode) {
			String value = null;
			try {
				return value = new String(Hex.encodeHex(md5(s, encode)));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return value;
		}
		
		public byte[] md5(String s, String encode) throws NoSuchAlgorithmException {
			Charset def = Charset.defaultCharset();
			if(encode != null) {
				def = Charset.forName(encode);
			}
			
			byte[] btInput = s.getBytes(def);
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			
			return md;
		}
		
	}
}
