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

import sviolet.thistle.entity.IllegalParamException;
import sviolet.thistle.util.conversion.Base64Utils;
import sviolet.thistle.util.conversion.ByteUtils;

import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;

public abstract class TezCrypt_Hand_BaseVeri<I> extends TezCom_Proc<I, Boolean> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private SignType signType;
    private String signStr;
    private byte[] sign;
    PublicKey publicKey;
    String type = "SHA256withRSA";
    boolean nio = true;

    public enum SignType {
        BASE64,
        HEX,
        RAW
    }

    public TezCrypt_Hand_BaseVeri propertySign(byte[] sign) {
        this.sign = sign;
        return this;
    }

    public TezCrypt_Hand_BaseVeri propertySign(String sign, SignType signType) {
        this.signStr = sign;
        this.signType = signType;
        return this;
    }

    public TezCrypt_Hand_BaseVeri propertyTypeMD5withRSA(RSAPublicKey publicKey){
        this.type = "MD5withRSA";
        this.publicKey = publicKey;
        return this;
    }

    public TezCrypt_Hand_BaseVeri propertyTypeSHA1withRSA(RSAPublicKey publicKey){
        this.type = "SHA1withRSA";
        this.publicKey = publicKey;
        return this;
    }

    public TezCrypt_Hand_BaseVeri propertyTypeSHA256withRSA(RSAPublicKey publicKey){
        this.type = "SHA256withRSA6";
        this.publicKey = publicKey;
        return this;
    }

    public TezCrypt_Hand_BaseVeri propertyTypeSHA256withECDSA(ECPublicKey publicKey){
        this.type = "SHA256withECDSA";
        this.publicKey = publicKey;
        return this;
    }

    public TezCrypt_Hand_BaseVeri propertyType(String type, ECPublicKey publicKey){
        this.type = type;
        this.publicKey = publicKey;
        return this;
    }

    public TezCrypt_Hand_BaseVeri optionNioDisabled(){
        this.nio = false;
        return this;
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    @Override
    public Boolean get() throws EzException {
        return super.get();
    }

    @Override
    public Boolean get(EzExceptionHandler exceptionHandler) {
        return super.get(exceptionHandler);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezCrypt_Hand_BaseVeri(TezCom_Proc<?, ?> previous) {
        super(previous);
    }

    byte[] getSign() throws IllegalParamException {
        if (signType == null) {
            throw new IllegalParamException("signType is null");
        }

        byte[] sign;
        switch (signType) {
            case RAW:
                if (this.sign == null) {
                    throw new IllegalParamException("sign is null");
                }
                return this.sign;
            case BASE64:
                if (this.signStr == null) {
                    throw new IllegalParamException("sign(signStr) is null");
                }
                return  Base64Utils.decode(signStr);
            case HEX:
                if (this.signStr == null) {
                    throw new IllegalParamException("sign(signStr) is null");
                }
                return  ByteUtils.hexToBytes(signStr);
            default:
                throw new IllegalParamException("signType is invalid");
        }
    }

}
