// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
package de.bytefish.fcmjava.client.tests.retry;

import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.functional.Action0;
import de.bytefish.fcmjava.client.functional.Func1;
import de.bytefish.fcmjava.client.http.IHttpClient;
import de.bytefish.fcmjava.client.retry.RetryUtils;
import de.bytefish.fcmjava.client.tests.testutils.TestUtils;
import de.bytefish.fcmjava.exceptions.FcmRetryAfterException;
import de.bytefish.fcmjava.http.client.IFcmClient;
import de.bytefish.fcmjava.http.options.IFcmClientSettings;
import de.bytefish.fcmjava.model.builders.FcmMessageOptionsBuilder;
import de.bytefish.fcmjava.requests.groups.CreateDeviceGroupMessage;
import de.bytefish.fcmjava.responses.CreateDeviceGroupMessageResponse;
import java.time.Duration;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FcmClientRetryTest {

	@Mock
	private IFcmClientSettings settingsMock;

	@Mock
	private IHttpClient httpClientMock;

	@Before
	public void Setup() {
		initMocks(this);
	}

	@Test
	public void retryWithThrowTest() {

		// Fake Message to send:
		final CreateDeviceGroupMessage createDeviceGroupMessage = new CreateDeviceGroupMessage(new FcmMessageOptionsBuilder().build(), new ArrayList<String>(), "Unit Test");

		when(httpClientMock.post(createDeviceGroupMessage, CreateDeviceGroupMessageResponse.class))
				.thenThrow(new FcmRetryAfterException(Duration.ZERO));

		// Create the Test Subject:
		final IFcmClient client = new FcmClient(settingsMock, httpClientMock);

		// Invoke it and make sure it throws:
//        TestUtils.assertThrows(() -> RetryUtils.getWithRetry(() -> client.send(createDeviceGroupMessage), 5), FcmRetryAfterException.class);
		TestUtils.assertThrows(new Action0() {
			@Override
			public void invoke() {
				RetryUtils.getWithRetry(new Func1<Action0>() {
					@Override
					public Action0 invoke() {
						client.send(createDeviceGroupMessage);
						return null;
					}
				}, 5);
			}
		}, FcmRetryAfterException.class);

		// And finally verify it has been called 5 times as set in the Mock Expectations:
		verify(httpClientMock, times(5))
				.post(createDeviceGroupMessage, CreateDeviceGroupMessageResponse.class);
	}

	@Test
	public void retryNotNecessaryTest() {

		// Fake Message to send:
		final CreateDeviceGroupMessage createDeviceGroupMessage = new CreateDeviceGroupMessage(new FcmMessageOptionsBuilder().build(), new ArrayList<String>(), "Unit Test");

		// Fake Message to receive:
		CreateDeviceGroupMessageResponse createDeviceGroupMessageResponse = new CreateDeviceGroupMessageResponse("Unit Test");

		when(httpClientMock.post(createDeviceGroupMessage, CreateDeviceGroupMessageResponse.class))
				.thenReturn(createDeviceGroupMessageResponse);

		// Create the Test Subject:
		final IFcmClient client = new FcmClient(settingsMock, httpClientMock);

		// Invoke it and make sure it throws:
//		TestUtils.assertDoesNotThrow(() -> RetryUtils.getWithRetry(() -> client.send(createDeviceGroupMessage), 5));
		TestUtils.assertDoesNotThrow(new Action0() {
			@Override
			public void invoke() {
				RetryUtils.getWithRetry(new Func1<Action0>() {
					@Override
					public Action0 invoke() {
						client.send(createDeviceGroupMessage);
						return null;
					}
				}, 5);
			}
		});

		// And finally verify it has been called 5 times as set in the Mock Expectations:
		verify(httpClientMock, times(1))
				.post(createDeviceGroupMessage, CreateDeviceGroupMessageResponse.class);
	}
}
