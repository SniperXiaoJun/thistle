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

public class TezGenKey_Encode_Bytes2Encoded extends TezCommon_Proc<byte[], String> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private Type type = Type.BASE64;

    private enum Type {
        HEX,
        BASE64
    }

    public TezGenKey_Encode_Bytes2Encoded propertyTypeHex(){
        this.type = Type.HEX;
        return this;
    }

    public TezGenKey_Encode_Bytes2Encoded propertyTypeBase64(){
        this.type = Type.BASE64;
        return this;
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    @Override
    public String get() throws EzException {
        return super.get();
    }

    @Override
    public String get(EzExceptionHandler exceptionHandler) {
        return super.get(exceptionHandler);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezGenKey_Encode_Bytes2Encoded(TezCommon_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    String onProcess(byte[] input) throws Exception {
        if (input == null) {
            return null;
        }
        if (type == null) {
            throw new IllegalParamException("type is null");
        }
        switch (type) {
            case HEX:
                return ByteUtils.bytesToHex(input);
            case BASE64:
                return Base64Utils.encodeToString(input);
            default:
                throw new IllegalParamException("type is invalid");
        }
    }

}
