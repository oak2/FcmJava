// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.fcmjava.client.tests.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.model.topics.Topic;
import de.bytefish.fcmjava.requests.data.DataMulticastMessage;
import de.bytefish.fcmjava.requests.topic.TopicUnicastMessage;
import de.bytefish.fcmjava.responses.FcmMessageResponse;
import java.time.Duration;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class FcmClientIntegrationTest {

    private class PersonData {

        private final String firstName;
        private final String lastName;

        public PersonData(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @JsonProperty("firstName")
        public String getFirstName() {
            return firstName;
        }

        @JsonProperty("lastName")
        public String getLastName() {
            return lastName;
        }
    }

    @Test
    @Ignore("This is an Integration Test using system properties to contact the FCM Server")
    public void SendTopicMessageTest() throws Exception {

        // Create the Client using system-properties-based settings:
        FcmClient client = new FcmClient(PropertiesBasedSettings.createFromDefault());

        // Message Options:
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setTimeToLive(Duration.ofHours(1))
                .build();

        // Send a Message:
        client.send(new TopicUnicastMessage(options, new Topic("news"), new PersonData("Philipp", "Wagner")));
    }

    @Test
    @Ignore("This is an Integration Test using system properties to contact the FCM Server")
    public void SendDataMulticastMessageTest() throws Exception {

        // Create the Client using system-properties-based settings:
        FcmClient client = new FcmClient(PropertiesBasedSettings.createFromDefault());

        // Message Options:
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setTimeToLive(Duration.ofHours(1))
                .build();

        ArrayList<String> registrationIds = new ArrayList<String>();
        registrationIds.add("invalid_key");

        // Send a Message:
        FcmMessageResponse msgResponse = client.send(new DataMulticastMessage(options, registrationIds, new PersonData("Philipp", "Wagner")));

        Assert.assertNotNull(msgResponse);
    }
}
