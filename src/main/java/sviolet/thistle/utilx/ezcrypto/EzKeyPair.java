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

import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class EzKeyPair <PublicType extends PublicKey, PrivateType extends PrivateKey> {

    private PublicType publicKey;
    private PrivateType privateKey;

    public EzKeyPair() {
    }

    public EzKeyPair(PublicType publicKey, PrivateType privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public PublicType getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicType publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateType getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateType privateKey) {
        this.privateKey = privateKey;
    }

    public abstract String getKeyAlgorithm();

}
