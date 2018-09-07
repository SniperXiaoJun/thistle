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
import sviolet.thistle.util.crypto.base.BaseCipher;
import sviolet.thistle.util.judge.CheckUtils;

import java.io.InputStream;

public class TezCrypt_Hand_StmSign extends TezCrypt_Hand_BaseSign<InputStream> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    //See TezCrypt_Hand_BaseSign

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    //See TezCrypt_Hand_BaseSign

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    //See TezCrypt_Hand_BaseSign

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezCrypt_Hand_StmSign(TezCom_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    byte[] onProcess(InputStream input) throws Exception {
        if (input == null) {
            throw new RuntimeException("input is null");
        }
        if (CheckUtils.isEmptyOrBlank(type)) {
            throw new IllegalParamException("type is null or empty");
        }
        if (privateKey == null) {
            throw new IllegalParamException("privateKey is null");
        }
        return BaseCipher.signInputStream(input, privateKey, type);
    }

}
