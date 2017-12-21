// Copyright (C) 2017 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.android.tools.idea.naveditor.property.editors

import com.android.tools.idea.common.property.NlProperty
import com.android.tools.idea.naveditor.NavModelBuilderUtil.*
import com.android.tools.idea.naveditor.NavTestCase
import com.android.tools.idea.uibuilder.property.fixtures.EnumEditorFixture
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class AnimationEditorTest : NavTestCase() {
  fun testValues() {
    val model = model("nav.xml",
        rootComponent("root")
            .unboundedChildren(
                fragmentComponent("f1")
                    .unboundedChildren(actionComponent("a1").withDestinationAttribute("f2")),
                activityComponent("activity1"),
                fragmentComponent("f2")
                    .unboundedChildren(actionComponent("a2").withDestinationAttribute("activity1"))))
        .build()
    val property = mock(NlProperty::class.java)
    `when`(property.components).thenReturn(listOf(model.find("a1")))
    `when`(property.model).thenReturn(model)

    EnumEditorFixture
        .create(::AnimationEditor)
        .setProperty(property)
        .showPopup()
        .expectChoices("none", null,
            "fade_in", "@anim/fade_in",
            "fade_out", "@anim/fade_out",
            "test1", "@animator/test1")

    `when`(property.components).thenReturn(listOf(model.find("a2")))
    EnumEditorFixture
        .create(::AnimationEditor)
        .setProperty(property)
        .showPopup()
        .expectChoices("none", null,
            "fade_in", "@anim/fade_in",
            "fade_out", "@anim/fade_out")
  }
}