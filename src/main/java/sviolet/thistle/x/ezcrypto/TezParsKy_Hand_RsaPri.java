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
import sviolet.thistle.util.crypto.base.BaseAsymKeyGenerator;

import java.security.interfaces.RSAPrivateKey;

public class TezParsKy_Hand_RsaPri extends TezCom_Proc<byte[], RSAPrivateKey> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private static final String KEY_ALGORITHM = "RSA";

    private Type type = Type.PKCS8;

    private enum Type {
        PKCS8
    }

    public TezParsKy_Hand_RsaPri propertyTypePKCS8(){
        this.type = Type.PKCS8;
        return this;
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    @Override
    public RSAPrivateKey get() throws EzException {
        return super.get();
    }

    @Override
    public RSAPrivateKey get(EzExceptionHandler exceptionHandler) {
        return super.get(exceptionHandler);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezParsKy_Hand_RsaPri(TezCom_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    RSAPrivateKey onProcess(byte[] input) throws Exception {
        if (input == null) {
            return null;
        }
        if (type == null) {
            throw new IllegalParamException("type is null");
        }
        switch (type) {
            case PKCS8:
                return (RSAPrivateKey) BaseAsymKeyGenerator.parsePrivateKeyByPKCS8(input, KEY_ALGORITHM);
            default:
                throw new IllegalParamException("type is invalid");
        }
    }

}
