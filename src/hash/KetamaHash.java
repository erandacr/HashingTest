/**
 * Copyright (C) 2009-2011 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING IN THE SOFTWARE.
 */

package hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Known hashing algorithms for locating a server for a key. Note that all hash
 * algorithms return 64-bits of hash, but only the lower 32-bits are
 * significant. This allows a positive 32-bit number to be returned for all
 * cases.
 */
public class KetamaHash {

    private static MessageDigest md5Digest = null;

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

    /**
     * Get the md5 of the given key.
     */
    public static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = (MessageDigest) md5Digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }
        md5.update(k.getBytes(StandardCharsets.UTF_8));
        return md5.digest();
    }

    /**
     * Compute the hash for the given key.
     *
     * @return a positive integer hash
     */
    public static long hash(final String k) {
        long rv = 0;

        byte[] bKey = computeMd5(k);
        rv = ((long) (bKey[3] & 0xFF) << 24)
            | ((long) (bKey[2] & 0xFF) << 16)
            | ((long) (bKey[1] & 0xFF) << 8)
            | (bKey[0] & 0xFF);

        return rv & 0xffffffffL; /* Truncate to 32-bits */
    }
}

