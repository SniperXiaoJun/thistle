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

import sviolet.thistle.util.common.CloseableUtils;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TezCom_Proc<I, O> {

    /* *****************************************************************************************************************
     * get结束取值
     * *****************************************************************************************************************/

    O get() throws EzException {
        return doFinal();
    }

    O get(EzExceptionHandler exceptionHandler){
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

    private TezCom_Proc<?, ?> firstProc;
    private TezCom_Proc<?, ?> nextProc;
    private List<Closeable> closeables;
    private AtomicBoolean done = new AtomicBoolean(false);

    TezCom_Proc(TezCom_Proc<?, ?> previous) {
        if (previous == null) {
            firstProc = this;
            closeables = new ArrayList<>(1);
        } else {
            firstProc = previous.firstProc;
            previous.nextProc = this;
        }
    }

    void addCloseable(Object closeable){
        if (closeable instanceof Closeable) {
            firstProc.closeables.add((Closeable) closeable);
        }
    }

    void closeAll(){
        for (Closeable closeable : firstProc.closeables) {
            CloseableUtils.closeQuiet(closeable);
        }
        firstProc.closeables = null;
    }

    O doFinal() throws EzException {

        if (!done.compareAndSet(false, true)) {
            throw new EzException("EasyCrypto: can not process again", 0);
        }

        try {

            TezCom_Proc<?, ?> currProc = firstProc;
            Object inputData = null;
            int step = 0;

            while (currProc != null) {
                Object outputData;
                try {
                    outputData = currProc.process(inputData);
                } catch (Exception e) {
                    throw new EzException("EasyCrypto: process error, step " + step + ", processor " + currProc.getClass().getSimpleName(), e, step);
                }
                currProc = currProc.nextProc;
                inputData = outputData;
                step++;
            }

            try {
                return (O) inputData;
            } catch (Exception e) {
                throw new EzException("EasyCrypto: process error, step " + step + " (result cast)", e, step);
            }

        } finally {
            closeAll();
        }

    }

    Object process(Object input) throws Exception{
        return onProcess((I) input);
    }

    abstract O onProcess(I input) throws Exception;

}
