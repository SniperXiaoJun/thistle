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

import sviolet.thistle.entity.IllegalParamException;
import sviolet.thistle.util.conversion.Base64Utils;
import sviolet.thistle.util.conversion.ByteUtils;
import sviolet.thistle.util.crypto.base.BaseAsymKeyGenerator;

public class TezGenKey_Encode_KeyPair2Encoded extends TezCommon_Proc<EzKeyPair<?, ?>, EzKeyPairEncoded> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private KeyType publicKeyType = KeyType.X509;
    private KeyType privateKeyType = KeyType.PKCS8;
    private EncodeType encodeType = EncodeType.BASE64;

    private enum KeyType {
        X509,
        PKCS8
    }

    private enum EncodeType {
        HEX,
        BASE64
    }

    public TezGenKey_Encode_KeyPair2Encoded propertyPublicKeyX509(){
        this.publicKeyType = KeyType.X509;
        return this;
    }

    public TezGenKey_Encode_KeyPair2Encoded propertyPrivateKeyPKCS8(){
        this.privateKeyType = KeyType.PKCS8;
        return this;
    }

    public TezGenKey_Encode_KeyPair2Encoded propertyEncodeBase64(){
        this.encodeType = EncodeType.BASE64;
        return this;
    }

    public TezGenKey_Encode_KeyPair2Encoded propertyEncodeHex(){
        this.encodeType = EncodeType.HEX;
        return this;
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    @Override
    public EzKeyPairEncoded get() throws EzException {
        return super.get();
    }

    @Override
    public EzKeyPairEncoded get(EzExceptionHandler exceptionHandler) {
        return super.get(exceptionHandler);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezGenKey_Encode_KeyPair2Encoded(TezCommon_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    EzKeyPairEncoded onProcess(EzKeyPair<?, ?> input) throws Exception {
        if (input == null) {
            return null;
        }
        if (publicKeyType == null) {
            throw new IllegalParamException("publicKeyType is null");
        }
        if (privateKeyType == null) {
            throw new IllegalParamException("privateKeyType is null");
        }

        EzKeyPairEncoded keyPairEncoded = new EzKeyPairEncoded();

        byte[] encodedPublicKey;
        switch (publicKeyType) {
            case X509:
                encodedPublicKey = BaseAsymKeyGenerator.encodePublicKeyToX509(input.getPublicKey(), input.getKeyAlgorithm());
                break;
            default:
                throw new IllegalParamException("publicKeyType is invalid");
        }
        keyPairEncoded.setPublicKey(encode(encodedPublicKey));

        byte[] encodedPrivateKey;
        switch (privateKeyType) {
            case PKCS8:
                encodedPrivateKey = BaseAsymKeyGenerator.encodePrivateKeyToPKCS8(input.getPrivateKey(), input.getKeyAlgorithm());
                break;
            default:
                throw new IllegalParamException("privateKeyType is invalid");
        }
        keyPairEncoded.setPrivateKey(encode(encodedPrivateKey));

        return keyPairEncoded;
    }

    private String encode(byte[] data) throws IllegalParamException {
        switch (encodeType) {
            case HEX:
                return ByteUtils.bytesToHex(data);
            case BASE64:
                return Base64Utils.encodeToString(data);
            default:
                throw new IllegalParamException("encodeType is invalid");
        }
    }

}
