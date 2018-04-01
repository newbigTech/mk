import React, { Component } from 'react'
import Choose from '../components/ProductList/Choose';
import Search from '../components/ProductList/Search';
import ChooseList from '../components/ProductList/ChooseList'
import {Panel, Row, Col} from '../components/Layout';
import History from '../components/History';
import ClassificationGoods from  '../components/Category';
import RecommendGoods from  '../components/VRecommend';
import { Link, IndexLink, browserHistory } from 'react-router'
import Icon from '../components/Icon';
import Pagenation from '../components/Pagenation';
import ProductGroup from '../components/ProductGroup';


export default class ProductList extends Component {

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  getInitState() {
    return {
      url: "",
      loading_flag: true,
      exist: [],
      statePage: 1,
      rankChoose: "",
      chooseLow: "",
      chooseHigh: "",
      description: "",
      filters: [],
      color: [],
      size: [],
      season: [],
      material: [],
      summary: 0,
      search_flag: false,
      change_flag: false,
      code: "",
      shop: [{
        "new": "",
        "colorPic": [
          ""
        ],
        "label": "",
        "categoryCode": "",
        "productName": "",
        "sales": "",
        "productCode": "",
        "size": [],
        "material": "",
        "price": "",
        "mainPic": "",
        "season": "",
        "style": [],
        "stock": ""
      }],
      searchCode: [],
      searchText: "",
      chooseName: "",
      storeCode: "",
    }
  }

  fetchSearchCode() {
    fetch(`${bdService}/h/searchSet/querySortDetail`, {
      headers: Hmall.getHeader({})
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          this.setState({searchCode: json});
        })
  }


  searchInfo(props) {
    let { categoryCode } = props.params,
        { pathname, search, query } = props.location,
        { page=1, description } = query,
        { searchFlag } = props,
        url = pathname + search,
        num = parseInt(page), pageSize = this.props.displayMode != 'C5' ? 16 : 20;
    if (searchFlag == true) {
      fetch(`${scService}/search/searchProductList`, {
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),
        method: 'post',
        body: JSON.stringify({
          description: description,
          pageInfo: {
            page: num,
            pageSize: pageSize
          }
        })
      }).then(Hmall.convertResponse('json', this))
          .then(json=> {
            this.setState({
              filters: json.resp[0],
              url: url,
              exist: [],
              rankChoose: "",
              shop: json.resp[1],
              summary: json.resp[2].productSum,
              color: json.resp[0][0].item,
              size: json.resp[0][1].item,
              season: json.resp[0][2].item,
              material: json.resp[0][3].item,
              statePage: num,
              description: description,
              chooseLow: "",
              chooseHigh: "",
              searchText: "",
              search_flag: true,
              loading_flag: false
            })
          }).catch(Hmall.catchHttpError());
    } else {
      fetch(`${scService}/search/searchProductList`, {
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),
        method: 'post',
        body: JSON.stringify({
          categoryCode: categoryCode,
          pageInfo: {
            page: num,
            pageSize: pageSize
          }
        })
      }).then(Hmall.convertResponse('json', this))
          .then(json=> {
            this.setState({
              filters: json.resp[0],
              url: url,
              exist: [],
              rankChoose: "",
              shop: json.resp[1],
              summary: json.resp[2].productSum,
              color: json.resp[0][0].item,
              size: json.resp[0][1].item,
              season: json.resp[0][2].item,
              material: json.resp[0][3].item,
              statePage: num,
              search_flag: false,
              code: categoryCode,
              chooseLow: "",
              chooseHigh: "",
              searchText: "",
              loading_flag: false
            })
          }).catch(Hmall.catchHttpError());
    }
  }

  componentWillMount() {
    let { change_flag, storeCode } = this.props.location.query;
    this.fetchSearchCode();
    Hmall.loadXiaoNeng();
    if (storeCode) {
      this.fetchSearchInfo(this.props);
    } else {
      if (change_flag === "true") {
        this.fetchSearchInfo(this.props);
      } else {
        this.searchInfo(this.props);
      }
    }

  }

  componentWillReceiveProps(nextProps) {
    Hmall.loadXiaoNeng();
    let { pathname, search, query } = nextProps.location, url = pathname + search, { change_flag, storeCode }=query;
    window.scrollTo(0,40);
    if (url != this.state.url) {
      this.setState({loading_flag: true});
      if (storeCode) {
        this.fetchSearchInfo(nextProps);
      } else {
        if (nextProps.location.query.change_flag === "true") {
          this.fetchSearchInfo(nextProps);
        } else {
          this.searchInfo(nextProps);
        }
      }
    }
  }


  fetchChoose(newExist = [], rank = this.state.rankChoose, pageChoose = 1) {

    var colorChoose = [],
        sizeChoose = [],
        seasonChoose = [],
        materialChoose = [],
        { color, size, season, material, search_flag, description, chooseLow, chooseHigh, searchText, code, storeCode } = this.state;

    newExist.forEach((item, i)=> {
      item.items.forEach((item)=> {
        if (color.indexOf(item) != -1) {
          colorChoose.push(item);
        } else if (size.indexOf(item) != -1) {
          sizeChoose.push(item);
        } else if (season.indexOf(item) != -1) {
          seasonChoose.push(item);
        } else if (material.indexOf(item) != -1) {
          materialChoose.push(item);
        }
      })
    });

    browserHistory.push({
      pathname: search_flag ? `/search.html` : `/c/${code}.html`,
      query: {
        description: description || undefined,
        storeCode: storeCode || undefined,
        exist: newExist.length ? JSON.stringify(newExist) : undefined,
        rank: rank || undefined,
        low: chooseLow || undefined,
        high: chooseHigh || undefined,
        change_flag: true,
        searchText: searchText || undefined,
        colorChoose: colorChoose.join(',') || undefined,
        sizeChoose: sizeChoose.join(',') || undefined,
        seasonChoose: seasonChoose.join(',') || undefined,
        materialChoose: materialChoose.join(',') || undefined,
        page: pageChoose
      }
    });

  }

  fetchSearchInfo(props) {

    let { categoryCode } = props.params,
        { pathname, search, query } = props.location,
        { page, rank, description, low, high, searchText, change_flag, exist,
            colorChoose, sizeChoose, seasonChoose, materialChoose, storeCode
            } = query,
        { searchFlag } = props,
        url = pathname + search, pageSize = this.props.displayMode != 'C5' ? 16 : 20,
        colorArr = [], sizeArr = [], seasonArr = [], materialArr = [], pageNum = parseInt(page) || 1;
    if (colorChoose) {
      colorArr = colorChoose.split(",");
    }
    if (sizeChoose) {
      sizeArr = sizeChoose.split(",");
    }
    if (seasonChoose) {
      seasonArr = seasonChoose.split(",");
    }
    if (materialChoose) {
      materialArr = materialChoose.split(",");
    }
    if (exist) {
      exist = JSON.parse(decodeURIComponent(exist));
    } else {
      exist = [];
    }
    if (storeCode) {
      fetch(`${scService}/search/searchWithStoreAndConditions`, {
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),
        method: 'post',
        body: JSON.stringify({
          pageInfo: {
            page: pageNum,
            pageSize: pageSize
          },
          "rank": rank || "",
          "priceRange": {"low": low || 0, "high": high || 0},
          storeCode: storeCode,
          color: colorArr,
          size: sizeArr,
          season: seasonArr,
          material: materialArr,
          insiteDescription: searchText || ""
        }),
      }).then(Hmall.convertResponse('json', this))
          .then(json=> {
            this.setState({
              url: url,
              filters: json.resp[1],
              shop: json.resp[0],
              summary: json.resp[2].productSum,
              color: json.resp[1][0].item,
              size: json.resp[1][1].item,
              season: json.resp[1][2].item,
              material: json.resp[1][3].item,
              statePage: pageNum,
              code: categoryCode,
              exist: exist,
              rankChoose: rank,
              chooseLow: low == 0 ? "" : low,
              chooseHigh: high == 0 ? "" : high,
              change_flag: change_flag,
              loading_flag: false,
              searchText: searchText,
              storeCode: storeCode,
            })
          }).catch(Hmall.catchHttpError());
    } else {
      if (searchFlag != true) {
        fetch(`${scService}/search/searchWithCategoryCodeAndConditions`, {
          headers: Hmall.getHeader({
            "Content-Type": "application/json"
          }),
          method: 'post',
          body: JSON.stringify({
            pageInfo: {
              page: pageNum,
              pageSize: pageSize
            },
            "rank": rank || "",
            "priceRange": {"low": low || 0, "high": high || 0},
            categoryCode: categoryCode,
            color: colorArr,
            size: sizeArr,
            season: seasonArr,
            material: materialArr,
            insiteDescription: searchText || ""
          }),
        }).then(Hmall.convertResponse('json', this))
            .then(json=> {
              this.setState({
                url: url,
                filters: json.resp[1],
                shop: json.resp[0],
                summary: json.resp[2].productSum,
                color: json.resp[1][0].item,
                size: json.resp[1][1].item,
                season: json.resp[1][2].item,
                material: json.resp[1][3].item,
                statePage: pageNum,
                code: categoryCode,
                exist: exist,
                rankChoose: rank,
                chooseLow: low == 0 ? "" : low,
                chooseHigh: high == 0 ? "" : high,
                change_flag: change_flag,
                loading_flag: false,
                searchText: searchText,
              })
            }).catch(Hmall.catchHttpError());
      } else {
        fetch(`${scService}/search/searchWithDescriptionAndConditions`, {
          headers: Hmall.getHeader({
            "Content-Type": "application/json"
          }),
          method: 'post',
          body: JSON.stringify({
            pageInfo: {
              page: pageNum,
              pageSize: pageSize
            },
            rank: rank || "",
            priceRange: {"low": low, "high": high},
            description: description,
            color: colorArr,
            size: sizeArr,
            season: seasonArr,
            material: materialArr,
            insiteDescription: searchText || ""
          }),
        }).then(Hmall.convertResponse('json', this))
            .then(json=> {
              this.setState({
                url: url,
                statePage: pageNum,
                filters: json.resp[0],
                shop: json.resp[1],
                summary: json.resp[2].productSum,
                color: json.resp[0][0].item,
                size: json.resp[0][1].item,
                season: json.resp[0][2].item,
                material: json.resp[0][3].item,
                description: description,
                exist: exist,
                search_flag: searchFlag,
                rankChoose: rank,
                change_flag: change_flag,
                chooseLow: low == 0 ? "" : low,
                chooseHigh: high == 0 ? "" : high,
                loading_flag: false,
                searchText: searchText
              })
            }).catch(Hmall.catchHttpError());
      }
    }

  }

  constructor(props) {
    super(props);
    this.state = this.getInitState();
  }


  close(i) {
    var newExist = this.state.exist;
    newExist.splice(i, 1);
    this.fetchChoose(newExist);
  }

  handleSingleClick(item_choose, title) {
    var newExist = this.state.exist;
    newExist.push({title: title, items: [item_choose]});
    this.fetchChoose(newExist);
  }

  closeAll() {
    this.fetchChoose([]);
  }

  handleSubmit(items, title) {
    var exist = this.state.exist;
    exist.push({title: title, items: items});
    this.setState({chooseName: ""})
    this.fetchChoose(exist);
  }

  handleKeyDown(event) {
    if (event.keyCode == 13) {
      this.fetchChoose(this.state.exist);
    }
  }

  handleSearch() {
    this.fetchChoose(this.state.exist);
  }

  handleChange(event) {
    this.setState({searchText: event.target.value});
  }

  changeHigh(event) {
    var value = event.target.value;
    if (!isNaN(value) && value >= 0) {
      this.setState({chooseHigh: value});
    } else if (value == "") {
      this.setState({chooseHigh: ""});
    }
  }

  blurHigh(event) {
    if (event.target.value != "") {
      var val = parseInt(event.target.value);
      var low = parseInt(this.state.chooseLow);
      if (val < low) {
        alert("请输入大于低价的数！");
        this.setState({chooseHigh: null});
      } else {
        this.setState({chooseHigh: val});
      }
    }
  }

  changeLow(event) {
    var value = event.target.value;
    if (!isNaN(value) && value >= 0) {
      this.setState({chooseLow: value});
    } else if (value == "") {
      this.setState({chooseLow: ""});
    }
  }

  blurLow(event) {
    if (event.target.value != "") {
      var val = parseInt(event.target.value);
      var high = parseInt(this.state.chooseHigh);
      if (val > high) {
        alert("请输入小于高价的数！");
        this.setState({chooseLow: null});
      } else {
        this.setState({chooseLow: val});
      }
    }
  }

  setPaging() {
    let items = [], page = this.state.statePage, { pathname, query } = this.props.location,
        pageNumber = this.getPageNumber(), _query = {};
    if (page == 1) {
      items.push(<Icon name="front" key="1"/>);
    } else {
      for (var key in query) {
        _query[key] = query[key];
      }
      _query.page = page - 1;
      items.push(<Link to={{ pathname, query:_query }} key="1">
        <Icon name="front-black" key="1"/>
      </Link>);
    }
    if (page < pageNumber) {
      for (var key in query) {
        _query[key] = query[key];
      }
      _query.page = page + 1;
      items.push(<Link to={{ pathname, query:_query }} key="2">
        <Icon name="last" key="2"/>
      </Link>);
    } else {
      items.push(<Icon name="last-white" key="2"/>);
    }
    return items;
  }

  getPageNumber() {
    let pageSize = this.props.displayMode != 'C5' ? 16 : 20;
    var num = Math.ceil(this.state.summary / pageSize)
    if (num > 0) {
      return num;
    } else {
      return 1;
    }
  }

  sortPrice(code) {
    if (this.state.rankChoose == code[1].code) {
      this.fetchChoose(this.state.exist, code[0].code);
    } else {
      this.fetchChoose(this.state.exist, code[1].code);
    }
  }

  getSort() {
    let { searchCode } = this.state;
    if(searchCode){
      return searchCode.map((item, i)=> {
        if (item.name == "价格") {
          return <li onClick={()=>{this.sortPrice(item.code)}}>
            价格
            <div className={"sort_price"+(this.state.rankChoose==item.code[1].code?" icon_price_Asc":""+
                        this.state.rankChoose==item.code[0].code?" icon_price_Desc":"")}
                 onClick={()=>{this.sortPrice(item.code)}}
            ></div>
          </li>
        } else {
          return (<li onClick={()=>{this.fetchChoose(this.state.exist,item.code)}}>{item.name}
            <div className={"icon_sort"+(this.state.rankChoose==item.code?" icon_onSort":"")}></div>
          </li>)
        }
      })
    }
  }

  render() {
    var items = this.state.exist.map((item, i)=> {
      var str = "";
      var length = item.items.length;
      item.items.map((item, index)=> {
        if (index != length - 1) {
          str += item + "、";
        } else {
          str += item;
        }
      })
      return (
          <Choose title={item.title} text={str} key={i} closeItem={this.close.bind(this,i)} key={i}/>
      );
    });


    var filters = this.state.filters.map((item, i)=> {
      var {exist} = this.state, flag = true;
      exist.map((child)=> {
        if (typeof (child.title) != "undefined") {
          if (item.name == child.title) {
            flag = false;
          }
        }
      })
      if (flag) {
        return (
            <ChooseList name={item.name} key={i} items={item.item} exist={exist}
                        submit={(items,title)=>this.handleSubmit(items,title)} loadingFlag={this.state.loading_flag}
                        choose={(name)=>{this.setState({chooseName:name})}}
                        cancel={()=>{this.setState({chooseName:""})}}
                        singleClick={(it,title)=>{this.handleSingleClick(it,title)}}
                        flag={item.name==this.state.chooseName}>
            </ChooseList>
        )
      }

    });

    let { description,searchText } = this.state;

    return (
        <div className="padding_content">
          {!!this.state.shop.length &&
          <Panel>
            <div className="list_1">
              <div className="exist_span">
                <span >全部结果</span><Icon name="allResult"></Icon>
              </div>
              <div className="div_item">
                {items}
                <Search onKeyDown={(event)=>{this.handleKeyDown(event)}} onChange={(event)=>{this.handleChange(event)}}
                        search={()=>{this.handleSearch()}}
                        text={this.state.searchText}/>
              </div>
              <div className="div_4">
                <span style={{display:this.state.exist.length!=0?"":"none"}}
                      onClick={this.closeAll.bind(this)}>清空筛选项</span>
                <Icon name="clear"></Icon>
              </div>
            </div>
            {filters}
          </Panel>
          }
          <Row>
            {
              this.props.displayMode != 'C5' ?
                  <Col width={270}>
                    <ClassificationGoods/>
                    <RecommendGoods />
                  </Col> : <Col width={0}></Col>
            }
            <Col style={this.props.displayMode == 'C5'? undefined: {marginLeft: 10}}>
              <div className="div_sort">
                <ul className="ul_sort">
                  { this.getSort() }
                </ul>
                <div className="div_price">
                  <div className="input_low">
                    <span className="icon_sale">￥</span>
                    <input type="text" value={this.state.chooseLow} onChange={(e)=>{this.changeLow(e)}}
                           onBlur={(e)=>{this.blurLow(e)}}/>
                  </div>
                  <span className="span_line">-</span>
                  <div className="input_low">
                    <span className="icon_sale">￥</span>
                    <input type="text" value={this.state.chooseHigh} onChange={(e)=>{this.changeHigh(e)}}
                           onBlur={(e)=>{this.blurHigh(e)}}/>
                  </div>
                  <input type="button" value="确定" className="button_b"
                         onClick={()=>{this.fetchChoose(this.state.exist)}}/>
                </div>
                <div className="div_number">
                  <span className="summary_span">共<span
                      style={{fontWeight:"bold",color:"#000000"}}>{this.state.summary}</span>件商品</span>
                  <span style={{color:"#ff0000"}}>{this.state.statePage}</span>
                  <span>/{this.getPageNumber()}</span>
                  {this.setPaging()}
                </div>
              </div>
              {this.state.loading_flag ? <div className="loading loading_width"></div> :
                  this.state.shop.length == 0 ?
                      <div className="search_nothing">
                        <div className="center_div">
                          <div className="bold_span">没有找到
                            {description != "" && searchText != "" ? <span
                                style={{color:"#ff0000"}}>与"{this.state.description} {this.state.searchText}"</span> : ""}
                            相关的宝贝
                          </div>
                        </div>
                      </div> :
                      <div>
                        <ProductGroup items={this.state.shop} displayMode={this.props.displayMode}></ProductGroup>
                        <Panel className="clearfix">
                          <Pagenation page={this.state.statePage}
                                      pagesize={this.props.displayMode != 'C5'?16:20}
                                      total={parseInt(this.state.summary)} location={this.props.location}>
                          </Pagenation>
                        </Panel>
                      </div>
              }
            </Col>
          </Row>
          <History></History>
        </div>
    );
  }

}