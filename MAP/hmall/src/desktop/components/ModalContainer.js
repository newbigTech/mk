import React,{ Component, PropTypes } from 'react';
import Modal from './Modal';

const IDGenerator = function* () {
  let id = 1;
  while(true){
    yield id++;
  }
}();

export default class ModalContainer extends Component{
  
  constructor(props) {
    super(props);
    this.state = {
      modals: []
    }
  }
  
  dialog(option) {
    const { modals } = this.state;
    if(!option.id) {
      option.id = IDGenerator.next().value;
    }
    modals.push(option);
    this.setState({modals});
  }
  
  close(id) {
    this.setState({modals: this.state.modals.filter(modal => modal.id != id)});
  }
  
  clear() {
    this.setState({modals:[]});
  }
  
  renderModals() {
    return this.state.modals.map((modal,index) => {
      return <Modal onClose={() => this.close(modal.id)} key={modal.id} {...modal} zIndex={1000+index}></Modal>;
    });
  }
  
  render() {
    return <div className="modal-container">{
      this.renderModals()
    }</div>;
  }
}
