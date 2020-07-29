/**
 *
 */
;(function($, window, document,undefined) {
    var triggerClass = "timepicker-input-4-js";

    function TimepickerVo(ele, option) {
        var _this = this;
        this.$ele = ele;
        this._default = {
            timeRegex: /^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/,
            defaultValue: "00:00:00",
        };
        this.options = $.extend({}, this._default, option);
    }

    TimepickerVo.prototype = {
        //初始化
        _init: function () {
            var oldVo = this.$ele.data("timepicker");
            if (oldVo) {
                oldVo.close();
            }
            this.$ele.data("timepicker", this);
            this.$ele.prop("readonly", true);
            this.$ele.addClass(triggerClass);
            return this.$ele;
        },
        //关闭
        close: function () {
            layer.close(this.$index);
        },
        show: function () {
            var _this = this;
            _this.close();
            var val = _this.$ele.val();
            if (!val || !_this.options.timeRegex.test(val)) {
                val = _this.options.defaultValue;
                _this.$ele.val(val);
            }

            var tmpHtml = [
                '<div class="timepicker-wrap">',
                '    <div class="timepicker-item-group">' +
                '       <div class="timepicker-item"><i class="arrow up"></i></div>' +
                '       <div class="timepicker-item"><input class="time-input hours" type="text"><span class="timepicker-split">:</span></div>',
                '       <div class="timepicker-item"><i class="arrow down"></i></div>',
                '    </div>' +
                '    <div class="timepicker-item-group">' +
                '       <div class="timepicker-item"><i class="arrow up"></i></div>' +
                '       <div class="timepicker-item"><input class="time-input minute" type="text"><span class="timepicker-split">:</span></div>',
                '       <div class="timepicker-item"><i class="arrow down"></i></div>',
                '    </div>' +
                '    <div class="timepicker-item-group">' +
                '       <div class="timepicker-item"><i class="arrow up"></i></div>' +
                '       <div class="timepicker-item"><input class="time-input second" type="text"></div>',
                '       <div class="timepicker-item"><i class="arrow down"></i></div>',
                '    </div>',
                '</div>',
            ].join("");

            _this.$index = layer.tips(tmpHtml, _this.$ele, {
                tipsModel: true,
                tips: [1, '#fff'],
                //area: ["180px", "70px"],
                time: -1,
                skin: "timepicker-tip",
                shade: [0.1, '#fff'],
                shadeClose: true,
                success: function (lay) {
                    var timeArr = val.match(/\d+/g);

                    var $hours = $(".hours", lay);
                    var $minute = $(".minute", lay);
                    var $second = $(".second", lay);

                    $hours.val(timeArr[0]);
                    $minute.val(timeArr[1]);
                    $second.val(timeArr[2]);

                    $(".time-input", lay).on("propertychange input change", function () {

                        var hours = _this.fixTwoWord($hours.val(), 23);
                        var minute = _this.fixTwoWord($minute.val(), 59);
                        var second = _this.fixTwoWord($second.val(), 59);

                        $hours.val(hours);
                        $minute.val(minute);
                        $second.val(second);

                        var newVal = hours + ":" + minute + ":" + second;
                        _this.$ele.val(newVal);
                    })
                },
                end: function () {
                    closeInterval()
                }
            });
        },
        fixTwoWord: function (val, max) {
            val = $.trim(parseInt(val));

            if (val.length == 0 || val < 0 || !/^\d+$/.test(val)) {
                return "00";
            }

            if (max && val > max) {
                val = max;
            }
            if (val.length == 1) {
                return "0" + val;
            }

            return val;
        }
    }

    //在注册插件
    $.fn.timepicker = function(options) {
        this.each(function () {
            var timepickerVo = new TimepickerVo($(this), options);
            return timepickerVo._init();
        })
        return this;
    }

    $(document).on("click focus", "." + triggerClass, function () {
        var timepicker = $(this).data("timepicker");
        if (timepicker) {
            timepicker.show();
        }
    })

    function toStep(_this) {
        var step = 1;
        if (_this.hasClass("down")) {
            step = -1;
        }
        var input = _this.parents(".timepicker-item-group").find(".time-input");
        var val = input.val();
        val = parseInt(val) + step;
        var max = 59;
        if (input.hasClass("hours")) {
            max = 23;
        }
        if (val > max) {
            val = "00";
        }
        if (val < 0) {
            val = max + "";
        }
        input.val(val);
        input.trigger("change");
    }
    var intervalIndex;
    function closeInterval() {
        clearTimeout(intervalIndex);
    }

    $(document).on("mousedown", ".timepicker-tip .arrow", function () {
        var _this = $(this);
        var time = 1000;
        var enentFn = function () {
            toStep(_this)
            time = 100;
            intervalIndex = setTimeout(enentFn, time);
        }
        intervalIndex = setTimeout(enentFn, time);
    })

    $(document).on("mouseup", function () {
        closeInterval()
    })

    $(document).on("click", ".timepicker-tip .arrow", function () {
        closeInterval()
        var _this = $(this);
        toStep(_this);
    })

})(jQuery, window, document);
