import React from 'react';
import { Link } from 'react-router';
import { Panel } from './Layout';

const Category = (props) => {
  
  const renderCategories = (categories) => {
    if(categories && categories.length){
      return (
        <ul>
        {
          categories.map( (item,index) => {
            let { code, name, categories, expand } = item;
            return (
              categories && categories.length ? 
                <li key={index} className={ `submenu${expand == 'Y'?'':' collapse'}` }>
                  <i onClick={e => toggleExpand(e,item)}></i>
                  <Link to={`/c/${code}.html`} activeClassName="active">
                    {name}
                  </Link>
                  {renderCategories(categories)}
                </li> :
                <li key={index}>
                  <Link to={`/c/${code}.html`} activeClassName="active">
                    {name}
                  </Link>
                </li>
            )
          })
        }
        </ul>);
    }
  },
  toggleExpand = (e, item) => {
    e.target.parentNode.classList.toggle('collapse');
    item.expand = item.expand == 'Y' ? 'N' : 'Y';
  }
  
  return <Panel className="h-component h-category">
    <div className="panel-header line">
      <span>商品分类</span>
    </div>
    {renderCategories(CMSConfig.categories)}
  </Panel>;
};

export default Category;