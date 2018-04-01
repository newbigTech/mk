import React, { PropTypes } from 'react';

const Panel = (props) => {
  const { id, width, children, style={}, className } = props;
  style.width = width;
  return <div id={id} className={`h-panel ${className}`} style={style}>
    {children}
  </div>;
},
Row = (props) => {
  const { id, children, style, className } = props,
    renderChildren = () => {
      let width = 0;
      return React.Children.map(children, (child,index) => {
        if( index != ((children.length || 1) - 1)){
          width += child.props.width || 0; 
        }else{
          let { style={} } = child.props;
          style.paddingLeft = width;
          delete style.width;
          child = React.cloneElement(child,{
            style
          });
        }
        return child;
      })
    }
  return (
    <div id={id} className={`h-row clearfix ${className}`} style={style}>
      {renderChildren()}
    </div>
  );
},
Col = (props) => {
  const { id, width, children, style={}, className } = props;
  style.width = width;
  return <div id={id} className={`h-col ${className}`} style={style}>
    {children}
  </div>; 
};

Panel.propTypes = Col.propTypes = {
  id: PropTypes.string,
  width: PropTypes.number,
  className: PropTypes.string,
  style: PropTypes.object
}

Row.propTypes = {
  id: PropTypes.string,
  className: PropTypes.string,
  style: PropTypes.object
}

Panel.defaultProps = Row.defaultProps = Col.defaultProps = {
  className: ''
}

export { Panel, Row, Col };