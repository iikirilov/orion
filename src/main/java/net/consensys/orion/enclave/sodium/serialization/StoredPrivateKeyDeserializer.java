/*
 * Copyright 2018 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.consensys.orion.enclave.sodium.serialization;

import net.consensys.orion.enclave.sodium.StoredPrivateKey;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public final class StoredPrivateKeyDeserializer extends StdDeserializer<StoredPrivateKey> {

  public StoredPrivateKeyDeserializer() {
    this(null);
  }

  public StoredPrivateKeyDeserializer(final Class<?> vc) {
    super(vc);
  }

  @Override
  public StoredPrivateKey deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
    final JsonNode rootNode = p.getCodec().readTree(p);
    final JsonNode typeNode = rootNode.get("type");
    if (typeNode == null) {
      throw new IOException("Unknown stored key format (missing 'type')");
    }
    final String type = typeNode.textValue();
    final JsonNode dataNode = rootNode.get("data");
    if (dataNode == null) {
      throw new IOException("Invalid stored key format (missing 'data')");
    }
    final JsonNode bytesNode = dataNode.get("bytes");
    if (bytesNode == null) {
      throw new IOException("Invalid stored key format (missing 'data.bytes')");
    }
    final String encoded = bytesNode.textValue();
    return new StoredPrivateKey(encoded, type);
  }
}
