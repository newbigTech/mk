import React, { Component } from 'react';
import {Panel, Row, Col} from '../components/Layout';
import { TabPanel, Tab } from '../components/Tab';
import Canvax from  '../components/ProductDetail/Canvas';
import ClassificationGoods from  '../components/Category';
import RecommendGoods from  '../components/VRecommend';
import { Link, IndexLink, browserHistory } from 'react-router';
import AlertMap from "../components/AlertMap";
import History from '../components/History';
import PageNotFound from './PageNotFound';
import Counter from '../components/ProductDetail/Counter';
import Icon from '../components/Icon';
import Button from "../components/Button";
import { getProvinces, getCitys, getDistricts } from '../components/Address';
import Radio from "../components/Radio";

class Comment extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectImg: ""
    };
  }

  setCommentPic(src) {
    if (src == this.state.selectImg) {
      this.setState({selectImg: ""});
    } else {
      this.setState({selectImg: src});
      this.refs.showImg.src = Hmall.cdnPath(src)
    }
  }

  render() {
    var { item } = this.props, imgs, flag = this.state.selectImg == "", chooseFlag;
    imgs = item.paths.map((child)=> {
      chooseFlag = this.state.selectImg == child;
      return <img src={Hmall.cdnPath(child)} style={{border:chooseFlag?"solid 2px #ff0000":""}}
                  onClick={()=>{this.setCommentPic(child)}}></img>
    })
    return <li>
      <div className="body_info" style={{display:item.weight==""?"none":""}}>
        <span>体重：{item.weight}公斤</span>
        <span className="height_span">身高：{item.height}厘米</span>
      </div>
      <div className="body_div">
        <div className="comment_div">
          <p className="bold_span">{item.evaluation}</p>
          <div className="show_img" style={{display:item.paths.length==0?"none":""}}>
            {imgs}
          </div>
          <div className="scale_img" style={{
            display:item.paths.length==0?"none":"",
            height:flag?"0px":"305px",
            transform:flag?"scale(0)":"scale(1)",
            marginBottom:flag?"0px":"5px"
            }}>
            <img ref="showImg"></img>
          </div>
          <p className="date_div">{this.props.date}</p>
          <p className="reply_div" style={{display:item.reply?"":"none"}}>回复：{item.reply}</p>
        </div>
        <div className="info_div">
          <p><span className="bold_span">颜色：</span><span className="normal_span">{item.styleText}</span></p>
          <p><span className="bold_span">尺码：</span><span className="normal_span">{item.size}</span></p>
        </div>
        <div className="phone_div">
          {item.mobileNumber}
        </div>
      </div>
    </li>
  }
}

const advicePageSize = 8;
const commentPageSize = 6;

export default class ProductDetail extends Component {


  getIpInfo(deliveryTemplateId) {
    Hmall.loadIpAddress(()=> {
      let ipAddress = returnCitySN["cip"];
      Hmall.setCookie("IP", ipAddress);
      if (Hmall.getCookie("IP") == ipAddress && Hmall.getCookie("IPAddress")) {
        let data = Hmall.getCookie("IPAddress");
        this.setState({
          addressCode: [data.region_id, data.city_id, ""],
          address: data.region + data.city,
          mail_capital: data.region,
          capitalCode: data.region_id,
        });
        this.setFreight(data.region, data.region_id, deliveryTemplateId);
      } else {
        fetch(`${bdService}/pointOfService/getInfoByIp`, {
          method: 'post',
          headers: Hmall.getHeader({"Content-Type": "application/json"}),
          body: JSON.stringify({
            "ip": ipAddress,
          })
        })
            .then(Hmall.convertResponse('json', this))
            .then(json=> {
              if (json.code == "0") {
                this.setState({
                  addressCode: [json.data.region_id, json.data.city_id, ""],
                  address: json.data.region + json.data.city,
                  mail_capital: json.data.region,
                  capitalCode: json.data.region_id,
                });
                Hmall.setCookie("IPAddress", json.data);
                this.setFreight(json.data.region, json.data.region_id, deliveryTemplateId);
              } else {
                console.log("查询IP地址失败")
              }
            })
            .catch(Hmall.catchHttpError());
      }
    }, this)
  }

  getInfo(props) {
    let userID = Hmall.getCookie('userid'), { productCode } = props.location.query, obj = localStorage.getItem("loginDetail");
    Hmall.loadXiaoNeng(productCode)
    if (userID) {
      this.setState({userID});
      //当用户登陆后判断是否被收藏
      fetch(`${urService}/customer/favorite/isFavorite/${productCode}`, {
        headers: Hmall.getHeader()
      })
          .then(Hmall.convertResponse('json', this))
          .then(json=> {
            if (json.success) {
              this.setState({collect_flag: json.success});
            } else if (obj) {
              //当用户在未登陆状态点击收藏按钮在其登录后收藏
              obj = JSON.parse(localStorage.getItem("loginDetail"));
              if (obj.clickType == "addCollect") {
                this.insertCollect(productCode);
                localStorage.removeItem("loginDetail");
              }
            }
          })
          .catch(Hmall.catchHttpError());
      //当用户在未登陆状态点击加入购物车按钮在其登录后加入
      if (obj) {
        obj = JSON.parse(localStorage.getItem("loginDetail"));
        if (obj.clickType == "addCart") {
          this.insertCart(obj);
          localStorage.removeItem("loginDetail");
        }
      }
    }

    //获取快递配送总库存
    this.getAllStock(productCode, true);

    //获取促销信息
    fetch(`${droolsService}/h/sale/calculation/optionByProductCode?productCode=` + productCode)
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          let items = [];
          if (json.resp && json.resp.length) {
            json.resp.map((item, i)=> {
              items.push(item.meaning);
            })
          }
          this.setState({meaning: items});
        })

    //获取二维码图片
    let url = encodeURIComponent(location.href);
    fetch(`${tpService}/thirdParty/share?content=` + url)
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          if(json.resp){
            this.setState({weixin_url: json.resp[0]});
          }
        });

    //获取评价总数
    fetch(`${commentService}/getEvaluationCount/${productCode}`, {
      headers: Hmall.getHeader()
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            this.setState({commentNum: json.total});
          }
        })
        .catch(Hmall.catchHttpError());

    //获取商品详情
    fetch(`${pdService}/product/query/${productCode}`, {
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      })
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          let { success, resp, msgCode } = json;
          if (success) {
            if (msgCode == "PD_PRODUCT_01") {
              this.setState({find_flag: false, displayFlag: true});
              return
            } else {
              if (msgCode == "PD_PRODUCT_02") {
                this.setState({approval: "off"});
              }
              let { summary={}, row, mainPicLarge, colorPic, mainPic, unity, modelPic, detailPic, desc,stockLevele } = resp[0],
                  {  name, deliveryTemplateId } = summary,
                  state = {
                    mainPic: mainPic ? mainPic : [],
                    mainPicLarge,
                    colorPic,
                    unity,
                    modelPic,
                    detailPic,
                    desc,
                    deliveryTemplateId,
                    summary,
                    productCode,
                    row,
                    displayFlag: true,
                    stockRange: stockLevele,
                  };
              if (userID) {
                //判断cookie中是否有地址信息
                if (Hmall.getCookie('AddressId')) {
                  let dataId = Hmall.getCookie("AddressId"), data = Hmall.getCookie("Address");
                  this.setState({
                    addressCode: [dataId[0], dataId[1], dataId[2]],
                    address: data[1] + data[2],
                    mail_capital: data[0],
                    capitalCode: dataId[0],
                  });
                  this.setFreight(data[0], dataId[0], deliveryTemplateId);
                } else {
                  //根据地址铺中默认地址来设定门店自提与快递配送的默认地址
                  fetch(`${urService}/getUser?access_token=${Hmall.getCookie('access_token')}`, {
                    headers: Hmall.getHeader({})
                  }).then(Hmall.convertResponse('json', this))
                      .then(json=> {
                        let province, city, district, pid = json.state, cid = json.city, did = json.district;
                        if (pid == null && cid == null && did == null) {
                          this.getIpInfo();
                        } else {
                          getProvinces(provinces=> {
                            province = provinces[json.state];
                            getCitys(json.state, citys => {
                              city = citys[json.city];
                              getDistricts(json.city, districts=> {
                                district = districts[json.district];
                                this.setState({
                                  addressCode: [json.state, json.city, json.district],
                                  address: city + district,
                                  mail_capital: province,
                                  capitalCode: json.state,
                                });
                                Hmall.setCookie("Address", [province, city, district], {
                                  path: '/'
                                });
                                Hmall.setCookie("AddressId", [pid, cid, did], {
                                  path: '/'
                                });
                                this.setFreight(province, json.state, deliveryTemplateId);
                              })
                            });
                          })
                        }
                      }).catch(Hmall.catchHttpError());
                }
              } else {
                //根据IP地址得到省市信息
                this.getIpInfo();
              }
              //将此商品信息加入浏览记录
              Hmall.addBrowserHistory({
                productCode,
                mainPic: mainPic ? mainPic[0] : "",
                productName: name,
                price: row[0] && row[0].price
              });
              this.setState(state);
              var anchor = document.getElementById("anchor");
              var top = anchor.offsetTop + anchor.offsetParent.offsetTop;
              window.addEventListener("scroll", ()=> {
                this.handleScroll(top)
              }, true);
            }

          } else {
            alert("查询失败");
          }
        })
        .catch(Hmall.catchHttpError());
  }


  componentWillMount() {
    getProvinces(provinces => {
      this.setState({provinces});
    });
    this.getInfo(this.props);
  }

  handleScroll(top) {
    if (document.getElementById("anchor")) {
      var anchor = document.getElementById("anchor");
      if (document.body.scrollTop >= top - 45) {
        anchor.className = `anchor_ul fixed_ul`;
      } else {
        anchor.className = "anchor_ul"
      }
    }

  }


  componentWillReceiveProps(nextProps) {
    var productCode = nextProps.location.query.productCode;
    if (productCode != this.state.productCode) {
      this.setState(this.getInitState());
      this.getInfo(nextProps);
    }
  }

  componentWillUnmount() {
    window.removeEventListener('scroll', this.handleScroll());
    this.isUnMounted = true;
  }

  getInitState() {
    return {
      userID: null,
      meaning: [],
      stockRange:{},
      deliveryTemplateId: "1",
      startFees: "9.00",
      productCode: "",
      productId: "",
      commentNum: 0,
      adviceNum: 0,
      commentPage: 1,
      advicePage: 1,
      showFlag: false,
      mouseTop: 0,
      mouseLeft: 0,
      picTop: 0,
      picLeft: 0,
      summary: {},
      "mainPicLarge": [],
      "colorPic": [],
      "mainPic": [],
      "unity": "/172286000/unity/480",
      resource: {"productCode": "", "showPics": [], "unity": ""},
      approval: "on",
      row: [{
        "productId": "172286000-1",
        "styleText": "011白色",
        "creationTime": 1489582539236,
        "priceType": "VaryPriceGroup",
        "pic": "/172286000/sku/561/green01.jpg",
        "version": 0,
        "platform": "3028db94-1054-4427-8e83-19d036df6a62",
        "barCode": "172286000-1-barCode",
        "uid": "49a882cb-1298-4361-8adf-39bb8b5ceb33",
        "productCode": "172286000",
        "size": "165/84A/M",
        "price": "145.00",
        "name": "毛绒外套女长袖立领外衣艾米恋冬季百搭拉链女装加厚摇粒绒短外套",
        "style": "白色",
        "currency": "RMB",
        "colorNo": "COL04",
        "sizeNo": "SMA004"
      }],
      "modelPic": [
        {
          "uid": "172286000-model-pc-1",
          "productCode": "172286000",
          "rank": 1,
          "type": "model-pc",
          "version": 0,
          "url": [
            "/172286000/model/rank1/first/591-822/1.jpg",
            "/172286000/model/rank1/other1/196-273/2.jpg",
            "/172286000/model/rank1/other2/196-273/3.jpg",
            "/172286000/model/rank1/other3/196-273/4.jpg"
          ]
        }
      ],
      "detailPic": [],
      "desc": {
        "uid": "0f700083-4bbd-49f1-9260-b7e05ff8d491",
        "productCode": "172286000",
        "instruction": "此商品无产品说明",
        "sizeChart": "此商品无产品尺寸",
        "description": "此商品无产品展示",
        "sizeAndTryOn": "此商品无产品尺寸",
        "version": 0,
        "points": "此商品无温馨提示"
      },
      weixin_url: "",
      distribution: "EXPRESS",
      slideHeight: 0,
      find_flag: true,
      displayFlag: false,
      size_class: "",
      store_class: "",
      change_flag: false,
      capital_flag: false,
      quantity: 1,
      address: "北京北京市",
      storeName: "",
      storeCode: "",
      AddressChoose: [0, 0, 0],
      stock: 1000,
      mail_capital: "北京",
      capitalCode: "110000",
      img_selected: 0,
      color_src: "",
      change_src_flag: false,
      start_code: 0,
      end_code: 5,
      size_choose: 0,
      color_choose: 0,
      show_unity: false,
      collect_flag: false,
      advices: [],
      comment: [],
      stockFlag: false,
      adviceFlag: true,
      loadingAdvices: true,
      loadingComment: true,
      addressCode: ["110000", "110100", ""],
      loadingStock: false,
      adviceButtonFlag: false,
      buyFlag: false,
      addCartFlag: false,
      anchor_choose: "",
      promotion_flag: true,
      promotion_expand: false,
    };
  }

  constructor(props) {
    super(props);
    this.state = this.getInitState();
  }

  getAllStock(productCode, flag) {
    let obj;
    if (flag) {
      obj = {
        "distribution": "EXPRESS",
        "productCode": productCode
      }
    } else {
      if (this.state.storeCode == "") {
        obj = {
          "distribution": "PICKUP",
          "productCode": productCode
        }
      } else {
        obj = {
          "distribution": "PICKUP",
          "productCode": productCode,
          "storeId": this.state.storeCode
        }
      }
    }
    fetch(`${stService}/stock/query`, {
      method: 'post',
      headers: Hmall.getHeader({"Content-Type": "application/json"}),
      body: JSON.stringify(obj)
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            this.setState({stock: json.resp[0].stock});
          }else {
            alert(json.msg);
            this.setState({ stock: 0 });
          }
        })
        .catch(Hmall.catchHttpError());
  }


  returnCode(pCode, calback) {
    getCitys(pCode, citys => {
      calback && calback([pCode, Object.keys(citys)[0], ""]);
    });
  }

  insertCart(obj) {
    this.setState({addCartFlag: true});
    fetch(`${ctService}/cart/insert`, {
      method: 'post',
      headers: Hmall.getHeader({"Content-Type": "application/json"}),
      body: JSON.stringify([obj])
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          this.setState({addCartFlag: false});
          if (json.success) {
            Hmall.Storage.get('miniCart').show(true);
          } else {
            alert("添加出错！");
          }
        }).catch(Hmall.catchHttpError());
  }

  addCart() {
    var style = "", styleAddress = "", code = "", store = "", obj,
        {distribution, productId, capitalCode, storeCode, storeName, addressCode, productCode, quantity}=this.state,
        url = "/product-detail.html?productCode=" + this.state.productCode,
        callback = (styleAddress) => {
          obj = {
            productCode: productCode,
            productId: productId,
            quantity: quantity,
            storeName: store,
            address: styleAddress,
            distribution: style,
            distributionId: code,
            clickType: "addCart",
          };
          if (this.state.userID == null) {
            obj.redirect = url;
            localStorage.setItem("loginDetail", JSON.stringify(obj));
            browserHistory.push(`login.html?redirect_url=${url}`);
          } else {
            this.insertCart(obj);
          }
        };
    if (distribution == "EXPRESS") {
      if (productId == "") {
        alert("请选择尺码与颜色！");
        return;
      } else {
        style = "EXPRESS";
        this.returnCode(capitalCode, callback);
        return;
      }
    } else {
      if (productId == "" || storeCode == "") {
        alert("请选择尺码,颜色与门店！");
        return
      } else {
        style = "PICKUP";
        styleAddress = addressCode;
        code = storeCode;
        store = storeName;
      }
    }
    callback(styleAddress);

  }

  changeVal(event) {
    var val = event.target.value;
    if (!isNaN(val) && val > 0) {
      this.setState({quantity: parseInt(val)});
    } else if (val == "") {
      this.setState({quantity: ""});
    }
  }

  checkVal(e) {
    var val = e.target.value;
    if (val == "") {
      this.setState({quantity: 1});
    }
  }

  add() {
    var num = parseInt(this.state.quantity) + 1;
    this.setState({quantity: num});

  }

  minus() {
    var num = parseInt(this.state.quantity) - 1;
    if (num > 0) {
      this.setState({quantity: num});
    }
  }

  fetchStock(color, size, storeCode = this.state.storeCode, callback) {
    var chooseItem, obj;
    this.state.row.map((item, i)=> {
      if (item.styleText == color && item.size == size) {
        chooseItem = item.productId;
      }
    })
    this.setState({productId: chooseItem, loadingStock: true});
    if (this.state.distribution == "EXPRESS") {
      obj = {
        productId: chooseItem,
        "distribution": "EXPRESS",
        "productCode": this.state.productCode
      }
    } else {
      if (this.state.storeCode != "") {
        obj = {
          productId: chooseItem,
          "distribution": "PICKUP",
          "productCode": this.state.productCode,
          "storeId": storeCode
        }
      } else {
        obj = {
          productId: chooseItem,
          "distribution": "PICKUP",
          "productCode": this.state.productCode,
        }
      }
    }
    fetch(`${stService}/stock/query`, {
      method: 'post',
      headers: Hmall.getHeader({"Content-Type": "application/json"}),
      body: JSON.stringify(obj)
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            this.setState({stock: json.resp[0].stock, loadingStock: false});
            callback && callback();
          }
        }).catch(Hmall.catchHttpError());
  }

  sizeChoose(size) {
    var choose = this.state.size_choose;
    if (choose == size) {
      this.setState({size_choose: "", productId: ""});
      if (this.state.color_choose != "") {
        this.getAllStock(this.state.productCode, true);
      }
    } else {
      this.setState({size_choose: size});
      if (this.state.color_choose != "") {
        this.fetchStock(this.state.color_choose, size);
      }
    }

  }

  colorChoose(styleText, picSmall) {
    var style = this.state.color_choose;
    if (style == styleText) {
      this.setState({color_choose: "", productId: ""});
      if (this.state.size_choose != "") {
        this.getAllStock(this.state.productCode, true);
      }
    } else {
      this.setState({
        color_choose: styleText,
        color_src: picSmall,
        change_src_flag: true,
        show_unity: false,
        img_selected: -1
      });
      if (this.state.size_choose != "") {
        this.fetchStock(styleText, this.state.size_choose);
      }
    }
  }

  insertCollect(productCode) {
    fetch(`${urService}/customer/favorite/insert/` + productCode, {
      method: 'post',
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            this.setState({collect_flag: true});
          }
        })
  }

  addToCollect(productCode = this.state.productCode) {
    if (this.state.userID != null) {
      if (this.state.collect_flag) {
        fetch(`${urService}/customer/favorite/delete/` + productCode, {
          method: 'post',
          headers: Hmall.getHeader({
            "Content-Type": "application/json"
          }),
        }).then(Hmall.convertResponse('json', this))
            .then(json=> {
              if (json.success) {
                this.setState({collect_flag: false});
              }
            })
      } else {
        this.insertCollect(productCode);
      }
    } else {
      var url = encodeURIComponent("/product-detail.html?productCode=" + this.state.productCode);
      localStorage.setItem("loginDetail", JSON.stringify({clickType: "addCollect", redirect: url}));
      browserHistory.push(`login.html?redirect_url=${url}`);
    }
  }

  getStyle(label) {
    var style;
    switch (label.position) {
      case "TR":
        style = {position: "absolute", top: "5px", right: "5px"};
        break;
      case "TL":
        style = {position: "absolute", top: "5px", left: "5px"};
        break;
      case "BR":
        style = {position: "absolute", bottom: "5px", right: "5px"};
        break;
      case "BL":
        style = {position: "absolute", bottom: "5px", left: "5px"};
        break;
    }
    return style;
  }

  buyNow() {
    var style = "", code, timestamp = new Date().getTime(), obj,
        { distribution,productId,storeCode,userID,productCode,productId,quantity,color_choose,size_choose,stock }=this.state;
    if (distribution == "EXPRESS") {
      if (productId == "") {
        alert("请选择尺码与颜色！");
        return
      } else {
        style = "EXPRESS";
        code = "";
      }
    } else {
      if (productId == "" || storeCode == "") {
        alert("请选择尺码,颜色与门店！");
        return
      } else {
        style = "PICKUP";
        code = storeCode;
      }
    }
    obj = {
      "distribution": style,
      "distributionId": code,
      "productCode": productCode,
      "productId": productId,
      "quantity": quantity,
      clickType: "buyNow",
      redirect: "/product-detail.html?productCode=" + this.state.productCode,
    };
    if (this.state.userID == null) {
      localStorage.setItem("loginDetail", JSON.stringify(obj));
      var url = encodeURIComponent("/product-detail.html?productCode=" + this.state.productCode);
      browserHistory.push(`login.html?redirect_url=${url}`);
    } else {
      this.setState({buyFlag: true});
      var callback = ()=> {
        if (stock > quantity) {
          var settlementArr = {
            "userId": userID,
            "creationTime": timestamp,
            "products": [obj],
          };
          fetch(`${odService}/order/createTempOrders`, {
            method: "post",
            headers: Hmall.getHeader({
              "Content-Type": "application/json"
            }),
            body: JSON.stringify({
              map: settlementArr
            })
          })
              .then(Hmall.convertResponse('json', this))
              .then(json => {
                this.setState({buyFlag: false});
                let {success , resp ,msgCode} = json
                if (success) {
                  browserHistory.push({pathname: '/payment/settlement.html', state: {tempIds: resp, flashCart: false}});
                } else {
                  if (msgCode == "OD_BLACKUSER") {
                    alert("你是黑名单用户，不能下单！！！")
                  } else {
                    alert("生成临时订单失败！" + msgCode);
                  }
                }
              }).catch(Hmall.catchHttpError());
        }
      }
      this.fetchStock(color_choose, size_choose, this.state.storeCode, callback);

    }
  }

  confirm(addressCode, address, storeName, storeCode, stock) {
    this.setState({
      addressCode: addressCode,
      address: address,
      storeName: storeName,
      storeCode: storeCode,
      stock: stock
    });
    this.hideMap();
  }

  cancel() {
    this.hideMap();
  }

  hideMap() {
    this.setState({store_class: "store_fade_in"});
  }

  showMap(flag) {
    this.setState({
      store_class: "store_fade_out",
      stockFlag: flag
    });
  }


  fetchAdvice(page) {
    this.setState({
      loadingAdvices: true
    });
    fetch(`${commentService}/getPurchaseAdvice/` + this.state.productCode, {
      method: 'post',
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify({
        "page": page,
        "pageSize": advicePageSize
      })
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            this.setState({
              advices: json.resp,
              loadingAdvices: false,
              adviceNum: parseInt(json.total),
              advicePage: parseInt(page),
            });
          } else {
            this.setState({
              loadingAdvices: false
            });
          }
        })
  }

  formatDate(time, flag) {
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    if (flag) {
      return y + "-" + m + "-" + d
    } else {
      return y + "." + m + "." + d
    }
  }

  getAdvices() {
    let replyDate, creationDate, advices = this.state.advices;
    if (this.state.loadingAdvices) {
      return <div className="loading"></div>
    }
    if (advices && advices.length != 0) {
      return advices.map((item)=> {
        replyDate = this.formatDate(new Date(item.replyTime), true);
        creationDate = this.formatDate(new Date(item.creationTime), true);
        return (<li key={item.creationTime}>
          <div className="div_first">
            <span className="consult_span">咨询：</span>
            <span className="consult_span">{item.purchaseAdvice}</span>
            <span> ({item.mobileNumber})</span>
            <span className="date_span">{creationDate}</span>
          </div>
          <div className="div_reply" style={{display:item.reply==""?"none":""}}>
            <span>回复：{item.reply}</span>
            <span className="date_span">{replyDate}</span>
          </div>
        </li>)
      })
    } else {
      return <div className="nothing_find">当前商品还无人咨询喔!</div>
    }
  }

  submitAdvice() {
    if (this.state.userID != null) {
      var advice = this.refs.advice.value;
      if (advice != "") {
        this.setState({adviceButtonFlag: true});
        fetch(`${commentService}/purchaseAdvice`, {
          method: 'post',
          headers: Hmall.getHeader({
            "Content-Type": "application/json"
          }),
          body: JSON.stringify({
            productCode: this.state.productCode,
            purchaseAdvice: advice
          })
        }).then(Hmall.convertResponse('json', this))
            .then(json=> {
              this.setState({adviceButtonFlag: false});
              if (json.success) {
                alert("咨询成功！请等候客服回应!");
                this.refs.advice.value = "";
              }
            })
      } else {
        alert("请输入字符！");
      }
    } else {
      alert("请先登录！");
    }

  }

  fetchComment(page) {
    this.setState({
      loadingComment: true
    });
    fetch(`${commentService}/getEvaluation/` + this.state.productCode, {
      method: 'post',
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify({
        "page": page,
        "pageSize": commentPageSize
      })
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            this.setState({
              comment: json.resp,
              loadingComment: false,
              commentNum: parseInt(json.total),
              commentPage: parseInt(page),
            });
          } else {
            this.setState({
              loadingComment: false
            });
          }
        })
  }

  getComment() {
    let { loadingComment,comment } = this.state;
    if (loadingComment) {
      return <div className="loading"></div>
    } else {
      if (comment && comment.length > 0) {
        return comment.map((item)=> {
          var createTime = this.formatDate(new Date(item.creationTime), false);
          return <Comment item={item} date={createTime}></Comment>
        })
      } else {
        return <div className="nothing_find">当前商品还无人评价喔！</div>
      }
    }
  }

  setCommentPage() {
    var items = [], {commentNum,commentPage}=this.state,
        allPage = Math.ceil(commentNum / commentPageSize);
    if (commentPage == 1) {
      items.push(<li key="last" className="unable_li">&lt; 上一页</li>);
    } else {
      items.push(<li key="last" onClick={()=>{this.fetchComment(commentPage-1)}}>&lt; 上一页</li>);
    }
    for (var i = 1; i <= allPage; i++) {
      if (i <= 3) {
        if (i == commentPage) {
          items.push(<li key={i} className="unable_li" key={i}>{i}</li>);
        } else {
          let num = i;
          items.push(<li key={i} onClick={()=>{this.fetchComment(num)}} key={i}>{i}</li>);
        }
      } else {
        item.push(<li className="no_border">···</li>);
      }
    }
    if (commentPage == allPage) {
      items.push(<li key="next" className="unable_li">下一页 &gt;</li>);
    } else {
      items.push(<li key="next" onClick={()=>{this.fetchComment(commentPage+1)}}>下一页 &gt;</li>);
    }
    return items;
  }

  setAdvicePage() {
    var items = [], {adviceNum,advicePage}=this.state,
        allPage = Math.ceil(adviceNum / advicePageSize);
    if (advicePage == 1) {
      items.push(<li className="unable_li">&lt; 上一页</li>);
    } else {
      items.push(<li onClick={()=>{this.fetchAdvice(advicePage-1)}}>&lt; 上一页</li>);
    }
    for (var i = 1; i <= allPage; i++) {
      if (i <= 3) {
        if (i == advicePage) {
          items.push(<li key={i} className="unable_li">{i}</li>);
        } else {
          let num = i;
          items.push(<li key={i} onClick={()=>{this.fetchAdvice(num)}}>{i}</li>);
        }
      } else {
        item.push(<li className="no_border">···</li>);
      }
    }
    if (advicePage == allPage) {
      items.push(<li className="unable_li">下一页 &gt;</li>);
    } else {
      items.push(<li onClick={()=>{this.fetchAdvice(advicePage+1)}}>下一页 &gt;</li>);
    }
    return items;
  }

  setFreight(name, key, deliveryTemplateId = this.state.deliveryTemplateId) {
    this.setState({mail_capital: name, capitalCode: key, capital_flag: false,});
    fetch(`${bdService}/zoneDeliveryModeValue/selectForStartFees`, {
      method: 'post',
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify({
        deliveryTemplateId: deliveryTemplateId, state: key
      })
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          this.setState({startFees: json.resp[0].startFees})
        })
  }

  setCapital() {
    let { provinces, capitalCode } = this.state,
        items = [],
        block_style = {
          backgroundColor: "#ff0000",
          color: "#ffffff"
        };
    for (let key in provinces) {
      items.push(<li style={key==capitalCode ? block_style : null} key={key}
                     onClick={()=>{this.setFreight(provinces[key],key)}}>{provinces[key]}</li>)
    }
    return items;
  }

  initializeChoose(distribution) {
    this.setState({distribution: distribution, capital_flag: false, size_choose: 0, color_choose: 0, productId: ""});
    this.getAllStock(this.state.productCode, distribution == "EXPRESS");
  }


  img_up() {
    let { start_code,end_code,img_selected,slideHeight } = this.state;
    if (start_code > 0) {
      if (img_selected == end_code) {
        this.setState({img_selected: end_code - 1});
      }
      this.setState({start_code: start_code - 1, end_code: end_code - 1, slideHeight: slideHeight + 88});
    }
  }

  img_down() {
    let { start_code, end_code, img_selected, mainPic, slideHeight } = this.state;

    if (end_code <= mainPic.length) {
      if (img_selected == start_code) {
        this.setState({img_selected: start_code + 1});
      }
      this.setState({start_code: start_code + 1, end_code: end_code + 1, slideHeight: slideHeight - 88});
    }
  }

  getImages() {
    var imgs = [], { mainPic, img_selected } = this.state;
    mainPic.map((item, i)=> {
      imgs.push(
          <li className={(i==img_selected?"content_1":"")} key={i}
              onMouseOver={()=>{this.setState({img_selected:i,change_src_flag:false,show_unity:false})}}>
            <img src={Hmall.cdnPath(item)}/>
          </li>);
    })

    return imgs;
  }

  getDetailPic() {
    var items = [], { detailPic }=this.state;
    if (detailPic) {
      for (var i = 0; i < detailPic.length; i += 2) {
        if ((i + 1) < detailPic.length) {
          items.push(<tr>
            <td rowSpan="1" width="50%">
              <img src={Hmall.cdnPath(detailPic[i])}/>
            </td>
            <td>
              <img src={Hmall.cdnPath(detailPic[i+1])}/>
            </td>
          </tr>)
        } else {
          items.push(<tr>
                <td rowSpan="1" width="50%">
                  <img src={Hmall.cdnPath(detailPic[i])}/>
                </td>
                <td>
                </td>
              </tr>
          )
        }
      }
    }
    return items;
  }

  getModelPic() {
    var { modelPic } = this.state, items = [];
    if (modelPic) {
      modelPic.map((child, i)=> {
        child.url.map((item, i)=> {
          if (i == 1) {
            items.push(
                <tr>
                  <td rowSpan="3" width="74.97%">
                    <img src={Hmall.cdnPath(child.url[0])}/>
                  </td>
                  <td>
                    <img src={Hmall.cdnPath(child.url[1])}/>
                  </td>
                </tr>
            )
          }
          if (i > 1) {
            items.push(
                <tr>
                  <td>
                    <img src={Hmall.cdnPath(item)}/>
                  </td>
                </tr>
            )
          }

        })
      })
    }
    return items;
  }

  expandPromotion() {
    let promotiom = this.refs.promotion;
    this.setState({promotion_expand: true});
    promotiom.onmouseleave = ()=>this.closePromotion();
  }

  closePromotion() {
    let promotiom = this.refs.promotion, { promotion_expand } = this.state;
    if (promotion_expand) {
      this.setState({promotion_expand: false});
    }
    promotiom.onmouseleave = ()=>null;
  }

  getMeaning() {
    let { meaning } = this.state, items = [];
    if (meaning.length > 0) {
      meaning.map((item, i)=> {
        items.push(
            <li key={i}>{item}</li>
        )
      })
    } else {
      items.push(<li>当前无促销活动，敬请期待!</li>)
    }
    return items;
  }

  getImgSrc() {
    let imgSrc = "", { mainPic } = this.state;
    if (mainPic && mainPic.length > 0) {
      mainPic.forEach((item, i)=> {
        if (i == this.state.img_selected) {
          imgSrc = item;
        }
      });
    }
    return imgSrc;
  }

  openSizeForm() {
    document.body.style.marginRight = "17px";
    document.body.style.overflowY = 'hidden';
    this.setState({size_class: "size_fade_out"});
  }

  closeSizeForm() {
    setTimeout(function () {
      document.body.style.marginRight = "0px";
      document.body.style.overflowY = 'auto';
    }, 500, this);
    this.setState({size_class: "size_fade_in"});
  }

  MoverByMouse(e) {
    var {detailImg} =this.refs, parent = detailImg.offsetParent, left, top,
        largePic = this.refs.largePic, cursor = this.refs.cursor, { mainPicLarge }=this.state;

    if (mainPicLarge && mainPicLarge.length > 0) {

      left = e.pageX - (detailImg.offsetLeft + parent.offsetLeft) + 1;
      top = e.pageY - (detailImg.offsetTop + parent.offsetTop) + 1;

      largePic.style.left = (-25 * (left > 120 ? left > 360 ? 240 : left - 120 : 0) / 12) + "px";
      largePic.style.top = (-25 * (top > 120 ? top > 360 ? 240 : top - 120 : 0) / 12) + "px";

      cursor.style.top = (top > 120 ? top > 360 ? 240 : top - 120 : 0) + "px";
      cursor.style.left = (left > 120 ? left > 360 ? 240 : left - 120 : 0) + "px"

      this.setState({showFlag: true});
    }
  }

  getStockInfo(){
   let { stockRange,stock }=this.state, { highStock_moreThan,lowStock_lessThan,lowStock_moreThan,outOfStock_lessThan } = stockRange;
    if( stock<= outOfStock_lessThan ){
      return "库存缺货";
    }else if(stock>=lowStock_moreThan&&stock<=lowStock_lessThan) {
      return "库存紧张";
    }else if(stock>=highStock_moreThan){
      return "库存充足";
    }
  }


  render() {

    var colorsArr = [];
    var sizeColorArr = [];

    if (this.state.size_choose != 0) {
      this.state.row.map((item, i)=> {
        if (item.size == this.state.size_choose && sizeColorArr.indexOf(item.styleText) == -1) {
          sizeColorArr.push(item.styleText)
        }
      })
    }

    var colors = this.state.row.map((item, i)=> {
      if (colorsArr.indexOf(item.styleText) == -1) {
        colorsArr.push(item.styleText);
        if (this.state.size_choose != 0 && sizeColorArr.indexOf(item.styleText) == -1) {
          return (
              <li className="color_li no_color_li" key={i}>
                <div className="flow_div">
                  <div className="content_div">{item.styleText}</div>
                </div>
                <img src={Hmall.cdnPath(item.pic)}/>
              </li>
          )
        } else {
          return <li className="color_li" onClick={()=>{this.colorChoose(item.styleText,item.pic)}} key={i}>
            <div className="flow_div">
              <div className="content_div">{item.styleText}</div>
            </div>
            <img style={{border:this.state.color_choose==item.styleText?"solid 1px #ff0000":"solid 1px #dadada"}}
                 src={Hmall.cdnPath(item.pic)}/>
            <div className="img_choose" style={{display:this.state.color_choose==item.styleText?"":"none"}}></div>
          </li>;
        }
      }
    })

    var sizeArr = [];
    var colorSizeArr = [];
    if (this.state.color_choose != 0) {
      this.state.row.map((item, i)=> {
        if (item.styleText == this.state.color_choose && colorSizeArr.indexOf(item.size) == -1) {
          colorSizeArr.push(item.size);
        }
      })
    }

    var sizes = this.state.row.map((item, i)=> {
      if (sizeArr.indexOf(item.size) == -1) {
        sizeArr.push(item.size);
        if (this.state.color_choose != 0 && colorSizeArr.indexOf(item.size) == -1) {
          return (
              <div className="size_detail no_size_detail" key={i}>
                {item.size}
              </div>
          )
        } else {
          return (
              <div className="size_detail" key={i}
                   style={{border:this.state.size_choose==item.size?"solid 1px #ff0000":""}}
                   onClick={()=>{this.sizeChoose(item.size)}}>
                {item.size}
                <div className="img_choose" style={{display:this.state.size_choose==item.size?"":"none"}}></div>
              </div>);
        }
      }
    })

    if (this.state.productId != "") {
      this.state.row.map((item, i)=> {
        if (item.productId == this.state.productId) {
          price = item.price;
        }
      })
    }


    var url = encodeURIComponent(location.href), price, distribution,
        { change_src_flag,showFlag,img_selected,mainPicLarge, capital_flag, storeCode, productId ,change_flag, stock, quantity, loadingStock, start_code, end_code, mainPic, promotion_expand, meaning,summary } = this.state,
        { isNoReasonToReturn , minPrice, maxPrice, isPickup, sex, code, name } = summary,
        over_stock = stock < quantity, label, style;

    if (this.state.distribution == "EXPRESS") {
      distribution = <div className="dispatch_span">
        <span className="bold_span">配送至 :</span><span className="span_black">{this.state.mail_capital}</span>
        <Icon name="address-open" onClick={()=>{this.setState({capital_flag:!capital_flag})}}></Icon>
        <span className="span_black">快递{this.state.startFees}元</span>
        <div className="choose_city"
             style={{top:capital_flag?"20px":"70px",opacity:capital_flag?"1":"0",visibility:capital_flag?"visible":"hidden"}}>
          <Icon name="address-close" onClick={()=>{this.setState({capital_flag:false})}}></Icon>
          <ul>
            {this.setCapital()}
          </ul>
        </div>
      </div>
    } else {
      distribution = <div className="dispatch_span">
        <span className="bold_span">选择店铺 :</span><span className="span_store">{this.state.address}</span>
        <span className="span_choose"
              onClick={(e)=>{this.showMap(false)}}>{this.state.storeName == "" ? "选择门店" : this.state.storeName}</span>
        <Link to={"/search.html?storeCode="+storeCode} className="span_store"
              style={{display: storeCode==""?"none":""}}>查看该店所有商品</Link>
      </div>
    }


    switch (this.state.summary.label) {
      case "new_product":
        style = this.getStyle(CMSConfig.new_product);
        label = <img style={style} src={Hmall.cdnPath(CMSConfig.new_product.urlPath)}/>
        break;
      case "concessional_rate":
        style = this.getStyle(CMSConfig.concessional_rate);
        label = <img style={style} src={Hmall.cdnPath(CMSConfig.concessional_rate.urlPath)}/>;
        break;
      case "time_doptimal":
        style = this.getStyle(CMSConfig.time_doptimal);
        label = <img style={style} src={Hmall.cdnPath(CMSConfig.time_doptimal.urlPath)}/>
        break;
    }

    if (this.state.displayFlag) {
      if (this.state.find_flag) {
        return (
            <div>
              <Panel className="div_detail">
                <div className={"div_sizeForm "+(this.state.size_class)}>
                  <div className="size_help">
                    <Icon name="size-close" onClick={()=>{this.closeSizeForm()}}></Icon>
                    <div className="size_div">
                      <div dangerouslySetInnerHTML={{__html:this.state.desc.sizeChart}}></div>
                      <div className="introduced_div" onClick={()=>{this.setState({change_flag:!change_flag})}}>
                        查看产品丈量方法说明
                        <Icon name="size-open" style={{transform:change_flag?"rotate(180deg)":"rotate(0deg)"}}></Icon>
                      </div>
                      <div style={{display:this.state.change_flag ? null:"none"}}>
                        <div className="shop_sizeForm change">
                          产品丈量方法说明
                        </div>
                        <div className="method_div">
                          实际尺寸的测量方法
                        </div>
                        <img className="method_img" src="/images/ProductDetail/measure.png"/>
                      </div>
                    </div>
                  </div>
                </div>
                <AlertMap store_class={this.state.store_class} show_flag={false} defaultCode={this.state.storeCode}
                          defaultAddress={this.state.addressCode} productCode={this.state.productCode}
                          productId={this.state.productId}
                          confirm={(addressCode,address,storeName,storeCode,stock)=>{this.confirm(addressCode,address,storeName,storeCode,stock)}}
                          cancel={()=>{this.cancel()}} show_stock={this.state.stockFlag}/>
                <Row>
                  <Col width={500} className="detail_div">
                    {this.state.show_unity ? <Canvax url={Hmall.cdnPath(this.state.unity)}></Canvax> :
                        <div onMouseMove={(e)=>{this.MoverByMouse(e)}}
                             onMouseOut={()=>{this.setState({showFlag:false})}} ref="detailImg">
                          {label}
                          <img
                              src={change_src_flag?Hmall.cdnPath(this.state.color_src):Hmall.cdnPath(this.getImgSrc())}
                              className="detail_img"/>
                          <div className="cursor-div"
                               style={{display:showFlag&&!change_src_flag&&mainPicLarge&&mainPicLarge.length>0?"":"none"}}
                               ref="cursor"></div>
                        </div>
                    }
                  </Col>
                  <Col width={80} className="img_div">
                    <div className={"img_order"+(start_code==0?" img_order_unClick":"")}
                         onClick={()=>{this.img_up()}}></div>
                    <div className="backdrop">
                      <ul style={{top:this.state.slideHeight+"px"}}>
                        {this.getImages()}
                        <li style={{border: this.state.show_unity?"solid 2px #ff0000":""}}
                            onClick={()=>{this.setState({show_unity:true,img_selected:-1})}}></li>
                      </ul>
                    </div>
                    <div className={"img_order"+(end_code > mainPic.length?" img_order_unClick":"")}
                         onClick={()=>{this.img_down()}}></div>
                  </Col>
                  <Col className="good_detail">
                    {
                      mainPicLarge && mainPicLarge.length > 0 ?
                          <div className="enlarge-div" style={{display:showFlag&&!change_src_flag?"":"none",}}>
                            <img src={Hmall.cdnPath(mainPicLarge[img_selected])} ref="largePic"/>
                          </div> : ""
                    }

                    <div className="title_span">
                      {sex + " " + name + " " + code}
                    </div>
                    <div className="special_span">
                      <a href={this.state.summary.subtitleUrl}>{this.state.summary.subtitle}</a>
                    </div>
                    <div className="price_div">
                      <span className="number_span">￥{productId == "" ? minPrice + "-" + maxPrice : price}</span>
                      <span className="first_span">初上市价格 ：</span>
                      <span className="first_price">￥{this.state.summary.originPrice}</span>
                    </div>
                    <div className={"promotion_info"+(promotion_expand?" change_style":"")}
                         style={{height:promotion_expand?(meaning.length*30+10)+"px":"30px"}}
                         ref="promotion">
                      <div className="activity_div">活动</div>
                      <ul className="promotion_div">
                        {this.getMeaning()}
                      </ul>
                      <div className="expand_promotion" style={{display:meaning.length<=1?"none":""}}
                           onClick={()=>{this.expandPromotion()}}>
                        展开促销<Icon name="promotion-express"></Icon>
                      </div>
                    </div>
                    {this.state.approval == "on" ? <div className="choose-div">
                      <div className="dispath_div">
                        <div className="dispath_style">
                          配送方式 :
                        </div>
                        <div className="express-div">
                          <Radio name="distribution" onChange={(e)=>{this.initializeChoose(e.target.value)}}
                                 value="EXPRESS"
                                 choose={this.state.distribution}></Radio><span>快递配送</span>
                        </div>
                        <div style={{ display:isPickup=="Y"?"":"none" }} className="pickup-div">
                          <Radio name="distribution" onChange={(e)=>{this.initializeChoose(e.target.value)}}
                                 value="PICKUP" choose={this.state.distribution}/>
                          <span>门店自提</span>
                        </div>
                      </div>
                      {distribution}
                      <div className="newlines"></div>
                      <div className="size_div">
                        <span className="span_text">尺码 :</span>
                        <div className="all_size">
                          {sizes}
                          <Icon name="clothes" onClick={()=>{this.openSizeForm()}}></Icon>
                          <span className="table_span" onClick={()=>{this.openSizeForm()}}>尺码表</span>
                        </div>
                      </div>
                      <div className="color_div">
                        <span className="span_text">颜色 :</span>
                        <ul>
                          {colors}
                        </ul>
                      </div>
                      <div className="number_div">
                        <span className="span_text">数量 :</span>
                        <Counter changeVal={(event)=>this.changeVal(event)} minus={()=>{this.minus()}}
                                 add={()=>{this.add()}} value={quantity} onBlur={(e)=>{this.checkVal(e)}}/>
                        <span className="span_1">件</span>
                        <span className="enough">{this.getStockInfo()}</span>
                              <span className="show_other" onClick={()=>{this.showMap(true)}}>
                                {this.state.distribution == "PICKUP" && this.state.size_choose != 0 && this.state.color_choose != 0 && this.state.storeCode != "" ? "查看其它门店库存" : ""}
                              </span>
                      </div>
                      <div className="over_stock" style={{
                      height:over_stock?"32px":"0px",
                      visibility:over_stock?"visible":"hidden",
                      }}>
                        <div className="warning_div"><Icon name="warning"></Icon></div>
                        <span className="over_span"> 您所填写的商品数量超过库存！</span>
                      </div>
                      <div className="button_div">
                        <Button text="立即购买" disabled={over_stock}
                                onClick={()=>{loadingStock||this.state.buyFlag?"":this.buyNow()}}
                                className={"button_a "+(over_stock?"disable_buyNow":"")}/>
                        <Button text="提交到购物车" disabled={over_stock}
                                onClick={() => {this.state.addCartFlag||loadingStock?"":this.addCart()}}
                                className={"button_b "+(over_stock?"disable_addCart":"")}>
                          <Icon name="cart"></Icon>
                        </Button>
                      </div>
                      <div className="footer_div">
                        <span style={{ display:isNoReasonToReturn=="Y"?"":"none"}}>七天无理由退换</span>
                        <div className="share_all">
                          <Icon name="share"></Icon>
                          <span>分享商品</span>
                          <div className="share_div">
                            <div className="weixin_div">
                              <div className="weixin_img">
                                <img src={`data:image/png;base64,`+this.state.weixin_url}></img>
                                <span className="share_span">扫一扫分享到微信</span>
                              </div>
                              <Icon name="weixin"></Icon>
                            </div>
                            <Icon name="blog" onClick={()=>{
                                        window.open("http://v.t.sina.com.cn/share/share.php?url="+url+"&title="+this.state.summary.name+"&pic="+Hmall.cdnPath(this.state.summary.mainPic));
                                        }}>
                            </Icon>
                            <Icon name="qqZone" onClick={()=>{
                                             window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url="+url+"&title="+this.state.summary.name);
                                             }}>
                            </Icon>
                          </div>
                        </div>
                        <div className={"collect_pic"+(this.state.collect_flag?" onCollect_pic":"")}
                             onClick={()=>{this.addToCollect()}}></div>
                        <span onClick={()=>{this.addToCollect()}}>收藏</span>
                      </div>
                    </div> : <div className="remove_shelves">此宝贝已下架</div>}
                  </Col>
                </Row>
              </Panel>
              <Row>
                <Col width={270}>
                  <ClassificationGoods />
                  <RecommendGoods />
                </Col>
                <Col className="right_div">
                  <TabPanel width={970}>
                    <Tab title="商品详情" width={323}>
                      <div className="content_body">
                        <div className="parameter_div">
                          <div className="parameter_span">
                            产品参数:
                          </div>
                          <ul className="detail_ul">
                            <li>材质成分：聚氨酯纤维（锦纶）100%</li>
                            <li>销售渠道类型：商城同款（线上线下都销售）</li>
                            <li>货号：{this.state.productCode}</li>
                            <li>风格：百搭</li>
                            <li>衣长：常规款</li>
                            <li>袖长：长袖</li>
                            <li>领型：一字领</li>
                            <li>图案：纯色</li>
                            <li>品牌：Uniqlo/优衣库</li>
                          </ul>
                        </div>
                        <div style={{marginTop:"20px"}}>
                          <img src={Hmall.cdnPath(CMSConfig.productDetailBanner.urlPath)}/>
                        </div>
                        <ul className="anchor_ul" id="anchor">
                          <li>
                            <Icon name={this.state.anchor_choose=="business_desc"?"choosePoint":"point"}></Icon>
                            <a href="#business_desc" onClick={()=>{this.setState({anchor_choose:"business_desc"})}}
                               style={{color:this.state.anchor_choose=="business_desc"?"#ff0000":""}}>
                              商家说明
                            </a>
                          </li>
                          <li>
                            <Icon name={this.state.anchor_choose=="detail_pic"?"choosePoint":"point"}></Icon>
                            <a href="#detail_pic" onClick={()=>{this.setState({anchor_choose:"detail_pic"})}}
                               style={{color:this.state.anchor_choose=="detail_pic"?"#ff0000":""}}>
                              细节图
                            </a>
                          </li>
                          <li>
                            <Icon name={this.state.anchor_choose=="size_try"?"choosePoint":"point"}></Icon>
                            <a href="#size_try" onClick={()=>{this.setState({anchor_choose:"size_try"})}}
                               style={{color:this.state.anchor_choose=="size_try"?"#ff0000":""}}>
                              尺寸及试穿
                            </a>
                          </li>
                          <li>
                            <Icon name={this.state.anchor_choose=="friendly_tips"?"choosePoint":"point"}></Icon>
                            <a href="#friendly_tips" onClick={()=>{this.setState({anchor_choose:"friendly_tips"})}}
                               style={{color:this.state.anchor_choose=="friendly_tips"?"#ff0000":""}}>
                              温馨提示
                            </a>
                          </li>
                        </ul>
                        <div className="content_detail">
                          <a name="business_desc">
                            <div dangerouslySetInnerHTML={{__html:this.state.desc.instruction}}></div>
                          </a>
                          <a name="detail_pic">
                            <div className="title_detail">
                              产品展示
                            </div>
                            <table className="content_table">
                              <tbody>
                              <tr>
                                <td colSpan="2">
                                  <div className="title_table">· 模特商品搭配展示</div>
                                </td>
                              </tr>
                              { this.getModelPic() }
                              </tbody>
                            </table>
                            <table className="content_table">
                              <tbody>
                              <tr>
                                <td colSpan="2">
                                  <div className="title_table">· 商品细节</div>
                                </td>
                              </tr>
                              {this.getDetailPic()}
                              </tbody>
                            </table>
                          </a>
                          <a name="size_try">
                            <div dangerouslySetInnerHTML={{__html:this.state.desc.sizeAndTryOn}}></div>
                            <p>
                              <img src="../images/ProductDetail/detail_26.jpg"/>
                            </p>
                          </a>
                          <a name="friendly_tips">
                            <div className="title_detail">
                              温馨提示
                            </div>
                            <p>
                              <img src="../images/ProductDetail/detail_23.jpg"/>
                            </p>
                            <p>
                              <img src="../images/ProductDetail/detail_24.jpg"/>
                            </p>
                          </a>
                        </div>
                      </div>
                    </Tab>
                    <Tab title={"商品评价（"+this.state.commentNum+"）"} width={323}
                         onClick={()=>{this.fetchComment(this.state.commentPage)}}>
                      <div className="div_height">
                        <ul className="rated_ul">
                          {this.getComment()}
                        </ul>
                        <div className="pagenation"
                             style={{display:this.state.commentNum!=0&&!this.state.loadingComment?"":"none"}}>
                          <ul>
                            {this.setCommentPage()}
                          </ul>
                        </div>
                      </div>
                    </Tab>
                    <Tab title="购买咨询" width={323} onClick={()=>{this.fetchAdvice(this.state.advicePage)}}>
                      <div className="div_height">
                        <ul className="advice_ul">
                          {this.getAdvices()}
                        </ul>
                        <div className="div_advice" style={{display:this.state.loadingAdvices?"none":""}}>
                          <div className="commit_div">
                            <Button className="advice_button" text="我要咨询"
                                    onClick={()=>{this.setState({adviceFlag:!this.state.adviceFlag})}}>
                              <Icon name="advices"/>
                            </Button>
                            <div className="pagenation" style={{display:this.state.adviceNum==0?"none":""}}>
                              <ul>
                                {this.setAdvicePage()}
                              </ul>
                            </div>
                          </div>
                          <div className="backdrop">
                            <div className="advice_submit"
                                 style={{top:this.state.adviceFlag?"0px":"-180px"}}>
                              <textarea className="textarea_span" placeholder="发表咨询：" ref="advice"></textarea>
                              <Button text="提交" disabled={this.state.adviceButtonFlag} className="button_a"
                                      onClick={()=>{this.submitAdvice()}}/>
                            </div>
                          </div>
                        </div>
                      </div>
                    </Tab>
                  </TabPanel>
                </Col>
              </Row>
              <History></History>
            </div>
        )
      } else {
        return <PageNotFound></PageNotFound>
      }
    } else {
      return <div className="loading"></div>
    }

  }
}