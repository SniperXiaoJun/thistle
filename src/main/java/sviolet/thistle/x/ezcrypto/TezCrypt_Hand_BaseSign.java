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

import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;

public abstract class TezCrypt_Hand_BaseSign<I> extends TezCom_Proc<I, byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    PrivateKey privateKey;
    String type = "SHA256withRSA";
    boolean nio = true;

    public TezCrypt_Hand_BaseSign propertyTypeMD5withRSA(RSAPrivateKey privateKey){
        this.type = "MD5withRSA";
        this.privateKey = privateKey;
        return this;
    }

    public TezCrypt_Hand_BaseSign propertyTypeSHA1withRSA(RSAPrivateKey privateKey){
        this.type = "SHA1withRSA";
        this.privateKey = privateKey;
        return this;
    }

    public TezCrypt_Hand_BaseSign propertyTypeSHA256withRSA(RSAPrivateKey privateKey){
        this.type = "SHA256withRSA6";
        this.privateKey = privateKey;
        return this;
    }

    public TezCrypt_Hand_BaseSign propertyTypeSHA256withECDSA(ECPrivateKey privateKey){
        this.type = "SHA256withECDSA";
        this.privateKey = privateKey;
        return this;
    }

    public TezCrypt_Hand_BaseSign propertyType(String type, ECPrivateKey privateKey){
        this.type = type;
        this.privateKey = privateKey;
        return this;
    }

    public TezCrypt_Hand_BaseSign optionNioDisabled(){
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
    public byte[] get() throws EzException {
        return super.get();
    }

    @Override
    public byte[] get(EzExceptionHandler exceptionHandler) {
        return super.get(exceptionHandler);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezCrypt_Hand_BaseSign(TezCom_Proc<?, ?> previous) {
        super(previous);
    }

}
