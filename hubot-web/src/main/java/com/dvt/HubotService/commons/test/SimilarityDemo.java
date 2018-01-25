package com.dvt.HubotService.commons.test;

import java.util.List;
import java.util.Set;

import me.xiaosheng.chnlp.AHANLP;
import me.xiaosheng.chnlp.distance.Word2VecSimi;

import org.junit.Test;

import com.ansj.vec.domain.WordEntry;

public class SimilarityDemo {
	//@Test
	public void test(){
		  System.out.println("猫 | 狗 : " + AHANLP.wordSimilarity("猫", "狗"));
//        System.out.println("计算机 | 电脑 : " + AHANLP.wordSimilarity("计算机", "电脑"));
//        System.out.println("计算机 | 男人 : " + AHANLP.wordSimilarity("计算机", "男人"));
//        
//        Set<WordEntry> similarWords = AHANLP.getSimilarWords("微积分", 10);
//        System.out.println("\n与\"微积分\"语义相似的前10个词语:");
//        for(WordEntry word : similarWords) {
//            System.out.println(word.name + " : " + word.score);
//        }
        
        String s1 = "查一下我的加班申请。";
        String s2 = "给我查查加班申请。";
        String s3 = "查询我的请假申请。";
        
        System.out.println();
        System.out.println("s1 : " + s1 + "\ns2 : " + s2 + "\ns3 : " + s3);
        System.out.println("s1 | s1 : " + AHANLP.sentenceSimilarity(s1, s1));
        System.out.println("s1 | s2 : " + AHANLP.sentenceSimilarity(s1, s2));
        System.out.println("s1 | s3 : " + AHANLP.sentenceSimilarity(s1, s3));
        
        String s4 = "我申请今天加班。";
        String s5 = "我今天要加班，帮我申请一下。";
                
        System.out.println("s4 | s5 : " + AHANLP.sentenceSimilarity(s4, s5));
        
        List<String> sen1words = AHANLP.getWordList(AHANLP.NLPSegment(s1, true));
        List<String> sen2words = AHANLP.getWordList(AHANLP.NLPSegment(s2, true));
        List<String> sen3words = AHANLP.getWordList(AHANLP.NLPSegment(s3, true));
        System.out.println("s1 | s1 : " + Word2VecSimi.sentenceSimilarity(sen1words, sen1words));
        System.out.println("s1 | s2 : " + Word2VecSimi.sentenceSimilarity(sen1words, sen2words));
        System.out.println("s1 | s3 : " + Word2VecSimi.sentenceSimilarity(sen1words, sen3words));
	}
	
	public static void main(String[] args) {
		String s1 = "查一下我的加班申请。";
        String s2 = "给我查查加班申请。";
        String s3 = "查询我的请假申请。";
        
        System.out.println(System.getProperty("file.encoding"));
        System.out.println("s1 : " + s1 + "\ns2 : " + s2 + "\ns3 : " + s3);
        System.out.println("s1 | s1 : " + AHANLP.sentenceSimilarity(s1, s1));
        System.out.println("s1 | s2 : " + AHANLP.sentenceSimilarity(s1, s2));
        System.out.println("s1 | s3 : " + AHANLP.sentenceSimilarity(s1, s3));
	}
}
