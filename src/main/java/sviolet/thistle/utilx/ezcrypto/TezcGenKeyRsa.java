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
import sviolet.thistle.util.crypto.base.BaseAsymKeyGenerator;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class TezcGenKeyRsa extends TezcGen<EzKeyPairRsa> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private static final String KEY_ALGORITHM = "RSA";

    private int bits = 2048;

    public TezcGenKeyRsa propertyBits1024(){
        this.bits = 1024;
        return this;
    }

    public TezcGenKeyRsa propertyBits2048(){
        this.bits = 2048;
        return this;
    }

    public TezcGenKeyRsa propertyBits(int bits){
        this.bits = bits;
        return this;
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    public TezcEncodeKeyPair continueEncode(){
        return new TezcEncodeKeyPair(this);
    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    @Override
    public EzKeyPairRsa get() throws EasyCryptoException {
        return super.get();
    }

    @Override
    public EzKeyPairRsa get(EzExceptionHandler exceptionHandler) {
        return super.get(exceptionHandler);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezcGenKeyRsa() {
    }

    @Override
    EzKeyPairRsa onGenerate() throws Exception {
        if (bits <= 0) {
            throw new IllegalParamException("bits <= 0");
        }
        KeyPair keyPair = BaseAsymKeyGenerator.generateRsaKeyPair(bits, KEY_ALGORITHM);
        return new EzKeyPairRsa((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
    }

}
