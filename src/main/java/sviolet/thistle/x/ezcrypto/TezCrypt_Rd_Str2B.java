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
import sviolet.thistle.util.crypto.PEMEncodeUtils;

public class TezCrypt_Rd_Str2B extends TezCom_Proc<String, byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private Type type = Type.RAW;
    private String charset = "UTF-8";

    public enum Type {
        BASE64,
        HEX,
        RAW
    }

    public TezCrypt_Rd_Str2B propertyTypeBase64() {
        this.type = Type.BASE64;
        return this;
    }

    public TezCrypt_Rd_Str2B propertyTypeHex() {
        this.type = Type.HEX;
        return this;
    }

    public TezCrypt_Rd_Str2B propertyTypeRaw() {
        this.type = Type.RAW;
        return this;
    }

    public TezCrypt_Rd_Str2B optionCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /* *****************************************************************************************************************
     * select选择流程
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    public TezParsKy_Trs_B2B continueTranscoding(){
        return new TezParsKy_Trs_B2B(this);
    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezCrypt_Rd_Str2B(TezCom_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    byte[] onProcess(String input) throws Exception {
        if (input == null) {
            return null;
        }
        if (type == null) {
            throw new IllegalParamException("type is null");
        }
        switch (type) {
            case BASE64:
                return Base64Utils.decode(input);
            case HEX:
                return ByteUtils.hexToBytes(input);
            case RAW:
                return input.getBytes(charset);
            default:
                throw new IllegalParamException("type is invalid");
        }
    }

}
