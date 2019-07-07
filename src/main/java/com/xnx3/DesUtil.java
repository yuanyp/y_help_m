package com.xnx3;

import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

public class DesUtil {

	public static final byte[] DESIV = { 35, 12, 37, 49, 101, 91, 14, 36 };
	public static final byte[] DESkey = { 19, 31, 97, 26, 10, 80, 11, 37 };
	
	public static String lotteryDataEntry = "{\"Code\":1,\"Msg\":\"成功\",\"Codes\":{\"Code1\":[\"02\",\"05\",\"08\",\"10\",\"32\",\"33\",\"02\"],\"Code2\":[\"04\",\"13\",\"14\",\"23\",\"26\",\"32\",\"10\"],\"Code3\":[\"05\",\"07\",\"16\",\"20\",\"21\",\"25\",\"05\"],\"Code4\":[\"08\",\"16\",\"19\",\"21\",\"31\",\"32\",\"06\"],\"Code5\":[\"08\",\"13\",\"16\",\"23\",\"27\",\"31\",\"08\"],\"Code6\":[\"01\",\"02\",\"04\",\"07\",\"10\",\"23\",\"04\"],\"Code7\":[\"04\",\"10\",\"13\",\"15\",\"22\",\"27\",\"04\"],\"Code8\":[\"15\",\"19\",\"23\",\"28\",\"29\",\"33\",\"04\"]}}";

	static AlgorithmParameterSpec iv;
	private static Key key;

	public static String decode(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
			throws Exception {
		init(paramArrayOfByte1, paramArrayOfByte2);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(2, key, iv);
		return new String(cipher.doFinal(Base64.decode(paramString, 0)), "UTF-8");
	}

	public static String encode(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
			throws Exception {
		init(paramArrayOfByte1, paramArrayOfByte2);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(1, key, iv);
		return Base64.encodeToString(cipher.doFinal(paramString.getBytes("utf-8")), 2);
	}

	private static void init(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec keySpec = new DESKeySpec(paramArrayOfByte1);
			key = keyFactory.generateSecret(keySpec);
			iv = new IvParameterSpec(paramArrayOfByte2);
			return;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	private static String checkSign(String paramString, Map<String, Object> CheckInfo, String rcode, String sign) {
		Object localObject1 = (LotteryDataEntry) new Gson().fromJson(paramString, LotteryDataEntry.class);
		paramString = rcode;
		localObject1 = ((LotteryDataEntry) localObject1).getStrFromArray(Integer.parseInt(paramString));
		StringBuilder localObject2 = new StringBuilder();
		((StringBuilder) localObject2).append("{\"Result\":");
		((StringBuilder) localObject2).append(CheckInfo.get("Result"));
		((StringBuilder) localObject2).append(",\"Info\":\"");
		((StringBuilder) localObject2).append(CheckInfo.get("Info"));
		((StringBuilder) localObject2).append("\",\"ExpireDate\":");
		((StringBuilder) localObject2).append(CheckInfo.get("ExpireDate"));
		((StringBuilder) localObject2).append("}");
		StringBuilder localStringBuilder = new StringBuilder();
		localStringBuilder.append("jsonCheckInfo:");
		localStringBuilder.append(localObject2.toString());
		localStringBuilder = new StringBuilder();
		localStringBuilder.append("Check=");
		localStringBuilder.append(localObject2);
		localStringBuilder.append("&RCode=");
		localStringBuilder.append(paramString);
		localStringBuilder.append("&Codes=");
		localStringBuilder.append((String) localObject1);
		StringBuilder paramString1 = new StringBuilder();
		paramString1.append("sb.toString:");
		paramString1.append(localStringBuilder.toString());
		System.out.println(paramString1);
		paramString = MD5Util.MD5(localStringBuilder.toString());
		localObject1 = new StringBuilder();
		((StringBuilder) localObject1).append("md5Sign=");
		((StringBuilder) localObject1).append(paramString);
		System.out.println("md5Sign:" + localObject1.toString().toUpperCase());
		return paramString.toString().toUpperCase();
	}

	public static void main(String[] args) {
		String str = "nY8GclglPqU4IwnW7nuxowwM5h0gLeAgYtunKIODduUGDZgbnR7seeNxK3jw1C9hS27aeUsrXZWT9UdaFqXNy9XTAe39V57pyub5rVPQ5rYOVprHvadafcLidnujxUSur8XRBASWShRqv5U10ntMTj6/VWuG25bgXdBOjN9HOxyeD1xHAlthNJ7sCEthrFJO";
		String str1 = "ZET4YhfNfcJ1FROHWVHvMd8sbAkQv0mtTbmRkCAi%2BlwraCk%2B22I2P7SqKJRyousgrq0ozn3rdjsKMjTyFn%2B%2FJZ4S4TnNGRKLQtVkuBEJfdesrYFt9TPirzDNq6gz6iDyKZOH0PNIeaeboJHD%2ByXwgY8vNbQ7z0Evuzg6TaUWo78UNJ764fp50XUiSIhc%2Ff6Q%2BpRoUPmthqY%3D";
		String req1 = "Xm41ejgJNQP2VGVZFmm2psRmQUOZwbpEJSkuvEuL9Wa0KgOCvtBgKHBLeWvkdF%2B%2F9POsDqbyqj1FYRRxVZZAt735e3rk4VZ9lNVVNMSf3dqn0MexJG06B6K9aHQCgeFUF8ZLHbeBb9MoOTQfZF5aaHlytgVaxv9OtatHGnGkPrZxN4%2F8mmTm3UeA9y1Mbvh4wWvPvx%2B8%2B5g%3D";
		String req1_mw = "";
		String res1 = "";
		String res11 = "nY8GclglPqU4IwnW7nuxo3rE/mzmpGEEbi3A8OFtoftpPrXGuPSIEIp9YLuJic3sNscXCgdYspfdtg9WotjevaY/upS33I6Ka9atDIc8kZ+ySIvtfq75oHwtnLgQy7wqPXaq34evsJyl/bkFlRp8lA==";
		String res1_mw = "";
		String res11_mw = "";

		String req2 = "ZET4YhfNfcJ1FROHWVHvMd8sbAkQv0mtTbmRkCAi%2BlwraCk%2B22I2P7SqKJRyousgrq0ozn3rdjsKMjTyFn%2B%2FJZ4S4TnNGRKLQtVkuBEJfdesrYFt9TPirzDNq6gz6iDyKZOH0PNIeaeboJHD%2ByXwgY8vNbQ7z0Evuzg6TaUWo78UNJ764fp50XUiSIhc%2Ff6Q%2BpRoUPmthqY%3D";
		String req2_mw = "";
		String res2 = "nY8GclglPqU4IwnW7nuxowwM5h0gLeAgYtunKIODduWEWEKLW7v3BL9jQlEZNyDaigSSytPyMF4ZMfax6jwrxBWDwritSeKGIcgtM11o2Cf026lFjDbPLxODUfoEw504qMAEWExTbBUcOM0QdQAXfSaMWqgNRUmBPBaMLkyldUimEBYI9SW/UN1rtZGEXJul";
		String res2_mw = "";
		String count_str = "Ry9rQ9+AWP1lrxtAhEw/VfbO9lE/vbhhltMLv+pzjDmrR/DJ3pwV2zhkwp1RFhhBMimMpbH8T1U=";
		/**
		 * sqlite data
		 * 4AFBED2AE661735386DD9A751CC6A1DA
		 * WMb2qydUYfMuOR3DAqwiM6v8HfAdStO9NL/g4PFbiFBHvmBDHQ83b1ezBZoRKPKl4VLqnLPsJyNGw+nJgoh32A==
		 * XekQKpR/0df7dr43VCP8dNFVaS8cvGNutApmNDMI3W3gZcYNj9qFSgZNfeQtBY5U8RFipGCShQa5Pd4ByW5DOonHeqkwOE04sHRzyfw1jPi3Z1VasWM3slE/oxnlIsmzxzaUt7Nwou+N205VZQPumw==
		 * 1562492979618
		 */
		String sqlite_data_a = "WMb2qydUYfMuOR3DAqwiM6v8HfAdStO9NL/g4PFbiFBHvmBDHQ83b1ezBZoRKPKl4VLqnLPsJyNGw+nJgoh32A==";
		String sqlite_data_b = "XekQKpR/0df7dr43VCP8dNFVaS8cvGNutApmNDMI3W3gZcYNj9qFSgZNfeQtBY5U8RFipGCShQa5Pd4ByW5DOonHeqkwOE04sHRzyfw1jPi3Z1VasWM3slE/oxnlIsmzxzaUt7Nwou+N205VZQPumw==";
		try {
			System.out.println(new String(Base64.decode(sqlite_data_b, 1),"utf-8"));
			String sqlite_data_a_mw = DesUtil.decode(count_str, DESkey, DESIV);
			System.out.println("sqlite_data_a_mw:" + sqlite_data_a_mw);
			String sqlite_data_b_mw = DesUtil.decode(sqlite_data_b, DESkey, DESIV);
			System.out.println("sqlite_data_b_mw:" + sqlite_data_b_mw);
			
			String count_str_mw = DesUtil.decode(count_str, DESkey, DESIV);
			Map<String,Object> count_str_mw_map = JSONObject.fromObject(count_str_mw);
			count_str_mw_map.put("Code", "0");
			count_str_mw_map.put("TryCount", "-1");
			String count_str_modify = JSONObject.fromObject(count_str_mw_map).toString();
			String count_modify_encode = DesUtil.encode(count_str_modify, DESkey, DESIV);
			System.out.println("修改使用次数：" + count_str_modify);
			System.out.println("修改使用次数密文：" + count_modify_encode);
			long a = 1562467604409L;
			Date date = new Date();
			date.setTime(a);
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			System.out.println("count_use》" + count_str_mw + ",date:" +sdf.format(date));
			
			
			// String a = DesUtil.decode(str, DESkey, DESIV);
			// String b = DesUtil.decode(URLDecoder.decode(str1, "utf-8"),
			// DESkey, DESIV);
			req1_mw = DesUtil.decode(URLDecoder.decode(req1, "utf-8"), DESkey, DESIV);
			res1_mw = DesUtil.decode(res1, DESkey, DESIV);
			res11_mw = DesUtil.decode(res11, DESkey, DESIV);
			res11_mw = res11_mw.replace("\"ExpireDate\":0", "\"ExpireDate\":5555");
			
			Map<String,Object> tokenMap = JSONObject.fromObject(res11_mw);
			
			String md5 = checkSign(lotteryDataEntry, (Map)tokenMap.get("CheckInfo"), tokenMap.get("RCode") +"", "1A35933F56D9284E8D1557AD22753ED4");
			tokenMap.put("Sign", md5);
			String createData = JSONObject.fromObject(tokenMap).toString();
			System.out.println("createData:"+createData);
			System.out.println("createData_des " + DesUtil.encode(createData, DESkey, DESIV));
			System.out.println("res11_mw:"+res11_mw);
			System.out.println("res11_mw_des " + DesUtil.encode(res11_mw, DESkey, DESIV));
			System.out.println("1:req " + req1_mw);
			System.out.println("1:res " + res1_mw);
			System.out.println("11:res " + res11_mw);
			req2_mw = DesUtil.decode(URLDecoder.decode(req2, "utf-8"), DESkey, DESIV);
			res2_mw = DesUtil.decode(res2, DESkey, DESIV);
			System.out.println("2:req " + req2_mw);
			System.out.println("2:res " + res2_mw);
			
			
			// System.out.println(a);
			// System.out.println(b);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
