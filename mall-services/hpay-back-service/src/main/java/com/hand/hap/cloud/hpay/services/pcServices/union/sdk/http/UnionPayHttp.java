/**
 *
 */
package com.hand.hap.cloud.hpay.services.pcServices.union.sdk.http;

import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.HttpClient;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.SDKUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


/**
 * @author jianjun.tan
 * @Title PropertiesUtil
 * @date 2017/05/25 19:08
 */
public class UnionPayHttp {
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(final String hostname, final SSLSession session) {
            return true;
        }
    };
    //默认字符编码
    public static String encoding = "UTF-8";

    //全渠道固定值
    public static String version = "5.0.0";
    private static Logger logger = LoggerFactory.getLogger(UnionPayHttp.class);

    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]
                {new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {//
                    }

                    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {//
                    }
                }};

        // Install the all-trusting trust manager
        try {
            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (final Exception e) {
            System.out.println("Error with SSL connection. " + e.getLocalizedMessage());
        }
    }

    public static HttpsURLConnection createSecureConnection(final URL url) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Connection", "Keep-Alive");
        return connection;
    }

    public static void unionDoPost(final String url, final Map<String, String> httpBody) throws IOException {
        OutputStream os = null;
        final HttpURLConnection connection;
        final URL requestURL = new URL(url);
        trustAllHosts();
        final HttpsURLConnection https = createSecureConnection(requestURL);
        https.setHostnameVerifier(DO_NOT_VERIFY);
        connection = https;
        connection.setRequestMethod("POST");
        HttpURLConnection.setFollowRedirects(true);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        if (httpBody != null && !httpBody.isEmpty()) {
            os = new BufferedOutputStream(connection.getOutputStream());
            os.write(encodePostBody(httpBody).getBytes());
            os.flush();
        }

        String response = "";
        try {
            System.out.println(connection.toString());
            response = readFromStream(connection.getInputStream());
        } catch (final FileNotFoundException e) {
            System.out.println("Error reading stream \"" + connection.getInputStream() + "\". " + e.getLocalizedMessage());
            response = readFromStream(connection.getErrorStream());
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String encodePostBody(final Map<String, String> parameters) {
        if (parameters == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();

        for (final Iterator<String> i = parameters.keySet().iterator(); i.hasNext(); ) {
            final String key = i.next();
            final Object parameter = parameters.get(key);
            if (!(parameter instanceof String)) {
                continue;
            }

            try {
                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode((String) parameter, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                // TODO report parse error
                System.out.println("Error encoding \"" + key + "\" or \"" + parameter + "\" to UTF-8 ." + e.getLocalizedMessage());
            }

            if (i.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    private static String readFromStream(final InputStream in) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    /**
     * 构造HTTP POST交易表单的方法示例
     *
     * @param action  表单提交地址
     * @param hiddens 以MAP形式存储的表单键值
     * @return 构造好的HTTP POST交易表单
     */
    public static String createHtml(final String action, final Map<String, String> hiddens) {
        final StringBuffer sf = new StringBuffer();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"" + action + "\" method=\"post\">");
        if (null != hiddens && 0 != hiddens.size()) {
            final Set<Entry<String, String>> set = hiddens.entrySet();
            final Iterator<Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                final Entry<String, String> ey = it.next();
                final String key = ey.getKey();
                final String value = ey.getValue();
                sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + value + "\"/>");
            }
        }
        sf.append("</form>");
        sf.append("</body>");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.all.pay_form.submit();");
        sf.append("</script>");
        sf.append("</html>");
        return sf.toString();
    }

    public static Map<String, String> signData(final Map<String, ?> contentData) {
        Entry<String, String> obj = null;
        final Map<String, String> submitFromData = new HashMap<String, String>();
        for (final Iterator<?> it = contentData.entrySet().iterator(); it.hasNext(); ) {
            obj = (Entry<String, String>) it.next();
            final String value = obj.getValue();
            if (StringUtils.isNotBlank(value)) {
                // 对value值进行去除前后空处理
                submitFromData.put(obj.getKey(), value.trim());
                //               LOG.info("===key-value==="+obj.getKey() + "-->" + String.valueOf(value));
            }
        }
        /**
         * 签名
         */
        SDKUtil.sign(submitFromData, encoding);

        return submitFromData;
    }

    /**
     * 功能：后台交易给银联地址发请求
     *
     * @param encoding 上送请求报文域encoding字段的值
     * @return 返回报文 map
     */
    public static Map<String, String> submitUrl(final Map<String, String> submitFromData, final String requestUrl,
                                                final String encoding) {
        String resultString = "";
        logger.info("请求银联地址:" + requestUrl);
        /**
         * 发送后台请求数据
         */
        final HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
        try {
            final int status = hc.send(submitFromData, encoding);
            if (200 == status) {
                resultString = hc.getResult();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        Map<String, String> resData = new HashMap<String, String>();
        /**
         * 验证签名
         */
        if (null != resultString && !"".equals(resultString)) {
            // 将返回结果转换为map
            resData = SDKUtil.convertResultStringToMap(resultString);
            if (SDKUtil.validate(resData, encoding)) {
                logger.info("验证签名成功,可以继续后边的逻辑处理");
            } else {
                logger.info("验证签名失败,必须验签签名通过才能继续后边的逻辑...");
            }
        }
        return resData;
    }


    /**
     * 订单号生成，该生产订单号仅用于测试，商户根据自己需要生成订单号
     *
     * @return
     */
    public static String generateOrdrNo() {
        final DateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        final StringBuilder sb = new StringBuilder(formater.format(new Date()));
        return sb.toString();
    }


}
