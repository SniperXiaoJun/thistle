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
     * parseKey 数据/对象 -> 密钥
     * *****************************************************************************************************************/

    public static TezParseKey_Trans_Bytes2Bytes parseKey(byte[] data) {
        return new TezParseKey_Trans_Bytes2Bytes(new TezParseKey_Src_Bytes(data));
    }

    public static TezParseKey_Read_String2Bytes parseKey(String data) {
        return new TezParseKey_Read_String2Bytes(new TezParseKey_Src_String(data));
    }

    public static TezParseKey_Read_InStream2Bytes parseKey(InputStream inputStream) {
        return new TezParseKey_Read_InStream2Bytes(new TezParseKey_Src_InStream(inputStream));
    }

    public static TezParseKey_Read_File2Bytes parseKey(File file) {
        return new TezParseKey_Read_File2Bytes(new TezParseKey_Src_File(file));
    }

    public static TezParseKey_Src_ExpMod parseKey(EzExponentAndModulus exponentAndModule) {
        return new TezParseKey_Src_ExpMod(exponentAndModule);
    }

    public static TezParseKey_Src_RsaPriKey parseKey(RSAPrivateKey rsaPrivateKey) {
        return new TezParseKey_Src_RsaPriKey(rsaPrivateKey);
    }

    /* *****************************************************************************************************************
     * generateKey 生成密钥
     * *****************************************************************************************************************/

    public static EasyCryptoGenKey generateKey(){
        return generatorKey;
    }

    /* *****************************************************************************************************************
     * generateCertificate 生成证书
     * *****************************************************************************************************************/

    public static EasyCryptoGenCert generateCertificate(){
        return generateCertificate;
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    private static final EasyCryptoGenKey generatorKey = new EasyCryptoGenKey();
    private static final EasyCryptoGenCert generateCertificate = new EasyCryptoGenCert();

}
