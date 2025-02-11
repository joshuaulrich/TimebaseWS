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
package com.epam.deltix.tbwg.webapp.model.charting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

import java.util.List;

/**
 * @author Daniil Yarmalkevich
 * Date: 8/19/2019
 */
public class TimeSeriesEntry {

    public TimeSeriesEntry() {}

    public TimeSeriesEntry(String target) {
        this.target = target;
        this.datapoints = new ObjectArrayList<>();
    }

    @JsonProperty
    public String target;

    @JsonProperty
    public List<Number[]> datapoints;

}
