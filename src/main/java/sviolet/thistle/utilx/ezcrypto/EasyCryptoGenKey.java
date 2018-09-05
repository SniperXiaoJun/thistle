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

public class EasyCryptoGenKey {

    /* *****************************************************************************************************************
     * select选择流程
     * *****************************************************************************************************************/

    public TezGenKy_Hand_Rsa selectRSA(){
        return new TezGenKy_Hand_Rsa();
    }

    public TezGenKy_Hand_Ecc selectECC(){
        return new TezGenKy_Hand_Ecc();
    }

    public TezGenKy_Hand_Aes selectAES(){
        return new TezGenKy_Hand_Aes();
    }

    public TezGenKy_Hand_Des selectDES(){
        return new TezGenKy_Hand_Des();
    }

    public TezGenKy_Hand_DesEde selectDESEDE(){
        return new TezGenKy_Hand_DesEde();
    }

    public TezGenKy_Hand_Sha selectSHA(byte[] seed) {
        return new TezGenKy_Hand_Sha(seed);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    EasyCryptoGenKey() {
    }

}
