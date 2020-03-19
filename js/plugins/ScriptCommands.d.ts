namespace $mvs {
  /**
   * 背景
   */
  enum enumBackground {
    /** 窗口 */
    Window = 0,
    /** 渐变 */
    Dim = 1,
    /** 透明 */
    Transparent = 2
  }
  /**
   * 纵向位置
   */
  enum enumPositionV {
    /** 顶部 */
    Top = 0,
    /** 中间 */
    Middle = 1,
    /** 底部 */
    Botton = 2
  }
  /**
   * 横向位置
   */
  enum enumPositionH {
    /** 左侧 */
    Left = 0,
    /** 中间 */
    Middle = 1,
    /** 右侧 */
    Right = 2
  }

  /**
   * 横向位置
   */
  enum enumItemType {
    /** 一般物品 */
    RegularItem = 1,
    /** 贵重物品 */
    KeyItem = 2,
    /** 隐藏物品A */
    HiddenItemA = 3,
    /** 隐藏物品B */
    HiddenItemB = 4
  }
  /**
   * 开关值
   */
  enum enumSwitchValue {
    /** 开 */
    ON = 0,
    /** 关 */
    OFF = 1
  }
  /**
   * 开关值
   */
  enum enumSelfSwitchName {
    A = "A",
    B = "B",
    C = "C",
    D = "D"
  }
  /**
   * 载具类型
   */
  enum enumVehicleType {
    /** 小船 */
    Boat = 0,
    /** 大船 */
    Ship = 1,
    /** 飞艇 */
    Airship = 2
  }
  /**
   * 朝向
   */
  enum enumDirection {
    /** 不变 */
    Retain = 0,
    /** 下 */
    Down = 1,
    /** 左 */
    Left = 2,
    /** 右 */
    Right = 3,
    /** 上 */
    Top = 4
  }
  /**
   * 淡入淡出
   */
  enum enumFadeType {
    /** 黑 */
    Black = 0,
    /** 白 */
    White = 1,
    /** 无 */
    None = 2
  }
  /**
   * 速度
   */
  enum enumSpeed {
    /** 八分之一 */
    x8Slower = 1,
    /** 四分之一 */
    x4Slower = 2,
    /** 二分之一 */
    x2Slower = 3,
    /** 标准速递 */
    Normal = 4,
    /** 二倍速 */
    x2Faster = 5,
    /** 四倍速 */
    x4Faster = 6
  }
  /**
   * 合成模式
   */
  enum enumBlendMode {
    /** 正常 */
    Normal = 0,
    /** 叠加 */
    Additive = 1,
    /** 正片叠底 */
    Multiply = 2,
    /** 滤色 */
    Screen = 3
  }
  /**
   * 天气
   */
  enum enumWeather {
    /** 无 */
    None = "none",
    /** 下雨 */
    Rain = "rain",
    /** 风暴 */
    Storm = "storm",
    /** 下雪 */
    Snow = "snow"
  }
  /**
   * 天气
   */
  enum enumAbility {
    /** 最大HP */
    MaxHP = 0,
    /** 最大MP */
    MaxMP = 1,
    /** 攻击 */
    Attack = 2,
    /** 防御 */
    Defense = 3,
    /** 魔法攻击 */
    MAttack = 4,
    /** 魔法防御 */
    MDefense = 5,
    /** 敏捷 */
    Agility = 6,
    /** 幸运 */
    Luck = 7
  }
  class RouteCommandGenerator {
    constructor()
    getCommands(
      repeat: boolean,
      skippable : boolean,
      wait : boolean
    ): {
      list: [{ code: number }];
      repeat: boolean;
      skippable: boolean;
      wait: boolean;
    };
    moveDown(): $mvs.RouteCommandGenerator;
    moveLeft(): $mvs.RouteCommandGenerator;
    moveRight(): $mvs.RouteCommandGenerator;
    moveUp(): $mvs.RouteCommandGenerator;
    moveLeftDown(): $mvs.RouteCommandGenerator;
    moveRightDown(): $mvs.RouteCommandGenerator;
    moveLeftUp(): $mvs.RouteCommandGenerator;
    moveRightUp(): $mvs.RouteCommandGenerator;
    moveRandom(): $mvs.RouteCommandGenerator;
    moveToward(): $mvs.RouteCommandGenerator;
    moveAway(): $mvs.RouteCommandGenerator;
    moveForward(): $mvs.RouteCommandGenerator;
    moveBackward(): $mvs.RouteCommandGenerator;
    jump(x: number, y: number): $mvs.RouteCommandGenerator;
    wait(duration: number): $mvs.RouteCommandGenerator;
    turnDown(): $mvs.RouteCommandGenerator;
    turnLeft(): $mvs.RouteCommandGenerator;
    turnRight(): $mvs.RouteCommandGenerator;
    turnUp(): $mvs.RouteCommandGenerator;
    turn90dR(): $mvs.RouteCommandGenerator;
    turn90dL(): $mvs.RouteCommandGenerator;
    turn180d(): $mvs.RouteCommandGenerator;
    turn90dRL(): $mvs.RouteCommandGenerator;
    turnRandom(): $mvs.RouteCommandGenerator;
    turnToward(): $mvs.RouteCommandGenerator;
    turnAway(): $mvs.RouteCommandGenerator;
    switchOn(switchId: number): $mvs.RouteCommandGenerator;
    switchOff(switchId: number): $mvs.RouteCommandGenerator;
    changeSpeed(speed: number): $mvs.RouteCommandGenerator;
    changeFreq(freq: number): $mvs.RouteCommandGenerator;
    walkAnimeOn(): $mvs.RouteCommandGenerator;
    walkAnimeOff(): $mvs.RouteCommandGenerator;
    stepAnimeOn(): $mvs.RouteCommandGenerator;
    stepAnimeOff(): $mvs.RouteCommandGenerator;
    dirFixOn(): $mvs.RouteCommandGenerator;
    dirFixOff(): $mvs.RouteCommandGenerator;
    throughOn(): $mvs.RouteCommandGenerator;
    throughOff(): $mvs.RouteCommandGenerator;
    transparentOn(): $mvs.RouteCommandGenerator;
    transparentOff(): $mvs.RouteCommandGenerator;
    changeImage(
      imageName: string,
      imageIndex: number
    ): $mvs.RouteCommandGenerator;
    changeOpacity(opacity: number): $mvs.RouteCommandGenerator;
    changeBlendMode(blendMode: number): $mvs.RouteCommandGenerator;
    playSE(
      name: string,
      volume = 90,
      pitch = 100,
      pan = 0
    ): $mvs.RouteCommandGenerator;
    callJsFunction(jsFunction: () => void): $mvs.RouteCommandGenerator;
  }
  /**=
   * 物品列表生成器
   * @property
   */
  class ItemListGenerator {
    /**
     * 添加一个道具
     * @param id 道具id
     * @param price =null,价格不传值按照默认价格
     */
    addItem(
      id: number,
      price: undefined | number = null
    ): $mvs.ItemListGenerator;
    /**
     * 添加一个武器
     * @paramid 武器id
     * @param price =null,价格不传值按照默认价格
     */
    addWeapon(
      id: number,
      price: undefined | number = null
    ): $mvs.ItemListGenerator;
    /**
     * 添加一个护甲
     * @param id 护甲id
     * @param price =null,价格不传值按照默认价格
     */
    addArmor(
      id: number,
      price: undefined | number = null
    ): $mvs.ItemListGenerator;
    /**
     * 完成操作并获取数据
     */
    done(): [];
  }
}
declare class $mvs {
  /**
   * @param interpreter 事件解析器
   * @param [indent 缩进等级
   */
  constructor(
    interpreter: Game_Interpreter,
    indent?:number
  );
  /**
   * 命令串构造完毕，立即执行。
   */
  done(): void;
  /**
   * 经过计算后再调用其它命令，多用临时决定的计算下一个命令的参数值。
   * @param mvCommand 计算回调函数
   */
  calculateAndCall(mvCommand: (cmd: $mvs) => void): $mvs;
  /**
   * 示一个文本窗口
   * @param textData 要显示的文本数组
   * @param faceName 角色头像素材名
   * @param faceIndex 角色头像在素材中的序号
   * @param background 背景：enumBackground
   * @param positionType 纵向位置：enumPositionV
   */
  showText(
    textData: string[],
    faceName = "",
    faceIndex = 0,
    background = $mvs.enumBackground.Window,
    positionType = $mvs.enumPositionV.Botton
  ): $mvs;
  /**
   * 显示一个选项窗口
   * @param choiceData 选项文本数组
   * @param cancelValue 取消时执行分支序号：-2取消，-1禁止取消
   * @param defaultValue 默认时执行分支序号：-1不执行
   * @param positionType 横向位置：enumPositionH
   * @param background 背景：enumBackground
   */
  showChoices(
    choiceData: string[],
    cancelValue = -2,
    defaultValue = 0,
    positionType = $mvs.enumPositionH.Right,
    background = $mvs.enumBackground.Window
  ): $mvs;
  /**
   * 选项窗口取消时的执行的代码
   * @param subCommand 匿名函数类型，分支匹配时执行的脚本
   */
  whenChoicesCancel(subCommand: (cmd: $mvs) => void): $mvs;
  /**
   * 结束条件分支
   */
  endWhen(): $mvs;
  /**
   * 数值输入处理
   * @param variableId 结果储存的变量id
   * @param maxDigits =1,位数1~8
   */
  inputNumber(variableId: number, maxDigits = 1): $mvs;
  /**
   * 物品选择处理
   * @param variableId 结果储存的变量id
   * @param itemType =2,物品类型：enumItemType
   */
  selectItem(variableId: number, itemType = $mvs.enumItemType.KeyItem): $mvs;
  /**
   * 显示滚动文字
   * @param textData 文本数组
   * @param speed =2,速度1~8
   * @param noFast =false,禁止快进
   */
  showScrollingText(textData: string[], speed = 2, noFast = false): $mvs;
  /**
   * 根据条件函数返回值创建分支
   * @param conditionFunction 匿名函数类型4，条件函数
   * @param subCommand 匿名函数类型，分支匹配时执行的脚本
   */
  whenFunctionIs(
    conditionFunction: () => boolean,
    subCommand: (cmd: $mvs) => void
  ): $mvs;
  /**
   * 条件都不满足时的else分支
   * @param subCommand 匿名函数类型，分支匹配时执行的脚本
   */
  whenElse(subCommand: (cmd: $mvs) => void): $mvs;
  /**
   * 循环
   * @param subCommand 匿名函数类型，循环内部时执行的脚本
   */
  loop(subCommand: (cmd: $mvs) => void): $mvs;
  /**
   * 跳出循环
   */
  loopBreak(): $mvs;
  /**
   * 终止本次事件处理
   */
  exitCommand(): $mvs;
  /**
   * 调用公共事件
   * @param eventId 公共事件id
   */
  callCommonEvent(eventId: number): $mvs;
  /**
   * 定义标签
   * @param labelName 标签名
   */
  defineLabel(labelName: string): $mvs;
  /**
   * 跳转到标签
   * @param labelName 标签名
   */
  gotoLabel(labelName: string): $mvs;

  /**
   * 设置开关值（支持批量）
   * @param srcValue 开关值：enumSwitchValue
   * @param switchIdStart 开关id范围开始
   * @param switchIdEnd 开关id范围结束，默认等于开始
   */
  setSwitchAsValue(
    srcValue: $mvs.enumSwitchValue,
    switchIdStart: number,
    switchIdEnd = switchIdStart
  ): $mvs;
  /**
   * 设置变量的值
   * @param valueFunction 取值函数
   * @param variablesIdStart 变量id范围开始
   * @param variablesIdEnd 变量id范围结束，默认等于开始
   */
  setVariablesAsScriptValue(
    valueFunction: () => number,
    variablesIdStart: number,
    variablesIdEnd = variablesIdStart
  ): $mvs;
  /**
   * 设置独立开关值
   * @param srcValue 开关值：enumSwitchValue
   * @param selfSwitchName 独立开关名：enumSelfSwitchName
   */
  setSelfSwitchAsValue(
    srcValue: $mvs.enumSwitchValue,
    selfSwitchId: $mvs.enumSelfSwitchName
  ): $mvs;
  /**
   * 开始倒计时器
   * @param count 秒数
   */
  startTimer(count: number): $mvs;
  /**
   * 收起倒计时器
   */
  stopTimer(): $mvs;
  /**
   * 增减金钱
   * @param amount 差值
   */
  changeGold(amount: number): $mvs;
  /**
   * 增减物品
   * @paramitemId 物品id
   * @param amount =1,差值
   */
  changeItem(itemId: number, amount = 1): $mvs;
  /**
   * 增减武器
   * @param weaponId 武器id
   * @param amount =1,差值
   * @param includeEquip =false,减少时是否包括已经装备的武器
   */
  changeWeapons(weaponId: number, amount = 1, includeEquip = false): $mvs;
  /**
   * 增减防具
   * @param armorId 护甲id
   * @param amount =1,差值
   * @param includeEquip =false,减少时是否包含已经装备的防具
   */
  changeArmors(armorId: number, amount = 1, includeEquip = false): $mvs;
  /**
   * 角色入队
   * @param actorId 角色id
   * @param isInitialize =false,是否初始化
   */
  addPartyMember(actorId: number, isInitialize = false): $mvs;
  /**
   * 角色离队
   * @param actorId 角色id
   */
  removePartyMember(actorId: number): $mvs;
  /**
   * 更改战斗bgm
   * @param name ='',声音名
   * @param volume =90,0~100音量
   * @param pitch =100,50~150音调
   * @param pan =0,-100~100声相
   */
  changeBattleBGM(name = "", volume = 90, pitch = 100, pan = 0): $mvs;
  /**
   * 更改战斗胜利me
   * @param name ='',声音名
   * @param volume =90,0~100音量
   * @param pitch =100,50~150音调
   * @param pan =0,-100~100声相
   */
  changeVictoryME(name = "", volume = 90, pitch = 100, pan = 0): $mvs;
  /**
   * 启用/禁用存档
   * @param isAble 启用/禁用
   */
  changeSaveAccess(isAble: boolean): $mvs;
  /**
   * 启用/禁用菜单
   * @paramisAble 启用/禁用
   */
  changeMenuAccess(isAble: boolean): $mvs;
  /**
   * 启用/禁用遇敌
   * @param isAble 启用/禁用
   */
  changeEncounterDisable(isAble: boolean): $mvs;
  /**
   * 启用/禁用整队
   * @param isAble 启用/禁用
   */
  changeFormationAccess(isAble: boolean): $mvs;
  /**
   * 更改窗口颜色
   * @param red =0,-255~255 红色偏移
   * @param green =0,-255~255 绿色偏移
   * @param blue =0,-255~255 蓝色偏移
   */
  changeWindowColor(red = 0, green = 0, blue = 0): $mvs;
  /**
   * 更改战斗失败me
   * @param name ='',声音名
   * @param volume =90,0~100音量
   * @param pitch =100,50~150音调
   * @param pan =0,-100~100声相
   */
  changeDefeatME(name = "", volume = 90, pitch = 100, pan = 0): $mvs;
  /**
   * 更改载具bgm
   * @param vehicleType 载具类型：enumVehicleType
   * @param name ='',声音名
   * @param volume =90,0~100音量
   * @param pitch =100,50~150音调
   * @param pan =0,-100~100声相
   */
  changeVehicleBGM(
    vehicleType: $mvs.enumVehicleType,
    name = "",
    volume = 90,
    pitch = 100,
    pan = 0
  ): $mvs;
  /**
   * 场所移动
   * @param mapId 地图id
   * @param x x坐标
   * @param y y坐标
   * @param d 方向：enumDirection
   * @param fadeType 淡入淡出：enumFadeType
   */
  transferPlayerDirect(
    mapId: number,
    x: number,
    y: number,
    d = $mvs.enumDirection.Retain,
    fadeType = $mvs.enumFadeType.Black
  ): $mvs;
  /**
   * 设置载具id
   * @param vehicleId 载具id
   * @param mapId 地图id
   * @param x x坐标
   * @param y y坐标
   */
  setVehicleLocationDirect(
    vehicleId: number,
    mapId: number,
    x: number,
    y: number
  ): $mvs;
  /**
   * 设置事件位置
   * @param eventId 事件id
   * @param x x坐标
   * @param y y坐标
   * @param d =0方向：enumDirection
   */
  setEventLocationDirect(
    eventId: number,
    x: number,
    y: number,
    d = $mvs.enumDirection.Retain
  ): $mvs;
  /**
   * 交换事件位置
   * @param eventId 事件id
   * @param eventId1 事件1id
   * @param d =0,方向：enumDirection
   */
  exchangeEventLocationWithAnother(
    eventId: number,
    eventId1: number,
    d = $mvs.enumDirection.Retain
  ): $mvs;
  /**=
   * 滚动地图
   * @param direction 方向：enumDirection
   * @param distance 距离
   * @param speed 速度：enumSpeed
   */
  scrollMap(
    direction: $mvs.enumDirection,
    distance: number,
    speed: $mvs.enumSpeed
  ): $mvs;
  /**
   * 设置移动路线
   * @param eventId 事件id：-1玩家，0本事件
   * @param routeCommandFunc 生成移动路线的函数\
   * 例如：\
   * setMovementRoute(0, function (routeCommand) {\
   * return routeCommand.moveForward()\
   * .turn90dRL()\
   * .end().getCommands(false,false,false);\
   * })
   */
  setMovementRoute(
    eventId: number,
    routeCommandFunc = (command: $mvs.RouteCommandGenerator) => ({
      list: [{ code: 0 }],
      repeat: false,
      skippable: false,
      wait: false
    })
  ): $mvs;
  /**
   * 载具乘降
   */
  gettingOnOffVehicles(): $mvs;
  /**
   * 更改人物透明状态
   * @param isTransparent 是否透明：enumSwitchValue
   */
  changeTransparency(isTransparent: $mvs.enumSwitchValue): $mvs;
  /**=
   * 显示动画
   * @param eventId 事件id：-1玩家，0本事件
   * @param animationId 动画id
   * @param wait 等待完成
   */
  showAnimation(eventId: number, animationId: number, wait: boolean): $mvs;
  /**=
   * 显示气球
   * @param eventId 事件id：-1玩家，0本事件
   * @param balloonId 气球id
   * @param wait 等待完成
   */
  showBalloonIcon(eventId: number, balloonId: number, wait: boolean): $mvs;
  /**
   * 暂时消除本事件
   */
  eraseEvent(): $mvs;
  /**=
   * 更改队列行进
   * @param isShow =0,是否
   */
  changePlayerFollowers(isShow = 0): $mvs;
  /**
   * 集合队列成员
   */
  gatherFollowers(): $mvs;
  /**
   * 淡出画面
   */
  fadeoutScreen(): $mvs;
  /**
   * 淡入画面
   */
  fadeinScreen(): $mvs;
  /**
   * 更改画面色调\
   * 例子red, green, blue, gray\
   * 正常画风：0，0，0，0\
   * 黑暗画风：-68，-68，-68，0\
   * 茶色画风：34，-34，-68，170\
   * 黄昏画风：68，-34，-34，0\
   * 夜晚画风：-68，-68，0，68
   * @param red =0,-255~255 红色偏移
   * @param green =0,-255~255 绿色偏移
   * @param blue =0,-255~255 蓝色偏移
   * @param gray =0,0~255 灰度
   * @param duration =60,持续多少帧
   * @param isWait =true,是否等待完成
   */
  tintScreen(
    red = 0,
    green = 0,
    blue = 0,
    gray = 0,
    duration = 60,
    isWait = true
  ): $mvs;
  /**
   * 闪烁画面
   * @param red =255,0~255 红色浓度
   * @param green =255,0~255 绿色浓度
   * @param blue =255,0~255 蓝色浓度
   * @param intensity =170,0~255 强度
   * @param duration =60,持续多少帧
   * @param isWait =true,是否等待完成
   */
  flashScreen(
    red = 255,
    green = 255,
    blue = 255,
    intensity = 170,
    duration = 60,
    isWait = true
  ): $mvs;
  /**
   * 震动屏幕
   * @param power =5,1~9强度
   * @param speed =5,1~9速度
   * @param duration =60,持续多少帧
   * @param isWait =true,是否等待完成
   */
  shakeScreen(power = 5, speed = 5, duration = 60, isWait = true): $mvs;
  /**
   * 等待
   * @param duration 持续多少帧
   */
  wait(duration: number): $mvs;
  /**
   * 显示图片
   * @param zindex 层号
   * @param picture 图片名
   * @param origin 原点位置：0左上，1中心
   * @param x x坐标
   * @param y y坐标
   * @param wScale =100,0~100宽比例
   * @param hScale =100,0~100高比例
   * @param Opacity =255,0~255不透明度
   * @param blendMode =0,合成模式：enumBlendMode
   */
  showPicture(
    zindex: number,
    picture: string,
    origin: number,
    x: number,
    y: number,
    wScale = 100,
    hScale = 100,
    Opacity = 255,
    blendMode = $mvs.enumBlendMode.Normal
  ): $mvs;
  /**
   * 移动图片
   * @param zindex 层号
   * @param origin 原点位置：0左上，1中心
   * @param x x坐标
   * @param y y坐标
   * @param wScale =100,0~100宽比例
   * @param hScale =100,0~100高比例
   * @param Opacity =255,0~255不透明度
   * @param blendMode 合成模式：enumBlendMode
   * @param duration 持续多少帧
   * @param isWait 是否等待完成
   */
  movePicture(
    zindex: number,
    origin: number,
    x: number,
    y: number,
    wScale = 100,
    hScale = 100,
    Opacity = 255,
    blendMode = $mvs.enumBlendMode.Normal,
    duration = 60,
    isWait = true
  ): $mvs;
  /**
   * 旋转图片
   * @param zindex 层号
   * @param angle -90~90角度
   */
  rotatePicture(zindex: number, angle: number): $mvs;
  /**
   * 更改图片色调\
   * 例子red, green, blue, gray\
   * 正常画风：0，0，0，0\
   * 黑暗画风：-68，-68，-68，0\
   * 茶色画风：34，-34，-68，170\
   * 黄昏画风：68，-34，-34，0\
   * 夜晚画风：-68，-68，0，68
   * @param zindex 层号
   * @param red =0,-255~255 红色偏移
   * @param green =0,-255~255 绿色偏移
   * @param blue =0,-255~255 蓝色偏移
   * @param gray =0,0~255 灰度
   * @param duration =60,持续多少帧
   * @param isWait =true,是否等待完成
   */
  tintPicture(
    zindex: number,
    red = 0,
    green = 0,
    blue = 0,
    gray = 0,
    duration = 60,
    isWait = true
  ): $mvs;
  /**
   * 擦除图片
   * @param zindex 层号
   */
  erasePicture(zindex: number): $mvs;
  /**
   * 设置天气
   * @param type ='none',天气类型：enumWeather
   * @param power =5,强度1~9
   * @param duration =60,持续多少帧
   * @param isWait =true,是否等待完成
   */
  setWeatherEffect(
    type = $mvs.enumWeather.None,
    power = 5,
    duration = 60,
    isWait = true
  ): $mvs;
  /**
   * 播放bgm
   * @param name ='',声音名
   * @param volume =90,0~100音量
   * @param pitch =100,50~150音调
   * @param pan =0,-100~100声相
   */
  playBGM(name = "", volume = 90, pitch = 100, pan = 0): $mvs;
  /**
   * 淡出bgm
   * @param duration 持续多少帧
   */
  fadeoutBGM(duration: number): $mvs;
  /**
   * 保存bgm
   */
  saveBGM(): $mvs;
  /**
   * 恢复bgm
   */
  resumeBGM(): $mvs;
  /**
   * 播放bgs
   * @param name ='',声音名
   * @param volume =90,0~100音量
   * @param pitch =100,50~150音调
   * @param pan =0,-100~100声相
   */
  playBGS(name = "", volume = 90, pitch = 100, pan = 0): $mvs;
  /**
   * 淡出bgs
   * @param duration 持续多少帧
   */
  fadeoutBGS(duration: number): $mvs;
  /**
   * 播放me
   * @param name ='',声音名
   * @param volume =90,0~100音量
   * @param pitch =100,50~150音调
   * @param pan =0,-100~100声相
   */
  playME(name = "", volume = 90, pitch = 100, pan = 0): $mvs;
  /**
   * 播放se
   * @param name ='',声音名
   * @param volume =90,0~100音量
   * @param pitch =100,50~150音调
   * @param pan =0,-100~100声相
   */
  playSE(name = "", volume = 90, pitch = 100, pan = 0): $mvs;
  /**
   * 停止se
   */
  stopSE(): $mvs;
  /**
   * 播放影片
   * @param name ='',影片名
   */
  playMovie(name = ""): $mvs;
  /**
   * 启用/禁用地图名显示
   * @param isMapNameDisplay =ON,是否显示地图名：enumSwitchValue
   */
  changeMapNameDisplay(isMapNameDisplay = $mvs.enumSwitchValue.ON): $mvs;
  /**
   * 更改地图图块集
   * @param tilesetId 图块集id
   */
  changeTileset(tilesetId: number): $mvs;
  /**
   * 设置战斗背景图
   * @param imageWall ='',墙图
   * @param imageGround ='',地图
   */
  changeBattleBackground(imageWall = "", imageGround = ""): $mvs;
  /**
   * 更改远景
   * @param imageName 图片名
   * @param repeatX =false,横向循环
   * @param repeatY =false,纵向循环
   * @param xVal =0,-32~32横向滚动值
   * @param yVal =0,-32~32纵向滚动值
   */
  changeParallax(
    imageName: string,
    repeatX = false,
    repeatY = false,
    xVal = 0,
    yVal = 0
  ): $mvs;
  /**
   * 战斗处理
   * @param troopId 敌群id
   * @param subCommandEscape 匿名函数类型，逃跑时调用。如需不可逃跑请赋值null
   * @param subCommandLoss 匿名函数类型，战败时调用。如需不可战败请赋值null
   * @param subCommandWin 匿名函数类型，成功时调用
   */
  battleProcessing(
    troopId: number,
    subCommandEscape = () => {},
    subCommandLoss = () => {},
    subCommandWin = () => {}
  ): $mvs;

  /**
   * 处理商店
   * @param isPurchaseOnly 是否仅能采购
   * @param getItems 回调函数，获取物品列表\
   * 例子：\
   * this.shopProcessing(false, itemList =>
   *  itemList
   *   .addItem(1)
   *   .addWeapon(3, 100)
   *   .done()
   * );
   */
  shopProcessing(isPurchaseOnly = false, getItems = () => [[0, 0, 0, 0]]): $mvs;
  /**
   * 起名界面
   * @param actorId 角色id
   * @param maxChars =8,最大字符数1~16
   */
  nameInputProcessing(actorId: number, maxChars = 8): $mvs;
  /**
   * 更改hp
   * @param actorId 角色id：0队列中的全部
   * @param amount 差值
   * @param causeDead =false,如果减少是否能引起死亡
   */
  changeHP(actorId: number, amount: number, causeDead = false): $mvs;
  /**
   * 更改mp
   * @param actorId 角色id：0队列中的全部
   * @param amount 差值
   */
  changeMP(actorId: number, amount: number): $mvs;
  /**
   * 更改tp
   * @param actorId 角色id：0队列中的全部
   * @param amount 差值
   */
  changeTP(actorId: number, amount: number): $mvs;
  /**
   * 更改角色状态
   * @param actorId 角色id：0队列中的全部
   * @param op 附加/移除：enumSwitchValue
   * @param stateId 状态id
   */
  changeState(actorId: number, op: $mvs.enumSwitchValue, stateId: number): $mvs;
  /**
   * 完全恢复
   * @param actorId 角色id：0队列中的全部
   */
  recoverAll(actorId: number): $mvs;
  /**
   * 更改exp
   * @param actorId 角色id：0队列中的全部
   * @param amount 差值
   * @param showLevelUp =false,如果升级是否显示升级信息
   */
  changeEXP(actorId: number, amount: number, showLevelUp = false): $mvs;
  /**
   * 更改等级
   * @param actorId 角色id：0队列中的全部
   * @param amount 差值
   * @param showLevelUp =false,如果升级是否显示升级信息
   */
  changeLevel(actorId: number, amount: number, showLevelUp = false): $mvs;
  /**
   * 更改角色能力
   * @param actorId 角色id：0队列中的全部
   * @param type 属性类型：enumAbility
   * @param amount 差值
   */
  changeAbility(actorId: number, type: $mvs.enumAbility, amount: number): $mvs;
  /**
   * 更改角色技能
   * @param actorId 角色id：0队列中的全部
   * @param op 附加/移除：enumSwitchValue
   * @param skillId 技能id
   */
  changeSkill(actorId: number, op: $mvs.enumSwitchValue, skillId: number): $mvs;
  /**
   * 更改装备
   * @param actorId 角色id
   * @param equipType 装备类别
   * @param equipId 装备id
   */
  changeEquipment(actorId: number, equipType: number, equipId: number): $mvs;
  /**
   * 更改名字
   * @param actorId 角色id
   * @param name 名字
   */
  changeName(actorId: number, name: string): $mvs;
  /**
   * 更改角色职业
   * @param actorId 角色id
   * @param classId 职业id
   * @param keepLevel =false,是否保持等级
   */
  changeClass(actorId: number, classId: number, keepLevel = false): $mvs;
  /**
   * 更改角色图片
   * @param actorId 角色id
   * @param characterImageName 行走图名
   * @param characterImageIndex 行走图index
   * @param faceImageName 头像图名
   * @param faceImageIndex 头像图index
   * @param battlerImageName 战斗图名
   */
  changeActorImages(
    actorId: number,
    characterImageName: string,
    characterImageIndex: number,
    faceImageName: string,
    faceImageIndex: number,
    battlerImageName: string
  ): $mvs;
  /**
   * 更改载具图
   * @param vehicleId 载具id
   * @param imageName 载具图片名
   * @param imageIndex 载具图index
   */
  changeVehicleImage(
    vehicleId: number,
    imageName: string,
    imageIndex: number
  ): $mvs;
  /**=
   * 更改角色昵称
   * @param actorId 角色id
   * @param nickName 昵称
   */
  changeClass(actorId: number, nickName): $mvs;
  /**
   * 更改角色简介
   * @param actorId 角色id
   * @param profile 简介
   */
  changeProfile(actorId: number, profile: string): $mvs;
  /**
   * 更改敌人hp
   * @param enemyIndex 敌人index：从0开始，-1整个敌群
   * @param amount 差值
   * @param causeDead =false,如果减少是否能引起死亡
   */
  changeEnemyHP(enemyIndex: number, amount: number, causeDead = false): $mvs;
  /**
   * 更改敌人mp
   * @param enemyIndex 敌人index：从0开始，-1整个敌群
   * @param amount 差值
   */
  changeEnemyMP(enemyIndex: number, amount: number): $mvs;
  /**
   * 更改敌人tp
   * @param enemyIndex 敌人index：从0开始，-1整个敌群
   * @param amount 差值
   */
  changeEnemyTP(enemyIndex: number, amount: number): $mvs;
  /**
   * 更改角色状态
   * @param enemyIndex 敌人index：从0开始，-1整个敌群
   * @param op 附加/移除：enumSwitchValue
   * @param stateId 状态id
   */
  changeEnemyState(
    enemyIndex: number,
    op: $mvs.enumSwitchValue,
    stateId: number
  ): $mvs;
  /**
   * 完全恢复
   * @param enemyIndex 敌人index：从0开始，-1整个敌群
   */
  enemyRecoverAll(enemyIndex: number): $mvs;
  /**
   * 敌人出现
   * @param enemyIndex 敌人index：从0开始
   */
  enemyAppear(enemyIndex: number): $mvs;
  /**
   * 敌人变身
   * @param enemyIndex 敌人index：从0开始
   * @param enemyId 敌人id(敌人种类)：从1开始
   */
  enemyTransform(enemyIndex: number, enemyId: number): $mvs;
  /**
   * 敌人显示动画
   * @param enemyIndex 敌人index：从0开始，-1整个敌群
   * @param animationId 动画id
   */
  showBattleAnimation(enemyIndex: number, animationId: number): $mvs;
  /**
   * 角色强制释放技能
   * @param actorId 角色id，
   * @param actionId 技能id
   * @param targetIndex 目标index，0开始，-1随机，-2上次选择
   */
  actorForceBattleAction(
    actorId: number,
    actionId: number,
    targetIndex: number
  ): $mvs;
  /**
   * 敌人强制释放技能
   * @param enemyIndex 敌人index，0开始
   * @param actionId 技能id
   * @param targetIndex 目标index，0开始，-1随机，-2上次选择
   */
  enemyForceBattleAction(
    enemyIndex: number,
    actionId: number,
    targetIndex: number
  ): $mvs;
  /**
   * 中断战斗
   */
  abortBattle(): $mvs;
  /**
   * 打开菜单画面
   */
  openMenuScreen(): $mvs;
  /**
   * 打开存档画面
   */
  openSaveScreen(): $mvs;
  /**
   * 游戏结束
   */
  gameOver(): $mvs;
  /**
   * 返回标题画面
   */
  returnTitleScreen(): $mvs;
  /**
   * 调用js函数
   * @param jsFunction js函数
   */
  callJsFunction(jsFunction: () => void): $mvs;
  /**
   * 调用插件命令
   * @param command 命令字符串参数
   */
  callPluginCommand(command: string): $mvs;
}
