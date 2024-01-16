package com._5xstar.netflowobserver.ipaddrquerier;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.security.cert.X509Certificate;

/**
 * 非业务逻辑工具
 * 庞海文  2024-1-5
 */
public class Ip2AddrUtil {

    /**
     * 通过HttpClient获取网站内容
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpsGet(final String url ) throws Exception {
        assert(url!=null);
        // Trust standard CA and those trusted by our custom strategy
        final SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial((chain, authType) -> {
                    final X509Certificate cert = chain[0];
                    //return "CN=5xstar.com".equalsIgnoreCase(cert.getSubjectDN().getName());
                    return true;
                })
                .build();
        final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .build();
        // Allow TLSv1.3 protocol only
        final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultTlsConfig(TlsConfig.custom()
                        .setHandshakeTimeout(Timeout.ofSeconds(30))
                        .setSupportedProtocols(TLS.V_1_3)
                        .build())
                .build();
        try(CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                .build()) {

            final HttpGet httpget = new HttpGet(url);

            //System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

            final HttpClientContext clientContext = HttpClientContext.create();
            return httpclient.execute(httpget, clientContext, response -> {
                //System.out.println("----------------------------------------");
                //System.out.println(httpget + "->" + new StatusLine(response));
                //EntityUtils.consume(response.getEntity());
                final SSLSession sslSession = clientContext.getSSLSession();
               // if (sslSession != null) {
               //     System.out.println("SSL protocol " + sslSession.getProtocol());
               //     System.out.println("SSL cipher suite " + sslSession.getCipherSuite());
               // }
                final String result = EntityUtils.toString(response.getEntity());
                //System.out.println("result:" + result);
                if(response.getCode()==200) return result;
                else return null;
            });
        }
    }
}
