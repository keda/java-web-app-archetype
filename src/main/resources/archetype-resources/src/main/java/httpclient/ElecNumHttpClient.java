package ${package}.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElecNumHttpClient {
    
    public final Logger log = LoggerFactory.getLogger(getClass());
    public final String PATH_SEPARATOR = "/";
    public final static String TIMESTAMP = "timestamp";
    public final static String PUBLICKEY = "publickey";
    public final static String SECRET = "secret";
    
    private CloseableHttpClient client;
    private String url ;
    private String parternId ;
    private String clientId ;
    private String customerId ;
    
    public CloseableHttpClient getClient() {
        return client;
    }
    
    public ElecNumHttpClient(String url, CloseableHttpClient client) {
        this.url = url;
        this.client = client;
    }
    
    public ElecNumHttpClient(String url, String parternId, String clientId,
           String customerId, CloseableHttpClient client) {
        super();
        this.url =url;
        this.parternId = parternId;
        this.clientId = clientId;
        this.customerId = customerId;
        this.client = client;
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
        try {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        String xml=getXML(params);
        System.out.println(xml);
        list.add(new BasicNameValuePair("logistics_interface",xml ));
        list.add(new BasicNameValuePair("data_digest", getSign(xml)));
        list.add(new BasicNameValuePair("clientId", clientId));
            HttpPost post = new HttpPost(b.build());
            post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
            return execute(post);
        } catch (URISyntaxException e) {
//            String str = reqParamsToString(params);
//            throw new AmssyApiException("构建POST请求参数失败![uri="+b.toString()+", params="+str+"]", e);
        } catch (UnsupportedEncodingException e) {
            throw new AmssyApiException("构建POST请求参数失败!", e);
        } catch (Exception re){
//            String str = reqParamsToString(params);
//            throw new AmssyApiException("POST请求失败![uri="+b.toString()+", params="+str+"]", re);
        }
        return "";
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

    private URIBuilder buildUri(String apiMethod) {
        URIBuilder b = new URIBuilder();
        
        String method = StringUtils.trimToNull(apiMethod);
        
        if(method == null) throw new IllegalArgumentException("apiMethod not null!");
        
        if(url.endsWith(PATH_SEPARATOR)) {
            b.setPath(url.concat(method));
        }else {
            b.setPath(url.concat(PATH_SEPARATOR).concat(method));
        }
        
        return b;
        
    }
    
    private String getXML(Map<String,Object> params){
        //数据
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<RequestOrder>");
        xmlBuilder.append("<clientID>"+this.clientId+"</clientID>"); 
        xmlBuilder.append("<logisticProviderID>YTO</logisticProviderID>");
        xmlBuilder.append("<customerId >"+customerId+"</customerId>");
        xmlBuilder.append("<txLogisticID>"+clientId+params.get("ID")+"</txLogisticID>");
        xmlBuilder.append("<tradeNo>"+params.get("TRADE_TID")+"</tradeNo>");
        xmlBuilder.append("<totalServiceFee>0.0</totalServiceFee>");
        xmlBuilder.append("<codSplitFee>0.0</codSplitFee>");
        xmlBuilder.append("<orderType>1</orderType>");
        xmlBuilder.append("<serviceType>0</serviceType>");//服务类型(1-上门揽收, 2-次日达 4-次晨达 8-当日达,0-自己联系)
        xmlBuilder.append("<flag>1</flag>");
        xmlBuilder.append("<sender>");
        xmlBuilder.append("<name>"+params.get("SEND_NAME")+"</name>");
        xmlBuilder.append("<postCode>"+params.get("SEND_ZIP")+"</postCode>");
        if(!StringUtils.isBlank((String)params.get("SEND_PHONE"))){
            //xmlBuilder.append("<phone>"+params.get("SEND_PHONE")+"</phone>");
        }
        if(!StringUtils.isBlank((String)params.get("SEND_MOBILE")))
        xmlBuilder.append("<mobile>"+params.get("SEND_MOBILE")+"</mobile>");
        xmlBuilder.append("<prov>"+params.get("SEND_STATE")+"</prov>");
        String city="";
        city=StringUtils.trimToEmpty((String)params.get("SEND_CITY"));
        if(!StringUtils.isBlank((String)params.get("SEND_DISTRICT"))){
            if(!StringUtils.isBlank((String)city))city=city+",";
            city += params.get("SEND_DISTRICT").toString();
        }
        xmlBuilder.append("<city>"+city+"</city>");
        xmlBuilder.append("<address>"+params.get("SEND_ADDRESS")+"</address>");
        xmlBuilder.append("</sender>");
        xmlBuilder.append("<receiver>");
        xmlBuilder.append("<name>"+params.get("RECEIVER_NAME")+"</name>");
        xmlBuilder.append("<postCode>"+params.get("RECEIVER_ZIP")+"</postCode>");
        if(!StringUtils.isBlank((String)params.get("RECEIVER_PHONE")))
        xmlBuilder.append("<phone>"+params.get("RECEIVER_PHONE")+"</phone>");
        if(!StringUtils.isBlank((String)params.get("RECEIVER_MOBILE")))
        xmlBuilder.append("<mobile>"+params.get("RECEIVER_MOBILE")+"</mobile>");
        xmlBuilder.append("<prov>"+params.get("RECEIVER_STATE")+"</prov>");
        String recity="";
        recity=StringUtils.trimToEmpty((String)params.get("RECEIVER_CITY"));
        if(!StringUtils.isBlank((String)params.get("RECEIVER_DISTRICT"))){
            if(!StringUtils.isBlank(recity)) recity=recity+",";
            recity += params.get("RECEIVER_DISTRICT").toString();
        }
        xmlBuilder.append("<city>"+recity+"</city>");
        xmlBuilder.append("<address>"+params.get("RECEIVER_ADDRESS")+"</address>");
        xmlBuilder.append("</receiver>");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm s.S z");
        Date d=new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, 1);//结束时间 是 开始时间后一天
        xmlBuilder.append("<sendStartTime>"+sdf.format(d)+"</sendStartTime>");
        xmlBuilder.append("<sendEndTime>"+sdf.format(c.getTime())+"</sendEndTime>");
        xmlBuilder.append("<goodsValue>0</goodsValue>");
        xmlBuilder.append("<totalValue>"+params.get("TOTAL_FEE")+"</totalValue>");
        xmlBuilder.append("<itemsValue>"+params.get("TOTAL_FEE")+"</itemsValue>");
        xmlBuilder.append("<items>");
        if(!StringUtils.isBlank((String)params.get("ORDER_INFOS"))){
            String[] orderInfos=params.get("ORDER_INFOS").toString().split(",");
            for(String orderInfo:orderInfos){
                String[] infos=orderInfo.split(";");
                xmlBuilder.append("<item>");
                xmlBuilder.append("<itemName>"+infos[1]+"</itemName>");
                xmlBuilder.append("<number>"+infos[0]+"</number>");
                xmlBuilder.append("<itemValue>"+infos[2]+"</itemValue>");
                xmlBuilder.append("</item>");
            }
            
        }
        xmlBuilder.append("</items>");
        xmlBuilder.append("<insuranceValue>0.0</insuranceValue>");
        xmlBuilder.append("<special>0</special>");
       // xmlBuilder.append("<remark>易碎品</remark>");
        xmlBuilder.append("</RequestOrder>");
        return xmlBuilder.toString();
    }
   
    /**
     * 描述:加密并生成签名
     * @param xml
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @author zhao
     * @date 2015-4-2 下午1:45:45
     */
    private String getSign(String xml) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        messagedigest.update((xml + parternId).getBytes("UTF-8"));
        byte[] abyte0 = messagedigest.digest();
        return new String(Base64.encodeBase64(abyte0));
    }
    public String getUrl() {
        return url;
    }

    public String getParternId() {
        return parternId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getCustomerId() {
        return customerId;
    }
  
}
