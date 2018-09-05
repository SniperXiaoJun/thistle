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

public class TezParseKey_Trans_Bytes2Bytes extends TezCommon_Proc<byte[], byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private Type type = Type.RAW;
    private String charset = "UTF-8";
    private EzFilterBytesToBytes bytesToBytesFilter;

    public enum Type {
        DECODE_AS_BASE64,
        DECODE_AS_HEX,
        DECODE_AS_PEM,
        TRANSCODE_BY_FILTER,
        RAW
    }

    public TezParseKey_Trans_Bytes2Bytes propertyAsBase64() {
        this.type = Type.DECODE_AS_BASE64;
        return this;
    }

    public TezParseKey_Trans_Bytes2Bytes propertyAsHex() {
        this.type = Type.DECODE_AS_HEX;
        return this;
    }

    public TezParseKey_Trans_Bytes2Bytes propertyAsPem() {
        this.type = Type.DECODE_AS_PEM;
        return this;
    }

    public TezParseKey_Trans_Bytes2Bytes propertyAsRaw() {
        this.type = Type.RAW;
        return this;
    }

    public TezParseKey_Trans_Bytes2Bytes propertyByFilter(EzFilterBytesToBytes bytesToBytesFilter){
        this.type = Type.TRANSCODE_BY_FILTER;
        this.bytesToBytesFilter = bytesToBytesFilter;
        return this;
    }

    public TezParseKey_Trans_Bytes2Bytes optionCharset(String charset) {
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

    TezParseKey_Trans_Bytes2Bytes(TezCommon_Proc<?, ?> previous) {
        super(previous);
    }

    @Override
    byte[] onProcess(byte[] input) throws Exception {
        if (input == null) {
            return null;
        }
        if (type == null) {
            throw new IllegalParamException("type is null");
        }
        switch (type) {
            case DECODE_AS_BASE64:
                return Base64Utils.decode(new String(input, charset));
            case DECODE_AS_HEX:
                return ByteUtils.hexToBytes(new String(input, charset));
            case DECODE_AS_PEM:
                return PEMEncodeUtils.pemEncodedToX509EncodedBytes(new String(input, charset));
            case RAW:
                return input;
            case TRANSCODE_BY_FILTER:
                if (bytesToBytesFilter == null) {
                    throw new IllegalParamException("bytesToBytesFilter is null");
                }
                return bytesToBytesFilter.filter(input);
            default:
                throw new IllegalParamException("type is invalid");
        }
    }

}
