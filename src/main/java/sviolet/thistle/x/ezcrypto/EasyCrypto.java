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

package sviolet.thistle.x.ezcrypto;

import java.io.File;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * [非线程安全, 一个流程只能执行一次]
 *
 * @author S.Violet
 */
public class EasyCrypto {

    /* *****************************************************************************************************************
     * handle 数据加密/解密/摘要/签名/验签
     * *****************************************************************************************************************/

    public static void from(byte[] data) {
        //TODO
    }

    public static void from(String data) {
        //TODO
    }

    public static void from(InputStream inputStream) {
        //TODO
    }

    public static TezCrypt_Src_File from(File file) {
        return new TezCrypt_Src_File(file);
    }

    /* *****************************************************************************************************************
     * formatKey 密钥 -> 数据
     * *****************************************************************************************************************/

    public static void formatKey(byte[] key) {
        //TODO
    }

    public static void formatKey(RSAPublicKey rsaPublicKey) {
        //TODO
    }

    public static void formatKey(RSAPrivateKey rsaPrivateKey) {
        //TODO
    }

    public static void formatKey(ECPublicKey ecPublicKey) {
        //TODO
    }

    public static void formatKey(ECPrivateKey ecPrivateKey) {
        //TODO
    }

    /* *****************************************************************************************************************
     * formatCertificate 证书 -> 数据
     * *****************************************************************************************************************/

    public static void formatCertificate(Certificate certificate) {
        //TODO
    }

    /* *****************************************************************************************************************
     * parseKey 数据/对象 -> 密钥
     * *****************************************************************************************************************/

    public static TezParsKy_Trs_B2B parseKey(byte[] data) {
        return new TezParsKy_Trs_B2B(new TezParsKy_Src_B(data));
    }

    public static TezParsKy_Rd_Str2B parseKey(String data) {
        return new TezParsKy_Rd_Str2B(new TezParsKy_Src_Str(data));
    }

    public static TezParsKy_Rd_InStm2B parseKey(InputStream inputStream) {
        return new TezParsKy_Rd_InStm2B(new TezParsKy_Src_InStm(inputStream));
    }

    public static TezParsKy_Rd_File2B parseKey(File file) {
        return new TezParsKy_Rd_File2B(new TezParsKy_Src_File(file));
    }

    public static TezParsKy_Src_ExpMod parseKey(EzExponentAndModulus exponentAndModule) {
        //TODO
        return new TezParsKy_Src_ExpMod(exponentAndModule);
    }

    public static TezParsKy_Src_RsaPriKy parseKey(RSAPrivateKey rsaPrivateKey) {
        //TODO
        return new TezParsKy_Src_RsaPriKy(rsaPrivateKey);
    }

    /* *****************************************************************************************************************
     * parseCertificate 数据/对象 -> 证书
     * *****************************************************************************************************************/

    public static void parseCertificate(byte[] data) {
        //TODO
    }

    public static void parseCertificate(String data) {
        //TODO
    }

    public static void parseCertificate(InputStream inputStream) {
        //TODO
    }

    public static void parseCertificate(File file) {
        //TODO
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
        //TODO
        //每次创建对象, 防止在没有依赖BouncyCastle时出错
        return new EasyCryptoGenCert();
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    private static final EasyCryptoGenKey generatorKey = new EasyCryptoGenKey();

}
