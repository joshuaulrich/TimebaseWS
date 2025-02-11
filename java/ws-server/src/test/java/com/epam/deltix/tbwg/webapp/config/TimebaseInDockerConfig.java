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
package com.epam.deltix.tbwg.webapp.config;

import com.epam.deltix.tbwg.webapp.utils.TimebaseInDocker;
import com.epam.deltix.tbwg.webapp.services.charting.datasource.MessageSourceFactory;
import com.epam.deltix.tbwg.webapp.services.timebase.SystemMessagesService;
import com.epam.deltix.tbwg.webapp.services.timebase.TimebaseService;
import com.epam.deltix.tbwg.webapp.services.timebase.TimebaseServiceImpl;
import com.epam.deltix.tbwg.webapp.settings.TimebaseSettings;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("withTb")
public class TimebaseInDockerConfig {

    @Bean
    @Profile("timebaseInDocker")
    public TimebaseService timebaseService(TimebaseSettings timebaseSettings, SystemMessagesService systemMessagesService, TimebaseInDocker docker) {
        timebaseSettings.setUrl("dxtick://localhost:" + docker.getTimebasePort());
        return new TimebaseServiceImpl(timebaseSettings, systemMessagesService);
    }

    @Bean
    @Profile("timebaseExternal")
    public TimebaseService timebaseService2(TimebaseSettings timebaseSettings, SystemMessagesService systemMessagesService) {
        return new TimebaseServiceImpl(timebaseSettings, systemMessagesService);
    }

    @Bean
    public MessageSourceFactory dataSource() {
        return Mockito.mock(MessageSourceFactory.class);
    }
}
