/*
 * Copyright (C) 2015-2018 S.Violet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project GitHub: https://github.com/shepherdviolet/thistle
 * Email: shepherdviolet@163.com
 */

package sviolet.thistle.x.easycrypto;

import org.junit.Test;
import sviolet.thistle.util.conversion.Base64Utils;
import sviolet.thistle.util.crypto.PEMEncodeUtils;
import sviolet.thistle.x.ezcrypto.EasyCrypto;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class EasyCryptoParseKeyTest {

    private static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBqMkEkz2VQ1VdbGx8BVhKJr7rJmWEDhYlkxXtRkm6+cU3GvEDJOah+z4rwPxMsYRgoyAEVRxl4YbqACVYCz+Q0p58+q2B03ur7yOKyG9IKmAPk0VrENsoBZOD5Oc6IFL2j0KxkH+7NCIvmkhjdabbxmGZMRBCYvgFgRPE0YWZZwIDAQAB";
    private static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIGoyQSTPZVDVV1sbHwFWEomvusmZYQOFiWTFe1GSbr5xTca8QMk5qH7PivA/EyxhGCjIARVHGXhhuoAJVgLP5DSnnz6rYHTe6vvI4rIb0gqYA+TRWsQ2ygFk4Pk5zogUvaPQrGQf7s0Ii+aSGN1ptvGYZkxEEJi+AWBE8TRhZlnAgMBAAECgYA/mRzQw7BHULnEk3Q6+RwvcwXerfzJY2d1ksoEkp+DuRQFTY++bRE7jtMV/xlCPSijhiAkP+MoDhFxIhUvNIU+uLcYnjjpctL4+JQbHW8gyGe5iE/YTFoBPlsuXBMAyZj5oYt8Z9PmtSUf0amYW57n3PZXrASuIWZdkDdjCXN9sQJBAO+WCYg+OmDH2x6PmvAFf4zARK2Soqz+gTalQ7cdxk7qPxGf5+eMifcAF8nrVRi2/Pdi3O8DXcYhIwiI8SHlM40CQQCKis446K4JwP09WlyuNC+7pKahZBjOPPenD6kZHkpauHBvTu5ENQgbKVkkPdKL1TpgOAVckQp4pgchhhIRFOnDAkBd62gjM5m49L+uFEd9jfo5V+nUTSZeQIqwvEp2T0K9DtDYfOHxAEPZQv0QV6ONQ7aY1/WZ8KzjDXxpTR2R1wLFAkAX4Z90vd8qPUMp7UamnMZRoSs8DMYd3vKNsKxkcQ0+ICb5ePCnf0C7deAw2BtQEVxzs5RxBWt5qpz2EWdR3HoDAkEA4hLhb7mAij5iSElO03TJgmjkqwotJwkU9Y8m90iKdZsHGlwIFfocUHTPqQ3SIJWSQGo4ziQ0diiUk8OvyowfJA==";

    private static final String ECC_PUBLICKEY = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE+Sboxe1cUh5Vd5ML5xSMkKXeLM+CJEwbiMJTAvb72dIWgF5sIrfXk7rhjxK8OggCT0ijTpV+ygfrh6aDeFE+Ng==";
    private static final String ECC_PRIVATEKEY = "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCC6bCHwHQYcRCOUlk1hQOM93QtVJ7GlAQMeCpotrKPS9Q==";

    @Test
    public void rsa() throws Exception {

        RSAPublicKey publicKey = EasyCrypto.parseKey(RSA_PUBLIC)
                .propertyTypeBase64()
                .selectRSAPublic()
                .propertyTypeX509()
                .get();

        publicKey = EasyCrypto.parseKey(PEMEncodeUtils.rsaPublicKeyToPEMEncoded(RSA_PUBLIC))
                .propertyTypePEM()
                .selectRSAPublic()
                .propertyTypeX509()
                .get();

        RSAPrivateKey privateKey = EasyCrypto.parseKey(RSA_PRIVATE)
                .propertyTypeBase64()
                .selectRSAPrivate()
                .propertyTypePKCS8()
                .get();

        privateKey = EasyCrypto.parseKey(PEMEncodeUtils.rsaPrivateKeyToPEMEncoded(RSA_PRIVATE))
                .propertyTypePEM()
                .selectRSAPrivate()
                .propertyTypePKCS8()
                .get();

        privateKey = EasyCrypto.parseKey(RSA_PRIVATE.getBytes())
                .propertyAsBase64()
                .selectRSAPrivate()
                .propertyTypePKCS8()
                .get();

        privateKey = EasyCrypto.parseKey(Base64Utils.decode(RSA_PRIVATE))
                .selectRSAPrivate()
                .propertyTypePKCS8()
                .get();

    }

    @Test
    public void ecc() throws Exception {

        ECPublicKey ecPublicKey = EasyCrypto.parseKey(RSA_PUBLIC)
                .propertyTypeBase64()
                .selectECCPublic()
                .get();

        ECPrivateKey ecPrivateKey = EasyCrypto.parseKey(RSA_PRIVATE)
                .propertyTypeBase64()
                .selectECCPrivate()
                .get();

    }

}
