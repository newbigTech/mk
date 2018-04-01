package com.hand.hap.cloud.hpay.services.phoneServices.wechat.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.CertificateException;

public class HttpUtil {
    private final static int CONNECT_TIMEOUT = 10000; // in milliseconds
    private final static String DEFAULT_ENCODING = "UTF-8";
    public static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String postData(String urlStr, String data) {
        return postData(urlStr, data, null);
    }

    public static String postDataWithCert(
            String certPath,
            String psw,
            String url,
            String reqXML)
            throws KeyStoreException,
            IOException,
            UnrecoverableKeyException,
            NoSuchAlgorithmException,
            KeyManagementException {
        KeyStore keyStore;
        keyStore = KeyStore.getInstance("PKCS12");

        FileInputStream instream = new FileInputStream(new File(certPath));
        try {
            keyStore.load(instream, psw.toCharArray());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, psw.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(reqXML, "UTF-8"));
        System.out.println("executing request" + httpPost.getRequestLine());

        CloseableHttpResponse response = httpclient.execute(httpPost);
        String respXML = EntityUtils.toString(response.getEntity(), "UTF-8");
        logger.info("----------------------------------------" + respXML);
        response.close();
        httpclient.close();
        return respXML;
    }


    public static String postData(String urlStr, String data, String contentType) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(CONNECT_TIMEOUT);
            if (contentType != null) {
                conn.setRequestProperty("content-type", contentType);
            }
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);
            if (data == null) {
                data = "";
            }
            writer.write(data);
            writer.flush();
            writer.close();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
            return sb.toString();
        } catch (IOException e) {
            logger.error("Error connecting to " + urlStr + ": " + e.getMessage());
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
            }
        }
        return null;
    }
}
