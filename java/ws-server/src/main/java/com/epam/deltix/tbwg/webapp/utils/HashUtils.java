/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.tbwg.webapp.utils;

import com.epam.deltix.util.LangUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HashUtils {

    public static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    public static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    public static final String HMAC_SHA384_ALGORITHM = "HmacSHA384";

    private static final ThreadLocal<Mac> HMAC_SHA1 = newLocalMac(HMAC_SHA1_ALGORITHM);
    private static final ThreadLocal<Mac> HMAC_SHA256 = newLocalMac(HMAC_SHA256_ALGORITHM);
    private static final ThreadLocal<Mac> HMAC_SHA384 = newLocalMac(HMAC_SHA384_ALGORITHM);

    private static final Map<String, ThreadLocal<Mac>> macs = new HashMap<>();

    static {
        macs.put(HMAC_SHA1_ALGORITHM, HMAC_SHA1);
        macs.put(HMAC_SHA256_ALGORITHM, HMAC_SHA256);
        macs.put(HMAC_SHA384_ALGORITHM, HMAC_SHA384);
    }

//    public static String hashToString(String value, String key, String algorithm) {
//        return DatatypeConverter.printHexBinary(hash(value, key.getBytes(), algorithm));
//    }

    public static byte[] hash(final String value, final byte[] secret, final String algorithm) {
        final ThreadLocal<Mac> localMac = macs.get(algorithm);

        if (localMac == null) {
            throw new RuntimeException("Unknown algorithm: " + algorithm);
        }

        final Mac mac = localMac.get();
        final SecretKeySpec key = new SecretKeySpec(secret, algorithm);

        try {
            mac.reset();
            mac.init(key);

            return mac.doFinal(value.getBytes());
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("Key is not supported.", e);
        }
    }

    public static byte[] hashHmacSha384(final byte[] value, final byte[] secret) {
        final Mac mac = HMAC_SHA384.get();
        final SecretKeySpec key = new SecretKeySpec(secret, HMAC_SHA384_ALGORITHM);

        try {
            mac.reset();
            mac.init(key);

            return mac.doFinal(value);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("Key is not supported.", e);
        }
    }

    private static ThreadLocal<Mac> newLocalMac(final String algorithm) {
        final Supplier<? extends Mac> supplier = () -> {
            try {
                return Mac.getInstance(algorithm);
            } catch (final NoSuchAlgorithmException e) {
                throw LangUtil.rethrowUnchecked(e);
            }
        };

        return ThreadLocal.withInitial(supplier);
    }

}
