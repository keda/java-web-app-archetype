package ${package}.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 核心代码就是毫秒级时间n位+机器ID workerIdBits 位+毫秒内序列sequenceBits位。
 * 第一段的毫秒级时间记录的是当前时间与之前一个相对时间的差值。
 * 使用序列号生成器之前，需要确认：
 * 		当前应用有对{user.home}目录的读写权限；或者创建{user.home}/OKDI_ID_WORKER.properties，然后为当前运行程序用户分配OKDI_ID_WORKER.properties的读写权限
 * 限制：每台机器上部署同一应用数量不得大于4个,毫秒内生成id在1024之内
 * @author xiansheng.meng
 * 
 */
public class IdWorker {
	private static Logger logger = LoggerFactory.getLogger(IdWorker.class);
	
	private static IdWorker idWorker =null;
	private static IdWorker idWorker2 = null;

	private long workerId;
	private final static long twepoch = 1361753741828L;
	
	private static   long ip=-1;
	// 当前毫秒内的序列数值
	private long sequence = 0;
	// 每个应用在一个机器上可以部署的数量2^2，
	private final static long workerIdBits = 2L;
	
	// 机器的位数，可以调整 ，
	private final static long ipBits = 8L;

	/**
	 * 最大的workerId的值，默认2^8 -1
	 */
	public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
	// 毫秒单位内产生id的位数，范围 0-1023
	private final static long sequenceBits = 10L;
	private final static long workerIdShift = sequenceBits;
	private final static long ipShift = sequenceBits+workerIdBits;

	private final static long timestampLeftShift = sequenceBits + workerIdBits+ipBits;

	/**
	 * 一个指定workerId一个毫秒内生成的序列号的最大数 默认1023
	 */
	public final static long sequenceMask = -1L ^ -1L << sequenceBits;

	private long lastTimestamp = -1L;
	
	private static long getIp(){
//		String name = InetAddress.getLocalHost().getHostName();
        // 获取IP地址
        try {
        	String ip = SystemHelper.getSystemLocalIp();
        	if(ip!=null){
         		logger.info("当前地址IP:", ip);
        		return Long.valueOf(ip.substring(ip.lastIndexOf(".")+1));
        	}

		} catch (UnknownHostException e) {
			logger.error("IdWorker 获取本机地址失败！！！", e);
		}
    	System.exit(0);
     	return 0;
	}
	/**
	 * 机器/JVM的序号0-1023, 数量可以调整IdWorker.workerIdBits参数
	 * 
	 * @param workerId
	 *            机器ip对应的生成唯一序列的编号，取值0-1023
	 */
	private IdWorker(final long workerId) {
		super();
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					maxWorkerId));
		}
		this.workerId = workerId;
	}

	/**
	 * 不带workId参数，默认在{user.home}/OKDI_ID_WORKER.properties文件中读取相应的配置信息，该文件中的配置信息自动产生
	 * 请确保在classes/idwork.properties属性文件中配置项目名称pro_name 的属性，最好使用拼音或英文来标示
	 */
	public static IdWorker getIdWorker() {
		if(idWorker==null){
			idWorker=new IdWorker();
		}
		return idWorker;
	}

	/**
	 * 机器/JVM的序号0-1023, 数量可以调整IdWorker.workerIdBits参数
	 * 同一个JVM中目前只支持一个workerId的IdWorker实例。
	 * 
	 * @param workerId
	 *            机器ip对应的生成唯一序列的编号，取值0-1023
	 */
	public static IdWorker getIdWorker(long workerId) {
		if (idWorker2 == null || workerId != idWorker2.workerId)
			idWorker2 = new IdWorker(workerId);
		return idWorker2;
	}

	/**
	 * 不带workId参数，默认在{user.home}/OKDI_ID_WORKER.properties文件中读取相应的配置信息，该文件中的配置信息自动产生
	 * 
	 */
	private IdWorker() {
		ip=getIp();
		workerId = -1; // 初始化为-1

		// ////////////在properties文件中获取项目名
		Properties prop = new Properties();
		 //读取jar包内配置 Loader.class.getClassLoader
		InputStream in=null;

		String pro_name = null;
		try {
			in = IdWorker.class.getClassLoader().getResource("idwork.properties").openStream();
			prop.load(in);
			pro_name = prop.getProperty("pro_name");
			if (pro_name != null) {
				pro_name = pro_name.trim();
			} else {
				// System.err.println(" idwork.properties文件中pro_name不存在");
				throw new RuntimeException(" idwork.properties文件中pro_name不存在");
			}
		} catch (IOException e) {
			// System.err.println("不存在idwork.properties配置文件,系统将在user.dir下自动产生当前实例对应的workid");
			workerId = -1;
			throw new RuntimeException("读取idwork.properties文件中pro_name失败",
					e.getCause());
		} catch (Exception e) {
			// System.err.println("不存在idwork.properties配置文件或者属性work_id不存在,系统将在user.dir下自动产生当前实例对应的workid");
			workerId = -1;
			throw new RuntimeException("读取idwork.properties文件中pro_name失败",
					e.getCause());
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// //获取 当前项目所在目录
		String userDir = System.getProperty("user.dir");

		// /////在classes目录下没有配置文件，通过程序自动管理
		// 当前用户目录工作目录
		String userHome = System.getProperty("user.home");
		String idworkPath = userHome + File.separator
				+ "OKDI_ID_WORKER.properties";
		System.out.println("项目工作目录对应的workerid配置："+idworkPath);
		//OKDI_ID_WORKER.properties 属性存储 pro_name+"$"+userDir=workerId
		File f = new File(idworkPath);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		InputStream is = null;

		Properties pps = new Properties();

		// <项目名,<工作目录,WORKID>>
		Map<String, Map<String, Integer>> workIdMap = new HashMap<String, Map<String, Integer>>();// 存放一个项目下，不同工作目录的workId
		try {
			is = new BufferedInputStream(new FileInputStream(f));
			pps.load(is);
		} catch (IOException e) {
			throw new RuntimeException("读取" + idworkPath + " 失败", e.getCause());
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		Iterator<Entry<Object, Object>> prosIt = pps.entrySet().iterator();
		while (prosIt != null && prosIt.hasNext()) {
			Entry<Object, Object> entry = prosIt.next();
			String proPath = (String) entry.getKey();
			int splitIndex = proPath.indexOf("$");
			if (splitIndex == -1) {
				continue;
			}
			String proName = proPath.substring(0, splitIndex);
			String proDirPath = proPath.substring(splitIndex+1);

			if (proName.equals(pro_name)) {// 项目名称匹配，工作目录匹配
				String _workerId = (String) entry.getValue();
				Integer wid = -1;
				try {
					wid = Integer.valueOf(_workerId);
				} catch (NumberFormatException e) {
					throw new RuntimeException(idworkPath
							+ "中，所有的属性值必须为0-1023的整形数字", e.getCause());
				}
				// 判断容器中是否有该项目信息
				if (!workIdMap.containsKey(proName)) {
					workIdMap.put(proName, new HashMap<String, Integer>());
				}
				// 项目，工作目录对应的workId
				Map<String, Integer> dirWorkerIdMap = workIdMap.get(proName);
				if (!dirWorkerIdMap.containsKey(proDirPath)) {
					dirWorkerIdMap.put(proDirPath, wid);
				}
				// 同一个项目下的，同一个工作目录 找到了，找到了workerId
				if (proDirPath.equals(userDir)) {
					workerId = wid;
				}
			}
		}
		// workerId值成功找到
		if (this.workerId > -1)
			return;

 		Map<String, Integer> dirWorkerIdMap = workIdMap.get(pro_name);
 		Collection<Integer> proWorkerIdColl=dirWorkerIdMap!=null?dirWorkerIdMap.values():null;
		Iterator<Integer> proWorkerIdIt = proWorkerIdColl!=null?proWorkerIdColl.iterator():null;

		// 获取已经存在的项目在不同工作目录下的最大workerid
		int maxWorkId = -1;
		while (proWorkerIdIt != null && proWorkerIdIt.hasNext()) {
			Integer _wid = proWorkerIdIt.next();
			if (maxWorkId < _wid)
				maxWorkId = _wid;
		}
		// 当前工作目录的最大工作id设置
		this.workerId = maxWorkId + 1;
		//workId值过大
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					maxWorkerId));
		}
		//将项目当前的工作目录对应的workerId更新到配置文件中
 		writeProperties(idworkPath,pro_name+"$"+userDir,this.workerId+"");
		//ip地址初始化
 		 
	}

	private static void writeProperties(String profilepath, String keyname,
			String keyvalue) {
		Properties props = new Properties();
		OutputStream fos = null;
		try {

			props.load(new FileInputStream(profilepath));
 			// 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			fos = new FileOutputStream(profilepath);
			props.setProperty(keyname, keyvalue);
			// 以适合使用 load 方法加载到 Properties 表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			props.store(fos, "Update '" + keyname + "' value");
		} catch (IOException e) {
			System.err.println("属性文件更新错误");
		}finally{
			if(fos!=null)
				try {
					fos.close();
				} catch (IOException e) {
 					e.printStackTrace();
				}			
		}
	}
 

	/**
	 * 获取一个序列号
	 * 
	 * @return 序列号
	 */
	public synchronized long nextId() {
		long timestamp = this.timeGen();
		// 一个毫秒没有过去
		if (this.lastTimestamp == timestamp) {
			this.sequence = (sequence + 1) & sequenceMask;
			// 毫秒内序列已经用到2^10 了
			if (this.sequence == 0) {
				logger.info("sequenceMask = " + sequenceMask);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
 					e.printStackTrace();
				}
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			// 一个毫秒已经过去，毫秒内序列进入下一个循环0-4096
			this.sequence = 0;
		}
		// 服务器时间调小了
		if (timestamp < this.lastTimestamp) {
			try {
			 throw new  RuntimeException(String.format("服务器时间向后调整了，拒绝生成id，调后了 %d毫秒",
						this.lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.lastTimestamp = timestamp;

		long nextId = (((timestamp - twepoch) << timestampLeftShift))
				|(ip<<ipShift)
				| (this.workerId << workerIdShift) 
				| (this.sequence);
		// System.out.println("timestamp:" + ((timestamp - twepoch)) +
		// ",workerId:"
		// + workerId + ",sequence:" + sequence+"\nnextId:"+nextId);
		return nextId;

	}

	/*
	 * private String formatStr(String str,int len){ int strLen=str.length();
	 * long cha=len-strLen; StringBuffer sb=new StringBuffer(str); for(int
	 * i=0;i<cha;i++){ sb.append("0"); } sb.append(str); return sb.toString(); }
	 */
	/**
	 * 获取大于lastTimestamp 的下一个时间戳
	 * 
	 * @param lastTimestamp
	 * @return
	 */
	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis() + "-1361753741828=``````````````````\n"
				+ (System.currentTimeMillis() - 1361753741828L));
		Calendar c = Calendar.getInstance();
		c.set(2013, 1, 1, 0, 0, 0);
		// /user.dir java.io.tmpdir
		String pro = System.getProperty("user.dir");
		System.out.println("user.dir\t" + pro);

//		System.out.println(c.getTimeInMillis());
		final IdWorker worker2 =   IdWorker.getIdWorker();
		long t1 = System.nanoTime();
		for(int j=0;j<1000;j++){
			new Thread(new Runnable(){
 				@Override
				public void run() {
 					for (int i = 0; i < 500; i++) {
 						long a = worker2.nextId();
 						// System.out.println(a);
 						System.out.println(a);
 						// try {
 						// Thread.currentThread().sleep(100);
 						// } catch (InterruptedException e) {
 						// // TODO Auto-generated catch block
 						// e.printStackTrace();
 						// }
// 						try {
// 							Thread.sleep((long) (Math.random() * 1000));
// 						} catch (InterruptedException e) {
// 							// TODO Auto-generated catch block
// 							e.printStackTrace();
// 						}
 					}					
				}}).start();
		}
		
		System.out.println("\n\n");
/*
		System.out.println("当前时间：" + System.currentTimeMillis());
		System.out.println("Long.MAX_VALUE:" + Long.MAX_VALUE);
		System.out.println("100000\t:" + (System.nanoTime() - t1) / 1000);
		System.out.println("-1L ^ -1L << workerIdBits:"
				+ (-1L ^ -1L << workerIdBits));
		System.out.println(16L << 4L);

		System.out.println(578L + "\t" + ((578L << 64L)));
		Date dt = new Date(1361753741828L);

		long t = System.currentTimeMillis();
		System.out.println(t - 1361753741828L);
		System.out.println("1361753741828L:"
				+ DateFormat.getDateTimeInstance().format(dt) + "\t" + t
				+ "-1361753741828L :" + (t - 1361753741828L));

		long ppd = (219L << 16L) | (192L << 8L) | 9L;
		System.out.println(ppd);
		System.out.print(Long.MAX_VALUE);*/
	}

}
