import React, { PropTypes } from 'react';
import TextBox from './TextBox';
import Icon from './Icon';

const SearchBox = (props) => {
  const { className, onSearch } = props,
    handleSearch = () => {
      onSearch(value);
    };
  let value = '';
  return <TextBox {...props} onEnter={e => handleSearch(e.target.value)} onChange={e => value = e.target.value} className={`h-search-box ${className}`} onSelect={handleSearch}>
    <Icon name="search" className="head-icon" onClick={onSearch && handleSearch}></Icon>
  </TextBox>;
};

SearchBox.propTypes = TextBox.propTypes;

export default SearchBox;