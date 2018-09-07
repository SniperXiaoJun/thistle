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

public class TezCrypt_Src_B extends TezCom_Proc_Src<byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * select选择流程
     * *****************************************************************************************************************/

//    public TezCrypt_Hand_BEcypt selectEncrypt(){
//        return new TezCrypt_Hand_BEcypt(this);
//    }
//
//    public TezCrypt_Hand_BDcypt selectDecrypt(){
//        return new TezCrypt_Hand_BDcypt(this);
//    }

    public TezCrypt_Hand_BDgst selectDigest(){
        return new TezCrypt_Hand_BDgst(this);
    }

    public TezCrypt_Hand_BSign selectSign(){
        return new TezCrypt_Hand_BSign(this);
    }

    public TezCrypt_Hand_BVeri selectVerify(){
        return new TezCrypt_Hand_BVeri(this);
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    public TezCrypt_Trs_B2B continueTranscode(){
        return new TezCrypt_Trs_B2B(this);
    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezCrypt_Src_B(byte[] input) {
        super(input);
    }

}
