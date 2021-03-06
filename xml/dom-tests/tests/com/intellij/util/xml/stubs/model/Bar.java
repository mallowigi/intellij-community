/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.util.xml.stubs.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Stubbed;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author Dmitry Avdeev
 */
public interface Bar extends DomElement {

  @Stubbed
  GenericAttributeValue<String> getString();

  @Stubbed
  GenericAttributeValue<Integer> getInt();

  @Stubbed
  @Attribute("class")
  GenericAttributeValue<PsiClass> getClazz();

  GenericAttributeValue<Integer> getNotStubbed();

  @SubTagList("notStubbed")
  List<NotStubbed> getNotStubbeds();

  NotStubbed addNotStubbed();
}
