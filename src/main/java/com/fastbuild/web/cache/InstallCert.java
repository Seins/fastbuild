package com.fastbuild.web.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 类名：com.fastbuild.web.cache.InstallCert
 * 创建者： 邓风森 .
 * 创建时间：2016/1/26
 */


public class InstallCert {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallCert.class);
    private String certPath;

    public static void loadingCert(String host, int port, String version) throws Exception {

        char[] passphrase;
        String p = "changeit";
        passphrase = p.toCharArray();

        File file = new File("jssecacerts");
        if (file.isFile() == false) {
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP + "lib"
                    + SEP + "security");
            file = new File(dir, "jssecacerts");
            if (file.isFile() == false) {
                file = new File(dir, "cacerts");
            }
        }
        System.out.println("Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf
                .getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory factory = context.getSocketFactory();

        System.out.println("Opening connection to " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(10000);
        try {
            LOGGER.info("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            LOGGER.info("No errors, certificate is already trusted");
        } catch (SSLException e) {
            LOGGER.error(" start ssl handshake occur error",e);
        }

        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            LOGGER.info("Could not obtain server certificate chain");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));

        LOGGER.info("Server sent " + chain.length + " certificate(s):");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            LOGGER.info(" " + (i + 1) + " Subject "
                    + cert.getSubjectDN());
            LOGGER.info("   Issuer  " + cert.getIssuerDN());
            sha1.update(cert.getEncoded());
            LOGGER.info("   sha1    " + toHexString(sha1.digest()));
            md5.update(cert.getEncoded());
            LOGGER.info("   md5     " + toHexString(md5.digest()));

        }

        LOGGER.info(" add to trusted keystore ,version: {}", version);
        int k;
        try {
            k = (version.length() == 0) ? 0 : Integer.parseInt(version) - 1;
        } catch (NumberFormatException e) {
            LOGGER.error("KeyStore not changed");
            return;
        }

        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        ks.setCertificateEntry(alias, cert);

        OutputStream out = new FileOutputStream("jssecacerts");
        ks.store(out, passphrase);
        out.close();

        LOGGER.info("cert loaded success : {}", cert);
        LOGGER.info("Added certificate to keystore 'jssecacerts' using alias '"
                + alias + "'");
    }

    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }

    private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }

}