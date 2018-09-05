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

import java.io.InputStream;

public class TezParseKey_Read_InStream2Bytes extends TezCommon_Proc<InputStream, byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private int limit = 4 * 1024 * 1024;
    private int buffSize = 1024;

    public TezParseKey_Read_InStream2Bytes propertyLimit(int maxLength) {
        this.limit = maxLength;
        return this;
    }

    public TezParseKey_Read_InStream2Bytes propertyBuffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    /* *****************************************************************************************************************
     * select选择流程
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezParseKey_Read_InStream2Bytes(TezCommon_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    byte[] onProcess(InputStream input) throws Exception {
        if (input == null) {
            return null;
        }
        return TezCommon_Util_InStream.readAll(input, limit, buffSize);
    }

}
