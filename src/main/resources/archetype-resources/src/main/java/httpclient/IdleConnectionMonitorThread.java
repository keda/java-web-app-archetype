package ${package}.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class IdleConnectionMonitorThread extends Thread implements InitializingBean, DisposableBean {
	private final Logger logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);
	private final HttpClientConnectionManager connMgr;
	private volatile boolean shutdown;
	
	public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
		super();
		this.connMgr = connMgr;
	}
	
	@Override
	public void run() {
		try {
			while (!shutdown) {
				
				synchronized (this) {
					wait(5000);
					PoolingHttpClientConnectionManager poolMgr = (PoolingHttpClientConnectionManager) connMgr;
					PoolStats totalStats = poolMgr.getTotalStats();
					connMgr.closeExpiredConnections();
					
					connMgr.closeIdleConnections(10, TimeUnit.SECONDS);
					
					logger.info(String.format("eviction idle connection done! max=%d, available=%d, pending=%d, leased=%d"
							,totalStats.getMax(), totalStats.getAvailable(), totalStats.getPending(), totalStats.getLeased()));
				}
				
			}
		} catch (Exception e) {
			logger.error("http连接池监控失败!", e);
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.start();
	}
	
	@Override
	public void destroy() {
		this.shutdown();
	}
	
}
