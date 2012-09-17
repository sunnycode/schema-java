/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sunnycode.schema.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.sunnycode.schema.Transform;



/**
 * Validates / transforms values using the schema-defined ENUM.
 */
public class EnumTransform implements Transform<Object, Integer> {
    private final Map<String, Integer> toMapping;
    private final List<String> values;

    public EnumTransform(List<String> values) {
        int id = 0;

        Map<String, Integer> toMapping = new LinkedHashMap<String, Integer>();
        List<String> theValues = new ArrayList<String>();

        for (String value : values) {
            toMapping.put(value, id);
            theValues.add(value);
            id += 1;
        }

        this.toMapping = Collections.unmodifiableMap(toMapping);
        this.values = Collections.unmodifiableList(theValues);
    }

    @Override
    public Integer pack(Object instance) throws TransformException {
        if (instance == null) {
            throw new TransformException("must not be null");
        }

        String instanceString = instance.toString();

        if (!toMapping.containsKey(instanceString)) {
            throw new TransformException("is not a valid enum value : "
                    + instanceString);
        }

        return toMapping.get(instanceString);
    }

    @Override
    public Object unpack(Integer instance) throws TransformException {
        if (instance == null) {
            throw new TransformException("must not be null");
        }

        if (instance < 0) {
            throw new TransformException(
                    "must be greater than or equal to zero");
        }

        if (instance >= values.size()) {
            throw new TransformException("value not found : " + instance);
        }

        return values.get(instance);
    }
}
