/**
 * Tencent is pleased to support the open source community by making Tars available.
 * <p>
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 * <p>
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * https://opensource.org/licenses/BSD-3-Clause
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * JCE 插件导航图标
 */
public final class JceNavIcons {
    private JceNavIcons() {}

    private static Icon load(String path) {
        return IconLoader.getIcon(path, JceNavIcons.class);
    }

    /** Proxy - 代理类 (橙色 P) */
    public static final Icon PROXY = load("/icons/nav/proxy.svg");

    /** Proxy Sync - 代理同步方法 (橙色 S) */
    public static final Icon PROXY_SYNC = load("/icons/nav/proxy_sync.svg");

    /** Proxy Async - 代理异步方法 (紫色 A) */
    public static final Icon PROXY_ASYNC = load("/icons/nav/proxy_async.svg");

    /** Proxy Promise - 代理 Promise 方法 (蓝色 P) */
    public static final Icon PROXY_PROMISE = load("/icons/nav/proxy_promise.svg");

    /** Callback - 回调方法 (绿色 C) */
    public static final Icon CALLBACK = load("/icons/nav/callback.svg");
}
