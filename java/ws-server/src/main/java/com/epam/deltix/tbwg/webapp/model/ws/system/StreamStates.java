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
package com.epam.deltix.tbwg.webapp.model.ws.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class StreamStates extends SystemMessage {

    protected final ConcurrentLinkedDeque<String> added = new ConcurrentLinkedDeque<>();
    protected final ConcurrentLinkedDeque<String> deleted = new ConcurrentLinkedDeque<>();
    protected final ConcurrentLinkedDeque<String> changed = new ConcurrentLinkedDeque<>();
    protected final ConcurrentLinkedDeque<RenameMessage> renamed = new ConcurrentLinkedDeque<>();

    protected AtomicLong id = new AtomicLong();

    public void putAdded(String key) {
        added.add(key);
    }

    public void putDeleted(String key) {
        deleted.add(key);
    }

    public void putChanged(String key) {
        changed.add(key);
    }

    public void putRenamed(String oldName, String newName) {
        renamed.add(new RenameMessage(oldName, newName));
    }

    @Override
    public SystemMessageType systemMessageType() {
        return SystemMessageType.DB_STATE;
    }

    @JsonProperty("id")
    public long getId() {
        return id.getAndIncrement();
    }

    @JsonProperty("added")
    public ConcurrentLinkedDeque<String> getAdded() {
        return added;
    }

    @JsonProperty("deleted")
    public ConcurrentLinkedDeque<String> getDeleted() {
        return deleted;
    }

    @JsonProperty("changed")
    public ConcurrentLinkedDeque<String> getChanged() {
        return changed;
    }

    @JsonProperty("renamed")
    public ConcurrentLinkedDeque<RenameMessage> getRenamed() {
        return renamed;
    }

    public synchronized void clear() {
        added.clear();
        deleted.clear();
        changed.clear();
        renamed.clear();
    }

    @Override
    public String toString() {
        return "StreamStates{" +
                "added=" + added +
                ", deleted=" + deleted +
                ", changed=" + changed +
                ", renamed=" + renamed +
                ", id=" + id +
                '}';
    }

    @JsonIgnore
    public synchronized boolean isEmpty() {
        return added.isEmpty() && deleted.isEmpty() && changed.isEmpty() && renamed.isEmpty();
    }

}
