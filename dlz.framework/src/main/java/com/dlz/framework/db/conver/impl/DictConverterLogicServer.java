package com.dlz.framework.db.conver.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.dlz.framework.db.cache.DictCache;
import com.dlz.framework.db.cache.bean.Dict;
import com.dlz.framework.db.cache.bean.DictItem;
import com.dlz.framework.db.conver.ILogicServer;
import com.dlz.framework.db.exception.DbException;
import org.slf4j.Logger;

@Component
@Lazy
class DictConverterLogicServer implements ILogicServer<Object,String> {
	private static Logger logger =org.slf4j.LoggerFactory.getLogger(DictConverterLogicServer.class);
	@Autowired
	@Nullable
	DictCache dictCache;

	@Override
	public Object conver2Str(Object value, String dictCode) {
		Dict dict=dictCache.get(dictCode);
		if(dict==null){
			throw new DbException("字典转换错误，字典【"+dictCode+"】未定义",1004);
		}
		DictItem item=dict.getItemMap().get(String.valueOf(value));
		if(item==null){
			logger.warn("字典转换错误，字典【"+dictCode+"】中的值【"+value+"】未找到！");
			return value;
		}
		return item.getText();
	}

	@Override
	public Object conver2Db(Object o, String para) {
		return null;
	}

}
