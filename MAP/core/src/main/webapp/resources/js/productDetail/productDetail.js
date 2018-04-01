$(function(){
	//点击编辑按钮实现编辑功能
	$("#product_edit").click(function(){
		//1.将某些文本框变成编辑状态
		$(".input_editable").attr("disabled",false);
		
		//2.将下拉框变成编辑状态
		for ( var i in $(".input_editable_DropDownList")) {
			if(!isNaN(i)){        //如果i是数字
				var dropdownlist = $($(".input_editable_DropDownList")[i]).data("kendoDropDownList");
				if (typeof(dropdownlist) != "undefined") { 
					dropdownlist.enable(true);
				} 
			}
		}
		
		//3.添加类目中的新增和删除按钮
		$("#productType_create").show();
		$("#productType_delete").show();
		
		//4.将文本编辑框变成可编辑状态
		if(record.attribute==0){    //如果还没有加载
			open_Ken_Editor_Is=false;
		}else{                      //如果已经加载   
			openKenEditor(false);
		}
		
		//5.将单选框（七天无理由退款）变成可编辑状态
		$("#pro_isNoReasonToReturn").attr("disabled",false);
		
	});
});



//打开页面的默认设置
function openPageNormalSet(){
	
	 //1.判断目录版本是否为UniqloCatalog-onlion，如果是它则，没有同步按钮,也没有修改按钮
    if(viewModelBasic.model.catalogueVersions=="UniqloCatalog-online"){
    	$("#product_sync").hide();
    	$("#product_edit").hide(); 
    	$("#product_save").hide();
    }
	 
	 //2.如果当前商品的状态是上架状态，那么去掉商品上架按钮
	 if(viewModelBasic.model.approval=="list"){
		 $("#product_putaway").hide();
	 }
	 	//如果当前商品的状态是下架，那么去掉商品下架的按 钮
	 if(viewModelBasic.model.approval=="delist"){
		 $("#product_down").hide();
	 }
	 
	 //3.将商品类目中的保存和删除按钮隐藏
	 $("#productType_create").hide();
	 $("#productType_delete").hide();
	 
	 //4.设置单选按钮是否选中
	 if(viewModelBasic.model.isNew=="Y"){               //是否新品
		 $("#pro_isNew").attr("checked",true);
	 }else{
		 $("#pro_isNew").attr("checked",false);
	 }
	 
	 if(viewModelBasic.model.inactive=="Y"){           //是否失效
		 $("#pro_inactive").attr("checked",true);
	 }else{
		 $("#pro_inactive").attr("checked",false);
	 }
	 
	 if(viewModelBasic.model.isLimitTime=="Y"){           //是否限时优惠
		 $("#pro_isLimitTime").attr("checked",true);
	 }else{
		 $("#pro_isLimitTime").attr("checked",false);
	 }
	 
	 if(viewModelBasic.model.isFavorable=="Y"){           //是否限时优惠
		 $("#pro_isFavorable").attr("checked",true);
	 }else{
		 $("#pro_isFavorable").attr("checked",false);
	 }
	 
	 if(viewModelBasic.model.isNoReasonToReturn=="Y"){           //是否支持七天退货
		 $("#pro_isNoReasonToReturn").attr("checked",true);
	 }else{
		 $("#pro_isNoReasonToReturn").attr("checked",false);
	 }
	 
}


//选项卡的显示
function showkendoTabStrip(){
	//设置选项卡
    $("#tabstrip").kendoTabStrip({
        animation:  {
            open: {
                effects: "fadeIn"
            }
        },
        select:function(e){                      //点击选项卡中获取
        	var title=$(e.item).attr("title");                //获取选项卡标题中li中的title的属性值
        	switch(title){
        		case "tab_basic":
        			//alert("tab_basic");
        			break;
        		case "tab_attribute":
        			//判断该选项卡是否已经被加载，如果已经加载，那么不重复加载
        			if(record.attribute==0){
        				//alert("null");
        				record.attribute=1;               //将该选项卡的状态从未加载变成已加载
        				getAttribute();
        			}
        			break;
        		case "tab_mutimedia":
        			//判断该选项卡是否已经被加载，如果已经加载，那么不重复加载
            		if(record.mutimedia==0){
            			///alert("null");
            			record.mutimedia=1;                   //将该选项卡的状态从未加载变成已加载
            			getImageInfo();                     //将数据保存到viewModelMultProduct中,并将数据显示到页面中
            		}
        			break;
				case "tab_type":
					//判断该选项卡是否已经被加载，如果已经加载，那么不重复加载
            		if(record.type==0){
            			//alert("null");
            			record.type=1;                   //将该选项卡的状态从未加载变成已加载
            			showShopType();
            		}
        			break;
				case "tab_sku":
					//判断该选项卡是否已经被加载，如果已经加载，那么不重复加载
            		if(record.sku==0){
            			//alert("null");
            			record.sku=1;                   //将该选项卡的状态从未加载变成已加载
            			showSKU();                         //显示sku列表信息
            		}
        			break;
        		default:
        			alert("页面有异常！请程序员更改！");
        	
        	}
        }
    });
}

