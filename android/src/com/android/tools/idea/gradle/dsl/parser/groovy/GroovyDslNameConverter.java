/*
 * Copyright (C) 2019 The Android Open Source Project
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
 */
package com.android.tools.idea.gradle.dsl.parser.groovy;

import com.android.tools.idea.gradle.dsl.parser.GradleDslNameConverter;
import com.android.tools.idea.gradle.dsl.parser.android.BuildTypeDslElement;
import com.android.tools.idea.gradle.dsl.parser.android.ProductFlavorDslElement;
import com.android.tools.idea.gradle.dsl.parser.elements.GradleDslElement;
import com.android.tools.idea.gradle.dsl.parser.semantics.SemanticsDescription;
import com.google.common.collect.ImmutableMap;
import com.intellij.psi.PsiElement;
import java.util.Map;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

public class GroovyDslNameConverter implements GradleDslNameConverter {
  @NotNull
  @Override
  public String psiToName(@NotNull PsiElement element) {
    return element.getText();
  }

  @NotNull
  @Override
  public String convertReferenceText(@NotNull GradleDslElement context, @NotNull String referenceText) {
    return referenceText;
  }

  @NotNull
  @Override
  public String externalNameForParent(@NotNull String modelName, @NotNull GradleDslElement context) {
    ImmutableMap<Pair<String,Integer>, Pair<String, SemanticsDescription>> map = context.getExternalToModelMap(this);
    for (Map.Entry<Pair<String,Integer>, Pair<String,SemanticsDescription>> e : map.entrySet()) {
      if (e.getValue().getFirst().equals(modelName)) {
        return e.getKey().getFirst();
      }
    }
    return modelName;
  }

  @NotNull
  @Override
  public String modelNameForParent(@NotNull String externalName, @NotNull GradleDslElement context) {
    if (externalName.contains(".")) {
      return modelNameForParent(externalName.substring(0, externalName.lastIndexOf(".")), context.getParent()) +
             "." +
             modelNameForParent(externalName.substring(externalName.lastIndexOf(".") + 1), context);
    }
    ImmutableMap<Pair<String,Integer>, Pair<String,SemanticsDescription>> map = context.getExternalToModelMap(this);
    for (Map.Entry<Pair<String,Integer>, Pair<String,SemanticsDescription>> e : map.entrySet()) {
      if (e.getKey().getFirst().equals(externalName)) {
        return e.getValue().getFirst();
      }
    }
    return externalName;
  }
}
