import React,{ Component } from 'react';
import Dropzone from 'react-dropzone';
import Button from './Button';


export default class PicUpload extends Component {

  constructor(props) {
    super(props);
    this.state = {
      img: [],
      nFile: this.props.defaultPath || [],
      uploadState: this.props.uploadState,
    }
  }

  componentWillReceiveProps(nextProps) {
    if(this.props.uploadState!=nextProps.uploadState){
      this.setState({nFile: this.props.defaultPath || []});
    }
  }



  onImageDrop(files) {
    if (files.length > 0) {
      let type = files[0].type;
      if (files[0].size < 10485760) {
        let { nFile } = this.state;
        if (nFile.length < 3) {
          nFile.push(files[0]);
          this.setState({nFile});
        } else {
          alert("图片超过三张,点击图片可删除图片");
        }
      } else {
        alert("单张图片不能超过10M");
      }
    } else {
      alert("请输入正确的格式！");
    }

  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  handleImageUpload() {
    let { nFile } = this.state,
        files = new FormData();
    nFile.forEach(file => {
      files.append('files', file);
    })
    fetch(`${fileService}/uploadImgs`, {
      method: 'post',
      headers: Hmall.getHeader({}),
      body: files
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          if (json.success) {
            this.props.getImage(json.resp);
            alert("图片上传成功！！");
          } else {
            if (json.msgCode == "UPLOAD_01") {
              alert("上传文件为空");
            } else if (json.magCode == "UPLOAD_02") {
              alert("上传文件数量超长");
            } else if (json.msgCode == "UPLOAD_03") {
              alert("上传文件类型不匹配");
            } else if (json.msgCode == "UPLOAD_04") {
              alert("单个文件大小不能超过10MB");
            } else if (json.msgCode == "UPLOAD_05") {
              alert("上传文件总大小不能超过30MB");
            }
          }
        })
        .catch(Hmall.catchHttpError())
  }

  deletePic(e,file) {
    let { nFile } = this.state,{deletePic} = this.props;
    nFile.splice(e.target.id, 1);
    if(!file.preview){
      deletePic(file)
    }
    this.setState({nFile});
  }

  showPic() {
    let { nFile } = this.state;
    if (nFile.length !== 0) {
      return nFile.map((file, index) => {
        return (
            <div className="img-float">
              <img width={120} src={file.preview||file}></img>
              <div className="delete_div" id={index} onClick={(e) => this.deletePic(e,file)}>点击可删除图片</div>
            </div>
        );
      })
    } else {
      return '';
    }
  }

  render() {
    return (
        <div className="FileUpload clearfix">
          <div className="clearfix">
            {this.showPic()}
          </div>
          <Dropzone className="dropzone" accept="image/*" multiple={false}
                    onDrop={(files) => {this.onImageDrop(files)}}>
            <span className="notice notice-word" style={{lineHeight:"38px"}}>请点击或拖拽到该区域选取图片</span>
          </Dropzone>
          <div className="button-upload">
            <Button onClick={() => this.handleImageUpload()} text="上传" width={80} heigth={30}></Button>
          </div>
        </div>
    );
  }
}