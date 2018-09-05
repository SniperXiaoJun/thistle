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

    public TezGenKey_Handle_Rsa selectRSA(){
        return new TezGenKey_Handle_Rsa();
    }

    public TezGenKey_Handle_Ecc selectECC(){
        return new TezGenKey_Handle_Ecc();
    }

    public TezGenKey_Handle_Aes selectAES(){
        return new TezGenKey_Handle_Aes();
    }

    public TezGenKey_Handle_Des selectDES(){
        return new TezGenKey_Handle_Des();
    }

    public TezGenKey_Handle_DesEde selectDESEDE(){
        return new TezGenKey_Handle_DesEde();
    }

    public TezGenKey_Handle_Sha selectSHA(byte[] seed) {
        return new TezGenKey_Handle_Sha(seed);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    EasyCryptoGenKey() {
    }

}
