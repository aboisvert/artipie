/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/artipie/blob/master/LICENSE.txt
 */
package com.artipie.npm.http;

import com.artipie.asto.Content;
import com.artipie.asto.Key;
import com.artipie.asto.Storage;
import com.artipie.asto.memory.InMemoryStorage;
import com.artipie.http.hm.RsHasBody;
import com.artipie.http.hm.RsHasStatus;
import com.artipie.http.hm.SliceHasResponse;
import com.artipie.http.rq.RequestLine;
import com.artipie.http.rq.RqMethod;
import com.artipie.http.rs.RsStatus;
import java.nio.charset.StandardCharsets;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link GetDistTagsSlice}.
 * @since 0.8
 */
class GetDistTagsSliceTest {

    /**
     * Test storage.
     */
    private Storage storage;

    @BeforeEach
    void init() {
        this.storage = new InMemoryStorage();
        this.storage.save(
            new Key.From("@hello/simple-npm-project", "meta.json"),
            new Content.From(
                String.join(
                    "\n",
                    "{",
                    "\"dist-tags\": {",
                    "    \"latest\": \"1.0.3\",",
                    "    \"second\": \"1.0.2\",",
                    "    \"first\": \"1.0.1\"",
                    "  }",
                    "}"
                ).getBytes(StandardCharsets.UTF_8)
            )
        ).join();
    }

    @Test
    void readsDistTagsFromMeta() {
        MatcherAssert.assertThat(
            new GetDistTagsSlice(this.storage),
            new SliceHasResponse(
                new RsHasBody(
                    "{\"latest\":\"1.0.3\",\"second\":\"1.0.2\",\"first\":\"1.0.1\"}",
                    StandardCharsets.UTF_8
                ),
                new RequestLine(RqMethod.GET, "/-/package/@hello%2fsimple-npm-project/dist-tags")
            )
        );
    }

    @Test
    void returnsNotFoundIfMetaIsNotFound() {
        MatcherAssert.assertThat(
            new GetDistTagsSlice(this.storage),
            new SliceHasResponse(
                new RsHasStatus(RsStatus.NOT_FOUND),
                new RequestLine(RqMethod.GET, "/-/package/@hello%2fanother-npm-project/dist-tags")
            )
        );
    }

}
