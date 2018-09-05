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

public class TezcGenKeySha extends TezcGen<byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private int bits = 128;
    private byte[] seed;

    public TezcGenKeySha propertyBits64(){
        this.bits = 64;
        return this;
    }

    public TezcGenKeySha propertyBits128(){
        this.bits = 128;
        return this;
    }

    public TezcGenKeySha propertyBits192(){
        this.bits = 192;
        return this;
    }

    public TezcGenKeySha propertyBits256(){
        this.bits = 256;
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

    TezcGenKeySha(byte[] seed) {
        this.seed = seed;
    }

    @Override
    byte[] onGenerate() throws Exception {
        if (bits <= 0) {
            throw new IllegalParamException("bits <= 0");
        }
        if (seed == null) {
            throw new IllegalParamException("seed is null");
        }
        switch (bits) {
            case 64:
                return BaseKeyGenerator.generateShaKey64(seed);
            case 128:
                return BaseKeyGenerator.generateShaKey128(seed);
            case 192:
                return BaseKeyGenerator.generateShaKey192(seed);
            case 256:
                return BaseKeyGenerator.generateShaKey256(seed);
            default:
                throw new IllegalParamException("invalid bits : " + bits);
        }
    }
}
