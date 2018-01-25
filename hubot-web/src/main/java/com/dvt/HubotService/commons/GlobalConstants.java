package com.dvt.HubotService.commons;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.commons.utils.XmlUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.time.nlp.TimeNormalizer;



public class GlobalConstants {
	public static final String PAGE_TEST = "test/main";
	public static final String PAGE_AUTOCODE = "autocode/main";
	
	public static final Map<String,HubotInterface> interfaces = Maps.newHashMap();
	public static final Map<String,String> partOfSpeech = Maps.newHashMap();
	
	public static TimeNormalizer normalizer;
	
	static{
		if(interfaces.isEmpty()){
			//解析Xml
			String filePath;
			try {
				filePath = GlobalConstants.class.getClassLoader().getResource("hubot.xml").toURI().getPath();
				Document document = XmlUtils.read(filePath);
				List<Node> hiFields = XmlUtils.XpathQuery(document, "/configuration/hubot-interface");
				for (Node _hi : hiFields) {
					Element hi = (Element)_hi;
					String interfaceKey = hi.attributeValue("name");//接口的唯一标识
					Boolean executable = Boolean.valueOf(hi.attributeValue("executable"));
					if(executable){//标注有效
						System.out.println("装载接口"+interfaceKey);
						HubotInterface O = new HubotInterface();
						List<Element> hiel = hi.elements();
						for (Element element : hiel) {
							if(element.getName().equals("description")){
								O.setDescription(element.getText());
							}else if(element.getName().equals("title")){
								O.setTitle(element.getText());
							}else if(element.getName().equals("regexpSentence")){
								O.setRegexpSentence(element.getText());
							}else if(element.getName().equals("entryUrl")){
								O.setEntryUrl(element.getText());
							}else if(element.getName().equals("redirectUrl")){
								O.setRedirectUrl(element.getText());
							}else if(element.getName().equals("postObject")){
								List<Element> els = element.elements();//处理待发送参数
								if(els.size()==1){
									Element belowPostObjectels = els.get(0);
									if("list".equals(belowPostObjectels.getName())){
										O.setPostObjectList(O.arrangeParamsArray(belowPostObjectels));
									}else if("object".equals(belowPostObjectels.getName())){
										O.setPostObject(O.arrangeParamsObject(belowPostObjectels));
									}
								}
							}
						}
						interfaces.put(interfaceKey, O);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(partOfSpeech.isEmpty()){
			Properties prop = new Properties();
			try {
				prop.load(GlobalConstants.class.getClassLoader().getResourceAsStream("speech.properties"));
				Enumeration en = prop.propertyNames();
		        while (en.hasMoreElements()) {
		            String key=(String) en.nextElement();
		            String value = prop.getProperty(key);
		            partOfSpeech.put(key, value);
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			URL url = TimeNormalizer.class.getResource("/TimeExp.m");
			System.out.println("加载time解析资源：" + url.toURI().toString());
			normalizer= new TimeNormalizer(url.toURI().toString());
			normalizer.setPreferFuture(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
