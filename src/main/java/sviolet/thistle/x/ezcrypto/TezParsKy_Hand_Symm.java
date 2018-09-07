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

public class TezParsKy_Hand_Symm extends TezCom_Proc<byte[], byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

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

    TezParsKy_Hand_Symm(TezCom_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    byte[] onProcess(byte[] input) throws Exception {
        return input;
    }

}
