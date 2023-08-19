package org.easycontactforms.core;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

@Slf4j
public class FirstStartupChecker {
    public void checkDirectories() {
        File pluginsDir = new File("plugins");
        if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
            createPluginDirectory(pluginsDir);
        }
        File configDir = new File("config");
        if (!configDir.exists() || !configDir.isDirectory()) {
            createConfigDirectory(configDir);

        }
        File secretsDir = new File("secrets");
        if(!secretsDir.exists() || !secretsDir.isDirectory()){
            generateSSLCertificate(secretsDir);
        }
    }

    private void createPluginDirectory(File pluginsDir){
        log.info("Plugin directory not found: " + pluginsDir + " | Adding directory");
        if (!pluginsDir.mkdirs()) {
            log.error("Could not create plugins directory");
        }
    }

    private void createConfigDirectory(File configDir){
        log.info("Config directory not found! Adding directory");
        if (!configDir.mkdirs()) {
            log.error("Could not create config directory");
            throw new RuntimeException("Could not create config directory");
        }
        InputStream resource = getClass().getClassLoader().getResourceAsStream("/application.properties");
        if (resource == null) {
            throw new IllegalArgumentException("application.properties not found!");
        } else {

            try {
                log.info("Adding standard configuration to config directory");
                byte[] data = resource.readAllBytes();

                OutputStream out = new FileOutputStream(configDir.getAbsolutePath() + System.getProperty("file.separator") + "application.properties");
                out.write(data);
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void generateSSLCertificate(File secretsDir)  {
        log.info("Building certificate");
        if (!secretsDir.exists() || !secretsDir.isDirectory()) {
            if(!secretsDir.mkdirs()){
                log.error("Could not create directory");
                return;
            }
        }
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPair keyPair = generateKeyPair();
            X509Certificate cert = generateSelfSignedCertificate(keyPair);
            savePrivateKeyToFile(keyPair.getPrivate(), secretsDir.getAbsolutePath() + System.getProperty("file.separator") + "private.key");
            saveCertificateToFile(cert, secretsDir.getAbsolutePath() + System.getProperty("file.separator") + "server.crt");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 365 * 24 * 60 * 60 * 1000L); // 1 year validity

        X500Name dnName = new X500Name("CN=localhost");

        BigInteger certSerialNumber = BigInteger.valueOf(System.currentTimeMillis());
        SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(keyPair.getPublic().getEncoded()));

        X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
                dnName,
                certSerialNumber,
                startDate,
                endDate,
                dnName,
                info
        );

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());

        return new JcaX509CertificateConverter().getCertificate(certificateBuilder.build(contentSigner));
    }

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048); // Key size
        return generator.generateKeyPair();
    }

    private void savePrivateKeyToFile(PrivateKey privateKey, String filePath) throws IOException {
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(filePath))) {
            pemWriter.writeObject(privateKey);
        }
    }

    private void saveCertificateToFile(X509Certificate certificate, String filePath) throws IOException {
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(filePath))) {
            pemWriter.writeObject(certificate);
        }
    }

}
