package com.soonhyung.main;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainClass {

	private static Logger logger = Logger.getLogger(MainClass.class);
	
	public static void main(String[] args) {
		try {
			int updCnt = updInningInfo();
			if(updCnt > 0){
				logger.info("집계 시작 ......................");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int updInningInfo(){
		logger.info("최신정보 다운로드 중 ......................");
		int returnValue = 0;
		try {
			int dbLastInning, realLastInning;
			String query = "INSERT INTO LOTTO_INNING_INFO ( INNING_NO, INNING_DATE, WIN_AMOUNT, WIN_PERSON, NO1, NO2, NO3, NO4, NO5, NO6, BONUS_NO ) VALUES ( %s, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s )";
			Document doc = Jsoup.connect("http://www.nlotto.co.kr/common.do?method=getLottoNumber").get();
			Elements contents = doc.select("body"); 
			String lottoData = contents.text();
			JSONObject object = (JSONObject)JSONValue.parse(lottoData);
			dbLastInning = DB.getLastInning();
			realLastInning = Integer.parseInt(object.get("drwNo").toString());
			for(dbLastInning++; dbLastInning <= realLastInning; dbLastInning++){
				doc = Jsoup.connect("http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=" + dbLastInning).get();
				contents = doc.select("body"); 
				lottoData = contents.text();
				object = (JSONObject)JSONValue.parse(lottoData);
				DB.iudQuery(String.format(query, object.get("drwNo")
											   , object.get("drwNoDate").toString().replaceAll("-", "")
											   , object.get("firstWinamnt"), object.get("firstPrzwnerCo")
											   , object.get("drwtNo1"), object.get("drwtNo2")
											   , object.get("drwtNo3"), object.get("drwtNo4")
											   , object.get("drwtNo5"), object.get("drwtNo6")
											   , object.get("bnusNo")));
				returnValue++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(returnValue + "건 다운로드 완료");
		return returnValue;
	}
}
