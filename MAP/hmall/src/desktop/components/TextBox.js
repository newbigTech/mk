import React, { Component, PropTypes } from 'react';
import onClickOutside from 'react-onclickoutside';

let _fetchId;

class SimpleTextBox extends Component{
  static propTypes = {
    placeholder: PropTypes.string,
    width: PropTypes.number,
    height: PropTypes.number,
    maxLength: PropTypes.number,
    name: PropTypes.string,
    readOnly: PropTypes.bool,
    className: PropTypes.string,
    autoCompleteService: PropTypes.string,
    autoCompleteField: PropTypes.string,
    onClick: PropTypes.func
  }
  
  static defaultProps = {
    autoCompleteField: 'word',
    className: 'default'
  }
  
  constructor(props) {
    super(props);
    this._cache = {}
    this.state = {
      value: props.value,
      autoCompleteItems: [],
      popup: false,
      selectedIndex: -1
    }
  }
  
  componentWillReceiveProps(nextProps) {
    let { value } = nextProps;
    if(value != this.props.value && value != this.state.value)
      this.setState({value});
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  get type() {
    return 'text';
  }
  
  fixPasswordBug() {
    return null;
  }
  
  firstChild() {
    let { children, className='icon-left' } = this.props;
    return React.Children.toArray(children).map((child, index) => {
      if(index == 0 && (children.length > 1 || className.indexOf('icon-right') == -1)) {
        let { className='', style={}, onClick } = child.props;
        if(onClick) {
          style.zIndex = 1;
        }
        return React.cloneElement(child, {
          className: 'left '+ className,
          style
        });
      }
    });
  }

  secondChild() {
    let { children, className='icon-left' } = this.props;
    return React.Children.toArray(children).map((child, index) => {
      if(children.length > 1 ? index == 1 : className.indexOf('icon-right') != -1) {
        let { className='', style={}, onClick } = child.props;
        if(onClick) {
          style.zIndex = 1;
        }
        return React.cloneElement(child, {
          className: 'right '+ className,
          style
        });
      }
    });
  }
  
  handleChange(e) {
    let { readOnly, onChange, autoCompleteService, autoCompleteField } = this.props,
      { value } = e.target; 
    if(readOnly) return;
    this.setState({ value });
    onChange && onChange(e);
    autoCompleteService && this.fetchRecords(value, autoCompleteService, autoCompleteField);
  }
  
  fetchRecords(value, svc, field) {
    if(_fetchId) {
      clearTimeout(_fetchId);
    }
    if(value) {
      let resp;
      if(resp = this._cache[value]){
        this.setData(resp);
      }else{
        _fetchId = setTimeout(() => {
          _fetchId = null;
          fetch(svc,{
            method: 'post',
            headers: Hmall.getHeader({"Content-Type": "application/json"}),
            body: JSON.stringify({
              [field]: value
            })
          })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            let { success, resp } = json;
            if(success){
              this._cache[value] = resp;
              this.setData(resp);
            }
          });
        }, 500, this);
      }
    }else{
      this.setData([]);
    }
  }
  
  setData(resp) {
    this.setState({autoCompleteItems: resp, popup: !!resp && !!resp.length});
  }
  
  handleKeyDown(e) {
    let { onEnter } = this.props,
      { popup, selectedIndex, autoCompleteItems } = this.state,
      { keyCode } = e;
    if(popup){
      if(keyCode == 38){
        e.preventDefault();
        if(selectedIndex > 0){
          this.setState({
            selectedIndex: selectedIndex - 1
          });
        }
      }else if(keyCode == 40){
        e.preventDefault();
        if(selectedIndex < autoCompleteItems.length - 1){
          this.setState({
            selectedIndex: selectedIndex + 1
          });
        }
      }else if(keyCode == 13 && selectedIndex != -1) {
        e.preventDefault();
        this.handleSelect(selectedIndex);
      }
    }else if(onEnter && keyCode == 13){
      e.preventDefault();
      onEnter(e);
    }
  }
  
  handleFocus(e) {
    let { onFocus, autoCompleteService } = this.props,
      { autoCompleteItems } = this.state;
    if(autoCompleteService && autoCompleteItems.length) {
      this.setState({
        popup: true
      })
    }
    onFocus && onFocus(e);
  }
  
  handleBlur(e) {
    let { onBlur } = this.props;
    onBlur && onBlur(e);
  }
  
  handleClickOutside() {
    if(this.state.popup) {
      this.setState({
        popup: false
      });
    }
  }
  
  handleSelect(index) {
    let { onSelect } = this.props,
      { autoCompleteItems } = this.state,
      value = autoCompleteItems[index];
    this.setState({
      value, 
      autoCompleteItems: [],
      selectedIndex: -1,
      popup: false
    });
    onSelect && onSelect(value);
  }
  
  render() {
    let { name, placeholder, style={}, className, readOnly, width, height, onClick, maxLength } = this.props,
      { value, autoCompleteItems, popup, selectedIndex } = this.state,
      readonlyClass = readOnly ? ' readonly':'',
      firstChild = this.firstChild(),
      secondChild = this.secondChild();
    if(firstChild.length){
      className += ' icon-left';
    }
    if(secondChild.length){
      className += ' icon-right';
    }
    style.width = width;
    style.height = height;
    return (
      <div className={`h-text-box ${className}`} style={style}>
        {firstChild}
        {this.fixPasswordBug()}
        <input className={readonlyClass} type={this.type} maxLength={maxLength} placeholder={placeholder} name={name} readOnly={readOnly} onKeyDown={e => this.handleKeyDown(e)} onChange={e => this.handleChange(e)} onFocus={e => this.handleFocus(e)} onBlur={e => this.handleBlur(e)} value={value} onClick={onClick} autoComplete="off"></input>
        {secondChild}
        {
          popup && !!autoCompleteItems.length && <div className="h-popup">
            <ul>
              {
                autoCompleteItems.map((item, index) => {
                  return <li key={index} onMouseOver={() => this.setState({selectedIndex: index})} className={index == selectedIndex && 'selected'} onClick={() => this.handleSelect(index)}><div>{item}</div></li>;
                })
              }
            </ul>
          </div>
        }
      </div>
    );
  }
}
const TextBox = onClickOutside(SimpleTextBox);
export { TextBox as default, SimpleTextBox };