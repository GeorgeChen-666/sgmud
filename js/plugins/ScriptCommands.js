(() => {
  setTimeout(() => {
    /**
     * 扩展解释器，在解析器当前执行的命令后面插入命令
     */
    Game_Interpreter.prototype.insertCommands = function(jsonCmds) {
      console.log("insertCommands", jsonCmds);
      this._freezeChecker = 0;
      if (!this._list) {
        // 没有此处判断仅能在地图事件中调用insertCommands插入命令，有此判断后可以随时随地插入命令。
        this.clear();
        this.setup(jsonCmds);
      } else {
        var listCmds = this._list; // 命令列表
        var listCount = listCmds.length; // 这个事件队列中一共多少条命令
        var cuCmdIndex = this._index; // 当前执行的是第几条命令
        var cuCmdIndent = this._indent; // 当前代码的缩进级别
        jsonCmds.forEach(function(e) {
          // 将参数命令的indent拼接到当前的indent级别下，参数的indent一定要从0开始。
          e.indent += cuCmdIndent;
        });
        this._list = listCmds
          .slice(0, cuCmdIndex + 1) // 在当前执行的事件后面插入
          .concat(jsonCmds)
          .concat(listCmds.slice(cuCmdIndex + 1, listCount));
      }
    };
    /**
     * 条件支持js代码
     */
    const Game_Interpreter_prototype_command111 =
      Game_Interpreter.prototype.command111;
    Game_Interpreter.prototype.command111 = function() {
      if (typeof this._params[0] === "function") {
        const result = !!this._params[0].apply(this);
        this._branch[this._indent] = result;
        if (this._branch[this._indent] === false) {
          this.skipBranch();
        }
        return true;
      } else {
        Game_Interpreter_prototype_command111.apply(this);
      }
    };
    /**
     * 使用js脚本给变量赋值
     */
    const Game_Interpreter_prototype_command122 =
      Game_Interpreter.prototype.command122;
    Game_Interpreter.prototype.command122 = function() {
      if (typeof this._params[4] === "function") {
        for (let i = this._params[0]; i <= this._params[1]; i++) {
          this.operateVariable(i, this._params[2], this._params[4].apply(this));
        }
        return true;
      } else {
        Game_Interpreter_prototype_command122();
      }
    };
    /**
     * 支持JS代码
     */
    const Game_Interpreter_prototype_command355 =
      Game_Interpreter.prototype.command355;
    Game_Interpreter.prototype.command355 = function() {
      if (typeof this._params[0] === "function") {
        this._params[0].apply(this);
        return true;
      } else {
        return Game_Interpreter_prototype_command355.apply(this);
      }
    };
    /**
     * 移动路径脚本支持js
     */
    const Game_Character_prototype_processMoveCommand =
      Game_Character.prototype.processMoveCommand;
    Game_Character.prototype.processMoveCommand = function(command) {
      if (
        command.code === Game_Character.ROUTE_SCRIPT &&
        typeof this._params[0] === "function"
      ) {
        this._params[0].apply(this);
      } else {
        Game_Character_prototype_processMoveCommand.apply(this, [command]);
      }
    };
  }, 100);
})();
/**
 * @class
 * @global
 */
class $mvs {
  /**
   * 构造函数
   * @param {*} interpreter 事件解析器
   * @param {number} [indent] 缩进等级
   */
  constructor(interpreter, indent = interpreter._indent) {
    this.interpreter = interpreter;
    this.indent = indent;
    this.commands = [];
  }
  /**
   * 命令串构造完毕，立即执行。
   */
  done() {
    this.interpreter.insertCommands(this.commands);
    this.commands = [];
  }
  /**
   * 经过计算后再调用其它命令，多用临时决定的计算下一个命令的参数值。
   * @param {*} mvCommand 计算回调函数
   */
  calculateAndCall(mvCommand) {
    mvCommand(this);
  }
  /**
   * 示一个文本窗口
   * @param {[string]} textData 要显示的文本数组
   * @param {string} [faceName] 角色头像素材名
   * @param {number} [faceIndex] 角色头像在素材中的序号
   * @param {enumBackground} [background] 背景：enumBackground
   * @param {enumPositionV} [positionType] 纵向位置：enumPositionV
   */
  showText(
    textData,
    faceName = "",
    faceIndex = 0,
    background = 0,
    positionType = 2
  ) {
    this.commands = this.commands.concat([
      {
        code: 101,
        indent: this.indent,
        parameters: [faceName, faceIndex, background, positionType]
      },
      ...textData.map(text => ({
        code: 401,
        indent: this.indent,
        parameters: [text]
      }))
    ]);
    return this;
  }
  /**
   * 显示一个选项窗口
   * @param {[string]} choiceData 选项文本数组
   * @param {number} [cancelValue] 取消时执行分支序号：-2取消，-1禁止取消
   * @param {number} [defaultValue] 默认时执行分支序号：-1不执行
   * @param {enumPositionH} [positionType] 横向位置：enumPositionH
   * @param {enumBackground} [background] 背景：enumBackground
   */
  showChoices(
    choiceData,
    cancelValue = -2,
    defaultValue = 0,
    positionType = 2,
    background = 0
  ) {
    this.commands.push({
      code: 102,
      indent: this.indent,
      parameters: [
        choiceData,
        cancelValue,
        defaultValue,
        positionType,
        background
      ]
    });
    return this;
  }
  /**
   * 选项窗口选中项目时执行的代码
   * @param {number} index 分支需要
   * @param {*} subCommand 匿名函数类型，分支匹配时执行的脚本
   */
  whenChoicesIndex(index, subCommand) {
    this.commands.push({
      code: 402,
      indent: this.indent,
      parameters: [index]
    });
    this.indent++;
    subCommand(this);
    this.commands.push({ code: 0, indent: this.indent });
    this.indent--;
    return this;
  }
  /**
   * 选项窗口取消时的执行的代码
   * @param {*} subCommand 匿名函数类型，分支匹配时执行的脚本
   */
  whenChoicesCancel(subCommand) {
    this.commands.push({ code: 403, indent: this.indent });
    this.indent++;
    subCommand(this);
    this.commands.push({ code: 0, indent: this.indent });
    this.indent--;
    return this;
  }
  /**
   * 结束条件分支
   */
  endWhen() {
    this.commands.push({ code: 404, indent: this.indent });
    //this.commands.push({ code: 412, indent: this.indent });
    return this;
  }
  /**
   * 数值输入处理
   * @param {number} variableId 结果储存的变量id
   * @param {number} [maxDigits] =1,位数1~8
   */
  inputNumber(variableId, maxDigits = 1) {
    this.commands.push({
      code: 103,
      indent: this.indent,
      parameters: [variableId, maxDigits]
    });
    return this;
  }
  /**
   * 物品选择处理
   * @param {number} variableId 结果储存的变量id
   * @param {enumItemType} [itemType] =2,物品类型：enumItemType
   */
  selectItem(variableId, itemType = 2) {
    this.commands.push({
      code: 104,
      indent: this.indent,
      parameters: [variableId, itemType]
    });
    return this;
  }
  /**
   * 显示滚动文字
   * @param {[string]} textData 文本数组
   * @param {number} =2,speed 速度1~8
   * @param {boolean} =false,noFast 禁止快进
   */
  showScrollingText(textData, speed = 2, noFast = false) {
    this.commands = this.commands.concat([
      {
        code: 105,
        indent: this.indent,
        parameters: [speed, noFast]
      },
      ...textData.map(text => ({
        code: 405,
        indent: this.indent,
        parameters: [text]
      }))
    ]);
    return this;
  }
  /**
   * 根据条件函数返回值创建分支
   * @param {*} conditionFunction 匿名函数类型4，条件函数
   * @param {*} subCommand 匿名函数类型，分支匹配时执行的脚本
   */
  whenFunctionIs(conditionFunction, subCommand) {
    this.commands.push({
      code: 111,
      indent: this.indent,
      parameters: [conditionFunction]
    });
    this.indent++;
    subCommand(this);
    this.indent--;
    return this;
  }
  /**
   * 条件都不满足时的else分支
   * @param {*} subCommand 匿名函数类型，分支匹配时执行的脚本
   */
  whenElse(subCommand) {
    this.commands.push({
      code: 411,
      indent: this.indent
    });
    this.indent++;
    subCommand(this);
    this.indent--;
    return this;
  }
  /**
   * 循环
   * @param {*} subCommand 匿名函数类型，循环内部时执行的脚本
   */
  loop(subCommand) {
    this.commands.push({
      code: 112,
      indent: this.indent
    });
    this.indent++;
    subCommand(this);
    this.indent--;
    this.commands.push({
      code: 413,
      indent: this.indent
    });
    return this;
  }
  /**
   * 跳出循环
   */
  loopBreak() {
    this.commands.push({
      code: 113,
      indent: this.indent
    });
    return this;
  }
  /**
   * 终止本次事件处理
   */
  exitCommand() {
    this.commands.push({
      code: 115,
      indent: this.indent
    });
    return this;
  }
  /**
   * 调用公共事件
   * @param {number}} eventId 公共事件id
   */
  callCommonEvent(eventId) {
    this.commands.push({
      code: 117,
      indent: this.indent,
      parameters: [eventId]
    });
    return this;
  }
  /**
   * 定义标签
   * @param {string} labelName 标签名
   */
  defineLabel(labelName) {
    this.commands.push({
      code: 118,
      indent: this.indent,
      parameters: [labelName]
    });
    return this;
  }
  /**
   * 跳转到标签
   * @param {string} labelName 标签名
   */
  gotoLabel(labelName) {
    this.commands.push({
      code: 119,
      indent: this.indent,
      parameters: [labelName]
    });
    return this;
  }

  /**
   * 设置开关值（支持批量）
   * @param {enumSwitchValue} srcValue 开关值：enumSwitchValue
   * @param {number} switchIdStart 开关id范围开始
   * @param {number} [switchIdEnd] 开关id范围结束，默认等于开始
   */
  setSwitchAsValue(srcValue, switchIdStart, switchIdEnd = switchIdStart) {
    this.commands.push({
      code: 121,
      indent: this.indent,
      parameters: [switchIdStart, switchIdEnd, srcValue]
    });
    return this;
  }
  /**
   * 设置变量的值
   * @param {*} valueFunction 取值函数
   * @param {number} variablesIdStart 变量id范围开始
   * @param {number} [variablesIdEnd] 变量id范围结束，默认等于开始
   */
  setVariablesAsScriptValue(
    valueFunction,
    variablesIdStart,
    variablesIdEnd = variablesIdStart
  ) {
    this.commands.push({
      code: 122,
      indent: this.indent,
      parameters: [variablesIdStart, variablesIdEnd, 0, 4, valueFunction]
    });
    return this;
  }
  /**
   * 设置独立开关值
   * @param {enumSwitchValue} srcValue 开关值：enumSwitchValue
   * @param {enumSelfSwitchName} selfSwitchName 独立开关名：enumSelfSwitchName
   */
  setSelfSwitchAsValue(srcValue, selfSwitchId) {
    this.commands.push({
      code: 123,
      indent: this.indent,
      parameters: [selfSwitchId, srcValue]
    });
    return this;
  }
  /**
   * 开始倒计时器
   * @param {number} count 秒数
   */
  startTimer(count) {
    this.commands.push({
      code: 124,
      indent: this.indent,
      parameters: [0, count]
    });
    return this;
  }
  /**
   * 收起倒计时器
   */
  stopTimer() {
    this.commands.push({
      code: 124,
      indent: this.indent,
      parameters: [1]
    });
    return this;
  }
  /**
   * 增减金钱
   * @param {number} amount 差值
   */
  changeGold(amount) {
    this.commands.push({
      code: 125,
      indent: this.indent,
      parameters: [0, 0, amount]
    });
    return this;
  }
  /**
   * 增减物品
   * @param {number} itemId 物品id
   * @param {number} [amount] =1,差值
   */
  changeItem(itemId, amount = 1) {
    this.commands.push({
      code: 126,
      indent: this.indent,
      parameters: [itemId, 0, 0, amount]
    });
    return this;
  }
  /**
   * 增减武器
   * @param {number} weaponId 武器id
   * @param {number} [amount] =1,差值
   * @param {boolean} [includeEquip] =false,减少时是否包括已经装备的武器
   */
  changeWeapons(weaponId, amount = 1, includeEquip = false) {
    this.commands.push({
      code: 127,
      indent: this.indent,
      parameters: [weaponId, 0, 0, amount, includeEquip]
    });
    return this;
  }
  /**
   * 增减防具
   * @param {number} armorId 护甲id
   * @param {number} [amount] =1,差值
   * @param {boolean} [includeEquip] =false,减少时是否包含已经装备的防具
   */
  changeArmors(armorId, amount = 1, includeEquip = false) {
    this.commands.push({
      code: 128,
      indent: this.indent,
      parameters: [armorId, 0, 0, amount, includeEquip]
    });
    return this;
  }
  /**
   * 角色入队
   * @param {number} actorId 角色id
   * @param {boolean} [isInitialize] =false,是否初始化
   */
  addPartyMember(actorId, isInitialize = false) {
    this.commands.push({
      code: 129,
      indent: this.indent,
      parameters: [actorId, 0, isInitialize]
    });
    return this;
  }
  /**
   * 角色离队
   * @param {number} actorId 角色id
   */
  removePartyMember(actorId) {
    this.commands.push({
      code: 129,
      indent: this.indent,
      parameters: [actorId, 1]
    });
    return this;
  }
  /**
   * 更改战斗bgm
   * @param {string} [name] ='',声音名
   * @param {number} [volume] =90,0~100音量
   * @param {number} [pitch] =100,50~150音调
   * @param {number} [pan] =0,-100~100声相
   */
  changeBattleBGM(name = "", volume = 90, pitch = 100, pan = 0) {
    this.commands.push({
      code: 132,
      indent: this.indent,
      parameters: [{ name, volume, pitch, pan }]
    });
    return this;
  }
  /**
   * 更改战斗胜利me
   * @param {string} [name] ='',声音名
   * @param {number} [volume] =90,0~100音量
   * @param {number} [pitch] =100,50~150音调
   * @param {number} [pan] =0,-100~100声相
   */
  changeVictoryME(name = "", volume = 90, pitch = 100, pan = 0) {
    this.commands.push({
      code: 133,
      indent: this.indent,
      parameters: [{ name, volume, pitch, pan }]
    });
    return this;
  }
  /**
   * 启用/禁用存档
   * @param {boolean} isAble 启用/禁用
   */
  changeSaveAccess(isAble) {
    this.commands.push({
      code: 134,
      indent: this.indent,
      parameters: [isAble]
    });
    return this;
  }
  /**
   * 启用/禁用菜单
   * @param {boolean} isAble 启用/禁用
   */
  changeMenuAccess(isAble) {
    this.commands.push({
      code: 135,
      indent: this.indent,
      parameters: [isAble]
    });
    return this;
  }
  /**
   * 启用/禁用遇敌
   * @param {boolean} isAble 启用/禁用
   */
  changeEncounterDisable(isAble) {
    this.commands.push({
      code: 136,
      indent: this.indent,
      parameters: [isAble]
    });
    return this;
  }
  /**
   * 启用/禁用整队
   * @param {boolean} isAble 启用/禁用
   */
  changeFormationAccess(isAble) {
    this.commands.push({
      code: 137,
      indent: this.indent,
      parameters: [isAble]
    });
    return this;
  }
  /**
   * 更改窗口颜色
   * @param {number} [red] =0,-255~255 红色偏移
   * @param {number} [green] =0,-255~255 绿色偏移
   * @param {number} [blue] =0,-255~255 蓝色偏移
   */
  changeWindowColor(red = 0, green = 0, blue = 0) {
    this.commands.push({
      code: 138,
      indent: this.indent,
      parameters: [[red, green, blue, 0]]
    });
    return this;
  }
  /**
   * 更改战斗失败me
   * @param {string} [name] ='',声音名
   * @param {number} [volume] =90,0~100音量
   * @param {number} [pitch] =100,50~150音调
   * @param {number} [pan] =0,-100~100声相
   */
  changeDefeatME(name = "", volume = 90, pitch = 100, pan = 0) {
    this.commands.push({
      code: 139,
      indent: this.indent,
      parameters: [{ name, volume, pitch, pan }]
    });
    return this;
  }
  /**
   * 更改载具bgm
   * @param {enumVehicleType} vehicleType 载具类型：enumVehicleType
   * @param {string} [name] ='',声音名
   * @param {number} [volume] =90,0~100音量
   * @param {number} [pitch] =100,50~150音调
   * @param {number} [pan] =0,-100~100声相
   */
  changeVehicleBGM(vehicleType, name = "", volume = 90, pitch = 100, pan = 0) {
    this.commands.push({
      code: 140,
      indent: this.indent,
      parameters: [vehicleType, { name, volume, pitch, pan }]
    });
    return this;
  }
  /**
   * 场所移动
   * @param {number} mapId 地图id
   * @param {number} x x坐标
   * @param {number} y y坐标
   * @param {enumDirection} [d] 方向：enumDirection
   * @param {enumFadeType} [fadeType] 淡入淡出：enumFadeType
   */
  transferPlayerDirect(mapId, x, y, d = 0, fadeType = 0) {
    this.commands.push({
      code: 201,
      indent: this.indent,
      parameters: [0, mapId, x, y, d, fadeType]
    });
    return this;
  }
  /**
   * 设置载具id
   * @param {number} vehicleId 载具id
   * @param {number} mapId 地图id
   * @param {number} x x坐标
   * @param {number} y y坐标
   */
  setVehicleLocationDirect(vehicleId, mapId, x, y) {
    this.commands.push({
      code: 202,
      indent: this.indent,
      parameters: [vehicleId, 0, mapId, x, y]
    });
    return this;
  }
  /**
   * 设置事件位置
   * @param {number} eventId 事件id
   * @param {number} x x坐标
   * @param {number} y y坐标
   * @param {enumDirection} [d] =0方向：enumDirection
   */
  setEventLocationDirect(eventId, x, y, d = 0) {
    this.commands.push({
      code: 203,
      indent: this.indent,
      parameters: [eventId, 0, x, y, d]
    });
    return this;
  }
  /**
   * 交换事件位置
   * @param {number} eventId 事件id
   * @param {number} eventId1 事件1id
   * @param {enumDirection} d =0,方向：enumDirection
   */
  exchangeEventLocationWithAnother(eventId, eventId1, d = 0) {
    this.commands.push({
      code: 203,
      indent: this.indent,
      parameters: [eventId, 2, eventId1, , d]
    });
    return this;
  }
  /**
   * 滚动地图
   * @param {enumDirection} direction 方向：enumDirection
   * @param {number} distance 距离
   * @param {enumSpeed} speed 速度：enumSpeed
   */
  scrollMap(direction, distance, speed) {
    this.commands.push({
      code: 204,
      indent: this.indent,
      parameters: [direction, distance, speed]
    });
    return this;
  }
  /**
   * 设置移动路线
   * @param {*} eventId 事件id：-1玩家，0本事件
   * @param {*} routeCommandFunc 生成移动路线的函数\
   * 例如：\
   * setMovementRoute(0, function (routeCommand) {\
   * return routeCommand.moveForward()\
   * .turn90dRL()\
   * .end().getCommands(false,false,false);\
   * })
   */
  setMovementRoute(
    eventId,
    routeCommandFunc = command => ({
      list: [],
      repeat: false,
      skippable: false,
      wait: false
    })
  ) {
    const routeParameters = routeCommandFunc(new RouteCommandGenerator());
    this.commands.push({
      code: 205,
      indent: this.indent,
      parameters: [eventId, routeParameters]
    });
    this.commands.push({
      code: 505,
      indent: this.indent,
      parameters: routeParameters.list
    });
    return this;
  }
  /**
   * 载具乘降
   */
  gettingOnOffVehicles() {
    this.commands.push({
      code: 206,
      indent: this.indent,
      parameters: []
    });
    return this;
  }
  /**
   * 更改人物透明状态
   * @param {enumSwitchValue} isTransparent 是否透明：enumSwitchValue
   */
  changeTransparency(isTransparent) {
    this.commands.push({
      code: 211,
      indent: this.indent,
      parameters: [isTransparent]
    });
    return this;
  }
  /**
   * 显示动画
   * @param {number} eventId 事件id：-1玩家，0本事件
   * @param {number} animationId 动画id
   * @param {boolean} wait 等待完成
   */
  showAnimation(eventId, animationId, wait) {
    this.commands.push({
      code: 212,
      indent: this.indent,
      parameters: [eventId, animationId, wait]
    });
    return this;
  }
  /**
   * 显示气球
   * @param {number} eventId 事件id：-1玩家，0本事件
   * @param {number} balloonId 气球id
   * @param {boolean} wait 等待完成
   */
  showBalloonIcon(eventId, balloonId, wait) {
    this.commands.push({
      code: 213,
      indent: this.indent,
      parameters: [eventId, balloonId, wait]
    });
    return this;
  }
  /**
   * 暂时消除本事件
   */
  eraseEvent() {
    this.commands.push({
      code: 214,
      indent: this.indent,
      parameters: []
    });
    return this;
  }
  /**
   * 更改队列行进
   * @param {boolean} [isShow] =0,是否
   */
  changePlayerFollowers(isShow = 0) {
    this.commands.push({
      code: 216,
      indent: this.indent,
      parameters: [isShow]
    });
    return this;
  }
  /**
   * 集合队列成员
   */
  gatherFollowers() {
    this.commands.push({
      code: 217,
      indent: this.indent,
      parameters: []
    });
    return this;
  }
  /**
   * 淡出画面
   */
  fadeoutScreen() {
    this.commands.push({
      code: 221,
      indent: this.indent,
      parameters: []
    });
    return this;
  }
  /**
   * 淡入画面
   */
  fadeinScreen() {
    this.commands.push({
      code: 222,
      indent: this.indent,
      parameters: []
    });
    return this;
  }
  /**
   * 更改画面色调\
   * 例子red, green, blue, gray\
   * 正常画风：0，0，0，0\
   * 黑暗画风：-68，-68，-68，0\
   * 茶色画风：34，-34，-68，170\
   * 黄昏画风：68，-34，-34，0\
   * 夜晚画风：-68，-68，0，68
   * @param {number} red =0,-255~255 红色偏移
   * @param {number} green =0,-255~255 绿色偏移
   * @param {number} blue =0,-255~255 蓝色偏移
   * @param {number} gray =0,0~255 灰度
   * @param {number} duration =60,持续多少帧
   * @param {boolean} isWait =true,是否等待完成
   */
  tintScreen(
    red = 0,
    green = 0,
    blue = 0,
    gray = 0,
    duration = 60,
    isWait = true
  ) {
    this.commands.push({
      code: 223,
      indent: this.indent,
      parameters: [[red, green, blue, gray], duration, isWait]
    });
    return this;
  }
  /**
   * 闪烁画面
   * @param {number} [red] =255,0~255 红色浓度
   * @param {number} [green] =255,0~255 绿色浓度
   * @param {number} [blue] =255,0~255 蓝色浓度
   * @param {number} [intensity] =170,0~255 强度
   * @param {number} [duration] =60,持续多少帧
   * @param {boolean} [isWait] =true,是否等待完成
   */
  flashScreen(
    red = 255,
    green = 255,
    blue = 255,
    intensity = 170,
    duration = 60,
    isWait = true
  ) {
    this.commands.push({
      code: 224,
      indent: this.indent,
      parameters: [[red, green, blue, intensity], duration, isWait]
    });
    return this;
  }
  /**
   * 震动屏幕
   * @param {number} [power] =5,1~9强度
   * @param {number} [speed] =5,1~9速度
   * @param {number} [duration] =60,持续多少帧
   * @param {boolean} [isWait] =true,是否等待完成
   */
  shakeScreen(power = 5, speed = 5, duration = 60, isWait = true) {
    this.commands.push({
      code: 225,
      indent: this.indent,
      parameters: [power, speed, duration, isWait]
    });
    return this;
  }
  /**
   * 等待
   * @param {number} duration 持续多少帧
   */
  wait(duration) {
    this.commands.push({
      code: 230,
      indent: this.indent,
      parameters: [duration]
    });
    return this;
  }
  /**
   * 显示图片
   * @param {number} zindex 层号
   * @param {string} picture 图片名
   * @param {number} origin 原点位置：0左上，1中心
   * @param {number} x x坐标
   * @param {number} y y坐标
   * @param {number} [wScale] =100,0~100宽比例
   * @param {number} [hScale] =100,0~100高比例
   * @param {number} [Opacity] =255,0~255不透明度
   * @param {enumBlendMode} [blendMode] =0,合成模式：enumBlendMode
   */
  showPicture(
    zindex,
    picture,
    origin,
    x,
    y,
    wScale = 100,
    hScale = 100,
    Opacity = 255,
    blendMode = 0
  ) {
    this.commands.push({
      code: 231,
      indent: this.indent,
      parameters: [
        zindex,
        picture,
        origin,
        0,
        x,
        y,
        wScale,
        hScale,
        Opacity,
        blendMode
      ]
    });
    return this;
  }
  /**
   * 移动图片
   * @param {number} zindex 层号
   * @param {string} picture 图片名
   * @param {number} origin 原点位置：0左上，1中心
   * @param {number} x x坐标
   * @param {number} y y坐标
   * @param {number} [wScale] =100,0~100宽比例
   * @param {number} [hScale] =100,0~100高比例
   * @param {number} [Opacity] =255,0~255不透明度
   * @param {enumBlendMode} [blendMode] 合成模式：enumBlendMode
   * @param {number} [duration] 持续多少帧
   * @param {boolean} [isWait] 是否等待完成
   */
  movePicture(
    zindex,
    origin,
    x,
    y,
    wScale = 100,
    hScale = 100,
    Opacity = 255,
    blendMode = 0,
    duration = 60,
    isWait = true
  ) {
    this.commands.push({
      code: 232,
      indent: this.indent,
      parameters: [
        zindex,
        "",
        origin,
        0,
        x,
        y,
        wScale,
        hScale,
        Opacity,
        blendMode,
        duration,
        isWait
      ]
    });
    return this;
  }
  /**
   * 旋转图片
   * @param {number} zindex 层号
   * @param {number} angle -90~90角度
   */
  rotatePicture(zindex, angle) {
    this.commands.push({
      code: 233,
      indent: this.indent,
      parameters: [zindex, angle]
    });
    return this;
  }
  /**
   * 更改图片色调\
   * 例子red, green, blue, gray\
   * 正常画风：0，0，0，0\
   * 黑暗画风：-68，-68，-68，0\
   * 茶色画风：34，-34，-68，170\
   * 黄昏画风：68，-34，-34，0\
   * 夜晚画风：-68，-68，0，68
   * @param {number} zindex 层号
   * @param {number} [red] =0,-255~255 红色偏移
   * @param {number} [green] =0,-255~255 绿色偏移
   * @param {number} [blue] =0,-255~255 蓝色偏移
   * @param {number} [gray] =0,0~255 灰度
   * @param {number} [duration] =60,持续多少帧
   * @param {boolean} [isWait] =true,是否等待完成
   */
  tintPicture(
    zindex,
    red = 0,
    green = 0,
    blue = 0,
    gray = 0,
    duration = 60,
    isWait = true
  ) {
    this.commands.push({
      code: 234,
      indent: this.indent,
      parameters: [zindex, [red, green, blue, gray], duration, isWait]
    });
    return this;
  }
  /**
   * 擦除图片
   * @param {number} zindex 层号
   */
  erasePicture(zindex) {
    this.commands.push({
      code: 235,
      indent: this.indent,
      parameters: [zindex]
    });
    return this;
  }
  /**
   * 设置天气
   * @param {enumWeather} [type] ='none',天气类型：enumWeather
   * @param {number} [power] =5,强度1~9
   * @param {number} [duration] =60,持续多少帧
   * @param {boolean} [isWait] =true,是否等待完成
   */
  setWeatherEffect(
    type = $mvs.enumWeather.None,
    power = 5,
    duration = 60,
    isWait = true
  ) {
    this.commands.push({
      code: 236,
      indent: this.indent,
      parameters: [type, power, duration, isWait]
    });
    return this;
  }
  /**
   * 播放bgm
   * @param {string} [name] ='',声音名
   * @param {number} [volume] =90,0~100音量
   * @param {number} [pitch] =100,50~150音调
   * @param {number} [pan] =0,-100~100声相
   */
  playBGM(name = "", volume = 90, pitch = 100, pan = 0) {
    this.commands.push({
      code: 241,
      indent: this.indent,
      parameters: [{ name, volume, pitch, pan }]
    });
    return this;
  }
  /**
   * 淡出bgm
   * @param {number} duration 持续多少帧
   */
  fadeoutBGM(duration) {
    this.commands.push({
      code: 242,
      indent: this.indent,
      parameters: [duration]
    });
    return this;
  }
  /**
   * 保存bgm
   */
  saveBGM() {
    this.commands.push({
      code: 243,
      indent: this.indent
    });
    return this;
  }
  /**
   * 恢复bgm
   */
  resumeBGM() {
    this.commands.push({
      code: 244,
      indent: this.indent
    });
    return this;
  }
  /**
   * 播放bgs
   * @param {string} [name] ='',声音名
   * @param {number} [volume] =90,0~100音量
   * @param {number} [pitch] =100,50~150音调
   * @param {number} [pan] =0,-100~100声相
   */
  playBGS(name = "", volume = 90, pitch = 100, pan = 0) {
    this.commands.push({
      code: 245,
      indent: this.indent,
      parameters: [{ name, volume, pitch, pan }]
    });
    return this;
  }
  /**
   * 淡出bgs
   * @param {number} duration 持续多少帧
   */
  fadeoutBGS(duration) {
    this.commands.push({
      code: 246,
      indent: this.indent,
      parameters: [duration]
    });
    return this;
  }
  /**
   * 播放me
   * @param {string} [name] ='',声音名
   * @param {number} [volume] =90,0~100音量
   * @param {number} [pitch] =100,50~150音调
   * @param {number} [pan] =0,-100~100声相
   */
  playME(name = "", volume = 90, pitch = 100, pan = 0) {
    this.commands.push({
      code: 249,
      indent: this.indent,
      parameters: [{ name, volume, pitch, pan }]
    });
    return this;
  }
  /**
   * 播放se
   * @param {string} [name] ='',声音名
   * @param {number} [volume] =90,0~100音量
   * @param {number} [pitch] =100,50~150音调
   * @param {number} [pan] =0,-100~100声相
   */
  playSE(name = "", volume = 90, pitch = 100, pan = 0) {
    this.commands.push({
      code: 250,
      indent: this.indent,
      parameters: [{ name, volume, pitch, pan }]
    });
    return this;
  }
  /**
   * 停止se
   */
  stopSE() {
    this.commands.push({
      code: 251,
      indent: this.indent
    });
    return this;
  }
  /**
   * 播放影片
   * @param {string} [name] ='',影片名
   */
  playMovie(name = "") {
    this.commands.push({
      code: 261,
      indent: this.indent,
      parameters: [name]
    });
    return this;
  }
  /**
   * 启用/禁用地图名显示
   * @param {enumSwitchValue} [isMapNameDisplay] =ON,是否显示地图名：enumSwitchValue
   */
  changeMapNameDisplay(isMapNameDisplay = $mvs.enumSwitchValue.ON) {
    this.commands.push({
      code: 281,
      indent: this.indent,
      parameters: [isMapNameDisplay]
    });
    return this;
  }
  /**
   * 更改地图图块集
   * @param {number} tilesetId 图块集id
   */
  changeTileset(tilesetId) {
    this.commands.push({
      code: 282,
      indent: this.indent,
      parameters: [tilesetId]
    });
    return this;
  }
  /**
   * 设置战斗背景图
   * @param {string} [imageWall] ='',墙图
   * @param {string} [imageGround] ='',地图
   */
  changeBattleBackground(imageWall = "", imageGround = "") {
    this.commands.push({
      code: 283,
      indent: this.indent,
      parameters: [imageWall, imageGround]
    });
    return this;
  }
  /**
   * 更改远景
   * @param {string} imageName 图片名
   * @param {boolean} [repeatX] =false,横向循环
   * @param {boolean} [repeatY] =false,纵向循环
   * @param {number} [xVal] =0,-32~32横向滚动值
   * @param {number} [yVal] =0,-32~32纵向滚动值
   */
  changeParallax(
    imageName,
    repeatX = false,
    repeatY = false,
    xVal = 0,
    yVal = 0
  ) {
    this.commands.push({
      code: 284,
      indent: this.indent,
      parameters: [imageName, repeatX, repeatY, xVal, yVal]
    });
    return this;
  }
  /**
   * 战斗处理
   * @param {number} troopId 敌群id
   * @param {*} [subCommandEscape] 匿名函数类型，逃跑时调用。如需不可逃跑请赋值null
   * @param {*} [subCommandLoss] 匿名函数类型，战败时调用。如需不可战败请赋值null
   * @param {*} [subCommandWin] 匿名函数类型，成功时调用
   */
  battleProcessing(
    troopId,
    subCommandEscape = () => {},
    subCommandLoss = () => {},
    subCommandWin = () => {}
  ) {
    const canEscape = subCommandEscape !== null;
    const canLose = subCommandLoss !== null;
    this.commands.push({
      code: 301,
      indent: this.indent,
      parameters: [0, troopId, canEscape, canLose]
    });
    if (canEscape) {
      this.commands.push({
        code: 601,
        indent: this.indent
      });
      this.indent++;
      subCommandEscape && subCommandEscape(this);
      this.indent--;
    }
    if (canLose) {
      this.commands.push({
        code: 602,
        indent: this.indent
      });
      this.indent++;
      subCommandLoss && subCommandLoss(this);
      this.indent--;
    }
    if (canEscape || canLose) {
      this.commands.push({
        code: 603,
        indent: this.indent
      });
      this.indent++;
      subCommandWin(this);
      this.indent--;
    }
    this.commands.push({
      code: 604,
      indent: this.indent
    });
    return this;
  }

  /**
   * 处理商店
   * @param {boolean} isPurchaseOnly 是否仅能采购
   * @param {*} getItems 回调函数，获取物品列表\
   * 例子：\
   * this.shopProcessing(false, itemList =>
   *   itemList
   *     .addItem(1)
   *     .addWeapon(3, 100)
   *     .done()
   *  );
   */
  shopProcessing(isPurchaseOnly = false, getItems = () => [[0, 0, 0, 0]]) {
    const [firstItem, ...restItems] = getItems(new ItemListGenerator());
    this.commands.push({
      code: 302,
      indent: this.indent,
      parameters: [...firstItem, isPurchaseOnly]
    });
    restItems.forEach(item => {
      this.commands.push({
        code: 302,
        indent: this.indent,
        parameters: item
      });
    });
    return this;
  }
  /**
   * 起名界面
   * @param {number} actorId 角色id
   * @param {number} [maxChars] =8,最大字符数1~16
   */
  nameInputProcessing(actorId, maxChars = 8) {
    this.commands.push({
      code: 303,
      indent: this.indent,
      parameters: [actorId, maxChars]
    });
    return this;
  }
  /**
   * 更改hp
   * @param {number} actorId 角色id：0队列中的全部
   * @param {number} amount 差值
   * @param {boolean} [causeDead] =false,如果减少是否能引起死亡
   */
  changeHP(actorId, amount, causeDead = false) {
    this.commands.push({
      code: 311,
      indent: this.indent,
      parameters: [
        0,
        actorId,
        amount >= 0 ? 0 : 1,
        0,
        Math.abs(amount),
        causeDead
      ]
    });
    return this;
  }
  /**
   * 更改mp
   * @param {number} actorId 角色id：0队列中的全部
   * @param {number} amount 差值
   */
  changeMP(actorId, amount) {
    this.commands.push({
      code: 312,
      indent: this.indent,
      parameters: [0, actorId, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
    });
    return this;
  }
  /**
   * 更改tp
   * @param {number} actorId 角色id：0队列中的全部
   * @param {number} amount 差值
   */
  changeTP(actorId, amount) {
    this.commands.push({
      code: 326,
      indent: this.indent,
      parameters: [0, actorId, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
    });
    return this;
  }
  /**
   * 更改角色状态
   * @param {number} actorId 角色id：0队列中的全部
   * @param {enumSwitchValue} op 附加/移除：enumSwitchValue
   * @param {number} stateId 状态id
   */
  changeState(actorId, op, stateId) {
    this.commands.push({
      code: 313,
      indent: this.indent,
      parameters: [0, actorId, op, stateId]
    });
    return this;
  }
  /**
   * 完全恢复
   * @param {number} actorId 角色id：0队列中的全部
   */
  recoverAll(actorId) {
    this.commands.push({
      code: 314,
      indent: this.indent,
      parameters: [0, actorId]
    });
    return this;
  }
  /**
   * 更改exp
   * @param {number} actorId 角色id：0队列中的全部
   * @param {number} amount 差值
   * @param {boolean} [showLevelUp] =false,如果升级是否显示升级信息
   */
  changeEXP(actorId, amount, showLevelUp = false) {
    this.commands.push({
      code: 315,
      indent: this.indent,
      parameters: [
        0,
        actorId,
        amount >= 0 ? 0 : 1,
        0,
        Math.abs(amount),
        showLevelUp
      ]
    });
    return this;
  }
  /**
   * 更改等级
   * @param {number} actorId 角色id：0队列中的全部
   * @param {number} amount 差值
   * @param {boolean} [showLevelUp] =false,如果升级是否显示升级信息
   */
  changeLevel(actorId, amount, showLevelUp = false) {
    this.commands.push({
      code: 316,
      indent: this.indent,
      parameters: [
        0,
        actorId,
        amount >= 0 ? 0 : 1,
        0,
        Math.abs(amount),
        showLevelUp
      ]
    });
    return this;
  }
  /**
   * 更改角色能力
   * @param {number} actorId 角色id：0队列中的全部
   * @param {enumAbility} type 属性类型：enumAbility
   * @param {number} amount 差值
   */
  changeAbility(actorId, type, amount) {
    this.commands.push({
      code: 317,
      indent: this.indent,
      parameters: [0, actorId, type, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
    });
    return this;
  }
  /**
   * 更改角色技能
   * @param {number} actorId 角色id：0队列中的全部
   * @param {enumSwitchValue} op 附加/移除：enumSwitchValue
   * @param {number} skillId 技能id
   */
  changeSkill(actorId, op, skillId) {
    this.commands.push({
      code: 318,
      indent: this.indent,
      parameters: [0, actorId, op, skillId]
    });
    return this;
  }
  /**
   * 更改装备
   * @param {number} actorId 角色id
   * @param {number} equipType 装备类别
   * @param {number} equipId 装备id
   */
  changeEquipment(actorId, equipType, equipId) {
    this.commands.push({
      code: 319,
      indent: this.indent,
      parameters: [actorId, equipType, equipId]
    });
    return this;
  }
  /**
   * 更改名字
   * @param {number} actorId 角色id
   * @param {string} name 名字
   */
  changeName(actorId, name) {
    this.commands.push({
      code: 320,
      indent: this.indent,
      parameters: [actorId, name]
    });
    return this;
  }
  /**
   * 更改角色职业
   * @param {number} actorId 角色id
   * @param {number} classId 职业id
   * @param {boolean} [keepLevel] =false,是否保持等级
   */
  changeClass(actorId, classId, keepLevel = false) {
    this.commands.push({
      code: 321,
      indent: this.indent,
      parameters: [actorId, classId, keepLevel]
    });
    return this;
  }
  /**
   * 更改角色图片
   * @param {number} actorId 角色id
   * @param {string} characterImageName 行走图名
   * @param {number} characterImageIndex 行走图index
   * @param {string} faceImageName 头像图名
   * @param {number} faceImageIndex 头像图index
   * @param {string} battlerImageName 战斗图名
   */
  changeActorImages(
    actorId,
    characterImageName,
    characterImageIndex,
    faceImageName,
    faceImageIndex,
    battlerImageName
  ) {
    this.commands.push({
      code: 322,
      indent: this.indent,
      parameters: [
        actorId,
        characterImageName,
        characterImageIndex,
        faceImageName,
        faceImageIndex,
        battlerImageName
      ]
    });
    return this;
  }
  /**
   * 更改载具图
   * @param {number} vehicleId 载具id
   * @param {string} imageName 载具图片名
   * @param {number} imageIndex 载具图index
   */
  changeVehicleImage(vehicleId, imageName, imageIndex) {
    this.commands.push({
      code: 323,
      indent: this.indent,
      parameters: [vehicleId, imageName, imageIndex]
    });
    return this;
  }
  /**
   * 更改角色昵称
   * @param {number} actorId 角色id
   * @param {string} nickName 昵称
   */
  changeClass(actorId, nickName) {
    this.commands.push({
      code: 324,
      indent: this.indent,
      parameters: [actorId, nickName]
    });
    return this;
  }
  /**
   * 更改角色简介
   * @param {number} actorId 角色id
   * @param {string} profile 简介
   */
  changeProfile(actorId, profile) {
    this.commands.push({
      code: 325,
      indent: this.indent,
      parameters: [actorId, profile]
    });
    return this;
  }
  /**
   * 更改敌人hp
   * @param {number} enemyIndex 敌人index：从0开始，-1整个敌群
   * @param {number} amount 差值
   * @param {boolean} [causeDead] =false,如果减少是否能引起死亡
   */
  changeEnemyHP(enemyIndex, amount, causeDead = false) {
    this.commands.push({
      code: 331,
      indent: this.indent,
      parameters: [
        enemyIndex,
        amount >= 0 ? 0 : 1,
        0,
        Math.abs(amount),
        causeDead
      ]
    });
    return this;
  }
  /**
   * 更改敌人mp
   * @param {number} enemyIndex 敌人index：从0开始，-1整个敌群
   * @param {number} amount 差值
   */
  changeEnemyMP(enemyIndex, amount) {
    this.commands.push({
      code: 332,
      indent: this.indent,
      parameters: [enemyIndex, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
    });
    return this;
  }
  /**
   * 更改敌人tp
   * @param {number} enemyIndex 敌人index：从0开始，-1整个敌群
   * @param {number} amount 差值
   */
  changeEnemyTP(enemyIndex, amount) {
    this.commands.push({
      code: 342,
      indent: this.indent,
      parameters: [enemyIndex, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
    });
    return this;
  }
  /**
   * 更改角色状态
   * @param {number} enemyIndex 敌人index：从0开始，-1整个敌群
   * @param {enumSwitchValue} op 附加/移除：enumSwitchValue
   * @param {number} stateId 状态id
   */
  changeEnemyState(enemyIndex, op, stateId) {
    this.commands.push({
      code: 333,
      indent: this.indent,
      parameters: [enemyIndex, op, stateId]
    });
    return this;
  }
  /**
   * 完全恢复
   * @param {number} enemyIndex 敌人index：从0开始，-1整个敌群
   */
  enemyRecoverAll(enemyIndex) {
    this.commands.push({
      code: 334,
      indent: this.indent,
      parameters: [enemyIndex]
    });
    return this;
  }
  /**
   * 敌人出现
   * @param {number} enemyIndex 敌人index：从0开始
   */
  enemyAppear(enemyIndex) {
    this.commands.push({
      code: 335,
      indent: this.indent,
      parameters: [enemyIndex]
    });
    return this;
  }
  /**
   * 敌人变身
   * @param {number} enemyIndex 敌人index：从0开始
   * @param {number} enemyId 敌人id(敌人种类)：从1开始
   */
  enemyTransform(enemyIndex, enemyId) {
    this.commands.push({
      code: 336,
      indent: this.indent,
      parameters: [enemyIndex, enemyId]
    });
    return this;
  }
  /**
   * 敌人显示动画
   * @param {number} enemyIndex 敌人index：从0开始，-1整个敌群
   * @param {number} animationId 动画id
   */
  showBattleAnimation(enemyIndex, animationId) {
    this.commands.push({
      code: 337,
      indent: this.indent,
      parameters: [enemyIndex, animationId, enemyIndex === -1]
    });
    return this;
  }
  /**
   * 角色强制释放技能
   * @param {number} actorId 角色id，
   * @param {number} actionId 技能id
   * @param {number} targetIndex 目标index，0开始，-1随机，-2上次选择
   */
  actorForceBattleAction(actorId, actionId, targetIndex) {
    this.commands.push({
      code: 339,
      indent: this.indent,
      parameters: [1, actorId, actionId, targetIndex]
    });
    return this;
  }
  /**
   * 敌人强制释放技能
   * @param {number} enemyIndex 敌人index，0开始
   * @param {number} actionId 技能id
   * @param {number} targetIndex 目标index，0开始，-1随机，-2上次选择
   */
  enemyForceBattleAction(enemyIndex, actionId, targetIndex) {
    this.commands.push({
      code: 339,
      indent: this.indent,
      parameters: [1, enemyIndex, actionId, targetIndex]
    });
    return this;
  }
  /**
   * 中断战斗
   */
  abortBattle() {
    this.commands.push({ code: 340, indent: this.indent });
    return this;
  }
  /**
   * 打开菜单画面
   */
  openMenuScreen() {
    this.commands.push({ code: 351, indent: this.indent });
    return this;
  }
  /**
   * 打开存档画面
   */
  openSaveScreen() {
    this.commands.push({ code: 352, indent: this.indent });
    return this;
  }
  /**
   * 游戏结束
   */
  gameOver() {
    this.commands.push({ code: 353, indent: this.indent });
    return this;
  }
  /**
   * 返回标题画面
   */
  returnTitleScreen() {
    this.commands.push({ code: 354, indent: this.indent });
    return this;
  }
  /**
   * 调用js函数
   * @param {*} jsFunction js函数
   */
  callJsFunction(jsFunction) {
    this.commands.push({
      code: 355,
      indent: this.indent,
      parameters: [jsFunction]
    });
    return this;
  }
  /**
   * 调用插件命令
   * @param {*} command 命令字符串参数
   */
  callPluginCommand(command) {
    this.commands.push({
      code: 356,
      indent: this.indent,
      parameters: [command]
    });
    return this;
  }
  /**
   * 背景
   * @readonly
   * @property
   * @enum {number}
   */
  static enumBackground = {
    /** 窗口 */
    Window: 0,
    /** 渐变 */
    Dim: 1,
    /** 透明 */
    Transparent: 2
  };
  /**
   * 纵向位置
   * @readonly
   * @property
   * @enum {number}
   */
  static enumPositionV = {
    /** 顶部 */
    Top: 0,
    /** 中间 */
    Middle: 1,
    /** 底部 */
    Botton: 2
  };
  /**
   * 横向位置
   * @readonly
   * @property
   * @enum {number}
   */
  static enumPositionH = {
    /** 左侧 */
    Left: 0,
    /** 中间 */
    Middle: 1,
    /** 右侧 */
    Right: 2
  };
  /**
   * 横向位置
   * @readonly
   * @property
   * @enum {number}
   */
  static enumItemType = {
    /** 一般物品 */
    RegularItem: 1,
    /** 贵重物品 */
    KeyItem: 2,
    /** 隐藏物品A */
    HiddenItemA: 3,
    /** 隐藏物品B */
    HiddenItemB: 4
  };
  /**
   * 开关值
   * @readonly
   * @property
   * @enum {number}
   */
  static enumSwitchValue = {
    /** 开 */
    ON: 0,
    /** 关 */
    OFF: 1
  };
  /**
   * 开关值
   * @readonly
   * @property
   * @enum {string}
   */
  static enumSelfSwitchName = {
    A: "A",
    B: "B",
    C: "C",
    D: "D"
  };
  /**
   * 载具类型
   * @readonly
   * @property
   * @enum {number}
   */
  static enumVehicleType = {
    /** 小船 */
    Boat: 0,
    /** 大船 */
    Ship: 1,
    /** 飞艇 */
    Airship: 2
  };
  /**
   * 朝向
   * @readonly
   * @property
   * @enum {number}
   */
  static enumDirection = {
    /** 不变 */
    Retain: 0,
    /** 下 */
    Down: 1,
    /** 左 */
    Left: 2,
    /** 右 */
    Right: 3,
    /** 上 */
    Top: 4
  };
  /**
   * 淡入淡出
   * @readonly
   * @property
   * @enum {number}
   */
  static enumFadeType = {
    /** 黑 */
    Black: 0,
    /** 白 */
    White: 1,
    /** 无 */
    None: 2
  };
  /**
   * 速度
   * @readonly
   * @property
   * @enum {number}
   */
  static enumSpeed = {
    /** 八分之一 */
    x8Slower: 1,
    /** 四分之一 */
    x4Slower: 2,
    /** 二分之一 */
    x2Slower: 3,
    /** 标准速递 */
    Normal: 4,
    /** 二倍速 */
    x2Faster: 5,
    /** 四倍速 */
    x4Faster: 6
  };
  /**
   * 合成模式
   * @readonly
   * @property
   * @enum {number}
   */
  static enumBlendMode = {
    /** 正常 */
    Normal: 0,
    /** 叠加 */
    Additive: 1,
    /** 正片叠底 */
    Multiply: 2,
    /** 滤色 */
    Screen: 3
  };
  /**
   * 天气
   * @readonly
   * @property
   * @enum {string}
   */
  static enumWeather = {
    /** 无 */
    None: "none",
    /** 下雨 */
    Rain: "rain",
    /** 风暴 */
    Storm: "storm",
    /** 下雪 */
    Snow: "snow"
  };
  /**
   * 天气
   * @readonly
   * @property
   * @enum {number}
   */
  static enumAbility = {
    /** 最大HP */
    MaxHP: 0,
    /** 最大MP */
    MaxMP: 1,
    /** 攻击 */
    Attack: 2,
    /** 防御 */
    Defense: 3,
    /** 魔法攻击 */
    MAttack: 4,
    /** 魔法防御 */
    MDefense: 5,
    /** 敏捷 */
    Agility: 6,
    /** 幸运 */
    Luck: 7
  };
  /**
   * 物品列表生成器
   * @property
   */
  ItemListGenerator = class {
    itemList = [];
    /**
     * 添加一个道具
     * @param {number} id 道具id
     * @param {number} [price] =null,价格不传值按照默认价格
     */
    addItem(id, price = null) {
      itemList.push(0, id, price === null ? 0 : 1, price);
    }
    /**
     * 添加一个武器
     * @param {number} id 武器id
     * @param {number} [price] =null,价格不传值按照默认价格
     */
    addWeapon(id, price = null) {
      itemList.push(1, id, price === null ? 0 : 1, price);
    }
    /**
     * 添加一个护甲
     * @param {number} id 护甲id
     * @param {number} [price] =null,价格不传值按照默认价格
     */
    addArmor(id, price = null) {
      itemList.push(2, id, price === null ? 0 : 1, price);
    }
    /**
     * 完成操作并获取数据
     */
    done() {
      return itemList;
    }
  };
  RouteCommandGenerator = class {
    constructor() {
      this.list = [];
    }
    getCommands(repeat, skippable, wait) {
      return {
        list: [...this.list, { code: 0 }],
        repeat,
        skippable,
        wait
      };
    }
    moveDown() {
      this.list.push({ code: 1 });
      return this;
    }
    moveLeft() {
      this.list.push({ code: 2 });
      return this;
    }
    moveRight() {
      this.list.push({ code: 3 });
      return this;
    }
    moveUp() {
      this.list.push({ code: 4 });
      return this;
    }
    moveLeftDown() {
      this.list.push({ code: 5 });
      return this;
    }
    moveRightDown() {
      this.list.push({ code: 6 });
      return this;
    }
    moveLeftUp() {
      this.list.push({ code: 7 });
      return this;
    }
    moveRightUp() {
      this.list.push({ code: 8 });
      return this;
    }
    moveRandom() {
      this.list.push({ code: 9 });
      return this;
    }
    moveToward() {
      this.list.push({ code: 10 });
      return this;
    }
    moveAway() {
      this.list.push({ code: 11 });
      return this;
    }
    moveForward() {
      this.list.push({ code: 12 });
      return this;
    }
    moveBackward() {
      this.list.push({ code: 13 });
      return this;
    }
    jump(x, y) {
      this.list.push({ code: 14, parameters: [x, y] });
      return this;
    }
    wait(duration) {
      this.list.push({ code: 15, parameters: [duration] });
      return this;
    }
    turnDown() {
      this.list.push({ code: 16 });
      return this;
    }
    turnLeft() {
      this.list.push({ code: 17 });
      return this;
    }
    turnRight() {
      this.list.push({ code: 18 });
      return this;
    }
    turnUp() {
      this.list.push({ code: 19 });
      return this;
    }
    turn90dR() {
      this.list.push({ code: 20 });
      return this;
    }
    turn90dL() {
      this.list.push({ code: 21 });
      return this;
    }
    turn180d() {
      this.list.push({ code: 22 });
      return this;
    }
    turn90dRL() {
      this.list.push({ code: 23 });
      return this;
    }
    turnRandom() {
      this.list.push({ code: 24 });
      return this;
    }
    turnToward() {
      this.list.push({ code: 25 });
      return this;
    }
    turnAway() {
      this.list.push({ code: 26 });
      return this;
    }
    switchOn(switchId) {
      this.list.push({ code: 27, parameters: [switchId] });
      return this;
    }
    switchOff(switchId) {
      this.list.push({ code: 28, parameters: [switchId] });
      return this;
    }
    changeSpeed(speed) {
      this.list.push({ code: 29, parameters: [speed] });
      return this;
    }
    changeFreq(freq) {
      this.list.push({ code: 30, parameters: [freq] });
      return this;
    }
    walkAnimeOn() {
      this.list.push({ code: 31 });
      return this;
    }
    walkAnimeOff() {
      this.list.push({ code: 32 });
      return this;
    }
    stepAnimeOn() {
      this.list.push({ code: 33 });
      return this;
    }
    stepAnimeOff() {
      this.list.push({ code: 34 });
      return this;
    }
    dirFixOn() {
      this.list.push({ code: 35 });
      return this;
    }
    dirFixOff() {
      this.list.push({ code: 36 });
      return this;
    }
    throughOn() {
      this.list.push({ code: 37 });
      return this;
    }
    throughOff() {
      this.list.push({ code: 38 });
      return this;
    }
    transparentOn() {
      this.list.push({ code: 39 });
      return this;
    }
    transparentOff() {
      this.list.push({ code: 40 });
      return this;
    }
    changeImage(imageName, imageIndex) {
      this.list.push({ code: 41, parameters: [imageName, imageIndex] });
      return this;
    }
    changeOpacity(opacity) {
      this.list.push({ code: 42, parameters: [opacity] });
      return this;
    }
    changeBlendMode(blendMode) {
      this.list.push({ code: 43, parameters: [blendMode] });
      return this;
    }
    playSE(name, volume = 90, pitch = 100, pan = 0) {
      this.list.push({ code: 44, parameters: [{ name, volume, pitch, pan }] });
      return this;
    }
    callJsFunction(jsFunction) {
      this.list.push({ code: 45, parameters: [jsFunction] });
      return this;
    }
  };
}
