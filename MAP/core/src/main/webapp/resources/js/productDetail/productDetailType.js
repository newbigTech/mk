$(function(){
	//点击新建按钮
	$("#productType_create").click(function(){
		
		//获取要添加的字段
		//var bb={ 
		//		typeName:"海浪",
		//		typeId:"123123"
		//};
		
		//viewModelShopType.model.resp.push(bb);   //将要添加的数据放到viewModel中
		
	});
	
	
	//点击删除按钮
	$("#productType_delete").click(function(){
		
		//获取要删除的集合信息
		var checked = grid_shopType.selectedDataItems();    //获取选中的对象集合信息
		
		//循环删除该对象信息
		if(checked.length>0){         //如果选中的集合个数大于0
			$.each(checked,function(i,v){
				viewModelShopType.model.resp.remove(v);
            });
		}
		
	});
	
});

