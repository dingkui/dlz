package com.dlz.apps.file.enums;


public enum FileTypeEnum {
	fwb("0"),//富文本编辑器
	logo("1"),//用户logo
	sfz("2"),//身份证正面
	xsz("3"),//学生证1
	yyzz("4"),//营业执照正面
	ht("5"),//合同
	dkpz("6"),//打款凭证	
	ewm("7"),//二维码
	jdjyj("8"),//甲定价依据
	htqd("9"),//项目-合同签订
	htsc("10"),//合同上传&图纸
	goodsLogo("11"),//商品缩略图
	sq("12"),//申请详情表
	yhkhxkz("13"),//银行开户许可证
	rysqb("14"),//运营中心申请人员表
	fwzlht("15");//房屋租赁合同
	/*
	 * 类型ID
	 */
	public String typeCode;
	
	private FileTypeEnum(String typeCode) {
		this.typeCode = typeCode;
	}
	
	/**  
	 * 获取typeCode
	 * @return typeCode typeCode  
	 */
	public String getTypeCode() {
		return typeCode;
	}
}
