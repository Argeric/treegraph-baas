package org.treegraph.baas.core.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@Slf4j
public class SSLUtils {

  public static void initSSL() {
    try {
      trustAllHttpsCertificates();
      setDefaultHostnameVerifier();
    } catch (Exception e) {
      log.error("SSLUtils", e);
    }
  }

  private static void trustAllHttpsCertificates() throws Exception {
    TrustManager[] allTrustCerts = Lists.newArrayList(new MyTrustManager())
        .toArray(new TrustManager[]{});
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, allTrustCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  }

  private static void setDefaultHostnameVerifier() {
    HostnameVerifier hv = (urlHostName, session) -> {
      log.debug("SSLUtils, URL Host: " + urlHostName + " vs. " + session.getPeerHost());
      return true;
    };
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
  }

  static class MyTrustManager implements TrustManager, X509TrustManager {

    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
    }
  }
}
