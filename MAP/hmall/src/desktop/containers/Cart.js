import React, { Component } from 'react'
import { Link, IndexLink, browserHistory } from 'react-router'
import { Panel, Row, Col} from '../components/Layout';
import Flow from '../components/Cart/Flow'
import SimilarityProduct from '../components/Cart/SimilarityProduct'
import Icon from '../components/Icon';
import Button from '../components/Button';
import AlertMap from "../components/AlertMap";
import HRecommend from "../components/HRecommend";
import CheckBox from "../components/CheckBox";


export default class Cart extends Component{
  componentWillUnmount(){
    this.isUnMounted = true;
  }
  componentDidMount(){

    {/*获得购物车中的商品*/}
    fetch(`${ctService}/cart`,{
      headers:Hmall.getHeader({ })
    })
        .then(Hmall.convertResponse('json',this))
        .then(json=>{
          let { resp , success } = json
          if(success){

            /*整合数据结构*/
            let good = [],good_distinct =[]
            resp.forEach((f,i)=>{
              good_distinct.push(f.distributionId)
            })
            let n = []
            for(let i = 0; i < good_distinct.length; i++) {
              if (n.indexOf(good_distinct[i]) == -1) n.push(good_distinct[i]);
            }
            n.forEach((n,i)=>{
              good[i] = {distributionId:"",checked:"",goods:[],distribution:""}
              good[i].distributionId = n
              good[i].distribution = n==""?"EXPRESS":"PICKUP"
              good[i].checked = true
              good[i].userId = resp[0].userId
            })
            good.forEach((g,ii)=>{
              resp.forEach((f,i)=>{
                if(f.distributionId==g.distributionId){
                  let goods = {
                    checked: true,
                    approval:"",
                    distribution:"",
                    distributionId: "",
                    address:[],
                    storeName:"",
                    productCode:"",
                    productId: "",
                    mainPic:"",
                    name:"",
                    style:"",
                    size:"",
                    price:"",
                    originPrice:"",
                    quantity:0,
                    cartId:"",
                    gDept:"",
                    intenCode:"",
                    code:""
                  }
                  goods.checked = f.productDetail.summaryInfo.approval=="list"?true:false
                  goods.approval = f.productDetail.summaryInfo.approval
                  goods.distribution = f.distribution
                  goods.distributionId = f.distributionId
                  goods.address = f.address
                  goods.storeName = f.storeName
                  goods.productCode = f.productCode
                  goods.productId = f.productId
                  goods.mainPic = f.productDetail.url
                  goods.name = f.productDetail.summaryInfo.name
                  goods.style = f.productDetail.productDetailInfo.styleText
                  goods.size = f.productDetail.productDetailInfo.size
                  goods.price = Number(f.productDetail.productDetailInfo.price)
                  goods.originPrice = f.productDetail.summaryInfo.originPrice
                  goods.quantity = Number(f.quantity)
                  goods.cartId = f.cartId
                  goods.gDept = f.productDetail.summaryInfo.gDept,
                  goods.intenCode = f.productDetail.summaryInfo.intenCode,
                  goods.code = f.productDetail.summaryInfo.code
                  g.goods.push(goods)
                }
              })
            })
            this.setState({fetch_status: "init"})

            this.getOptionCartTest(good)
          }
        })
        .catch(Hmall.catchHttpError(()=>{
          this.setState({
            fetch_status: 'error'
          });
        }))
  }
  constructor(props) {
    super(props);
    {/*父组件初始化参数*/}
    this.order_goods = {"userId":"","creationTime":0,
      "products":[{"distribution":"","distributionId":"","productCode":"",
        "productId":"","quantity":"",}]},
        this.state = {
          recommend: CMSConfig.recommends,
          success: "",
          sales: [],
          mapOutStyle: "",
          good:[],
          quantity: 0,
          price: 0,
          store_class: "",
          cartId: "",
          max_count_arr: [],
          fetch_status: "uninit",
          update_message: "",
          update_message_id:"",
          distributionId: "",
          productId :"" ,
          no_stocks: [],
          address_codes:[],
          productCode:"",
          quantity_stock:0 ,
          option_success:false,
          cartBuffer:{},
          buttonClick:false,
          weixin_url : ""
        }
  }
  /*测试促销接口*/
  getOptionCartTest(good){
    let count = 0 , cart_total = 0.00 , buffer = this.state.cartBuffer , content = {"cartprice":0,"items":[]};
    this.setState({good:good})
    good.forEach((g,i)=>{
      g.goods.forEach((good,i)=>{
        let { checked , approval , quantity , price , productCode} = good
        if(checked&&approval=="list"){
          count = count + quantity
          cart_total = quantity * price + cart_total
          content.items.push({"id":productCode,"count":quantity+""})
        }
      })
    })
    content.cartprice = cart_total
    Hmall.loadXiaoNeng(content)
    fetch(`${ctService}/cart/getOrderListByCart`,{
      method:"POST",
      headers: Hmall.getHeader({
        "Content-Type":"application/json"
      }),
      body:JSON.stringify(
          good
      )
    })
        .then(Hmall.convertResponse('json',this))
        .then(json=>{
              let { resp , success } = json
              if(success){
                fetch(`${droolsService}/sale/calculation/optionCart`,{
                  method:"POST",
                  headers: Hmall.getHeader({
                    "Content-Type":"application/json"
                  }),
                  body:JSON.stringify(
                      resp
                  )
                })
                    .then(Hmall.convertResponse('json',this))
                    .then(json=>{
                          let { resp , success ,msgCode} = json  , price = 0
                          if(success){
                            buffer[JSON.stringify(good)] = resp
                            resp.forEach((r,i)=>{
                              price = r.price.account + price
                            })
                            this.setState({sales:resp,quantity:count,price:price,option_success:true,cartBuffer:buffer})
                          }else{
                            this.setState({sales:[],quantity:count,price:cart_total,option_success:false})
                          }
                        }
                    )
                    .catch(Hmall.catchHttpError(() =>
                        this.setState({sales:[],quantity:count,price:cart_total,option_success:false})));
              }else{
                this.setState({sales:[],quantity:count,price:cart_total,option_success:false})
              }
            }
        )
        .catch(Hmall.catchHttpError(() =>
            this.setState({sales:[],quantity:count,price:cart_total,option_success:false})));


  }

  /*调用促销接口*/
  getOptionCart(good){
    let {option_success , cartBuffer} = this.state , count = 0 ,no_select_count = 0 ,  cart_total = 0.00 , buffer = cartBuffer ,price = 0 ;
    good.forEach((g,i)=>{
      if(g.checked==false)
        no_select_count = no_select_count + 1
      g.goods.forEach((good,i)=>{
        if(good.checked&&good.approval=="list"){
          count = count + good.quantity
          cart_total = good.quantity * good.price + cart_total
        }
      })
    })
    this.setState({good:good,quantity:count})

    if(option_success&&no_select_count!=good.length){
      if(cartBuffer[JSON.stringify(good)]){
        cartBuffer[JSON.stringify(good)].forEach((r,i)=>{
          price = r.price.account + price
        })

        this.setState({sales:cartBuffer[JSON.stringify(good)],quantity:count,price:price,cartBuffer:buffer})
      }else{
        fetch(`${ctService}/cart/getOrderListByCart`,{
          method:"POST",
          headers: Hmall.getHeader({
            "Content-Type":"application/json"
          }),
          body:JSON.stringify(
              good
          )
        })
            .then(Hmall.convertResponse('json',this))
            .then(json=>{
                  let { resp , success } = json
                  if(success){
                    fetch(`${droolsService}/sale/calculation/optionCart`,{
                      method:"POST",
                      headers: Hmall.getHeader({
                        "Content-Type":"application/json"
                      }),
                      body:JSON.stringify(
                          resp
                      )
                    })
                        .then(Hmall.convertResponse('json',this))
                        .then(json=>{
                              let { resp , success ,msgCode} = json
                              if(success){
                                buffer[JSON.stringify(good)] = resp
                                resp.forEach((r,i)=>{
                                  price = r.price.account + price
                                })
                                this.setState({sales:resp,quantity:count,price:price,cartBuffer:buffer})
                              }
                            }
                        )
                        .catch(Hmall.catchHttpError());
                  }
                }
            )
            .catch(Hmall.catchHttpError());
      }

    }else{
      this.setState({sales:[],price:cart_total})
    }
  }

  /*错误信息*/
  messageCode(code,goods){
    let arr = this.state.good
    arr.forEach((a,i)=>{
      a.goods.forEach((g,i)=>{
        if(g.cartId==goods.cartId){
          g.quantity = 1
        }
      })
    })
    this.setState({update_message_id:goods.cartId})
    Hmall.Storage.get('miniCart').deleteCart(0);
    this.getOptionCart(arr)
    switch (code){
      case "CT_CLIENT_03" : return this.setState({update_message:"库存不存在"});
      case "CT_CLIENT_02" : return this.setState({update_message:"库存不足"});
      case "CT_CLIENT_01" : return this.setState({update_message:"库存接口访问超时"});
      case "service_error" : return this.setState({update_message:"接口异常"});
    }
  }
  /*更改商品数量*/
  handleClickCount(goods,change){
    let quantity = 0 , arr = this.state.good
    if(change=="up"){
      quantity = goods.quantity + 1
      arr.forEach((a,i)=>{
        a.goods.forEach((g,i)=>{
          if(g.cartId==goods.cartId){
            g.quantity = quantity
          }
        })
      })
    }else if(change=="down"){
      quantity = goods.quantity - 1
      if(quantity>=1){
        arr.forEach((a,i)=>{
          a.goods.forEach((g,i)=>{
            if(g.cartId==goods.cartId){
              g.quantity = quantity
            }
          })
        })
      }
    }else{
      if(!isNaN(goods.quantity)&&goods.quantity>0&&goods.quantity<99999){
        quantity = goods.quantity
      }else{
        quantity = 1
        arr.forEach((a,i)=>{
          a.goods.forEach((g,i)=>{
            if(g.cartId==goods.cartId){
              g.quantity = quantity
            }
          })
        })
      }
    }
    if(goods.approval=="list"){
      this.setState({update_message:""})
      {/*组件将要渲染时调用*/}
      if (this.timeoutId){
        clearTimeout(this.timeoutId);
      }
      this.timeoutId = setTimeout(()=>{
        //${ctService}
        //http://10.211.111.2:5555/hmall-ct-service/cart
        fetch(`${ctService}/cart/update`,{
          method:"POST",
          headers:Hmall.getHeader({
            "Content-Type":"application/json"
          }),
          body:JSON.stringify({
            cartId:goods.cartId,
            quantity:quantity,
            productId:goods.productId,
            productCode:goods.productCode,
            distribution:goods.distribution,
            distributionId:goods.distributionId
          })
        })
            .then(Hmall.convertResponse('json',this))
            .then(json=>{
              let { success , msgCode } = json
              if(success){
                this.getOptionCart(arr)
              }else{
                this.messageCode(msgCode,goods)
              }})
            .catch(Hmall.catchHttpError(()=>{
              this.messageCode("service_error",goods)
            }))
      },300,this)

    }
  }
  /*输入更改商品数量*/
  handleOnChangeQuantity(id,event){
    let arr = this.state.good ,value = event.target.value,quantity = 0
    //!isNaN(value)&&
    if(value>0&&value<99999){
      quantity = value
    }else if(value==""){
      quantity = ""
    }else{
      quantity = 1
    }
    arr.forEach((a,i)=>{
      a.goods.forEach((g,i)=>{
        if(g.cartId==id){
          g.quantity = Number(quantity)
        }
      })
    })
    this.setState({good:arr})
  }
  /*商品控制地点*/
  handleClickSelectUp(){
    let arr = this.state.good
    arr.forEach((g,i)=>{
      let count = 0
      g.goods.forEach((goods,ii)=>{
        if(goods.checked==true){
          count = count + 1
        }
      })
      if(count == g.goods.length){
        g.checked =  true
      }else{
        g.checked =  false
      }
    })
    this.setState({good:arr})
  }
  /*地点控制商品*/
  handleOnChangeSelectDown(id,event){
    let checked = false ,arr = this.state.good
    if(event.target.value == "Y"){
      checked = true
    }else{
      checked = false
    }

    arr.forEach((a,i)=>{
      if(id == a.distributionId){
        a.checked = checked
        a.goods.forEach((g,i)=>{
          g.checked = checked
        })
      }
    })
    this.getOptionCart(arr)
  }
  /*选择商品*/
  handleOnChangeSelect(id){
    let arr = this.state.good
    arr.forEach((a,i)=>{
      a.goods.forEach((g,ii)=>{
        if(g.cartId==id){
          g.checked = !g.checked
        }
      })
    })
    this.setState({good:arr})
    this.handleClickSelectUp()
    this.getOptionCart(this.state.good)

  }
  /*全选*/
  handleClickSelectALLUp(flag,event){
    let checked = false , arr = this.state.good
    if(flag){
      if(event.state.value=="N"){
        checked = true
      }else{
        checked = false
      }
      arr.forEach((a,i)=>{
        a.checked = checked
        a.goods.forEach((g,i)=>{
          g.checked = checked
        })
      })
      this.getOptionCart(arr)
    }else{
      if(!event){
        checked = true
      }else{
        checked = false
      }
      arr.forEach((a,i)=>{
        a.checked = checked
        a.goods.forEach((g,i)=>{
          g.checked = checked
        })
      })
      this.getOptionCart(arr)
    }
  }
  /*删除已选*/
  handleClickDelete(flag){
    let select_all = []
    this.state.good.forEach((g,i)=>{
      g.goods.forEach((goods,ii)=>{
        if(goods.checked==true){
          select_all.push(goods.cartId)
        }
      })
    })
    if(select_all.length==0){
      alert("请选中需要删除的商品")
    }else{
      if(flag){
          {/*组件将要渲染时调用*/}
          fetch(`${ctService}/cart/multDelete`,{
            method:"post",
            headers:Hmall.getHeader({
              "Content-Type":"application/json"
            }),
            body:JSON.stringify({
              select:select_all
            })
          })
              .then(Hmall.convertResponse('json',this))
              .then(json=>{
                if(json.success){
                  let arr = this.state.good
                  select_all.forEach((cartId)=>{
                    arr.forEach((a,ii)=>{
                      if(a.checked){
                        arr.splice(ii,1)
                      }else{
                        a.goods.forEach((g,i)=>{
                          if(g.checked){
                            a.goods.splice(i,1)
                          }
                        })
                      }
                    })
                  })
                  Hmall.Storage.get('miniCart').deleteCart(select_all.length)
                  this.getOptionCart(arr)
                }else{
                  alert("删除商品失败")
                }
              });
      }else{
        {/*组件将要渲染时调用*/}
        fetch(`${ctService}/cart/multDelete`,{
          method:"post",
          headers:Hmall.getHeader({
            "Content-Type":"application/json"
          }),
          body:JSON.stringify({
            select:select_all
          })
        })
            .then(Hmall.convertResponse('json',this))
            .then(json=>{
              if(json.success){
                let arr = this.state.good
                select_all.forEach((cartId)=>{
                  arr.forEach((a,ii)=>{
                    if(a.checked){
                      arr.splice(ii,1)
                    }else{
                      a.goods.forEach((g,i)=>{
                        if(g.checked){
                          a.goods.splice(i,1)
                        }
                      })
                    }
                  })
                })
                Hmall.Storage.get('miniCart').deleteCart(select_all.length)
                this.getOptionCart(arr)
              }else{
                alert("删除商品失败")
              }
            });
      }


    }

  }
  /*删除单个商品*/
  handleClickGoods(id,flag,approval){
    let text =  "是否确认删除此商品", count = approval?0:1

    if(flag){
        fetch(`${ctService}/cart/delete/${id}`,{
          method:"post",
          headers: Hmall.getHeader({ }),
        })
            .then(Hmall.convertResponse('json',this))
            .then(json =>{
              if(json.success){
                let arr = this.state.good
                arr.forEach((a,ii)=>{
                  a.goods.forEach((g,i)=>{
                    if(g.cartId == id){
                      if(a.goods.length==1){
                        arr.splice(ii,1)
                      }else{
                        a.goods.splice(i,1)
                      }
                    }
                  })
                })
                Hmall.Storage.get('miniCart').deleteCart(count)
                this.getOptionCart(arr)
              }})
            .catch(Hmall.catchHttpError());
    }else{
      fetch(`${ctService}/cart/delete/${id}`,{
        method:"post",
        headers: Hmall.getHeader({ }),
      })
          .then(Hmall.convertResponse('json',this))
          .then(json =>{
            if(json.success){
              let arr = this.state.good
              arr.forEach((a,ii)=>{
                a.goods.forEach((g,i)=>{
                  if(g.cartId == id){
                    if(a.goods.length==1){
                      arr.splice(ii,1)
                    }else{
                      a.goods.splice(i,1)
                    }
                  }
                })
              })
              Hmall.Storage.get('miniCart').deleteCart(1)
              this.getOptionCart(arr)
            }})
          .catch(Hmall.catchHttpError());
    }

  }
  /*移入收藏夹*/
  handleClickAddFavorite(code,flag,id){
    let select_all = []
    if(flag){
      this.state.good.forEach((g,i)=>{
        g.goods.forEach((goods,ii)=>{
          if(goods.checked==true){
            select_all.push(goods.productCode)
          }
        })
      })
    }else{
      select_all.push(code)
    }
    if(select_all.length!=0){
        {/*组件将要渲染时调用*/}
        fetch(`${urService}/customer/favorite/multInsert`,{
          method:"post",
          headers:Hmall.getHeader({
            "Content-Type":"application/json"
          }),
          body:JSON.stringify(
              select_all
          )
        })
            .then(Hmall.convertResponse('json',this))
            .then(json=>{if(json.success){
              if(flag){
                this.handleClickDelete(false)
              }else{
                this.handleClickGoods(id,false)
              }
            }else{"添加失败"}});
    }else{
      alert("请选择需要添加的商品")
    }
  }
  /*分享*/
  handleClickShare(e){
    let arr = [] , { protocol, host, hostname } = location
    this.state.good.forEach((a,i)=>{
      a.goods.forEach((g,i)=>{
        if(g.checked){
          let arrs = {code:"",name:"",mainPic:""}
          arrs.code = g.productCode
          arrs.name = g.name
          arrs.mainPic = g.mainPic
          arr.push(arrs)
        }
      })
    })
    if(arr.length==0){
      alert("请选中分享的商品")
    }else{
      if(e==1){
        let url = encodeURIComponent(`${protocol}//${hostname == 'localhost' ? 'hhmall.saas.hand-china.com' : host}/product-detail.html?productCode=`+arr[0].code)+"&title="+arr[0].name+"&pic="+arr[0].mainPic
        fetch(`${tpService}/thirdParty/share?content=` + url)
            .then(Hmall.convertResponse('json', this))
            .then(json=> {
              this.setState({weixin_url: json.resp[0]});
            });

      }else if(e==2){
        let url = "url="+encodeURIComponent(`${protocol}//${hostname == 'localhost' ? 'hhmall.saas.hand-china.com' : host}/product-detail.html?productCode=`+arr[0].code)+"&title="+arr[0].name+"&pic="+arr[0].mainPic
        window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?"+url)
      }else{
        let allUrl = ""
        arr.forEach((a,i)=>{
          if(i!=0){
            allUrl = encodeURIComponent(`${protocol}//${hostname == 'localhost' ? 'hhmall.saas.hand-china.com' : host}/product-detail.html?productCode=`+arr[i].code)+arr[i].name+allUrl
          }
        })
        //+"&pic="+arr[0].mainPic
        let url = "url="+encodeURIComponent(`${protocol}//${hostname == 'localhost' ? 'hhmall.saas.hand-china.com' : host}/product-detail.html?productCode=`+arr[0].code)+"&title="+arr[0].name+allUrl
        window.open("http://service.weibo.com/share/share.php?"+url)
      }

    }
  }
  //判断是否有选中商品
  checkedSelect(){
    let select_all = []
    this.state.good.forEach((g,i)=>{
      g.goods.forEach((goods,i)=>{
        if(goods.checked){
          select_all.push(goods)
        }
      })
    })
    return select_all
  }
  /*提交订单*/
  handleClickSubmit(){
    let settlementArr = this.order_goods,select_all = [],
        stocks = [] , stock_flag = false,
        { no_stocks , good} = this.state
    select_all = this.checkedSelect()

    select_all.map((goods,i)=>{
      let  stock = {
        "distribution":"",
        "productCode":"",
        "productId":"",
        "storeId":"",
        "quantity":""
      }
      stock.distribution = goods.distribution
      stock.productCode = goods.productCode
      stock.productId =  goods.productId
      stock.storeId = goods.distributionId
      stock.quantity = goods.quantity
      stocks.push(stock)
      stock = {}
    })
    let callback = () => this.setState({buttonClick: false});
    this.setState({buttonClick: true});
    fetch(`${stService}/stock/validateStock`,{
      method:"post",
      headers: Hmall.getHeader({
        "Content-Type":"application/json"
      }),
      body:JSON.stringify(stocks)
    })
        .then(Hmall.convertResponse('json',this,callback))
        .then(json =>{
          let { success , resp } = json
          if(success){
            let arr = no_stocks
            resp.forEach((r,i)=>{
              if(!r.isEnough){
                arr.push(r.productId)
              }
            })
            if(arr.length==0){
              settlementArr.products.length = select_all.length
              settlementArr.userId = select_all[0].userId
              settlementArr.creationTime = Date.now()
              select_all.map((goods,i)=>{
                let p = settlementArr.products[i] = {}
                p.distribution = goods.distribution
                p.distributionId = goods.distributionId
                p.productCode = goods.productCode
                p.productId = goods.productId
                p.quantity = goods.quantity
              })
              //http://10.211.101.4:5555/hmall-od-service/order/createTempOrders
              fetch(`${odService}/order/createTempOrders`,{
                method:"post",
                headers: Hmall.getHeader({
                  "Content-Type":"application/json"
                }),
                body:JSON.stringify({
                  map:settlementArr
                })
              })
                  .then(Hmall.convertResponse('json',this,callback))
                  .then(json =>{
                    let {success , resp ,msgCode} = json
                    if(success){
                      browserHistory.push(
                          {pathname:'/payment/settlement.html',
                            state:{tempIds:resp,flashCart:true,delete_count:select_all.length}
                          });
                    }else if(msgCode == "OD_BLACKUSER"){
                      alert("你是黑名单用户，不能下单！！！")
                    }else{
                      alert("生成临时订单失败！"+msgCode)
                    }
                  })
                  .catch(Hmall.catchHttpError(callback));
            }else{
              alert("有商品库存不足")
            }
          }else{
            alert("查看库存失败")
          }
        })
        .catch(Hmall.catchHttpError(()=>{
          alert("查看库存失败")
          callback()
        }));



  }
  /*地图取消，收回*/
  cancel(){
    this.setState({store_class: "store_fade_in"})
  }
  /*地图确定，更改配送方式*/
  confirm(address,storeCode,storeName,distributionId,stock){
    if(stock>=this.state.quantity_stock){
      if(distributionId=="delivery"){
        distributionId = ""
      }
      let same_flag = true ,
          { cartId , good } = this.state
      good.forEach((a,i)=>{
        a.goods.forEach((g,i)=>{
          if(g.cartId==cartId&&g.distributionId==distributionId){
            same_flag = false
          }
        })
      })
      if(same_flag){
        {/*组件将要渲染时调用*/}
        //${ctService}
        fetch(`${ctService}/cart/update`,{
          method:"POST",
          headers:Hmall.getHeader({
            "Content-Type":"application/json"
          }),
          body:JSON.stringify({
            cartId: cartId,
            address: address,
            storeName: storeName,
            distribution: distributionId==""?("EXPRESS"):("PICKUP"),
            distributionId: distributionId
          })
        })
            .then(Hmall.convertResponse('json',this))
            .then(json=>{
              if(json.success){
                let arr = good, goods = {},flag = false , count_ii =0
                arr.forEach((a,ii)=>{
                  a.goods.forEach((g,i)=>{
                    if(g.cartId == cartId && a.goods.length == 1){
                      flag = true
                      //arr.splice(ii,1)
                      goods = a
                      arr[ii] = {}
                      count_ii = ii
                      goods.distributionId = distributionId
                      goods.goods[0].distributionId = distributionId
                      goods.goods[0].address = address
                      goods.goods[0].storeName = storeName
                      goods.goods[0].distribution = distributionId==""?("EXPRESS"):("PICKUP")
                    }else if(g.cartId == cartId && a.goods.length > 1){
                      flag = false
                      a.goods.splice(i,1)
                      goods = g
                      goods.distributionId = distributionId
                      goods.address = address
                      goods.storeName = storeName
                      goods.distribution = distributionId==""?("EXPRESS"):("PICKUP")
                    }
                  })
                })
                if(flag){
                  let flag2 = true
                  arr.forEach((a,i)=>{
                    if(a.distributionId==goods.distributionId){
                      a.goods.push(goods.goods[0])
                      arr.splice(count_ii,1)
                      flag2 = false
                    }
                  })
                  if(flag2){
                    // arr.push(goods)
                    arr[count_ii] = goods
                  }
                }else {
                  let flag3 = true ,userId = ""
                  arr.forEach((a,i)=>{
                    userId = a.userId
                    if(a.distributionId == distributionId){
                      a.goods.push(goods)
                      flag3 = false
                    }
                  })
                  if(flag3){
                    let goods2 = {distributionId:distributionId ,checked:goods.checked,goods:[goods],userId:userId}
                    arr.push(goods2)
                  }
                }
                this.getOptionCart(arr)
              }});
      }
    }else{
      alert(storeName+"： 门店库存不足！")
    }
    this.setState({store_class: "store_fade_in"})
  }

  onClickMap(goods){
    this.setState({store_class:"store_fade_out",cartId:goods.cartId,distributionId:goods.distributionId,
      productId:goods.productId,address_codes:goods.address,productCode:goods.productCode,quantity_stock:goods.quantity})
  }
  /*遍历获得每个配送地点的商品*/
  renderGoods(){
    let { update_message , good ,no_stocks ,update_message_id ,sales } = this.state;

    return good.map((g,ii)=>{
      let select_arr = [] , count = true
      select_arr.length = g.goods.length
      let goods = g.goods.map((goods,i)=>{
        let obj = {"gDept":goods.gDept,
          "intenCode":goods.intenCode,
          "code":goods.code}
        return(
            <tr key={i} className="cartList">
              {/*判断是否为失效*/}
              <td className="top">{goods.approval=="list"?
                  <CheckBox value={goods.checked?"Y":"N"} width={14} height={14} onChange={()=>{this.handleOnChangeSelect(goods.cartId)}}/>:""}
              </td>
              <td className="top">
                {goods.approval=="list"?<span onClick={()=>{this.onClickMap(goods)}}>{goods.distributionId==""?"快递配送":goods.storeName}</span>:
                <Icon name="cart-out"/>}

              </td>
              <td className="top">
                <p><Link to={"/product-detail.html?productCode="+goods.productCode}><img src={Hmall.cdnPath(goods.mainPic)}/></Link></p>
                <ul>
                  <li><Link to={"/product-detail.html?productCode="+goods.productCode}>{goods.name}</Link></li>
                  <br/>
                  <li><Icon name="7days" title="7天无理由退换"/></li>
                </ul>
              </td>
              <td className="top">
                <ul>
                  <li><span>颜色：</span>{goods.style}</li>
                  <li><span>尺码：</span>{goods.size}</li>
                </ul>
              </td>
              <td className="top">
                ￥{goods.price.toFixed(2)}<br/><br/>
                初上市价格<br/>
                ￥{goods.originPrice}
              </td>
              <td className="top">
                {goods.approval=="list"?(<div>{goods.quantity<=1? <Icon name="cart-no-down"/>:
                    <Icon name="cart-down" onClick={()=>{this.handleClickCount(goods,"down")}}/>}
                  <input type="text" value={goods.quantity} onChange={(event)=>{this.handleOnChangeQuantity(goods.cartId,event)}} onBlur={()=>{this.handleClickCount(goods,"blur")}}/>
                  <Icon name="cart-add" onClick={()=>{this.handleClickCount(goods,"up")}}/>
                </div>):(1)}
                <span className="notice notice-word" style={{display:update_message_id==goods.cartId&&update_message!=""?"block":"none"}}>{update_message}</span>
                <span className="notice notice-word" style={{display:no_stocks.indexOf(goods.productId)!=-1?"block":"none"}}>库存不足</span>
              </td>
              <td  className="top" style={goods.approval=="list"?{color:"#ff0000"}:{color:"#000"}}>
                ￥{(goods.price*goods.quantity).toFixed(2)}
              </td>
              <td className="top">
                {goods.approval=="list"?<ul>
                  <li>
                    <span className="hover-span" onClick={()=> Hmall.dialog({
                      title: '提示',
                      text: '是否添加到收藏夹商品。',
                      buttons: [{
                        text: '我再看看',
                        className: 'red'
                      },{
                        text: '确认移入',
                        onClick: ()=>this.handleClickAddFavorite(goods.productCode,false,goods.cartId)
                      }]
                    })}>移入收藏夹</span>
                    </li>
                  <li>
                    <span className="hover-span" onClick={()=> Hmall.dialog({
                      title: '提示',
                      text: '是否删除该商品。',
                      buttons: [{
                        text: '我再看看',
                        className: 'red'
                      },{
                        text: '确认删除',
                        onClick: ()=>this.handleClickGoods(goods.cartId,true)
                      }]
                    })}
                 >删除</span></li>
                  <li>
                    <SimilarityProduct onMouseEnter={()=>{}} obj={obj} margin_left={1141} right={80}/>
                  </li>
                </ul>:
                    <a onClick={()=> Hmall.dialog({
                      title: '提示',
                      text: '是否删除该商品。',
                      buttons: [{
                        text: '我再看看',
                        className: 'red'
                      },{
                        text: '确认删除',
                        onClick: ()=>this.handleClickGoods(goods.cartId,true)
                      }]
                    })}>删除</a>}
              </td>
            </tr>
        )
      })
      let activities = sales.map((s,i)=>{
        if(s.distributionId == g.distributionId){
          return s.activities.map((a,ii)=>{
            count = false
            return (
                <li key={ii} >已享优惠：{a.activityName}</li>
            )
          })
        }
      })
      return (
          <tbody key={ii}>
          <tr className="pickup-express">
            <td>
              <CheckBox value={g.checked?"Y":"N"} width={14} height={14} onChange={(event)=>{this.handleOnChangeSelectDown(g.distributionId,event)}}/>
            </td>
            <td colSpan="7"><span className="sort">{g.distributionId==""?("快递配送"):(g.goods[0].storeName)}</span></td>
          </tr>
          {goods}
          {count?<tr/>:<tr className="account-introduce">
            <td colSpan="8">
              <ul>{activities}</ul>
            </td>
          </tr>}

          <tr><td colSpan="8"></td></tr>
          </tbody>
      )
    })
  }
  //判断是否全选
  renderSelectAll(){
    let l = 0,{ good }= this.state
    good.forEach((g,i)=>{
      if(g.checked){
        l = l + 1
      }
    })
    if(l==good.length){
      return true
    }else{
      return false
    }

  }
  render() {
    let url = encodeURIComponent(`http://hhmall.saas.hand-china.com/product-detail.html?productCode=`),
        { good ,store_class ,quantity , price , fetch_status , distributionId , productId , productCode ,address_codes , buttonClick , weixin_url} = this.state,
        select_all = this.checkedSelect()

    return (
        <section>
          <div className="Cart">
            <Flow count={1}/>
            {/*弹出的地图*/}
            <AlertMap store_class={store_class}  show_stock={true}
                      cancel={()=>{this.cancel()}}
                      confirm={(address,storeCode,storeName,distributionId,stock)=>{this.confirm(address,storeCode,storeName,distributionId,stock)}}
                      show_flag={true}
                      defaultCode={distributionId}
                      defaultAddress={address_codes}
                      productId={productId}
                      productCode={productCode}
            />
            {
              (() => {
                switch(fetch_status){
                  case 'uninit': return <div className="loading"></div>;
                  case 'init': return  good.length==0?
                      <div className="emptyCart">
                        <div className="div-empty">
                          <span><Icon name="cart-empty"/> 购物车里空空如也，赶紧去  </span>
                          <Link to="/">  逛逛吧 ></Link>
                        </div>
                        <HRecommend/>
                      </div>
                      :
                      <div>
                        <div>
                          {/*表格*/}
                          <table>
                            {/*表头*/}
                            <thead>
                            <tr>
                              <th width="40"></th>
                              <th width="160">配送方式</th>
                              <th width="390">商品信息</th>
                              <th width="150">颜色/尺码</th>
                              <th width="160">价格</th>
                              <th width="110">数量</th>
                              <th width="130">小计</th>
                              <th width="110">操作</th>
                            </tr>
                            </thead>
                            {/*表体*/}
                            {this.renderGoods()}
                          </table>
                        </div>
                        <div className="check-all">
                          <div className="check-all-body">
                            <ul>
                              <li>
                                <CheckBox value={this.renderSelectAll()?"Y":"N"} width={14} height={14} name="select-all" ref="select_all"
                                          onChange={()=>{this.handleClickSelectALLUp(false,this.renderSelectAll())}}/>
                              </li>
                              <li onClick={()=>{this.handleClickSelectALLUp(true,this.refs.select_all)}}>全选</li>
                              <li onClick={()=> Hmall.dialog({
                                title: '提示',
                                text: '是否删除已选。',
                                buttons: [{
                                  text: '我再看看',
                                  className: 'red'
                                },{
                                  text: '确认删除',
                                  onClick: ()=>this.handleClickDelete(true)
                                }]
                              })}>删除已选</li>
                              <li onClick={()=> Hmall.dialog({
                                title: '提示',
                                text: '是否移入收藏夹。',
                                buttons: [{
                                  text: '我再看看',
                                  className: 'red'
                                },{
                                  text: '确认移入',
                                  onClick: ()=>this.handleClickAddFavorite("",true,"")
                                }]
                              })}>移入收藏夹</li>
                              <li>分享</li>
                              <li>
                                <Icon name="cart-qq" onClick={()=>{this.handleClickShare(2) }}/>
                                <Icon name="cart-weibo" onClick={()=>{this.handleClickShare(3) }}/>
                                <Icon name="cart-weixin" onClick={()=>{this.handleClickShare(1) }}/>
                                {weixin_url!=""?<img src={`data:image/png;base64,`+weixin_url} onClick={()=>{this.setState({weixin_url:""})}}></img>:""}
                              </li>
                              <li><Button className="red" onClick={()=>{this.handleClickSubmit()}} disabled = {select_all.length==0||buttonClick}
                                          text="立即结算" height={52} width={145}/></li>
                              <li><span>总价（不含运费）：</span><span>￥</span>{price.toFixed(2)}</li>
                              <li><span>已选商品 </span>{quantity}<span> 件</span></li>
                            </ul>
                          </div>
                        </div>
                        <HRecommend/>
                      </div> ;
                  case 'error':return <h1 className="error">网页出错</h1>
                }
              })()
            }
          </div>
        </section>
    );
  }
}



