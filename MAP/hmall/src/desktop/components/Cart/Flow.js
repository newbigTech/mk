/**
 * Created by ZWL on 2016/11/8.
 */
import React,{ Component, PropTypes } from 'react';

export default class Flow extends Component {
  
  static propTypes = {
    count: PropTypes.number
  }
  
  static defaultProps = {
    count: 3
  }
  
  render(){
    let { count } = this.props
    return(
    <div className="cart-flow">
      <ul>
        {
          ['查看购物车','订单确认','订单支付'].map((text,index) => {
            return <li key={index} className={index == count - 1 && 'active'}><div>{index + 1}</div><span>{text}</span></li>
          })
        }
      </ul>
    </div>
    )
  }
}