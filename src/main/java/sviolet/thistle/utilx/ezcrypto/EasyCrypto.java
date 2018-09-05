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

package sviolet.thistle.utilx.ezcrypto;

import java.io.File;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class EasyCrypto {

    /* *****************************************************************************************************************
     * handle处理数据
     * *****************************************************************************************************************/

    public static TezcSrcBytes handle(byte[] data) {
        return new TezcSrcBytes(data);
    }

    public static TezcSrcString handle(String data) {
        return new TezcSrcString(data);
    }

    public static TezcSrcExpMod handle(EzExponentAndModulus exponentAndModule) {
        return new TezcSrcExpMod(exponentAndModule);
    }

    public static TezcSrcRsaPriKey handle(RSAPrivateKey rsaPrivateKey) {
        return new TezcSrcRsaPriKey(rsaPrivateKey);
    }

    public static TezcSrcRsaPubKey handle(RSAPublicKey rsaPublicKey) {
        return new TezcSrcRsaPubKey(rsaPublicKey);
    }

    /* *****************************************************************************************************************
     * from处理数据源
     * *****************************************************************************************************************/

    public static TezcFromInStm from(InputStream inputStream) {
        return new TezcFromInStm(inputStream);
    }

    public static TezcFromFile from(File file) {
        return new TezcFromFile(file);
    }

    /* *****************************************************************************************************************
     * generate生成
     * *****************************************************************************************************************/

    public static EasyCryptoGenerateKey generateKey(){
        return generatorKey;
    }

    public static EasyCryptoGenerateCertificate generateCertificate(){
        return generateCertificate;
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    private static final EasyCryptoGenerateKey generatorKey = new EasyCryptoGenerateKey();
    private static final EasyCryptoGenerateCertificate generateCertificate = new EasyCryptoGenerateCertificate();

}
