package cn.taskeren.jdb;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;

public class JsonDatabase {

	protected final File file;
	protected final String newConfigContent;

	protected JSONObject json;

	protected boolean autosave;

	public JsonDatabase(File file) {
		this(file, "{}");
	}

	public JsonDatabase(File file, String newConfigContent) {
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
	public JsonDatabase save() {
		FileUtil.writeUtf8String(json.toStringPretty(), file);
		return this;
	}

	/**
	 * 自动保存
	 * @return
	 */
	public JsonDatabase setAutoSave() {
		this.autosave = true;
		return this;
	}

	/**
	 * 退出时自动保存
	 */
	public JsonDatabase setAutoExitSave() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::save));
		return this;
	}

	public JsonDatabase set(String key, Object val) {
		json.put(key, val);
		if(autosave) save();
		return this;
	}

	public JsonDatabase set(Object key, Object val) {
		set(String.valueOf(key), val);
		return this;
	}

	public Object get(Object key) {
		return json.get(String.valueOf(key));
	}

	public <T> T get(String key, Class<T> cls) {
		return json.get(key, cls);
	}

	public <T> T get(Object key, Class<T> cls) {
		return get(String.valueOf(key), cls);
	}

	public <T> T get(String key, T defaultVal) {
		return (T) json.getOrDefault(key, defaultVal);
	}

	public <T> T get(Object key, T defaultVal) {
		return get(String.valueOf(key), defaultVal);
	}

	public boolean has(String key) {
		return get(key) != null;
	}

}
