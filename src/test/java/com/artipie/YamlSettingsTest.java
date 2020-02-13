/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 artipie.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie;

import com.artipie.asto.fs.FileStorage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link YamlSettings}.
 *
 * @since 0.1
 */
class YamlSettingsTest {

    @Test
    public void shouldBuildStorageFromSettings() throws Exception {
        final YamlSettings settings = new YamlSettings(
            "meta:\n  storage:\n    type: fs\n    path: /artipie/storage\n"
        );
        MatcherAssert.assertThat(
            settings.storage(),
            Matchers.instanceOf(FileStorage.class)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "meta:\n",
        "meta:\n  storage:\n",
        "meta:\n  storage:\n    type: unknown\n",
        "meta:\n  storage:\n    type: fs\n"
    })
    public void shouldFailProvideStorageFromBadYaml(final String yaml) {
        final YamlSettings settings = new YamlSettings(yaml);
        Assertions.assertThrows(RuntimeException.class, settings::storage);
    }
}
