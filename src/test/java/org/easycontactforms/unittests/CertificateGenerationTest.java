package org.easycontactforms.unittests;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.easycontactforms.core.FirstStartupChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.security.auth.x500.X500Principal;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;

public class CertificateGenerationTest {

    @Test
    public void testKeyPairGeneration() throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        FirstStartupChecker checker = new FirstStartupChecker();
        KeyPair keyPair = checker.generateKeyPair();
        Assertions.assertNotNull(keyPair);
        Assertions.assertNotNull(keyPair.getPublic());
        Assertions.assertNotNull(keyPair.getPrivate());
    }

    @Test
    public void testCertificateGeneration() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        FirstStartupChecker checker = new FirstStartupChecker();
        KeyPair keyPair = checker.generateKeyPair();

        X509Certificate certificate = checker.generateSelfSignedCertificate(keyPair);

        Assertions.assertNotNull(certificate);

        X500Principal principal = certificate.getIssuerX500Principal();
        Assertions.assertEquals("CN=localhost", principal.getName());
    }
}
