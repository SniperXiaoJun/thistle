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

import java.io.InputStream;

public class TezCrypt_Rd_Stm2B extends TezCom_Proc<InputStream, byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private int limit = 4 * 1024 * 1024;
    private int buffSize = 1024;

    public TezCrypt_Rd_Stm2B propertyLimit(int maxLength) {
        this.limit = maxLength;
        return this;
    }

    public TezCrypt_Rd_Stm2B optionBuffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    /* *****************************************************************************************************************
     * select选择流程
     * *****************************************************************************************************************/

    //TODO

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    public TezCrypt_Trs_B2B continueTranscoding(){
        return new TezCrypt_Trs_B2B(this);
    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezCrypt_Rd_Stm2B(TezCom_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    byte[] onProcess(InputStream input) throws Exception {
        if (input == null) {
            return null;
        }
        return TezCom_Util_Stm.readAll(input, limit, buffSize);
    }

}
