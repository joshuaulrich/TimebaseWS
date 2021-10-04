/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.tbwg.model.schema.changes;

import com.epam.deltix.tbwg.model.schema.SchemaBuilder;
import com.epam.deltix.tbwg.model.schema.SchemaUtils;
import com.epam.deltix.tbwg.model.schema.TypeDef;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.epam.deltix.qsrv.hf.tickdb.schema.ClassDescriptorChange;
import com.epam.deltix.qsrv.hf.tickdb.schema.SchemaChange;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassDescriptorChangeDef implements SchemaChangeDef {

    @JsonIgnore
    private final ClassDescriptorChange classDescriptorChange;

    public ClassDescriptorChangeDef(ClassDescriptorChange classDescriptorChange) {
        this.classDescriptorChange = classDescriptorChange;
    }

    @JsonProperty("source")
    public TypeDef getSource() {
        return SchemaBuilder.toTypeDef(classDescriptorChange.getSource(), false);
    }

    @JsonProperty("target")
    public TypeDef getTarget() {
        return SchemaBuilder.toTypeDef(classDescriptorChange.getTarget(), false);
    }

    @JsonProperty("fieldChanges")
    public List<FieldChangeWrapper> getFieldChanges() {
        return Arrays.stream(classDescriptorChange.getChanges())
                .filter(fieldChange -> fieldChange.getChangeImpact() != SchemaChange.Impact.None)
                .map(SchemaUtils::fieldChange)
                .collect(Collectors.toList());
    }

    @Override
    public SchemaChange.Impact getChangeImpact() {
        return classDescriptorChange.getChangeImpact();
    }
}
