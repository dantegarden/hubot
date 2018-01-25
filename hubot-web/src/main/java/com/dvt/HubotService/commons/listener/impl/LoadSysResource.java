package com.dvt.HubotService.commons.listener.impl;

import java.net.URL;
import java.util.List;

import me.xiaosheng.chnlp.AHANLP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.dvt.HubotService.commons.GlobalConstants;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;
import com.time.nlp.TimeNormalizer;

@Component
public class LoadSysResource implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
		if(event.getApplicationContext().getParent() != null){
			try {
				System.out.println("样本全量加载到内存......");
				String s1 = "查一下我的加班申请。";
		        String s2 = "给我查查加班申请。";
		        String s3 = "查询我的请假申请。";
		        
		        System.out.print(AHANLP.sentenceSimilarity(s1, s1));
		        System.out.print(AHANLP.sentenceSimilarity(s1, s2));
		        System.out.print(AHANLP.sentenceSimilarity(s1, s3));
		        AHANLP.DependencyParse(s1);
		        
				System.out.print("加载词性库......");
				GlobalConstants.partOfSpeech.isEmpty();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	

	
}
