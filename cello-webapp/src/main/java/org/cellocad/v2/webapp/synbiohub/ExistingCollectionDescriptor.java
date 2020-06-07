/*
 * Copyright (C) 2020 Boston University (BU)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.cellocad.v2.webapp.synbiohub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

/**
 * A collection to be updated on a SynBioHub instance.
 *
 * @author Timothy Jones
 * @date 2020-06-06
 */
public class ExistingCollectionDescriptor extends SubmittableCollectionDescriptor {

  private URI uri;
  private OverwriteMerge overwrite;

  /** Initializes a newly created {@link ExistingCollectionDescriptor}. */
  @JsonCreator
  public ExistingCollectionDescriptor(
      @JsonProperty("uri") final URI uri,
      @JsonProperty("overwrite") final OverwriteMerge overwrite) {
    this.uri = uri;
    this.overwrite = overwrite;
  }

  /**
   * Getter for {@code uri}.
   *
   * @return The value of {@code uri}.
   */
  public URI getUri() {
    return uri;
  }

  /**
   * Getter for {@code overwrite}.
   *
   * @return The value of {@code overwrite}.
   */
  public OverwriteMerge getOverwrite() {
    return overwrite;
  }
}
