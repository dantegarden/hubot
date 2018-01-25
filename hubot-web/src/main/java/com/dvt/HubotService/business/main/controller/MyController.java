package com.dvt.HubotService.business.main.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import me.xiaosheng.chnlp.AHANLP;
import me.xiaosheng.chnlp.parser.DependencyParser;
import me.xiaosheng.chnlp.seg.NERTerm;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dvt.HubotService.business.main.dto.EntryBO;
import com.dvt.HubotService.business.main.dto.HubotDTO;
import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.ReqParam;
import com.dvt.HubotService.business.main.dto.SimilarityBean;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.entity.Result;
import com.dvt.HubotService.commons.exception.OApiException;
import com.dvt.HubotService.commons.exception.OApiResultException;
import com.dvt.HubotService.commons.utils.HttpHelper;
import com.dvt.HubotService.commons.utils.JsonUtils;
import com.dvt.HubotService.commons.utils.JsonValidator;
import com.dvt.HubotService.commons.utils.RegexUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.seg.common.Term;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;

@Controller
@RequestMapping("/myhubot")
public class MyController {
	
	private static final Logger logger = LoggerFactory.getLogger(MyController.class);
	private static final String token = "ROCKET_TOKEN";
	
	@RequestMapping("test")
	@ResponseBody
	public String test(HttpServletRequest request){
		String myMes = "我申请加班。开始时间是今天早上的7点，结束时间是晚上九点。";
        
        HubotInterface myHif = findSuitableInterface(myMes);
        if(myHif!=null){
        	List<String> senList = AHANLP.splitSentence(myMes);
        	for (String sentence : senList) {
				System.out.println(sentence);
				
				for (String regexpKey : myHif.getRegexpEntryMap().keySet()) {//遍历所有待填参数
					if(sentence.contains(regexpKey)){//此参数在这句话里
						EntryBO ebo = myHif.getRegexpEntryMap().get(regexpKey);

						if(ebo.getType().equals("date")){//参数是date类型
							GlobalConstants.normalizer.parse(sentence);
							TimeUnit[] unit = GlobalConstants.normalizer.getTimeUnit();
							String sometime = DateUtil.formatDateDefault(unit[0].getTime());
							System.out.println(sometime);
							ebo.getEntry().setValue(sometime);
						}

					}
				}
				
				
			}
        	
        	System.out.println(JsonUtils.JavaBeanToJson(myHif.getPostObject()));
        	
        }else{
        	//TODO
        	//没找到可调接口的逻辑
        }
       
        
//		HubotInterface hif = GlobalConstants.interfaces.get("addRequestForOvertime");
//		System.out.println("猫 | 狗 : " + AHANLP.wordSimilarity("猫", "狗"));
//		String s1 = "查一下我的加班申请。";
//        String s2 = "给我查查加班申请。";
//        String s3 = "查询我的请假申请。";
//        
//        System.out.println("s1 : " + s1 + "\ns2 : " + s2 + "\ns3 : " + s3);
//        System.out.println("s1 | s1 : " + AHANLP.sentenceSimilarity(s1, s1));
		return "";
	}
	
	@RequestMapping("show")
	@ResponseBody
	public Result show(HttpServletRequest request) throws IOException {
		Map<String,String> params = Maps.newHashMap();
		
		Enumeration pnu=request.getParameterNames(); 
		while(pnu.hasMoreElements()){  
			String paraName=(String)pnu.nextElement();  
			if(new JsonValidator().validate(paraName)){
				System.out.println("接收到来自机器人的JSON数据:"+ paraName);
				HubotDTO hubotMsg = JsonUtils.jsonToJavaBean(paraName, HubotDTO.class);
				try {
					//TODO 1.分词  语义解析
					String says = hubotMsg.getMessage();
					//TODO 2.根据分词 寻找该调哪个接口
					if(says.equals("我的加班申请")){
						HubotInterface hi = GlobalConstants.interfaces.get("seeRequestForOvertime");
						System.out.println(hi.getDescription());
						
						//3.接口调用
						String nonceStr = "abcdefg";
						String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
						String signature = sign(nonceStr, timeStamp);
						
						String entryUrl = hi.getEntryUrl();
						String redirectUrl = hi.getRedirectUrl();//"http://192.168.1.161:8069/web?min=1&limit=80&view_type=list&model=apply.for.overtime&action=518&menu_id=324#min=1&limit=80&view_type=list&model=apply.for.overtime&action=518&menu_id=324";
						ReqParam rp = new ReqParam(nonceStr, timeStamp, signature, hubotMsg.getUserid(), hi.getTitle(), redirectUrl);
						
						params.put("params", JsonUtils.JavaBeanToJson(rp));
						String backJson = HttpHelper.startPost(entryUrl, params);
					}
					
					return new Result(0, null);
				} catch (Exception e) {
					e.printStackTrace();
					return new Result(2, e.getMessage(), null);
				}
			}
		}
		
		return new Result(2, "未接收到请求参数", null);
		
	}
	
	/**寻找合适的接口*/
	private HubotInterface findSuitableInterface(String mes){
		System.out.println("切分句子:");
        List<String> senList = AHANLP.splitSentence(mes);
        for (int i = 0; i < senList.size(); i++){
            System.out.println("切分结果"+(i) + " : " + senList.get(i));
        }
		
		HubotInterface myHif = null;                                      
        List<SimilarityBean> similarList = Lists.newArrayList();
        for (String ifkey : GlobalConstants.interfaces.keySet()) {
        	HubotInterface hif = GlobalConstants.interfaces.get(ifkey);
        	float similarity = AHANLP.sentenceSimilarity(senList.get(0), hif.getRegexpSentence());
        	similarList.add(new SimilarityBean(similarity, ifkey));
        	System.out.println("接口"+ifkey+"的例句(" + hif.getRegexpSentence()+ ")与(" +senList.get(0)+")的相似度为:"  + similarity);
		}
        
        Collections.sort(similarList);//按相似度排序，找最高的
        if(similarList.get(0).getSimilarity()>=0.75f){//相似度高于0.75才算找到
        	System.out.println("认为"+similarList.get(0).getKey()+"是最佳匹配的接口");
        	myHif = GlobalConstants.interfaces.get(similarList.get(0).getKey());
        }else{
        	System.out.println("没有找到匹配度高的接口");
        }
        return myHif;
	}
	
	
	private String sign(String nonceStr, String timeStamp) throws OApiException {
		
		String plain = this.token + "." + timeStamp + "." + nonceStr;

		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			return bytesToHex(sha1.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new OApiResultException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new OApiResultException(e.getMessage());
		}
	}
	
	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
