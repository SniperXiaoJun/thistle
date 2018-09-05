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
import sviolet.thistle.util.crypto.base.BaseKeyGenerator;

import java.security.SecureRandom;

public class TezcGenKeyDes extends TezcGen<byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private static final String KEY_ALGORITHM = "DES";

    private int bits = 56;
    private byte[] seed;
    private SecureRandom secureRandom;

    public TezcGenKeyDes propertyBits64(){
        this.bits = 56;
        return this;
    }

    public TezcGenKeyDes propertyBits(int bits){
        this.bits = bits;
        return this;
    }

    public TezcGenKeyDes optionSeed(byte[] seed) {
        this.seed = seed;
        return this;
    }

    public TezcGenKeyDes optionSecureRandom(SecureRandom secureRandom){
        this.secureRandom = secureRandom;
        return this;
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    public TezcEncodeBytes continueEncode(){
        return new TezcEncodeBytes(this);
    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    @Override
    public byte[] get() throws EasyCryptoException {
        return super.get();
    }

    @Override
    public byte[] get(EzExceptionHandler exceptionHandler) {
        return super.get(exceptionHandler);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezcGenKeyDes() {
    }

    @Override
    byte[] onGenerate() throws Exception {
        if (bits <= 0) {
            throw new IllegalParamException("bits <= 0");
        }
        if (secureRandom != null) {
            return BaseKeyGenerator.generateKey(secureRandom, bits, KEY_ALGORITHM);
        } else if (seed != null) {
            return BaseKeyGenerator.generateKey(seed, bits, KEY_ALGORITHM);
        } else {
            return BaseKeyGenerator.generateKey((SecureRandom) null, bits, KEY_ALGORITHM);
        }
    }

}
