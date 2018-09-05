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

package sviolet.thistle.utilx.easycrypto;

import org.junit.Test;
import sviolet.thistle.utilx.ezcrypto.EasyCrypto;
import sviolet.thistle.utilx.ezcrypto.EzKeyPairEncoded;

import java.security.SecureRandom;

public class EasyCryptoGenKeyTest {

    @Test
    public void aes() throws Exception {

        //生成256位AES密钥
        String key = EasyCrypto.generateKey()
                .selectAES()
                .propertyBits256()
                .continueEncode()
                .propertyTypeHex()
                .get();

        System.out.println(key);

        //生成128位AES密钥, 指定种子, 不安全!!!
        key = EasyCrypto.generateKey()
                .selectAES()
                .propertyBits128()
                .optionSeed("123456".getBytes())
                .continueEncode()
                .propertyTypeBase64()
                .get();

        System.out.println(key);

        //生成128位AES密钥, 指定SecureRandom, 不安全!!!
        key = EasyCrypto.generateKey()
                .selectAES()
                .propertyBits128()
                .optionSecureRandom(new SecureRandom())
                .continueEncode()
                .propertyTypeBase64()
                .get();

        System.out.println(key);

    }

    @Test
    public void des() throws Exception {

        String key = EasyCrypto.generateKey()
                .selectDES()
                .propertyBits64()
                .continueEncode()
                .propertyTypeHex()
                .get();

        System.out.println(key);

    }

    @Test
    public void desede() throws Exception {

        String key = EasyCrypto.generateKey()
                .selectDESEDE()
                .propertyBits128()
                .continueEncode()
                .propertyTypeHex()
                .get();

        System.out.println(key);

        key = EasyCrypto.generateKey()
                .selectDESEDE()
                .propertyBits128()
                .continueEncode()
                .propertyTypeBase64()
                .get();

        System.out.println(key);

    }

    @Test
    public void sha() throws Exception {

        String key = EasyCrypto.generateKey()
                .selectSHA("123456".getBytes())
                .propertyBits64()
                .continueEncode()
                .propertyTypeBase64()
                .get();

        System.out.println(key);

        key = EasyCrypto.generateKey()
                .selectSHA("123456".getBytes())
                .propertyBits128()
                .continueEncode()
                .propertyTypeHex()
                .get();

        System.out.println(key);

        key = EasyCrypto.generateKey()
                .selectSHA("123456".getBytes())
                .propertyBits192()
                .continueEncode()
                .propertyTypeHex()
                .get();

        System.out.println(key);

        key = EasyCrypto.generateKey()
                .selectSHA("123456".getBytes())
                .propertyBits256()
                .continueEncode()
                .propertyTypeHex()
                .get();

        System.out.println(key);

    }

    @Test
    public void rsa() throws Exception {

        EzKeyPairEncoded keyPairEncoded = EasyCrypto.generateKey()
                .selectRSA()
                .propertyBits1024()
                .continueEncode()
                .propertyPublicKeyX509()
                .propertyPrivateKeyPKCS8()
                .propertyEncodeBase64()
                .get();

        System.out.println(keyPairEncoded.getPublicKey());
        System.out.println(keyPairEncoded.getPrivateKey());

        keyPairEncoded = EasyCrypto.generateKey()
                .selectECDSA()
                .propertyTypeSECP256R1()
                .continueEncode()
                .propertyPublicKeyX509()
                .propertyPrivateKeyPKCS8()
                .propertyEncodeBase64()
                .get();

        System.out.println(keyPairEncoded.getPublicKey());
        System.out.println(keyPairEncoded.getPrivateKey());

    }

}
