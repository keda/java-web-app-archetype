package ${package}.httpclient;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientFactoryBuilder {
	public final Logger log = LoggerFactory.getLogger(getClass());
	private PoolingHttpClientConnectionManager connManager;
	private RequestConfig requestConfig;
	
	public HttpClientFactoryBuilder() {
		
	}
	
	public HttpClientFactoryBuilder(PoolingHttpClientConnectionManager connManager, RequestConfig requestConfig) {
		this.connManager = connManager;
		this.requestConfig = requestConfig;
	}

	public CloseableHttpClient bulidHttpClient() {
		
		HttpClientBuilder cb = HttpClients.custom();
		cb.disableAuthCaching();
		cb.disableCookieManagement();
		cb.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE);
		cb.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

		    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		    	
		        HeaderElementIterator it = new BasicHeaderElementIterator(
		                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		        while (it.hasNext()) {
		            HeaderElement he = it.nextElement();
		            String param = he.getName();
		            String value = he.getValue();
		            if (value != null && param.equalsIgnoreCase("timeout")) {
		                try {
		                	log.info(String.format("TTT:%s=%s", param, value));
		                    return Long.parseLong(value) * 1000;
		                } catch(NumberFormatException ignore) {
		                }
		            }
		        }
		        HttpHost target = (HttpHost) context.getAttribute(
		                HttpClientContext.HTTP_TARGET_HOST);
		        if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
		            // Keep alive for 5 seconds only
		            return 5 * 1000;
		        } else {
		            // otherwise keep alive for 30 seconds
		            return 30 * 1000;
		        }
		    }

		});
		if( this.connManager != null ) {
			//FIXME:didn't work! why? fixme...
			HttpRoute r = new HttpRoute(new HttpHost("gw.api.tbsandbox.com", 443, "https"));
			this.connManager.setMaxPerRoute(r, 100);
			cb.setConnectionManager(this.connManager);
			
		}
		if( this.requestConfig != null ) {
			cb.setDefaultRequestConfig(this.requestConfig);
		}
		
		return cb.build();
	}
}
