/**
 * 上传插件
 *
 */
;(function($, window, document,undefined) {
    //定义对象
    function MyUploadVo(ele, option) {
        var _this = this;
        _this.$ele = ele;
        _this._default = {
            onChange: function () {
                _this.upload();
            },
            type: "post",
            dataType: "json",
            async: true,
            fileType: ["xls", "xlsx"], //文件类型
            maxSize: 1024 * 1024 * 20  //文件大小
        };
        _this.options = $.extend({}, _this._default, option);

        var successFn = _this.options.success;
        _this.options.success = function (data, textStatus, xhr) {
            _this.$ele.myProgress("doProgress", 100);
            if (successFn) {
                successFn(data, textStatus, xhr);
            }
        };
        var errorFn = _this.options.error;
        _this.options.error = function (xhr, textStatus) {
            _this.$ele.myProgress("doProgress", -1);
            if (errorFn) {
                errorFn(xhr, textStatus);
            }
        };
    }
    //定义对象方法
    MyUploadVo.prototype = {
        //初始化
        _init: function () {
            var _this = this;
            if(_this.$ele.data("MyUpload")) {
                _this.clear();
            }
            _this.$ele.data("MyUpload", _this);

            _this.$ele.on("click", function () {
                if (_this.$fileELe) {
                    _this.$fileELe.remove();
                }
                _this.$ele.siblings(".my-upload-file").remove();

                _this.$fileELe = $('<input class="my-upload-file" name="file" type="file" style="display: none;"/>');
                _this.$ele.after(_this.$fileELe);
                _this.$fileELe.on("change", _this.options.onChange);

                _this.$fileELe.trigger("click");
            })

            return this.$ele;
        },
        clear: function () {
            this.$fileELe.remove();
            this.$ele.data("MyUpload", null);
            this.$ele.off("click");
        },
        upload: function () {
            var _this = this;
            var formData = new FormData();
            formData.append("file", this.$fileELe[0].files[0]);
            //初始化进度条
            _this.$ele.myProgress();
            //ajax对象
            var request;
            if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
                request = new XMLHttpRequest();
            }
            else {// code for IE6, IE5
                request = new ActiveXObject("Microsoft.XMLHTTP");
            }

            //绑定progress事件的回调函数
            request.upload.onprogress = function (ev) {
                if (ev.lengthComputable) {
                    var percent = ev.loaded / ev.total * 100;
                    _this.$ele.myProgress("doProgress", percent);
                }
            };
            //响应
            request.onreadystatechange = function () {
                if (request.readyState != 4) {
                    return;
                }
                if (request.status==200) {
                    var data = request.responseText;
                    if (_this.options.success) {
                        _this.options.success(JSON.parse(data), request.statusText, request);
                    }
                } else {
                    if (_this.options.error) {
                        _this.options.error(request, request.statusText);
                    }
                }
            }
            //发送请求
            request.open(_this.options.type, _this.options.url, _this.options.async);
            request.send(formData);
        }
    }

    //注册插件
    $.fn.myUpload = function(options) {
        var myUploadVo = new MyUploadVo(this, options);
        return myUploadVo._init(this);
    }

})(jQuery, window, document);