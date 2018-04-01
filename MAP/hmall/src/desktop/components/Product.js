import React, { Component, PropTypes } from 'react';
import { Link } from 'react-router';
import Picture from './Picture';

export default class Product extends Component {
  
  static propTypes = {
    width: PropTypes.number,
    loadPicture: PropTypes.bool,
    info: PropTypes.shape({
      productCode: PropTypes.string,
      label: PropTypes.string,
      productName: PropTypes.string,
      colorPic: PropTypes.arrayOf(PropTypes.string),
      stock: PropTypes.string,
      name: PropTypes.string
    }).isRequired
  }
  
  static defaultProps = {
    loadPicture: true
  }

  constructor(props) {
    super(props);
    this.state={
      colorsShow: false,
      markShow: false,
      selectedPic : this.props.info.mainPic,
      offsetHeight: 0,
    };
  }


  getColors(colorPic, count) {
    if(colorPic && colorPic.length){
      let { offsetHeight, selectedPic, colorsShow } = this.state;
      return <div className="cover" style={{height:offsetHeight}}>
        <div className="cover-color">
        {
          colorPic.map((color,i) => {
            return <Picture onClick={e => {e.preventDefault();this.setState({ selectedPic: color })}} key={i} lazy={false} load={colorsShow} src={Hmall.cdnPath(color)} className={ selectedPic == color ? 'selected' : null}></Picture>;
          })
        }
        </div>
      </div>;
    }
  }

  renderCornerMark(label) {
    if(label && (label = CMSConfig[label])){
      let { urlPath, position } = label;
      return <img className={`corner-mark ${position}`} src={Hmall.cdnPath(urlPath)} />;
    }
  }

  renderSaleOut(stock){
    if( stock=="N" ){
      let label = CMSConfig["sold_out"];
      return <img className={`corner-mark ${label.position}`} src={Hmall.cdnPath(label.urlPath)} />;
    }
  }

  handleMouseEnter(count) {
    this.setState({ offsetHeight:43 + count*39, colorsShow: true });
  }
  
  render(){
    let { width, info, loadPicture } = this.props,
      { markShow, selectedPic } = this.state,
      { productCode, label, productName, price, colorPic, stock , name} = info,
      height = width && width - 20,count;
    if(colorPic && colorPic.length){
      count = Math.floor(colorPic.length/6);
    }
    return <div className="h-product">
      <div style={{width}} className="product-content">
        <div className="product-background" onMouseEnter={() => this.handleMouseEnter(count)} onMouseLeave={()=>{this.setState({offsetHeight:0})}}>
          <Link to={"/product-detail.html?productCode="+productCode}>
            <Picture lazy={loadPicture} load={loadPicture} onLoad={() => this.setState({markShow: true})} src={Hmall.cdnPath(selectedPic)} className="background-pic" height={height}></Picture>
            {markShow && this.renderSaleOut(stock)}
            {markShow && this.renderCornerMark(label)}
          </Link>
          {this.getColors(colorPic, count)}
        </div>
        <Link to={"/product-detail.html?productCode="+productCode}>
        <p>{productName || name} </p>
        <h3 className="product-price" style={{color: label ? "#f00" : null}}>￥{Number(price).toFixed(2)}</h3>
        </Link>
      </div>
    </div>;
  }
}