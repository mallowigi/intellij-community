// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.openapi.extensions;

import org.jetbrains.annotations.NotNull;

/**
 * Extensions should implement this interface when it is important to find out what particular plugin has provided this extension.
 */
public interface PluginAware {
  /**
   * Called by extensions framework when extension is loaded from plugin.xml descriptor.
   * @param pluginDescriptor descriptor of the plugin that provided this particular extension.
   */
  void setPluginDescriptor(@NotNull PluginDescriptor pluginDescriptor);
}
