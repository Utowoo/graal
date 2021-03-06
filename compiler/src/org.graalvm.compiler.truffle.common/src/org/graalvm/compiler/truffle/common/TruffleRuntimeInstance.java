/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.graalvm.compiler.truffle.common;

import java.lang.reflect.Method;

/**
 * Initializes and stores the singleton {@code TruffleRuntime} instance. Separating this from
 * {@link TruffleCompilerRuntimeInstance} is necessary to support
 * {@link TruffleCompilerRuntime#getRuntimeIfAvailable()}.
 */
final class TruffleRuntimeInstance {
    /**
     * The singleton instance.
     */
    static final Object INSTANCE = init();

    /**
     * Accesses the Truffle runtime via reflection to avoid a dependency on Truffle that will expose
     * Truffle types to all classes depending on {@code org.graalvm.compiler.truffle.common}.
     */
    private static Object init() {
        try {
            Class<?> truffleClass = Class.forName("com.oracle.truffle.api.Truffle");
            Method getRuntime = truffleClass.getMethod("getRuntime");
            Object truffleRuntime = getRuntime.invoke(null);
            return truffleRuntime;
        } catch (Error e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
