package cn.taskeren.jcfg;

import cn.hutool.core.io.FileUtil;
import org.junit.Test;

import java.util.Random;

public class TestJCfg {

	@Test
	public void testJCfg() {

		final JsonConfig jcfg = new JsonConfig(FileUtil.file("config.cfg")).setAutoExitSave();
		jcfg.set("id", 1024);
		jcfg.save();

		System.out.println(jcfg.get("id"));
		System.out.println(jcfg.get("id", Boolean.class));

		System.out.println(jcfg.get("default", "This is the default Value."));

		jcfg.set("random", new Random().nextLong());

	}

}
