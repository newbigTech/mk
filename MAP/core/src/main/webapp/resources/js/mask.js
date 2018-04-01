var _html = "<div id='loading' class='modal-backdrop fade in' style='z-index:99999;width: 100%;opacity:0.8;position: absolute;'>"+
 				"<div class='text-center modal-dialog' style='width: 100%; top:40%;left:0%;'>"+
 				"<div style='display:block;text-align:center;'>" +
 				"<img src='../resources/images/waiting.gif'/>" +
 				"</div>"+
 				" <div style=' font-size: 16px; padding-top: 5px;display:inline;color:#ffffff;text-align:center;'>" +
 				"<div class='mask-msg' style='width:20%;margin-left:40%'>" +
 				"</div>" +
 				"</div>"+
 				"</div>"+
 			"</div>";     
      
    
 function startMask(content,parent){
	var div = $(_html);
	div.find(".mask-msg").append(content);
	var parentItem;
	if(parent){
		if(parent.append){
			parentItem = parent;
		} else {
			parentItem = $(""+parent);
		}
	} else {
		parentItem = $("body");
	}
	parentItem.append(div);  
	
	var t = parentItem.position().top;
	var l = parentItem.position().left;
	var h = parentItem.height();
	var w = parentItem.width();
	$("#loading").css("top",t);
	$("#loading").css("left",l);
	$("#loading").css("height",h);
	$("#loading").css("width",w);
	
 }  
   
 function endMask(){  
    var _mask = $('#loading');  
    if(_mask.length > 0){
    	_mask.remove();
    }     
 }  