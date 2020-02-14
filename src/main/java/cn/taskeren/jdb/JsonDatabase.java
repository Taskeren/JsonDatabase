package cn.taskeren.jdb;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class JsonDatabase<K, V> {

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

	public JsonDatabase set(K key, V val) {
		json.put(key.toString(), val);
		if(autosave) save();
		return this;
	}

	public Object getRaw(String key) {
		return json.get(key);
	}

	public V get(K key) {
		return (V) json.get(key.toString());
	}

	public V get(K key, V defaultVal) {
		return (V) json.getOrDefault(key.toString(), defaultVal);
	}

	public <C> C convert(K key, Type t) {
		return convert(key, t, null);
	}

	public <C> C convert(K key, C defaultVal) {
		return convert(key, defaultVal.getClass(), defaultVal);
	}

	public <C> C convert(K key, Type t, C defaultVal) {
		return Convert.convert(t, get(key));
	}

	public boolean has(K key) {
		return get(key) != null;
	}

	// Supers
	public void forEach(BiConsumer<K, V> c) {
		json.forEach((k,v) -> c.accept((K)k, (V)v));
	}

	public Set<String> keySet() {
		return json.keySet();
	}

}
