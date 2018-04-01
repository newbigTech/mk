$(function(){
	//点击保存按钮
	$("#product_save").click(function(){
		
		var data={
				catalogVersion:catalogueVersions,
				productCode:productCode,
				basic:"",
				attr:"",
				desc:"",
				type:""
		};          //存储要提交的数据
		
		if(record.basic==1){       //如果基本数据已经加载，那么将修改的基本数据放到data中
			//中间固定部门
			var pro_sex=$("input[name=pro_sex]").val();                //性别
			var pro_Name=$("input[name=pro_Name]").val();                //商品名称
			
			//summary表中的数据
			var pro_GDept=$("input[name=pro_GDept]").val();                //G部门
			var pro_intenCode=$("input[name=pro_intenCode]").val();                //集约码
			var pro_freightTemplate=$("input[name=pro_freightTemplate]").val();     //运费模板
			//var pro_remark=$("input[name=pro_remark]").val();               //备注修改取消
			var pro_isNoReasonToReturn=document.getElementById("pro_isNoReasonToReturn").checked;      //获取七天无理由是否选中
			if(pro_isNoReasonToReturn==true){
				pro_isNoReasonToReturn="Y";
			}else{
				pro_isNoReasonToReturn="N";
			}
			
			var pro_subheading=$("input[name=pro_subheading]").val();         //副标题
			var pro_subheadingHref=$("input[name=pro_subheadingHref]").val();       //副标题链接
			var pro_season=$("input[name=pro_Yearseason]").val();                  //上市年份季节
			
			var pro_modelHeight=$("input[name=pro_modelHeight]").val();                  //模特高度
			var pro_model_Size=$("input[name=pro_model_Size]").val();                  //模特尺码
			var pro_description=$("input[name=pro_description]").val();                  //一句话描述
			
			//将数据放到data中
			data.basic={
					sex:pro_sex,                //性别
					name:pro_Name,                //名称
					gDept:pro_GDept,             //G部门
					intenCode:pro_intenCode,                 //集约码
					isNoReasonToReturn:pro_isNoReasonToReturn,     //获取七天无理由是否选中
					deliveryTemplateId:pro_freightTemplate,     //运费模板
					//remark:pro_remark,
					subtitle:pro_subheading,                     //副标题
					subtitleUrl:pro_subheadingHref,              //副标题链接
					listYearSeason:pro_season,                 //上市年份季节
					modelHeight:pro_modelHeight,              //模特高度
					modelSize:pro_model_Size,                       //模特尺码
					introduce:pro_description                   //一句话描述
			}
		}
		if(record.attribute==1){      //如果基本数据已经加载，那么将修改的商品属性数据放到data中
			
			
			//材质和含量
			var pro_texture1=$("input[name=pro_texture1]").val();                  //材质1
			var pro_texture1_Text=$("input[name=pro_texture1]").data("kendoDropDownList").text();//内容
			var pro_texture1Percent=$("input[name=pro_texture1Percent]").val();        //材质1含量
			
			var pro_texture2=$("input[name=pro_texture2]").val();                  //材质2
			var pro_texture2_Text=$("input[name=pro_texture2]").data("kendoDropDownList").text();//内容
			var pro_texture2Percent=$("input[name=pro_texture2Percent]").val();        //材质2含量
			
			var pro_texture3=$("input[name=pro_texture3]").val();                  //材质3
			var pro_texture3_Text=$("input[name=pro_texture3]").data("kendoDropDownList").text();//内容
			var pro_texture3Percent=$("input[name=pro_texture3Percent]").val();        //材质3含量
			
			var pro_texture4=$("input[name=pro_texture4]").val();                  //材质4
			var pro_texture4_Text=$("input[name=pro_texture4]").data("kendoDropDownList").text();//内容
			var pro_texture4Percent=$("input[name=pro_texture4Percent]").val();        //材质4含量
			
			var pro_texture5=$("input[name=pro_texture5]").val();                  //材质5
			var pro_texture5_Text=$("input[name=pro_texture5]").data("kendoDropDownList").text();//内容
			var pro_texture5Percent=$("input[name=pro_texture5Percent]").val();        //材质5含量
			
			data.attr={
					attr1:{
						materi101Value:pro_texture1,
						materi101:pro_texture1_Text,
						materi101content:pro_texture1Percent
					},
					attr2:{
						materi102Value:pro_texture2,
						materi102:pro_texture2_Text,
						materi102content:pro_texture2Percent
					},
					attr3:{
						materi103Value:pro_texture3,
						materi103:pro_texture3_Text,
						materi103content:pro_texture3Percent
					},
					attr4:{
						materi104Value:pro_texture4,
						materi104:pro_texture4_Text,
						materi104content:pro_texture4Percent
					},
					attr5:{
						materi105Value:pro_texture5,
						materi105:pro_texture5_Text,
						materi105content:pro_texture5Percent
					}
			}
			
			//描述表中的字段
			var texteditor_explain=kenEditor_explain.html();       //产品说明
			var texteditor_show=kenEditor_show.html();          //产品展示
			var texteditor_size=kenEditor_size.html();       //产品尺寸
			var texteditor_sizeTable=kenEditor_sizeTable.html();       //产品尺寸表
			
			data.desc={
					description:texteditor_show,
					instruction:texteditor_explain,
					sizeAndTryOn:texteditor_size,
					sizeChart:texteditor_sizeTable
			}
			
		}
		if(record.type==1){           //如果商品类目已经加载，那么执行商品类目的修改
			//获取所有商品类目的Id，打包发送到后台
			data.type=viewModelShopType.model.resp;
		}
		
		//发送保存请求
		saveProductDetail(data);
		
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});


