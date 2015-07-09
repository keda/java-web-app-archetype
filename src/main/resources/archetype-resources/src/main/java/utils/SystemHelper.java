package ${package}.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 
/**
 * 
 * 本机系统信息
 * 
 */
public final class SystemHelper {
	private static final Logger  logger = LoggerFactory.getLogger(SystemHelper.class);

	// 获得系统属性集
	public static Properties props = System.getProperties();
	// 操作系统名称
	public static String OS_NAME = getPropertery("os.name");
	// 行分页符
	public static String OS_LINE_SEPARATOR = getPropertery("line.separator");
	// 文件分隔符号
	public static String OS_FILE_SEPARATOR = getPropertery("file.separator");
	static List<String>  ips=new ArrayList(); 

	/**
	 * 
	 * 根据系统的类型获取本服务器的ip地址--公网ip
	 * 
	 * InetAddress inet = InetAddress.getLocalHost(); 但是上述代码在Linux下返回127.0.0.1。
	 * 主要是在linux下返回的是/etc/hosts中配置的localhost的ip地址，
	 * 而不是网卡的绑定地址。后来改用网卡的绑定地址，可以取到本机的ip地址：）：
	 * 
	 * @throws UnknownHostException
	 */
	public static String getSystemNetIp() throws UnknownHostException {
		InetAddress inet = null;
		String osname = getSystemOSName();
		System.out.println("当前系统：" + osname);
		try {
			// 针对window系统
			if (osname.toLowerCase().indexOf("windows") == 0) {
				inet = getWinNetIp();
				return inet.getHostAddress();
				// 针对linux系统
			} else {
				if(ips.size()==0)
					ips=SystemHelper.getUnixIpList();
				String netIp=null;
				if(ips.size()==1)
					return ips.get(0);
				 for(String ip:ips){
					 if(!isInnerIP(ip)){
						 netIp=ip;
						 break;
					 }
				 }
				 if(netIp==null)
					 return ips.get(1);
				 return netIp;
			}
		 
		} catch (SocketException e) {
			logger.error("获取本机ip错误" + e.getMessage());
			throw new UnknownHostException("获取本机ip错误" + e.getMessage());
		}
 	}
	/**
	 * 获取内网ip
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getSystemLocalIp() throws UnknownHostException {
		InetAddress inet = null;
		String osname = getSystemOSName();
		System.out.println("当前系统：" + osname);
		try {
			// 针对window系统
			if (osname.toLowerCase().indexOf("windows") == 0) {
				inet = getWinNetIp();
				return inet.getHostAddress();
				// 针对linux系统
			} else {
				if(ips.size()==0)
					ips=SystemHelper.getUnixIpList();
				 for(String ip:ips){
 					 if(isInnerIP(ip))
						 return ip;
				 }
			}
			if (null == inet) {
				throw new UnknownHostException("不存在局域网ip");
			}
		} catch (SocketException e) {
			logger.error("获取本机ip错误" + e.getMessage());
			throw new UnknownHostException("获取本机ip错误" + e.getMessage());
		}
		return null;
	}

	/**
	 * 获取FTP的配置操作系统
	 * 
	 * @return
	 */
	public static String getSystemOSName() {
		// 获得系统属性集
		Properties props = System.getProperties();
		// 操作系统名称
		String osname = props.getProperty("os.name");	 
		return osname;
	}

	/**
	 * 获取属性的值
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getPropertery(String propertyName) {
		return props.getProperty(propertyName);
	}

	/**
	 * 获取window 本地ip地址
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	private static InetAddress getWinNetIp() throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();		
		System.out.println("本机的ip=" + inet.getHostAddress());
		return inet;
	}
 
	
	private static List<String> getUnixIpList() throws SocketException {
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface
				.getNetworkInterfaces();
		InetAddress ip = null;
		List<String> ipList=new ArrayList<String>();
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) netInterfaces
					.nextElement();
			if ((ni.isLoopback()) || (ni.isVirtual()) || (!ni.isUp())) {
				continue;
			}
			Enumeration<InetAddress> ips = ni.getInetAddresses();
			while (ips.hasMoreElements()) {
				ip = (InetAddress) ips.nextElement();
				String ha=ip.getHostAddress();
				if (ha.indexOf(":") == -1) {
					ipList.add(ha);
 
				}
			}

		}

		return ipList;
	}

	 

	/**
	 * 
	 * 获取当前运行程序的内存信息
	 * 
	 * @return
	 */
	public static final String getRAMinfo() {
		Runtime rt = Runtime.getRuntime();
		return "RAM: " + rt.totalMemory() + " bytes total, " + rt.freeMemory()
				+ " bytes free.";
	}
	
	
	
	public static boolean isInnerIP(String ipAddress){   
        boolean isInnerIp = false;   
        long ipNum = getIpNum(ipAddress);   
        /**  
        私有IP：A类  10.0.0.0-10.255.255.255  
               B类  172.16.0.0-172.31.255.255  
               C类  192.168.0.0-192.168.255.255  
        当然，还有127这个网段是环回地址  
        **/  
        long aBegin = getIpNum("10.0.0.0");   
        long aEnd = getIpNum("10.255.255.255");   
        long bBegin = getIpNum("172.16.0.0");   
        long bEnd = getIpNum("172.31.255.255");   
        long cBegin = getIpNum("192.168.0.0");   
        long cEnd = getIpNum("192.168.255.255");   
        isInnerIp = isInner(ipNum,aBegin,aEnd) || isInner(ipNum,bBegin,bEnd) || isInner(ipNum,cBegin,cEnd) || ipAddress.equals("127.0.0.1");
        logger.debug("ip:"+ipAddress+"是内网ip:"+isInnerIp);
        return isInnerIp;              
}  
	private static long getIpNum(String ipAddress) {   
	    String [] ip = ipAddress.split("\\.");   
	    long a = Integer.parseInt(ip[0]);   
	    long b = Integer.parseInt(ip[1]);   
	    long c = Integer.parseInt(ip[2]);   
	    long d = Integer.parseInt(ip[3]);   
	  
	    long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;   
	    return ipNum;   
	}  
	
	
	private static boolean isInner(long userIp,long begin,long end){   
	     return (userIp>=begin) && (userIp<=end);   
	}  
	
	public static void main(String[] args) throws SocketException{
		
		List<String> ips=SystemHelper.getUnixIpList(); 
// 		SystemHelper.logger.info("linux ip:\n{}",ips);
		try {
			System.out.println("局域网ip："+SystemHelper.getSystemLocalIp());
			System.out.println("公网ip："+SystemHelper.getSystemNetIp());
		} catch (UnknownHostException e) {
 			e.printStackTrace();
		}
		
		
	 
	}
}