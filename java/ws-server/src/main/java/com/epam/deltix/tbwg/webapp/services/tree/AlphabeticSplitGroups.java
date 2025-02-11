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
package com.epam.deltix.tbwg.webapp.services.tree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class AlphabeticSplitGroups implements SplitGroupsStrategy {

    @Override
    public <T> Map<String, TreeGroup<T>> split(List<T> elements, int prefixLength, int groupSize, Function<T, String> keyProvider) {
        Map<String, TreeGroup<T>> groups;
        int prefixSize = prefixLength;
        do {
            prefixSize++;
            groups = splitGroupsInternal(elements, prefixSize, keyProvider);
        } while (groups.size() < 2);

        return groups;
    }

    private <T> Map<String, TreeGroup<T>> splitGroupsInternal(List<T> elements, int prefixLength, Function<T, String> keyProvider) {
        Map<String, TreeGroup<T>> groups = new LinkedHashMap<>();
        for (int i = 0; i < elements.size(); ++i) {
            String prefix;
            String key = keyProvider.apply(elements.get(i));
            if (key.length() < prefixLength) {
                prefix = key;
            } else {
                prefix = key.substring(0, prefixLength);
            }

            groups.computeIfAbsent(prefix, k -> new TreeGroup<>(prefix, new ArrayList<>()))
                .getElements().add(elements.get(i));
        }

        return groups;
    }

}
