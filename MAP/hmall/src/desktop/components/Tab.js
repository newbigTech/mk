import React, { Component, PropTypes } from 'react';
import { Link } from 'react-router';

class TabPanel extends Component{
  static propTypes = {
    width: PropTypes.number
  }
  
  constructor (props) {
    super(props);

    let currentIndex = 0;
    React.Children.forEach(this.props.children, (element, index) => {
      if(element.props.active){
        currentIndex = index;
        return false;
      }
    });
    
    this.state = { currentIndex };
  }
  
  getActiveClasses(index){
    return index == this.state.currentIndex? 'active' : null;
  }
  
  childrenHead() {
    return React.Children.map(this.props.children, (element, index) => 
      (<div className={this.getActiveClasses(index)} style={{width: element.props.width}}
            onClick={() => {if(element.props.onClick!=null){element.props.onClick()}this.setState({currentIndex: index})}}>
        <h2>{element.props.title}</h2></div>)
    );
  }
  
  childrenContent() {
    let content = null;
    React.Children.forEach(this.props.children, (element, index) =>{
      if(index == this.state.currentIndex){
        content = (<div className='active'>{element.props.children}</div>)
      }
    })
    return content;
  }
  
  render() {
    return (
      <div className="h-tab-panel" id={this.props.id} style={{width: this.props.width}} onClick={this.props.onClick}>
        <div className="h-tab-head clearfix">
          {this.childrenHead()}
        </div>
        <div className="h-tab-body">
          {this.childrenContent()}
        </div>
      </div>
    );
  }
}

const Tab = () => {};
Tab.propTypes = {
  title: PropTypes.string,
  width: PropTypes.number,
  active: PropTypes.bool
};

export { TabPanel, Tab } 