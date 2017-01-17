// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
package de.bytefish.fcmjava.model.topics;

import java.util.List;

public class TopicList {

	private final List<Topic> topics;

	public TopicList(List<Topic> topics) {
		this.topics = topics;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public String getTopicsCondition() {
		StringBuilder tc = new StringBuilder();
		for (int i = 0; i < topics.size(); i++) {
			tc.append(String.format("'%s' in topics", topics.get(i)));
			if (i < topics.size() - 1) {
				tc.append(" || ");
			}
		}
		return tc.toString();
//        return topics.stream()
//                .map(topic -> String.format("'%s' in topics", topic))
//                .collect(Collectors.joining(" || "));
	}
}
