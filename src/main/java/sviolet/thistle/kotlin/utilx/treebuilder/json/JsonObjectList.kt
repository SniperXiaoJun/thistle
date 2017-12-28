/*
 * Copyright (C) 2015-2017 S.Violet
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

package sviolet.thistle.kotlin.utilx.treebuilder.json

import com.google.gson.JsonObject

class JsonObjectList
internal constructor(
        val bean: JsonObject
){

    private var key: String? = null
    private var hasIterable = false
    private var iterable: Any? = null

    /**
     * key, required
     */
    infix fun k(key: String?): JsonObjectList {
        this.key = key
        return this
    }

    /**
     * iterable, optional.
     */
    infix fun i(iterable: Iterable<*>): JsonObjectList {
        this.iterable = iterable
        hasIterable = true
        return this
    }

    /**
     * iterable, optional
     */
    infix fun i(array: Array<*>): JsonObjectList {
        this.iterable = array
        hasIterable = true
        return this
    }

    /**
     * block(to build JsonArray)
     */
    infix fun v(block: JsonArrayBuilder.(Any?) -> Unit) {
        if (key == null){
            throw IllegalArgumentException("You should invoke method \"k\" to set key before set value")
        }
        val array = JsonArrayBuilder()
        if (hasIterable) {
            val i = iterable
            if (i is Iterable<*>) {
                i.forEach {
                    array.block(it)
                }
            } else if (i is Array<*>) {
                i.forEach {
                    array.block(it)
                }
            } else {
                throw IllegalArgumentException("The \"iterable\" argument cannot be iterate")
            }
        } else {
            array.block(null)
        }
        bean.add(key, array.bean)
    }
}