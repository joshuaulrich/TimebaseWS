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
package com.epam.deltix.tbwg.webapp.services.charting.queries;

import com.epam.deltix.tbwg.webapp.model.charting.ChartType;
import com.epam.deltix.tbwg.webapp.services.charting.TimeInterval;

public class BookSymbolQueryImpl extends SymbolQueryImpl implements BookSymbolQuery {

    private final int levelsCount;

    public BookSymbolQueryImpl(String stream, String symbol, ChartType type, TimeInterval interval,
                               int maxPointsCount, long pointInterval, int levels, boolean live)
    {
        super(stream, symbol, type, interval, maxPointsCount, pointInterval, live);

        this.levelsCount = levels;
    }

    @Override
    public int getLevelsCount() {
        return levelsCount;
    }
}
