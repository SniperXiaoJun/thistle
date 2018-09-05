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
import sviolet.thistle.util.common.CloseableUtils;
import sviolet.thistle.util.conversion.Base64Utils;
import sviolet.thistle.util.conversion.ByteUtils;

import java.io.*;

public class TezcFromFile2Bytes extends TezcProc<File, byte[]> {

    /* *****************************************************************************************************************
     * property必要参数 / option可选参数
     * *****************************************************************************************************************/

    private int limit = 1024 * 1024;
    private int buffSize = 1024;

    public TezcFromFile2Bytes propertyLimit(int maxLength) {
        this.limit = maxLength;
        return this;
    }

    public TezcFromFile2Bytes propertyBuffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    /* *****************************************************************************************************************
     * select选择流程
     * *****************************************************************************************************************/

    public void selectEncrypt(){
        //TezcBytesEnc
    }

    public void selectDecrypt(){
        //TezcBytesDec
    }

    public void selectSign(){
        //TezcBytesSign
    }

    public void selectVerify(){
        //TezcBytesVeri
    }

    public void selectDigest(){
        //TezcBytesDige
    }

    public void selectParseKey(){

    }

    public void selectParseCertificate(){

    }

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    TezcFromFile2Bytes(TezcProc<?, ?> previous) {
        super(previous);
    }

    @Override
    byte[] onProcess(File input) throws Exception {
        if (buffSize < 1024){
            throw new IllegalParamException("buffSize < 1024");
        }
        if (input == null) {
            throw new IllegalParamException("file is null");
        }
        if (!input.exists()) {
            throw new FileNotFoundException("File not found, dir:" + input.getAbsolutePath());
        }
        if (!input.isFile()) {
            throw new FileNotFoundException("Not a file, dir:" + input.getAbsolutePath());
        }
        if (input.length() > limit) {
            throw new FileNotFoundException("File too large, limit:" + limit + ", actualL" + input.length() + ", dir:" + input.getAbsolutePath());
        }

        BufferedInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) input.length());
        try {
            inputStream = new BufferedInputStream(new FileInputStream(input));
            byte[] buff = new byte[buffSize];
            int length;
            while ((length = inputStream.read(buff)) >= 0) {
                outputStream.write(buff, 0, length);
            }
        } finally {
            CloseableUtils.closeQuiet(inputStream);
        }

        return outputStream.toByteArray();
    }

}
