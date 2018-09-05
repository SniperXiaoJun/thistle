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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TezCom_Util_InStm {

    public static byte[] readAll(InputStream input, int limit, int buffSize) throws Exception {
        if (buffSize < 1024){
            throw new IllegalParamException("buffSize < 1024");
        }
        if (input == null) {
            throw new IllegalParamException("inputStream is null");
        }

        BufferedInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(buffSize);
        try {
            inputStream = new BufferedInputStream(input);
            byte[] buff = new byte[buffSize];
            int length;
            int totalLength = 0;
            while ((length = inputStream.read(buff)) >= 0) {
                totalLength += length;
                if (totalLength > limit) {
                    throw new FileNotFoundException("InputStream too large, limit:" + limit + ", now:" + totalLength);
                }
                outputStream.write(buff, 0, length);
            }
        } finally {
            CloseableUtils.closeQuiet(inputStream);
        }

        return outputStream.toByteArray();
    }

}
