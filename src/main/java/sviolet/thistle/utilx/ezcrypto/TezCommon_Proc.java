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

import sviolet.thistle.util.common.CloseableUtils;

public abstract class TezCommon_Proc<I, O> {

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    public O get() throws EzException {
        return doFinal();
    }

    public O get(EzExceptionHandler exceptionHandler){
        try {
            return doFinal();
        } catch (EzException e) {
            if (exceptionHandler != null) {
                exceptionHandler.onException(e.getStep(), e.getCause());
            }
        }
        return null;
    }

    /* *****************************************************************************************************************
     * inner logic
     * *****************************************************************************************************************/

    private TezCommon_Proc<?, ?> firstProc;
    private TezCommon_Proc<?, ?> nextProc;

    TezCommon_Proc(TezCommon_Proc<?, ?> previous) {
        if (previous == null) {
            firstProc = this;
        } else {
            firstProc = previous.firstProc;
            previous.nextProc = this;
        }
    }

    O doFinal() throws EzException {

        TezCommon_Proc<?, ?> currProc = firstProc;
        Object inputData = null;
        int step = 0;

        while (currProc != null) {
            Object outputData;
            try {
                outputData = currProc.process(inputData);
            } catch (Exception e) {
                throw new EzException("EasyCrypto process error, step " + step + ", processor " + currProc.getClass().getSimpleName(), e, step);
            } finally {
                //close input
                CloseableUtils.closeIfCloseable(inputData);
            }
            currProc = currProc.nextProc;
            inputData = outputData;
            step++;
        }

        try {
            return (O) inputData;
        } catch (Exception e) {
            throw new EzException("EasyCrypto process error, step " + step + " (result cast)", e, step);
        }
    }

    Object process(Object input) throws Exception{
        return onProcess((I) input);
    }

    abstract O onProcess(I input) throws Exception;

}
