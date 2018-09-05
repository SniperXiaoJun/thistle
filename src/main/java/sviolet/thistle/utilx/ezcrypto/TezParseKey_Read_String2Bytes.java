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
import sviolet.thistle.util.conversion.Base64Utils;
import sviolet.thistle.util.conversion.ByteUtils;
import sviolet.thistle.util.crypto.PEMEncodeUtils;

public class TezParseKey_Read_String2Bytes extends TezCommon_Proc<String, byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private Type type = Type.X509_BASE64;
    private String charset = "UTF-8";

    public enum Type {
        X509_BASE64,
        X509_HEX,
        X509_PEM,
        RAW
    }

    public TezParseKey_Read_String2Bytes propertyTypeBase64() {
        this.type = Type.X509_BASE64;
        return this;
    }

    public TezParseKey_Read_String2Bytes propertyTypeHex() {
        this.type = Type.X509_HEX;
        return this;
    }

    public TezParseKey_Read_String2Bytes propertyTypePEM() {
        this.type = Type.X509_PEM;
        return this;
    }

    public TezParseKey_Read_String2Bytes propertyTypeRaw() {
        this.type = Type.RAW;
        return this;
    }

    public TezParseKey_Read_String2Bytes optionCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /* *****************************************************************************************************************
     * select选择流程
     * *****************************************************************************************************************/

    public TezParseKey_Handle_Symmetry selectAES(){
        return new TezParseKey_Handle_Symmetry(this);
    }

    public TezParseKey_Handle_Symmetry selectDES(){
        return new TezParseKey_Handle_Symmetry(this);
    }

    public TezParseKey_Handle_Symmetry selectDESEDE(){
        return new TezParseKey_Handle_Symmetry(this);
    }

    public TezParseKey_Handle_RsaPri selectRSAPrivate(){
        return new TezParseKey_Handle_RsaPri(this);
    }

    public TezParseKey_Handle_RsaPub selectRSAPublic(){
        return new TezParseKey_Handle_RsaPub(this);
    }

    public TezParseKey_Handle_EccPri selectECCPrivate(){
        return new TezParseKey_Handle_EccPri(this);
    }

    public TezParseKey_Handle_EccPub selectECCPublic(){
        return new TezParseKey_Handle_EccPub(this);
    }

    /* *****************************************************************************************************************
     * continue继续流程
     * *****************************************************************************************************************/

    public TezParseKey_Trans_Bytes2Bytes continueTranscoding(){
        return new TezParseKey_Trans_Bytes2Bytes(this);
    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezParseKey_Read_String2Bytes(TezCommon_Proc<?, ?> previous) {
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
            case X509_BASE64:
                return Base64Utils.decode(input);
            case X509_HEX:
                return ByteUtils.hexToBytes(input);
            case X509_PEM:
                return PEMEncodeUtils.pemEncodedToX509EncodedBytes(input);
            case RAW:
                return input.getBytes(charset);
            default:
                throw new IllegalParamException("type is invalid");
        }
    }

}
