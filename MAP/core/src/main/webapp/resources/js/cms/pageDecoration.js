!function(CMS,K){
  
  function init(props){
    contextPath = props.contextPath;
    fileServer = props.fileServer;
    csrf = props.csrf;
    createData();
    loadGlobalProps(function(){
      initComboBox(kendo.observable({
        data: {
          code: props.code || 'index'
        }
      }));
    });
    initToolBar();
    initColorPicker();
    initBackgroundImage();
    initBackgroundFit();
    initBackgroundFixed();
    initDragAndDrop();
  }
  
  function mergePage(data, page) {
    if(data.__status == 'update' && page){
      for(var key in page) {
        if(key == 'components'){
          var components =  [];
          $.each(page.components, function(index2,cmp2){
            $.each(data.components,function(index,cmp){
              if(cmp.__status != 'delete' && cmp2.templateContentSlot == cmp.templateContentSlot){
                if(cmp.componentObject){
                  var cmplines =  cmp.componentObject.productGroupLines,
                    cmp2lines = cmp2.componentObject.productGroupLines,
                    lines = [];
                  if(cmplines) {
                    $.each(cmplines, function(index3,line){
                      $.each(cmp2lines, function(index4,line2){
                        if(line.__status != 'delete' && line.product == line2.product){
                          line2.id = line2.pk;
                          lines.push($.extend(true, line, line2));
                        }
                      });
                    });
                    cmp.componentObject.productGroupLines = cmp2.componentObject.productGroupLines = lines;
                  }
                }
                components.push($.extend(true, cmp, cmp2));
                return false;
              }
            });
          });
          data.components = components;
        }else{
          data[key] = page[key];
        }
      }
      data.__status = null;
    }
    global.changes = [];
  }
  
  function getGlobalChanges(){
    if(global.changes && global.changes.length){
      var changes = [];
      $.each(global.changes, function(index, change){
        changes.push($.extend({
          code: change,
        },global[change]));
      });
      return changes;
    }
    return null;
  }
  
  function createData() {
    viewModel = kendo.observable({
      preview: function(){
        var me = this,
          data = me.data;
        if(data.__status == 'update' || global.changes.length){
          $.ajax({
            type: 'POST',
            url: contextPath + '/cms/pageDecoration/preview',
            dataType   : "json",
            contentType: "application/json",
            data: JSON.stringify({
              page: data.__status == 'update' ? data.toJSON() : null,
              globals: getGlobalChanges()
            }),
            success: function(resp){
              if(resp.success){
                mergePage(data, resp.page);
                $(window).off('beforeunload.'+data.code);
                window.open(data.homePage == 'Y' ? '/' : data.url,'preview').focus();
              }else{
                kendo.ui.showErrorDialog({
                  title: $l('hap.error'),
                  message: resp.message
                });
              }
            }
          });
        }else {
          window.open(data.homePage == 'Y' ? '/' : data.url,'preview').focus();
        }
      },
      save: function(){
        var me = this,
          data = me.data;
        if(data.__status == 'update' || global.changes.length){
          $.ajax({
            type: 'POST',
            url: contextPath + '/cms/pageDecoration/submit',
            dataType   : "json",
            contentType: "application/json",
            data: JSON.stringify({
              page: data.__status == 'update' ? data.toJSON() : null,
              globals: getGlobalChanges()
            }),
            success: function(resp){
              if(resp.success){
                mergePage(data, resp.page);
                $(window).off('beforeunload.'+data.code);
                kendo.ui.showInfoDialog({
                  title: $l('hap.tip.info'),
                  message: '保存成功'
                });
              }else{
                kendo.ui.showErrorDialog({
                  title: $l('hap.error'),
                  message: resp.message
                });
              }
            }
          });
        }
      },
      publish: function(){
        var me = this,
        data = me.data;
        $.ajax({
          type: 'POST',
          url: contextPath + '/cms/pageDecoration/publish',
          dataType   : "json",
          contentType: "application/json",
          data: JSON.stringify({
            page: data.approveStatus == 'UNPUBLISH' || data.__status == 'update' ? data.toJSON() : null,
            globals: getGlobalChanges()
          }),
          success: function(resp){
            if(resp.success){
              mergePage(data, resp.page);
              $(window).off('beforeunload.'+data.code);
              kendo.ui.showInfoDialog({
                title: $l('hap.tip.info'),
                message: '发布成功'
              });
            }else{
              kendo.ui.showErrorDialog({
                title: $l('hap.error'),
                message: resp.message
              });
            }
          }
        });
      }
    }).bind('change', function(e){
      var field = e.field,
        value = this.get(field);
      if(field == 'data'){
        loadPage(value);
      }else{
        var data = this.data;
        if(!data.__status == 'update') {
          $(window).on('beforeunload.'+data.code,function(){
            return true;
          });
        }
        data.__status = 'update';
        switch(field){
          case 'data.backgroundColor':
            currentPage.setBackgroundColor(value);
            break;
          case 'data.backgroundImagePath':
            currentPage.setBackgroundImage(value);
            break;
          case 'data.backgroundFit':
            currentPage.setBackgroundFit(value);
            break;
          case 'data.backgroundFixed':
            currentPage.setBackgroundFixed(value);
            break;
        }
      }
    });
  }
  
  function initToolBar() {
    kendo.bind($('#toolbar-button'),viewModel);
  }
  
  function initDragAndDrop() {
    $('#components-container').mousedown(function(e){
      e.preventDefault();
      var target = $(e.target);
      if(target.is('.component')||(target = target.parent('.component'))){
        var componentType = target.attr('data-component');
        if(componentType == 'ProductGroup'){
          draggItem = {
            componentType: componentType,
            displayMode: 'C4'
          };
        }else if(componentType == 'ProductGroup2'){
          draggItem = {
            componentType: 'ProductGroup',
            displayMode: 'C5'
          };
        }else{
          draggItem = {
            componentType: componentType
          };
        }
        $('body').addClass('dragging').on('mousemove.drag',function(e){
          e.preventDefault();
          var target = $(e.target),
            templateContentSlot = draggItem.templateContentSlot,
            newTemplateContentSlot = target.is('.cms-content-slot') && target.parent().attr('data-component'),
            wrapper = currentPage.wrapper;
          if(newTemplateContentSlot != templateContentSlot) {
            if(templateContentSlot){
              $('[data-component='+templateContentSlot+'] .cms-content-slot', wrapper).removeClass('hover');
              delete draggItem.templateContentSlot;
            }
            if(newTemplateContentSlot){
              var newTemplateContentSlot = target.parent().attr('data-component');
                target.addClass('hover');
                draggItem.templateContentSlot = newTemplateContentSlot;
            } 
          }
        }).on('mouseup.drag',function(){
          e.preventDefault();
          $('body').removeClass('dragging').off('.drag');
          if(draggItem.templateContentSlot){
            currentPage.addComponent(draggItem);
          }
          draggItem = null;
        });
      }
    });
  }
  
  function initColorPicker() {
    $("#backgroundColor").show().kendoColorPicker({
      autoupdate: false,
      messages: {
        apply  : "应用",
        cancel : "取消"
      }
    });
    kendo.bind($("#backgroundColor"),viewModel);
  }
  
  function initBackgroundImage() {
    $("#backgroundImage input[type=file]").kendoUpload({
      async: {
        saveUrl: contextPath + '/cms/pageDecoration/upload?' + csrf.parameterName + '=' + csrf.token
      },
      showFileList: false,
      multiple: false,
      upload: function(e){
        e.data = {
          sourceType: '',
          sourceKey: '',
          page: viewModel.get('data').pk
        }
      },
      success: function (e) {
        var response = e.response,
          file = response.file;
        if(response.success){
          viewModel.set('data.backgroundImage', file.fileId);
          viewModel.set('data.backgroundImagePath', file.filePath);
        }
      }
    }); 
    
    $('#backgroundImage span').click(function(){
      viewModel.set('data.backgroundImage',null);
      viewModel.set('data.backgroundImagePath', null);
    });
    kendo.bind($("#backgroundImage"),viewModel);
  }
  
  function initBackgroundFit() {
    $('#backgroundFit').kendoDropDownList({
      dataSource: cmsBackgroundFit,
      dataTextField: 'meaning',
      dataValueField: 'value'
    });
    kendo.bind($("#backgroundFit"),viewModel);
  }
  
  function initBackgroundFixed() {
    $("#backgroundFixed").kendoCheckbox({
      checkedValue: 'Y',
      uncheckedValue: 'N'
    });
    kendo.bind($('#backgroundFixed'), viewModel);
  }
  
  function initComboBox(data) {
    var pageListTplt = ['<div class="popup-page-list">',
      '<div class="popup-head">',
        '<div class="pull-right">',
          '<span class="btn btn-default">新建页面</span> ',
          '<span class="btn btn-default">管理页面</span>',
        '</div>',
      '</div>',
      '<div class="popup-body">',
        '#= pageGroups#',
      '</div>',
    '</div>'],
    pageGroupTplt = ['<div class="page-group">',
      '<div class="page-group-head">#: head#</div>',
      '<div class="page-group-body">',
        '<ul class="k-list k-reset">#= items#</ul>',
      '</div>',
    '</div>'];
    kendo.bind($('#page-list-combobox').kendoDropDownList({
      dataSource: {
        transport: {
          read: contextPath + '/cms/pageDecoration/queryPageList'
        },
        schema: {
          data: 'rows'
        }
      },
      dataTextField: 'name',
      dataValueField: 'code',
      open: function(){
        var me = this,
          element = me.popup.element,
          code = me.dataItem().code;
        element.width(600);
        if(!me.popuprenderEditored){
          var pageTypeList = [
              {code: 'base_page', name: '基础页'},
              {code: 'product_list_page', name: '产品列表页'},
              {code: 'custom_page', name: '自定义页'},
              {code: 'activity_page', name: '活动页'}
            ],
            template = kendo.template(pageListTplt.join('')),
            groupTemplate = kendo.template(pageGroupTplt.join('')),
            itemTemplate = kendo.template('<li tabindex="-1" unselectable="on" class="k-item#:selected#" role="option" data-offset-index="#:index#">#:name#</li>'),
            datas = me.dataItems();
          element.find('.k-list-scroller').html(template({
            pageGroups: $.map(pageTypeList,function(type){
              return groupTemplate({
                head: type.name,
                items: $.map(pageGroupfilter(datas,type.code),function(item){
                  return itemTemplate({
                    index: item.index,
                    name: item.page.name,
                    selected: item.page.code == code ? ' selected' : ''
                  });
                }).join('')
              })
            }).join('')
          })).on('click','li.k-item',function(e){
            element.find('.k-list-scroller .selected').removeClass('selected');
            $(e.target).addClass('selected');
            me._select(e.target).done(function(){
              me._blur();
            });
          });
          me.popuprenderEditored = true;
        }
      },
      dataBound: function(){
        if(!firstLoad){
          loadData.call(this);
          firstLoad = true;
        }
      },
      change: loadData,
      height: 400
    }), data);
  }
  
  function loadData() {
    viewModel.set('data',this.dataItem());
  }
  
  function loadPage(data){
    if(data.components && data.template){
      renderEditorPage(data);
      currentPage.setBackgroundImage(data.backgroundImagePath);
    }else{
      loadComponents(data);
      loadTemplate(data);
    }
  }

  function loadGlobalProps(callback) {
    $.ajax({
      url: contextPath + '/cms/pageDecoration/queryGlobalProps',
      success: function(resp){
        global = resp;
        global.changes = [];
        callback();
      }
    });
  }
  function loadComponents(data) {
    $.ajax({
      url: contextPath + '/cms/pageDecoration/queryComponents?page='+data.pk,
      success: function(components){
        data.components = components;
        renderEditorPage(data);
      }
    })
  }
  function loadTemplate(data) {
    $.ajax({
      url: contextPath + '/cms/templates/'+data.pageTemplateCode+'.html',
      success: function(template){
        data.template = template;
        renderEditorPage(data);
      }
    })
  }

  function renderGlobalComponents(page) {
    var header = page.find('header'),
      footer = page.find('footer'),
      category = page.find('[data-category]'),
      vRecommend = page.find('[data-v-recommend]');
    header.length && header.html('<div class="h-component h-custom">'+clipLink(global.header)+'</div>');
    footer.length && footer.html('<div class="h-component h-custom">'+clipLink(global.footer)+'</div>');
    category.length && category.html(CategoryEditor.prototype.getPreview(global.categories));
    vRecommend.length && vRecommend.html(VRecommendEditor.prototype.getPreview(global.recommends));
  }
  
  function renderEditorPage(data){
    var page = pages[data.code];
    currentPage && currentPage.hide();
    if(page){
      currentPage = page.show();
    }else if(data.components && data.template){
      currentPage = new Page(data);
    }
  }
  function pageGroupfilter(datas,pageTypeCode){
    var filters = [];
    $.each(datas,function(index,page){
      if(page.pageTypeCode == pageTypeCode){
        filters.push({
          index: index,
          page: page
        });
      }
    });
    return filters;
  }
  function padNum(num) {
    return String(num).length == 1 ? '0' + num : num;
  }
  
  var viewModel,
    firstLoad,
    draggItem,
    contextPath,
    fileServer,
    csrf,
    currentPage,
    pages = {},
    global = {},
    globalEditors = {},
    clipLink = function(text){
      return text.replace(/(<a[^>]*)href=([\'\"])(.*?)\2([^>]*>)/ig,function(t,$1,$2,$3,$4){
        return $1+$4;
      });
    },
    productTplt = function(product) {
      var width = product.width,
        height = width && width - 20,
        label = global[product.label];
      return [
        '<li class="h-product" >',
        '<div class="product-content"',width ? ' style="width:'+width+'px"' : '','>',
          '<div class="product-background">',
            label ?'<img class="corner-mark ' + label.position+'" src="' + fileServer + label.urlPath+'">' : '',
            '<img src="',fileServer,product.mainPic,'" class="background-pic"',height ? ' style="height:'+height+'px"' : '','>',
          '</div>',
          '<p>',product.productName,'</p>',
          '<h3 class="product-price">',
            '￥',product.price,
          '</h3>',
        '</div>',
      '</li>'].join('');
    },
    Page = kendo.Observable.extend({
      init: function(data){
        var me = this,
          code = data.code,
          components = data.components,
          element = $('<div id="page-'+code+'" class="page-wrapper '+data.pageTemplateCode+'"></div>')
            .appendTo($('#page-container')).html(data.template);
        if(code == 'login'){
          $('div[data-code=login]', element).show();
          $('div[data-code=register]', element).remove();
        }
        if(code == 'register'){
          $('div[data-code=register]', element).show();
          $('div[data-code=login]', element).remove();
        }
        me.wrapper = element;
        pages[code] = me;
        me.components = {};
        element.find('[data-component]').each(function(slot){
          var templateContentSlot = (slot = $(this)).attr('data-component'),
            editor;
          if(templateContentSlot == 'loginRegisterBanner' || templateContentSlot == 'passwordFindBanner' || templateContentSlot == 'productDetailBanner' ){
            if(editor = globalEditors[templateContentSlot]){
              editor.addWrap(templateContentSlot, me);
            }else{
              globalEditors[templateContentSlot] = new BannerEditor({
                templateContentSlot: templateContentSlot,
                componentObject: global[templateContentSlot]
              },me,true);
            }
          }else{
            slot.html('<div class="cms-content-slot">'+templateContentSlot+'</div>');
          }
        });
        me.setBackgroundColor(data.backgroundColor);
        me.setBackgroundImage(data.backgroundImagePath);
        me.setBackgroundFit(data.backgroundFit);
        me.setBackgroundFixed(data.backgroundFixed);
        renderGlobalComponents(element);
        $.each(components, function(index, cmp){
          me.addComponent(cmp);
        });
      },
      show: function(){
        this.wrapper.show();
        return this;
      },
      hide: function(){
        this.wrapper.hide();
        return this;
      },
      addComponent: function(cmp){
        cmp.__status = null;
        new editors[cmp.componentType](cmp,this);
      },
      setBackgroundColor: function(color){
        this.wrapper.css({'background-color': color || global.backgroundColor});
      },
      setBackgroundImage: function(path){
        var image = (path || global.backgroundImage),
          el = $('#backgroundImage img');
        this.wrapper.css({'background-image': image ? 'url(' + fileServer + image + ')' : 'none'});
        path ? el.attr('src', fileServer + image) : el.removeAttr('src');
      },
      setBackgroundFixed: function(fixed){
        this.wrapper.css({'background-attachment': (fixed || global.backgroundFixed)== 'Y' ? 'fixed' : 'scroll'});
      },
      setBackgroundFit: function(fit){
        this.wrapper.css(
        function(){
          switch(fit || global.backgroundFit || 'MIDDLE'){
            case 'FILL':
              return {
                'background-repeat': 'no-repeat',
                'background-position': 'center',
                'background-size': '100% 100%'
              }
            case 'MIDDLE':
              return {
                'background-repeat': 'no-repeat',
                'background-position': 'center',
                'background-size': 'auto auto'
              }
            case 'STRETCHING':
              return {
                'background-repeat': 'no-repeat',
                'background-position': 'center',
                'background-size': 'auto 100%'
              }
            case 'RESPONSIVE':
              return {
                'background-repeat': 'no-repeat',
                'background-position': 'top',
                'background-size': '100% auto'
              }
            case 'TILE':
              return {
              'background-repeat': 'repeat',
              'background-position': 'left top',
              'background-size': 'auto auto'
            }
          }
        }());
      }
    }),
    Editor = kendo.Observable.extend({
      editable: true,
      popupTplt: ['<div class="panel">',
        '<div class="panel-body">',
          '#=content#',
        '</div>',
        '<div class="panel-footer clearfix" style="position: absolute;bottom: 0;left: 0;">',
          '<div class="pull-right">',
            '<span class="btn btn-success" data-bind="click:save">保存</span>',
            '<span class="btn btn-danger" data-bind="click:cancel" style="margin-left:20px">取消</span>',
          '</div>',
        '</div>',
      '</div>'],
      init: function(cmp,page,global){
        var me = this,
          templateContentSlot = cmp.templateContentSlot,
          componentObject = cmp.componentObject,
          wrapper = me.wrapper = me.initWrapper(templateContentSlot, page);
        if((!cmp.pk || global) && !componentObject){
          me.props = cmp.componentObject = $.extend({},me.defaultProps);
          if(cmp.displayMode){
            me.props.displayMode = cmp.displayMode;
            delete cmp.displayMode;
          }
          var components = viewModel.get('data.components')||[];
          components.push(cmp);
          viewModel.set('data.components',components);
          viewModel.trigger('change');
        }else{
          me.props = componentObject || $.extend({},me.defaultProps);
        }
        if(!global){
          page.components[templateContentSlot] = me;
          me.page = page;
        }
        me.cmp = cmp;
        me.global = global;
        me.preview = me.initPreview(wrapper);
        me.toolbar = me.initToolbar(wrapper);
        me.editable && me.initEditor();
      },
      initWrapper: function(templateContentSlot, page){
        var me = this;
        return $('[data-component='+templateContentSlot+']',page.wrapper).html('')
          .hover(function(){
            me.handleMouseEnter()
          },function(){
            me.handleMouseLeave()
          });
      },
      initPreview: function(wrapper){
        var preview = $('<div class="preview"></div>').appendTo(wrapper);
        this.renderPreview(preview);
        return preview;
      },
      initToolbar: function(wrapper){
        var me = this,
          global = me.global;
        return $([
          '<div class="cms-component-toolbar">',
            '<div class="bar">',
              '<label>',me.props.name,'</label>',
              me.editable ? '<i class="fa fa-edit" title="编辑"></i>':'',
              !global && me.getPrevCode() ? '<i class="fa fa-arrow-up" title="上移"></i>' : '',
              !global && me.getNextCode() ? '<i class="fa fa-arrow-down" title="下移"></i>' : '',
              !global ? '<i class="fa fa-close" title="移除"></i>' : '',
              !global ? '<i class="fa fa-copy" title="复用"></i>' : '',
            '</div>',
          '</div>'].join('')).appendTo(wrapper).click(function(e){me.handleToolbarClick(e)});
      },
      addWrap: function(templateContentSlot, page){
        var me = this,
          wrapper = me.initWrapper(templateContentSlot, page),
          toolbar = me.initToolbar(wrapper),
          preview = me.initPreview(wrapper);
        me.wrapper = me.wrapper.add(wrapper);
        me.toolbar = me.toolbar.add(toolbar);
        me.preview = me.preview.add(preview);
      },
      initEditor: function(){
        var me = this;
        me.popup = $(kendo.template(me.popupTplt.join(''))({
          content: me.editorTplt.join('')
        })).appendTo(me.wrapper).hide().kendoWindow({
            width: me.popupWidth,
            height: me.popupHeight,
            modal: true,
            title: me.editorTitle,
            close: function(){
              me.hideEditor();
            }
        }).data('kendoWindow').center();
        me.model = kendo.observable({
          save: function(){
            if(!me.masking){
              me.handleEditComplete();
            }
          },
          cancel: function(){
            me.handleEditCancel();
          }
        });
        me.renderEditor();
        kendo.bind(me.popup.element,me.model);
      },
      showEditor: function(){
        var me = this;
        me.editing = true;
        me.toolbar.hide();
        me.popup.open();
      },
      hideEditor: function(){
        var me = this;
        me.editing = false;
      },
      getPrevCode: function(){
        var myCode = this.cmp.templateContentSlot,
          prevCode = 'section' + padNum((Number(myCode.match(/\d+/)) - 1));
        if($('[data-component='+prevCode+']', this.page.wrapper).length){
          return prevCode;
        }
      },
      getNextCode: function(){
        var myCode = this.cmp.templateContentSlot,
          nextCode = 'section' + padNum((Number(myCode.match(/\d+/)) + 1));
        if($('[data-component='+nextCode+']', this.page.wrapper).length){
          return nextCode;
        }
      },
      up: function(){
        this.move(this.getPrevCode());
      },
      down: function(){
        this.move(this.getNextCode());
      },
      move: function(newCode){
        if(newCode){
          var me = this,
            cmp = me.cmp,
            page = me.page,
            myCode = cmp.templateContentSlot,
            target = page.components[newCode];
          me.destroy();
          cmp.templateContentSlot = newCode;
          if(target) {
            let pcmp = target.cmp;
            target.destroy();
            pcmp.templateContentSlot = myCode;
            page.addComponent(pcmp);
          }
          page.addComponent(cmp);
          viewModel.trigger('change');
        }
      },
      reuse: function(){
        
      },
      handleToolbarClick: function(e){
        var me = this,
          t = $(e.target);
        if(t.is('i.fa-edit')){
          me.showEditor();
        }else if(t.is('i.fa-arrow-up')){
          me.up();
        }else if(t.is('i.fa-arrow-down')){
          me.down();
        }else if(t.is('i.fa-close')){
          me.destroy();
        }else if(t.is('i.fa-copy')){
          me.reuse();
        }
      },
      handleMouseEnter: function(){
        if(!this.editing && !draggItem)
          this.toolbar.show();
      },
      handleMouseLeave: function(){
        if(!this.editing && !draggItem)
          this.toolbar.hide();
      },
      handleEditComplete: function(){
        var me = this;
        me.renderPreview(me.preview);
        if(me.global){
          var templateContentSlot = me.cmp.templateContentSlot;
          global.changes.push(templateContentSlot);
        }else{
          viewModel.trigger('change');
        }
        me.popup.close();
      },
      handleEditCancel: function(){
        this.popup.close();
      },
      mask: function(){
        this.masking = true;
        this.popup.element.find('.panel-footer .btn-success').addClass('disabled');
      },
      unmask: function(){
        this.masking = false;
        this.popup.element.find('.panel-footer .btn-success').removeClass('disabled');
      },
      destroy: function(){
        var me = this,
          cmp = me.cmp,
          components = viewModel.get('data').components,
          myCode = cmp.templateContentSlot;
        if(cmp.pk){
          cmp.__status = 'delete';
          viewModel.trigger('change');
        }else{
          components.splice(components.indexOf(cmp), 1);
        }
        delete me.page.components[myCode];
        me.toolbar.off();
        me.wrapper.off().html('<div class="cms-content-slot">'+myCode+'</div>');
      }
    }),
    CustomEditor = Editor.extend({
      defaultProps: {
        name: '自定义组件',
        content: ''
      },
      editorTitle: '编辑自定义组件',
      editorTplt: ['<div class="row"></div>'],
      popupWidth: 900,
      popupHeight: 475,
      renderEditor: function(){
        var me = this,
          popup = me.popup,
          style = {'font-size': '16px', 'background-image': 'none', 'line-height': 'initial'};
        me.editor = K.create(popup.element.find('.row').html('<textarea>'+me.props.content+'</textarea>').children('textarea')[0], {
          items : [
           'source', '|', 
           'bold', 'italic', 'fontname', 'fontsize', 'lineheight', 'strikethrough', 'underline', '|', 
           'anchor', 'link', 'unlink', 'forecolor', 'hilitecolor', 'undo', 'redo', 'indent', 'outdent', 'insertunorderedlist', 'insertorderedlist', 'selectall', 'removeformat', '/', 
           'formatblock', 'justifyleft', 'justifycenter', 'justifyright', 'table', 'image'],
          height: 300,
          width: '100%',
          cssPath: '/build/desktop.hmall.css',
          allowImageUpload: true,
          uploadJson : contextPath + '/cms/pageDecoration/upload?' + csrf.parameterName + '=' + csrf.token,
          fileServer: fileServer,
          allowFileManager : false
        });
      },
      renderPreview: function(preview){
        var content = this.props.content,
          iframe = $('<iframe border="0" style="display:block;width:100%;min-height:40px;position: relative;width: 1920px;margin-left: -335px;" hidefocus="true" frameborder="0"></iframe').appendTo(preview.empty()),
          frameWindow = iframe[0].contentWindow,
          frameDoc = frameWindow.document,
          link = frameDoc.createElement('link');
        link.href = '/build/desktop.hmall.css';
        link.rel = 'stylesheet';
        frameDoc.write('<div class="content" style="padding-bottom:10px">'+(content? clipLink(content) : '<div class="cms-content-slot-disabled">无内容</div>')+'</div>');
        frameDoc.head.appendChild(link);
        frameDoc.body.style.cssText='overflow:hidden;margin:0;padding:0';
        var id = setInterval(function(){
          var height = $(frameDoc.body.children[0]).outerHeight();
          if(height > 10){
            clearInterval(id);
            iframe.height(height);
          }
        },100);
      },
      handleEditComplete: function(){
        var me = this,
          content = me.editor.html();
        me.props.content = content;
        Editor.prototype.handleEditComplete.call(me);
      }
    }),
    BannerEditor = Editor.extend({
      defaultProps: {
        name: '横幅组件'
      },
      editorTitle: '编辑横幅组件',
      editorTplt: ['<div class="row">',
       '<div class="col-sm-6 form-group">',
         '<label class="control-label">图片</label>',
         '<div style="width:100%">',
           '<input class="form-control" style="width:60%" readonly data-role="maskedtextbox" data-bind="value: data.urlPath" />',
           '<input name="files" type="file" class="btn btn-default"></input>',
         '</div>',
       '</div>',
       '<div class="col-sm-6">',
         '<label class="control-label">链接地址</label>',
         '<input class="form-control" style="width:100%" data-role="maskedtextbox" data-bind="value: data.link" />',
       '</div>',
     '</div>'],
      previewTplt: '<div class="h-component h-banner" style="width:100%;min-height:40px"><img src="#:urlPath#"></img></div>',
      popupWidth: 555,
      popupHeight: 230,
      renderEditor: function(){
        var me = this,
          props = me.props,
          model = me.model,
          element = me.popup.element;
        model.set('data', {
          urlPath: props.urlPath || '',
          link: props.link || ''
        });
        element.find('input[type=file]').kendoUpload({
          async: {
            saveUrl: contextPath + '/cms/pageDecoration/upload?' + csrf.parameterName + '=' + csrf.token
          },
          showFileList: false,
          multiple: false,
          upload: function(e){
            me.mask();
            e.data = {
              sourceType: '',
              sourceKey: ''
            }
          },
          success: function (e) {
            var response = e.response,
              file = response.file;
            if(response.success){
              model.set('data',{
                url: file.fileId,
                urlPath: file.filePath
              });
            }
            me.unmask();
          }
        });
      },
      renderPreview: function(preview){
        var me = this;
        preview.html(kendo.template(me.previewTplt)({
          urlPath: fileServer + me.props.urlPath
        }));
      },
      handleEditComplete: function(){
        var me = this,
          props = me.props,
          data = me.model.get('data');
        props.url = data.url;
        props.urlPath = data.urlPath;
        Editor.prototype.handleEditComplete.call(me);
      }
    }),
    MediaPlayerEditor = Editor.extend({
      defaultProps: {
        name: '媒体播放组件',
        autoplay: 'N',
        width: 800,
        height: 400
      },
      editorTitle: '编辑媒体播放组件',
      videoTplt: ['<video class="h-component h-video video-js vjs-default-skin" style="margin-left:auto;margin-right:auto;" width="#:width#" height="#:height#" preload="none" #:autoplay# data-setup="{}" controls="controls" poster="">',
        '<source src="#:url#" type="video/mp4"></source>',
      '</video>'],
      previewTplt:'#=video#',
      editorTplt: ['<div class="row">',
        '<div class="col-sm-12 form-group">',
          '<label class="control-label">链接地址</label>',
          '<input class="form-control" style="width:100%" data-role="maskedtextbox" data-bind="value: data.url" />',
        '</div>',
      '</div>',
      '<div class="row">',
        '<div class="col-sm-6 form-group">',
          '<label class="control-label">宽度</label>',
          '<input class="form-control" style="width:100%" data-role="maskedtextbox" data-bind="value: data.width" />',
        '</div>',
        '<div class="col-sm-6 form-group">',
          '<label class="control-label">高度</label>',
          '<input class="form-control" style="width:100%" data-role="maskedtextbox" data-bind="value: data.height" />',
        '</div>',
      '</div>'],
      popupWidth: 555,
      popupHeight: 230,
      renderPreview: function(preview) {
        var me = this,
          props = me.props,
          url = props.url,
          width = props.width,
          height = props.height,
          autoplay = props.autoplay == 'Y' ? 'autoplay' : '';
        preview.html(kendo.template(me.previewTplt)({
          video: url ? kendo.template(me.videoTplt.join(''))({
            url: url,
            autoplay: autoplay,
            width: width,
            height: height
          }): '<div style="background-color: #fff;width:'+width+'px;height:'+height+'px"></div>'
        }));
        //url && videojs(preview.find('video')[0]);
      },
      renderEditor: function(){
        var props = this.props;
        this.model.set('data', {
          url: props.url || '',
          width: props.width,
          height: props.height
        });
      },
      handleEditComplete: function(){
        var me = this,
          data = me.model.get('data');
        me.props.url = data.url;
        Editor.prototype.handleEditComplete.call(me);
      }
    }),
    publicProductGroupEditor = null,
    ProductGroupEditor = Editor.extend({
      defaultProps: {
        name: '商品组件'
      },
      previewTplt: '<div class="h-component h-product-group #:displayMode#"><ul>#=list#</ul></div>',
      renderPreview: function(preview) {
        var me = this,
          props = me.props,
          productGroupLines = props.productGroupLines;
        preview.html( productGroupLines && productGroupLines.length ? kendo.template(me.previewTplt)({
          displayMode: props.displayMode,
          list: $.map(productGroupLines,function(product){
            return product.__status == 'delete' ? '' :productTplt({
              productName: product.name,
              price: product.maxPrice,
              mainPic: product.mainPic,
              label: product.label
            });
          }).join('')
        }) : '<div class="cms-content-slot-disabled">无内容</div>');
      },
      initEditor: function(){
        this.props.productGroupLines=[];
      },
      renderEditor: function(){},
      showEditor: function(){
        var me = this;
        if(!publicProductGroupEditor){
          publicProductGroupEditor = $('<div></div>').appendTo(me.wrapper).kendoWindow({
              width: 900,
              height: 500,
              modal: true,
              title: '编辑商品组件',
              content: contextPath + '/cms/cms_shop_recommend.html?type=product',
              close: function(){
                publicProductGroupEditor.owner.hideEditor();
              },
              refresh: function(){
                me.addProducts();
              }
          }).data('kendoWindow').center();
          $('#hmall-style').clone().appendTo(publicProductGroupEditor.wrapper);
        }else{
          me.addProducts();
        }
        me.popup = publicProductGroupEditor;
        publicProductGroupEditor.owner = this;
        Editor.prototype.showEditor.call(me);
      },
      handleEditComplete: function(){
        var me = this,
          data = me.model.get('data');
        me.props.url = data.url;
        Editor.prototype.handleEditComplete.call(me);
      },
      addProducts: function(){
        lineDataSource.cancelChanges()
        $.each(this.props.productGroupLines, function(i, data){
          if(data.__status == 'delete'){
            lineDataSource._destroyed.push(data);
          }else{
            lineDataSource.add(data);
          }
        });
      }
    }),
    HRecommendEditor = Editor.extend({
      defaultProps: {
        name: '店铺推荐-横组件'
      },
      productWidth: 188,
      editable: false,
      previewTplt:'<div class="h-panel h-component h-h-recommend"><div class="panel-header"><span>店铺推荐</span></div><div class="panel-body">#=list#</div></div>',
      renderPreview: function(preview) {
        preview.html(this.getPreview());
      },
      getPreview: function(){
        return kendo.template(this.previewTplt)({
          list: this.renderRecommends(global.recommends)
        });
      },
      renderRecommends: function(recommends){
        var me = this;
        if(recommends && recommends.length){
          return [
            '<ul>',
            $.map(recommends, function(recommend){
              return productTplt($.extend({
                width: me.productWidth
              },recommend));
            }).join(''),
            '</ul>'
          ].join('');
        }else{
          return '';
        }
      }
    }),
    VRecommendEditor = HRecommendEditor.extend({
      defaultProps: {
        name: '店铺推荐-竖组件'
      },
      productWidth: 240,
      previewTplt: '<div class="h-panel h-component h-v-recommend"><div class="panel-header line"><span>店铺推荐</span></div><div class="scroll-wrapper">#=list#</div><div class="recommend-footer"><i class="slide up"></i><i class="slide down"></i></div></div>'
    }),
    CategoryEditor = Editor.extend({
      defaultProps: {
        name: '店铺分类组件'
      },
      editable: false,
      previewTplt:'<div class="h-panel h-component h-category"><div class="panel-header line"><span>商品分类</span></div>#=list#</div>',
      renderPreview: function(preview) {
        preview.html(this.getPreview());
      },
      getPreview: function(){
        return kendo.template(this.previewTplt)({
          list: this.renderCategories(global.categories)
        });
      },
      renderCategories: function(categories){
        var me = this;
        if(categories && categories.length){
          return [
            '<ul>',
              categories.map(function(item,index){
                var name = item.name,
                  categories = item.categories,
                  expand = item.expand;
                return (
                  categories && categories.length ? 
                    ['<li class="submenu',expand == 'Y' ? '':' collapse','" style="display: list-item">',
                      '<i></i>',
                      '<a>',name,'</a>',
                      me.renderCategories(categories),
                    '</li>'].join('') :
                    ['<li>',
                      '<a>',name,'</a>',
                    '</li>'].join('')
                )
              }).join(''),
            '</ul>'].join('');
        }
        return '';
      }
    }),
    HistoryEditor = Editor.extend({
      defaultProps: {
        name: '最近浏览组件'
      },
      editable: false,
      previewTplt:'<div class="h-panel h-component h-history"><div class="panel-header"><span>最近浏览</span></div><div class="background_div" style="height: 272px;"></div></div>',
      renderPreview: function(preview) {
        preview.html(this.previewTplt);
      }
    }),
    editors = {
      Custom: CustomEditor,
      Banner: BannerEditor,
      MediaPlayer: MediaPlayerEditor,
      ProductGroup: ProductGroupEditor,
      HRecommend: HRecommendEditor,
      VRecommend: VRecommendEditor,
      Category: CategoryEditor,
      History: HistoryEditor
    };

  CMS.PD = {
    init: init,
    closeWindow: function() {
      publicProductGroupEditor.close();
    },
    updateProducts: function(created,updated,destroyed) {
      if(created.length || updated.length || destroyed.length){
        var editor = publicProductGroupEditor.owner,
          productGroupLines = editor.props.productGroupLines;
        $.each(destroyed, function(i,destroy){
          $.each(productGroupLines, function(j, data){
            if(data.pk == destroy.pk){
              data.__status = 'delete';
              return false;
            }
          });
        });
        $.each(updated, function(i,update){
          $.each(productGroupLines, function(j, data){
            if(data.pk == update.pk){
              $.extend(data,update);
              return false;
            }
          });
        });
        $.each(created, function(i,create){
          var find = false;
          $.each(productGroupLines, function(j, data){
            if(data.product == create.product){
              find = true;
              return false
            }
          });
          if(!find){
            productGroupLines.push(create);
          }
        });
        editor.renderPreview(editor.preview);
        viewModel.trigger('change');
      }
      publicProductGroupEditor.close();
    }
  }
}(window.CMS = {},window.KindEditor);