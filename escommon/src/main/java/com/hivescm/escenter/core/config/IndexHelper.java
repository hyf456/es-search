package com.hivescm.escenter.core.config;

import java.util.Map;

import com.google.gson.JsonObject;
import com.hivescm.search.index.DataType;

public class IndexHelper {
	private Map<String, JsonObject> properties;

	public IndexHelper(Map<String, JsonObject> properties) {
		this.properties = properties;
	}

	public boolean isTextField(String name) {
		if (properties.containsKey(name)) {
			return isTextField(properties.get(name));
		}
		return false;
	}

	public boolean isKeywordField(String name) {
		if (properties.containsKey(name)) {
			return isKeywordField(properties.get(name));
		}
		return false;
	}

	public boolean isSupportTextAndKeywordField(String name) {
		if (properties.containsKey(name)) {
			return isSupportTextAndKeyword(properties.get(name));
		}
		return false;
	}

	private boolean isTextField(JsonObject jsonObject) {
		return matchType(jsonObject, DataType.TEXT);
	}

	private boolean isKeywordField(JsonObject jsonObject) {
		return matchType(jsonObject, DataType.KEYWORD);
	}

	private boolean isSupportTextAndKeyword(JsonObject jsonObject) {
		if (matchType(jsonObject, DataType.TEXT)) {
			if (jsonObject.has("fields")) {
				jsonObject = jsonObject.get("fields").getAsJsonObject();
				if (jsonObject.has("keyword")) {
					{ // {"type":"text","fields":{"keyword":{"type":"keyword","ignore_above":256}}}
						jsonObject = jsonObject.get("keyword").getAsJsonObject();
						if (jsonObject.get("type").getAsString().equals("keyword")) {
							return true;
						}
					}
					// } else if (jsonObject.has("raw")) {
					// {// {"type": "text","fields": {"raw": {"type": "keyword"}}}
					// jsonObject = jsonObject.get("raw").getAsJsonObject();
					// if (jsonObject.get("type").getAsString().equals("keyword")) {
					// return true;
					// }
					// }
				}
			}
		}
		return false;
	}

	private boolean matchType(JsonObject jsonObject, DataType type) {
		if (jsonObject.has("type")) {
			if (type.name().equalsIgnoreCase(jsonObject.get("type").getAsString())) {
				return true;
			}
		}
		return false;
	}
}
