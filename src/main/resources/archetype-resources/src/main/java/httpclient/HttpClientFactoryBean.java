package ${package}.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class HttpClientFactoryBean implements FactoryBean<CloseableHttpClient>, InitializingBean, DisposableBean {
	
	private CloseableHttpClient client;
	
	private PoolingHttpClientConnectionManager connManager;
	private RequestConfig requestConfig;
	
	public PoolingHttpClientConnectionManager getConnManager() {
		return connManager;
	}

	public void setConnManager(PoolingHttpClientConnectionManager connManager) {
		this.connManager = connManager;
	}
	
	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		HttpClientFactoryBuilder cfb = new HttpClientFactoryBuilder(this.connManager, this.requestConfig);
		
		this.client = bulidHttpClient(cfb);
		
	}

	private CloseableHttpClient bulidHttpClient(HttpClientFactoryBuilder cfb) {
		
		return cfb.bulidHttpClient();
	}

	@Override
	public CloseableHttpClient getObject() throws Exception {
		return this.client;
	}

	@Override
	public Class<?> getObjectType() {
		return (this.client != null) ? this.client.getClass() : CloseableHttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void destroy() throws Exception {
		this.client.close();
	}

}
