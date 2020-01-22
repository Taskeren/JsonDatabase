package cn.taskeren.jdb;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestJDB {

	@Test
	public void testJDB() {

		final JsonDatabase jdb = new JsonDatabase(FileUtil.file("config.cfg")).setAutoExitSave();
		jdb.set("id", 1024);
		jdb.save();

		System.out.println(jdb.get("id"));
		System.out.println(jdb.get("id", Boolean.class));

		System.out.println(jdb.get("default", "This is the default Value."));

		jdb.set("random", new Random().nextLong());

	}

	@Test
	public void testAutoSave() {

		final JsonDatabase jdb = new JsonDatabase(FileUtil.file("autosave.test.jdb")).setAutoSave();
		for(int i=0; i<10; i++) {
			Object val = RandomUtil.randomLong();
			jdb.set("gesh", val);
			System.out.println("Set Gesh to "+val);
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
		}

	}

}
