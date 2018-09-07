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

import java.io.File;

public class TezCrypt_Src_File extends TezCom_Proc_Src<File> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * select选择流程
     * *****************************************************************************************************************/

    public TezCrypt_Hand_FileEcypt selectEncrypt(){
        return new TezCrypt_Hand_FileEcypt(this);
    }

    public TezCrypt_Hand_FileDcypt selectDecrypt(){
        return new TezCrypt_Hand_FileDcypt(this);
    }

    public TezCrypt_Hand_FileDgst selectDigest(){
        return new TezCrypt_Hand_FileDgst(this);
    }

    public TezCrypt_Hand_FileSign selectSign(){
        return new TezCrypt_Hand_FileSign(this);
    }

    public TezCrypt_Hand_FileVeri selectVerify(){
        return new TezCrypt_Hand_FileVeri(this);
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    public TezCrypt_Rd_File2B continueTranscode(){
        return new TezCrypt_Rd_File2B(this);
    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezCrypt_Src_File(File input) {
        super(input);
    }

}
