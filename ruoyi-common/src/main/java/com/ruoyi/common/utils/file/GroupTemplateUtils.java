package com.ruoyi.common.utils.file;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class GroupTemplateUtils {
	private Logger log= LoggerFactory.getLogger(getClass());
	private ClasspathResourceLoader resourceLoader = null;
	private Configuration cfg = null;
	private GroupTemplate gt = null;
	public GroupTemplate getGT() {
		if (gt == null) {
			try {
				resourceLoader = new ClasspathResourceLoader();
				cfg = Configuration.defaultConfiguration();
				gt = new GroupTemplate(resourceLoader, cfg);
			} catch (IOException e) {
				log.error("异常", e);
			}
		}
		return gt;

	}

	/**
	 * 根据模版生成字符串
	 * @param templatePath
	 * @param p
	 * @return
	 */
	public String genContent(String templatePath, Map<String, String> p) {
		try {
			Template t = getGT().getTemplate(templatePath);
			Map<String, Object> shared = new HashMap<String, Object>();
			for (String key:p.keySet()) {
				shared.put(key, p.get(key));
			}
			gt.setSharedVars(shared);
			return t.render();
		} catch (BeetlException e) {
			log.error("异常", e);
			return "";
		} catch (Exception e) {
			log.error("异常", e);
			return "";
		}
	}

	/**
	 * 生成文件
	 * @param templatePath
	 * @param p
	 * @return
	 */
	public boolean genFile(String templatePath, Map<String, String> p, String filePath){
		try {
			String content = genContent(templatePath, p);
			File file = new File(filePath);
			if (!file.exists()){
				file.createNewFile();
			}
			FileUtils.writeStringToFile(file, content, "utf-8", false);
			return true;
		} catch (Exception e){
			log.error("异常：", e);
			return false;
		}
	}

	public static void main(String[] args) {
		GroupTemplateUtils groupTemplateUtils = new GroupTemplateUtils();
		Map<String, String> p = new HashMap<>();
		p.put("mnemonic", "ten blouse impose text luxury inner about cigar muscle toy olympic upon");
		String content = groupTemplateUtils.genContent("/moban/wallet-config.yaml", p);
		System.out.println(content);
	}
}
