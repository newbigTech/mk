//创建和初始化地图函数：
function initMap(city, level) {
  createMap(city, level);//创建地图
  setMapEvent();//设置地图事件
  addMapControl();//向地图添加控件
}

function initializeMarkers(markers, callback, callback2) {
  if (markers) {
    clearOverlays();
    addMapOverlay(markers, callback);//向地图添加覆盖物
    map.addEventListener("tilesloaded", ()=> {
      loadSearch(callback2)
    });
  }
}

function clearOverlays() {
  if(map){
    map.clearOverlays();
  }
  markerArr = [];
};

function createMap(city, level) {
  map = new BMap.Map("map");//设置以哪个id来绘画map，在调用此方法时确保此元素已在body中
  map.centerAndZoom(city, level);//设置中心点坐标以及地图级别
}

function loadSearch(callback2) {
  var bs = map.getBounds();   //获取可视区域
  var bssw = bs.getSouthWest();   //可视区域左下角
  var bsne = bs.getNorthEast();   //可视区域右上角
  var arr = []
  markerArr.forEach((item)=> {
    var lng = item.item.getPosition().lng;
    var lat = item.item.getPosition().lat;
    if (lng >= bssw.lng && lng <= bsne.lng && lat >= bssw.lat && lat <= bsne.lat) {
      arr.push(item.resp);
    }
  });
  callback2 && callback2(arr);
}


function setMapEvent() {
  map.enableScrollWheelZoom();
  map.enableKeyboard();
  map.enableDragging();
  map.enableDoubleClickZoom()
}

function addClickHandler(target, window, resp, callback) {
  target.addEventListener("click", function () {
    target.openInfoWindow(window);
    moveMap(target.getPosition().lng, target.getPosition().lat);
    callback && callback(resp);
  });
}

function moveMap(x, y, code) {
  markerArr.forEach((item)=> {
    if (item.resp.code == code) {
      item.item.openInfoWindow(item.window);
    }
  })
  if (map.getZoom() < 11) {
    map.setZoom(11);
  }
  map.panTo(new BMap.Point(x, y));
}


function addMapOverlay(markers, callback) {
  for (var index = 0; index < markers.length; index++) {
    var point = new BMap.Point(markers[index].longitude, markers[index].latitude);
    if(index<9){
      var myIcon = new BMap.Icon("../images/number/" + (index + 1) + ".png", new BMap.Size(27, 45));
    }else {
      var myIcon = new BMap.Icon("../images/number/point.png", new BMap.Size(7,7));
    }


    //var content = '<div style="margin:0;line-height:20px;padding:2px;">' +
    //  '<img src="../img/baidu.jpg" alt="" style="float:right;zoom:1;overflow:hidden;width:100px;height:100px;margin-left:3px;"/>' +
    //  '地址：北京市海淀区上地十街10号<br/>电话：(010)59928888<br/>简介：百度大厦位于北京市海淀区西二旗地铁站附近，为百度公司综合研发及办公总部。' +
    //  '</div>';
    //
    //var searchInfoWindow = null;
    //searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
    //  title  : "百度大厦",      //标题
    //  width  : 290,             //宽度
    //  height : 105,              //高度
    //  panel  : "panel",         //检索结果面板
    //  enableAutoPan : true,     //自动平移
    //  searchTypes   :[
    //    BMAPLIB_TAB_SEARCH,   //周边检索
    //    BMAPLIB_TAB_TO_HERE,  //到这里去
    //    BMAPLIB_TAB_FROM_HERE //从这里出发
    //  ]
    //});

    var marker = new BMap.Marker(point, {icon: myIcon});
    var opts = {
      width: 200,
      height: 77,
      title: '<div style="background-color: #f00;color: #fff;font-size: 14px;font-weight: 700;line-height: 18px;padding: 7px 10px;">' + markers[index].displayName + '</div>',
      enableMessage: false
    };

    var infoWindow = new BMap.InfoWindow('<div style="background-color: #000;color: #fff;font-size: 12px;line-height: 16px;padding: 7px 10px;">地址：' + markers[index].address+'</div>', opts);

    addClickHandler(marker, infoWindow, markers[index], callback);


    map.addOverlay(marker);               // 将标注添加到地图中
    markerArr.push({item: marker, resp: markers[index], window: infoWindow});
    marker.setAnimation(BMAP_ANIMATION_DROP); //跳动的动画
  }
}
//向地图添加控件
function addMapControl() {
  var scaleControl = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});
  scaleControl.setUnit(BMAP_UNIT_IMPERIAL);
  map.addControl(scaleControl);
  var navControl = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_LEFT, type: BMAP_NAVIGATION_CONTROL_LARGE});
  map.addControl(navControl);
  var overviewControl = new BMap.OverviewMapControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT, isOpen: true});
  map.addControl(overviewControl);
}

// 百度地图API功能
function G(id) {
  return document.getElementById(id);
}

function createSearch() {
  var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
      {
        "input": "suggestId"
        , "location": map
      });

  ac.addEventListener("onhighlight", function (e) {  //鼠标放在下拉列表上的事件
    var str = "";
    var _value = e.fromitem.value;
    var value = "";
    if (e.fromitem.index > -1) {
      value = _value.province + _value.city + _value.district + _value.street + _value.business;
    }
    str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

    value = "";
    if (e.toitem.index > -1) {
      _value = e.toitem.value;
      value = _value.province + _value.city + _value.district + _value.street + _value.business;
    }
    str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
    G("searchResultPanel").innerHTML = str;
  });

  var myValue;
  ac.addEventListener("onconfirm", function (e) {    //鼠标点击下拉列表后的事件
    var _value = e.item.value;
    myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
    G("searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

    setPlace();
  });

  function setPlace() {
    map.clearOverlays();    //清除地图上所有覆盖物
    function myFun() {
      var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
      map.centerAndZoom(pp, 18);
      map.addOverlay(new BMap.Marker(pp));    //添加标注
    }

    var local = new BMap.LocalSearch(map, { //智能搜索
      onSearchComplete: myFun
    });
    local.search(myValue);
  }
}


var map, markerArr = [];

export { initMap, moveMap, initializeMarkers };