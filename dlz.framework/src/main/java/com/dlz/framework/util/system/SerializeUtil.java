package com.dlz.framework.util.system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 描述：对象序列化和反序列化
 * 
 * @author dk
 */
public class SerializeUtil {
	public static void main(String[] args) {
		p(new StringBuilder("xassa"));
		p("");
		p("XXsdas");
		p("阿瑟东十分士大夫SDFADFCSASDAWD");
		p(1l);
		p(1d);
		p(1);
	}
	private static void p(Object o){
		byte[] b=serialize(o);
		System.out.println();
		for (int i = 0; i < b.length; i++) {
			System.out.print(b[i]+" ");
		}
	}
	private static String CHARSET="UTF-8";
	public static byte[] serialize(Object value) {
		if (value == null) {
			throw new NullPointerException("Can't serialize null");
		}
		if(value instanceof CharSequence){
			try {
				return value.toString().getBytes(CHARSET);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		byte[] rv = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(value);
			os.close();
			bos.close();
			rv = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			try {
				if (os != null)
					os.close();
				if (bos != null)
					bos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return rv;
	}

	public static Object deserialize(byte[] in) {
		if(in.length<6 || in[0]!=-84 || in[1]!=-19 || in[2]!=0 || in[3]!=5){
			try {
				return new String(in,CHARSET);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Object rv = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				rv = is.readObject();
				is.close();
				bis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (bis != null)
					bis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return rv;
	}
}
