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
import sviolet.thistle.util.crypto.base.BaseAsymKeyGenerator;

import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class TezGenKy_Hand_Ecc extends TezCom_Gen<EzKeyPairEcc> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private static final String KEY_ALGORITHM = "EC";

    private String type = "secp256r1";

    public TezGenKy_Hand_Ecc propertyTypeSECP256R1(){
        this.type = "secp256r1";
        return this;
    }

    public TezGenKy_Hand_Ecc propertyType(String type){
        this.type = type;
        return this;
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    public TezGenKy_Encd_KyPr2Encd continueEncoding(){
        return new TezGenKy_Encd_KyPr2Encd(this);
    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    @Override
    public EzKeyPairEcc get() throws EzException {
        return super.get();
    }

    @Override
    public EzKeyPairEcc get(EzExceptionHandler exceptionHandler) {
        return super.get(exceptionHandler);
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezGenKy_Hand_Ecc() {
    }

    @Override
    EzKeyPairEcc onGenerate() throws Exception {
        if (type == null) {
            throw new IllegalParamException("type is null");
        }
        KeyPair keyPair = BaseAsymKeyGenerator.generateEcKeyPair(type, KEY_ALGORITHM);
        return new EzKeyPairEcc((ECPublicKey) keyPair.getPublic(), (ECPrivateKey) keyPair.getPrivate());
    }

}
