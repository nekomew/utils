/**
 * 进度条  依赖layer.js jquery.js
 * get 获取 MyProgressVo对象
 * close 关闭
 * doProgress 根据参数 控制进度 返回 -1 进度条已销毁 0 进行中 1 完成
 *
 * 调用 $ele.myProgress('doProgress', pv); pv为进度 百分比
 *
 * 例：
 $("#aa").myProgress();
 var a = 0;
 var b = setInterval(function () {
    if ($("#aa").myProgress("doProgress", ++a)) {
        clearInterval(b);
    }
  }, 50);
 */
;(function($, window, document,undefined) {

    function MyProgressVo(ele, option) {
        var _this = this;
        this.$ele = ele;
        this._default = {
            backgroundColor: "#fff",
            width: "200px",
            height: "auto",
            time: -1,
            max: 100,
            complete: function () {
                _this.close();
            }
        };
        this.options = $.extend({}, this._default, option);
    }

    MyProgressVo.prototype = {
        //初始化
        _init: function () {
            var _this = this;
            var oldVo = this.$ele.data("MyProgressVo");
            if (oldVo) {
                oldVo.close();
            }
            this.$ele.data("MyProgressVo", this);

            _this.$index = layer.tips(
                [
                    '<div style="background-color: #c0c0c0; height: 5px; border-radius: 4px; overflow: hidden;">',
                    '    <div class="progress-div" style="background-color: #0e90d2; height: 10px; width: 0%;">',
                    '    </div>',
                    '</div>',
                    '<span style="color: #0e90d2; margin: 0;">',
                    '    <span  class="progress-span" style="margin: 0;">0</span>%',
                    '</span>',
                    '<a class="progress-close" href="javascript: void(0);" style="float: right;">x</a>',
                ].join("")
                , _this.$ele, {
                    tipsModel: true,
                    tips: [3, _this.options.backgroundColor],
                    area: [_this.options.width, _this.options.height],
                    time: _this.options.time,
                    success: function (lay) {
                        _this.$content = lay;
                        $(".progress-close", lay).on("click", function () {
                           _this.close();
                        });
                    }
                });

            return this.$ele;
        },
        //关闭
        close: function () {
            layer.close(this.$index);
            this.$ele.data("MyProgressVo", null);
        },
        //处理进度
        //val 百分比值 0~100 -1 表示出现错误
        //返回 -1 进度条已销毁 0 进行中 1 完成
        doProgress(val) {
            if (val == -1) {
                $(".progress-div", this.$content).css({backgroundColor: '#F00'});
                val = 100;
            }

            if (val >= this.options.max) {
                val = this.options.max;
                setTimeout(this.options.complete, 1000);
            }

            $(".progress-div", this.$content).width(val + "%");
            $(".progress-span", this.$content).text(val);

            return val >= this.options.max ? 1 : 0;
        }
    }

    //在注册插件
    $.fn.myProgress = function(options, val) {
        if(options == "get") {
            return this.data("MyProgressVo");
        }
        if(options == "close") {
            var vo = this.data("MyProgressVo");
            vo ? vo.close() : null;
            return this;
        }
        if(options == "doProgress") {
            var vo = this.data("MyProgressVo");
            return vo ? vo.doProgress(val) : -1;
        }

        var myProgressVo = new MyProgressVo(this, options);
        return myProgressVo._init(this);
    }

})(jQuery, window, document);