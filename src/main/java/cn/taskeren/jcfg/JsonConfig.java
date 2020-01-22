package cn.taskeren.jcfg;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;

public class JsonConfig {

	protected final File file;
	protected final String newConfigContent;

	protected JSONObject json;

	public JsonConfig(File file) {
		this(file, "{}");
	}

	public JsonConfig(File file, String newConfigContent) {
		this.file = file;
		this.newConfigContent = newConfigContent;
		runConfiguration();
	}

	/**
	 * 读取配置文档
	 */
	protected void runConfiguration() {
		if(!file.exists()) {
			FileUtil.writeUtf8String(newConfigContent, file);
		}

		json = JSONUtil.parseObj(FileUtil.readUtf8String(file));
	}

	/**
	 * 保存配置文档
	 */
	public JsonConfig save() {
		FileUtil.writeUtf8String(json.toStringPretty(), file);
		return this;
	}

	/**
	 * 退出时自动保存
	 */
	public JsonConfig setAutoExitSave() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::save));
		return this;
	}

	public <T> T set(String key, T val) {
		json.put(key, val);
		return val;
	}

	public Object get(String key) {
		return json.get(key);
	}

	public <T> T get(String key, Class<T> cls) {
		return json.get(key, cls);
	}

	public <T> T get(String key, T defaultVal) {
		return (T) json.getOrDefault(key, defaultVal);
	}

}
