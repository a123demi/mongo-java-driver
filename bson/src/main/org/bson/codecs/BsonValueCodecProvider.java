/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bson.codecs;

import org.bson.BsonTimestamp;
import org.bson.BsonType;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.BsonArray;
import org.bson.types.BsonBinary;
import org.bson.types.BsonBoolean;
import org.bson.types.BsonDateTime;
import org.bson.types.BsonDbPointer;
import org.bson.types.BsonDocument;
import org.bson.types.BsonDocumentWrapper;
import org.bson.types.BsonDouble;
import org.bson.types.BsonInt32;
import org.bson.types.BsonInt64;
import org.bson.types.BsonJavaScript;
import org.bson.types.BsonJavaScriptWithScope;
import org.bson.types.BsonMaxKey;
import org.bson.types.BsonMinKey;
import org.bson.types.BsonNull;
import org.bson.types.BsonObjectId;
import org.bson.types.BsonRegularExpression;
import org.bson.types.BsonString;
import org.bson.types.BsonSymbol;
import org.bson.types.BsonUndefined;
import org.bson.types.BsonValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code CodecProvider} for all subclass of BsonValue.
 *
 * @since 3.0
 */
public class BsonValueCodecProvider implements CodecProvider {
    private static final Map<BsonType, Class<? extends BsonValue>> DEFAULT_BSON_TYPE_CLASS_MAP;

    private final Map<Class<?>, Codec<?>> codecs = new HashMap<Class<?>, Codec<?>>();

    /**
     * Construct a new instance with the default codec for each BSON type.
     */
    public BsonValueCodecProvider() {
        addCodecs();
    }

    public static Class<? extends BsonValue> getClassForBsonType(final BsonType bsonType) {
        return DEFAULT_BSON_TYPE_CLASS_MAP.get(bsonType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (codecs.containsKey(clazz)) {
            return (Codec<T>) codecs.get(clazz);
        }

        if (clazz == BsonArray.class) {
            return (Codec<T>) new BsonArrayCodec(registry);
        }

        if (clazz == BsonDocument.class) {
            return (Codec<T>) new BsonDocumentCodec(registry);
        }

        if (clazz == BsonDocumentWrapper.class) {
            return (Codec<T>) new BsonDocumentWrapperCodec(registry.get(BsonDocument.class));
        }

        if (clazz == BsonJavaScriptWithScope.class) {
            return (Codec<T>) new BsonJavaScriptWithScopeCodec(registry.get(BsonDocument.class));
        }

        return null;
    }

    private void addCodecs() {
        addCodec(new BsonNullCodec());
        addCodec(new BsonBinaryCodec());
        addCodec(new BsonBooleanCodec());
        addCodec(new BsonDateTimeCodec());
        addCodec(new BsonDBPointerCodec());
        addCodec(new BsonDoubleCodec());
        addCodec(new BsonInt32Codec());
        addCodec(new BsonInt64Codec());
        addCodec(new BsonMinKeyCodec());
        addCodec(new BsonMaxKeyCodec());
        addCodec(new BsonJavaScriptCodec());
        addCodec(new BsonObjectIdCodec());
        addCodec(new BsonRegularExpressionCodec());
        addCodec(new BsonStringCodec());
        addCodec(new BsonSymbolCodec());
        addCodec(new TimestampCodec());
        addCodec(new BsonUndefinedCodec());
    }

    private <T extends BsonValue> void addCodec(final Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
    }

    static {
        Map<BsonType, Class<? extends BsonValue>> map = new HashMap<BsonType, Class<? extends BsonValue>>();

        map.put(BsonType.NULL, BsonNull.class);
        map.put(BsonType.ARRAY, BsonArray.class);
        map.put(BsonType.BINARY, BsonBinary.class);
        map.put(BsonType.BOOLEAN, BsonBoolean.class);
        map.put(BsonType.DATE_TIME, BsonDateTime.class);
        map.put(BsonType.DB_POINTER, BsonDbPointer.class);
        map.put(BsonType.DOCUMENT, BsonDocument.class);
        map.put(BsonType.DOUBLE, BsonDouble.class);
        map.put(BsonType.INT32, BsonInt32.class);
        map.put(BsonType.INT64, BsonInt64.class);
        map.put(BsonType.MAX_KEY, BsonMaxKey.class);
        map.put(BsonType.MIN_KEY, BsonMinKey.class);
        map.put(BsonType.JAVASCRIPT, BsonJavaScript.class);
        map.put(BsonType.JAVASCRIPT_WITH_SCOPE, BsonJavaScriptWithScope.class);
        map.put(BsonType.OBJECT_ID, BsonObjectId.class);
        map.put(BsonType.REGULAR_EXPRESSION, BsonRegularExpression.class);
        map.put(BsonType.STRING, BsonString.class);
        map.put(BsonType.SYMBOL, BsonSymbol.class);
        map.put(BsonType.TIMESTAMP, BsonTimestamp.class);
        map.put(BsonType.UNDEFINED, BsonUndefined.class);

        DEFAULT_BSON_TYPE_CLASS_MAP = Collections.unmodifiableMap(map);
    }
}