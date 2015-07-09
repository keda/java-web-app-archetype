package ${package}.httpclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class SSLConnectionFactory {
	
	public SSLConnectionFactory() {
		
	}
	
	public static Registry<ConnectionSocketFactory> build() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		
		SSLContextBuilder sslContextBuilder = SSLContexts.custom();
		
		sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
			
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
				return true;
			}
		});
		
		SSLContext sslContext = sslContextBuilder.build();
		
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext  , new X509HostnameVerifier() {
			
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
			
			@Override
			public void verify(String host, String[] cns, String[] subjectAlts)
					throws SSLException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void verify(String host, X509Certificate cert) throws SSLException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void verify(String host, SSLSocket ssl) throws IOException {
				// TODO Auto-generated method stub
				
			}
		});
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
		        .<ConnectionSocketFactory> create()
		        .register("https", sslsf)
		        .register("http", PlainConnectionSocketFactory.getSocketFactory())
		        .build();
		
		return socketFactoryRegistry;
	}
}
