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

  class $mvs {
    constructor(interpreter, indent = interpreter._indent) {
      this.interpreter = interpreter;
      this.indent = indent;
      this.commands = [];
    }
    done() {
      this.interpreter.insertCommands(this.commands);
      this.commands = [];
    }

    calculateAndCall(mvCommand) {
      mvCommand(this);
      return this;
    }

    showText(
      textData,
      faceName = "",
      faceIndex = 0,
      background = $mvs.enumBackground.Window,
      positionType = $mvs.enumPositionV.Botton
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
    showChoices(
      choiceData,
      cancelValue = -2,
      defaultValue = 0,
      positionType = $mvs.enumPositionH.Right,
      background = $mvs.enumBackground.Window
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
    whenChoicesCancel(subCommand) {
      this.commands.push({ code: 403, indent: this.indent });
      this.indent++;
      subCommand(this);
      this.commands.push({ code: 0, indent: this.indent });
      this.indent--;
      return this;
    }
    endWhen() {
      this.commands.push({ code: 404, indent: this.indent });
      //this.commands.push({ code: 412, indent: this.indent });
      return this;
    }
    inputNumber(variableId, maxDigits = 1) {
      this.commands.push({
        code: 103,
        indent: this.indent,
        parameters: [variableId, maxDigits]
      });
      return this;
    }
    selectItem(variableId, itemType = $mvs.enumItemType.KeyItem) {
      this.commands.push({
        code: 104,
        indent: this.indent,
        parameters: [variableId, itemType]
      });
      return this;
    }
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
    loopBreak() {
      this.commands.push({
        code: 113,
        indent: this.indent
      });
      return this;
    }
    exitCommand() {
      this.commands.push({
        code: 115,
        indent: this.indent
      });
      return this;
    }
    callCommonEvent(eventId) {
      this.commands.push({
        code: 117,
        indent: this.indent,
        parameters: [eventId]
      });
      return this;
    }
    defineLabel(labelName) {
      this.commands.push({
        code: 118,
        indent: this.indent,
        parameters: [labelName]
      });
      return this;
    }
    gotoLabel(labelName) {
      this.commands.push({
        code: 119,
        indent: this.indent,
        parameters: [labelName]
      });
      return this;
    }
    setSwitchAsValue(srcValue, switchIdStart, switchIdEnd = switchIdStart) {
      this.commands.push({
        code: 121,
        indent: this.indent,
        parameters: [switchIdStart, switchIdEnd, srcValue]
      });
      return this;
    }
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
    setSelfSwitchAsValue(srcValue, selfSwitchId) {
      this.commands.push({
        code: 123,
        indent: this.indent,
        parameters: [selfSwitchId, srcValue]
      });
      return this;
    }
    startTimer(count) {
      this.commands.push({
        code: 124,
        indent: this.indent,
        parameters: [0, count]
      });
      return this;
    }
    stopTimer() {
      this.commands.push({
        code: 124,
        indent: this.indent,
        parameters: [1]
      });
      return this;
    }
    changeGold(amount) {
      this.commands.push({
        code: 125,
        indent: this.indent,
        parameters: [0, 0, amount]
      });
      return this;
    }
    changeItem(itemId, amount = 1) {
      this.commands.push({
        code: 126,
        indent: this.indent,
        parameters: [itemId, 0, 0, amount]
      });
      return this;
    }
    changeWeapons(weaponId, amount = 1, includeEquip = false) {
      this.commands.push({
        code: 127,
        indent: this.indent,
        parameters: [weaponId, 0, 0, amount, includeEquip]
      });
      return this;
    }
    changeArmors(armorId, amount = 1, includeEquip = false) {
      this.commands.push({
        code: 128,
        indent: this.indent,
        parameters: [armorId, 0, 0, amount, includeEquip]
      });
      return this;
    }
    addPartyMember(actorId, isInitialize = false) {
      this.commands.push({
        code: 129,
        indent: this.indent,
        parameters: [actorId, 0, isInitialize]
      });
      return this;
    }
    removePartyMember(actorId) {
      this.commands.push({
        code: 129,
        indent: this.indent,
        parameters: [actorId, 1]
      });
      return this;
    }
    changeBattleBGM(name = "", volume = 90, pitch = 100, pan = 0) {
      this.commands.push({
        code: 132,
        indent: this.indent,
        parameters: [{ name, volume, pitch, pan }]
      });
      return this;
    }
    changeVictoryME(name = "", volume = 90, pitch = 100, pan = 0) {
      this.commands.push({
        code: 133,
        indent: this.indent,
        parameters: [{ name, volume, pitch, pan }]
      });
      return this;
    }
    changeSaveAccess(isAble) {
      this.commands.push({
        code: 134,
        indent: this.indent,
        parameters: [isAble]
      });
      return this;
    }
    changeMenuAccess(isAble) {
      this.commands.push({
        code: 135,
        indent: this.indent,
        parameters: [isAble]
      });
      return this;
    }
    changeEncounterDisable(isAble) {
      this.commands.push({
        code: 136,
        indent: this.indent,
        parameters: [isAble]
      });
      return this;
    }
    changeFormationAccess(isAble) {
      this.commands.push({
        code: 137,
        indent: this.indent,
        parameters: [isAble]
      });
      return this;
    }
    changeWindowColor(red = 0, green = 0, blue = 0) {
      this.commands.push({
        code: 138,
        indent: this.indent,
        parameters: [[red, green, blue, 0]]
      });
      return this;
    }
    changeDefeatME(name = "", volume = 90, pitch = 100, pan = 0) {
      this.commands.push({
        code: 139,
        indent: this.indent,
        parameters: [{ name, volume, pitch, pan }]
      });
      return this;
    }
    changeVehicleBGM(
      vehicleType,
      name = "",
      volume = 90,
      pitch = 100,
      pan = 0
    ) {
      this.commands.push({
        code: 140,
        indent: this.indent,
        parameters: [vehicleType, { name, volume, pitch, pan }]
      });
      return this;
    }
    transferPlayerDirect(
      mapId,
      x,
      y,
      d = $mvs.enumDirection.Retain,
      fadeType = $mvs.enumFadeType.Black
    ) {
      this.commands.push({
        code: 201,
        indent: this.indent,
        parameters: [0, mapId, x, y, d, fadeType]
      });
      return this;
    }
    setVehicleLocationDirect(vehicleId, mapId, x, y) {
      this.commands.push({
        code: 202,
        indent: this.indent,
        parameters: [vehicleId, 0, mapId, x, y]
      });
      return this;
    }
    setEventLocationDirect(eventId, x, y, d = $mvs.enumDirection.Retain) {
      this.commands.push({
        code: 203,
        indent: this.indent,
        parameters: [eventId, 0, x, y, d]
      });
      return this;
    }
    exchangeEventLocationWithAnother(
      eventId,
      eventId1,
      d = $mvs.enumDirection.Retain
    ) {
      this.commands.push({
        code: 203,
        indent: this.indent,
        parameters: [eventId, 2, eventId1, , d]
      });
      return this;
    }
    scrollMap(direction, distance, speed) {
      this.commands.push({
        code: 204,
        indent: this.indent,
        parameters: [direction, distance, speed]
      });
      return this;
    }
    setMovementRoute(
      eventId,
      routeCommandFunc = command => ({
        list: [],
        repeat: false,
        skippable: false,
        wait: false
      })
    ) {
      const routeParameters = routeCommandFunc(new $mvs.RouteCommandGenerator());
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
    gettingOnOffVehicles() {
      this.commands.push({
        code: 206,
        indent: this.indent,
        parameters: []
      });
      return this;
    }
    changeTransparency(isTransparent) {
      this.commands.push({
        code: 211,
        indent: this.indent,
        parameters: [isTransparent]
      });
      return this;
    }
    showAnimation(eventId, animationId, wait) {
      this.commands.push({
        code: 212,
        indent: this.indent,
        parameters: [eventId, animationId, wait]
      });
      return this;
    }
    showBalloonIcon(eventId, balloonId, wait) {
      this.commands.push({
        code: 213,
        indent: this.indent,
        parameters: [eventId, balloonId, wait]
      });
      return this;
    }
    eraseEvent() {
      this.commands.push({
        code: 214,
        indent: this.indent,
        parameters: []
      });
      return this;
    }
    changePlayerFollowers(isShow = 0) {
      this.commands.push({
        code: 216,
        indent: this.indent,
        parameters: [isShow]
      });
      return this;
    }
    gatherFollowers() {
      this.commands.push({
        code: 217,
        indent: this.indent,
        parameters: []
      });
      return this;
    }
    fadeoutScreen() {
      this.commands.push({
        code: 221,
        indent: this.indent,
        parameters: []
      });
      return this;
    }
    fadeinScreen() {
      this.commands.push({
        code: 222,
        indent: this.indent,
        parameters: []
      });
      return this;
    }
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
    shakeScreen(power = 5, speed = 5, duration = 60, isWait = true) {
      this.commands.push({
        code: 225,
        indent: this.indent,
        parameters: [power, speed, duration, isWait]
      });
      return this;
    }
    wait(duration) {
      this.commands.push({
        code: 230,
        indent: this.indent,
        parameters: [duration]
      });
      return this;
    }
    showPicture(
      zindex,
      picture,
      origin,
      x,
      y,
      wScale = 100,
      hScale = 100,
      Opacity = 255,
      blendMode = $mvs.enumBlendMode.Normal
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
    movePicture(
      zindex,
      origin,
      x,
      y,
      wScale = 100,
      hScale = 100,
      Opacity = 255,
      blendMode = $mvs.enumBlendMode.Normal,
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
    rotatePicture(zindex, angle) {
      this.commands.push({
        code: 233,
        indent: this.indent,
        parameters: [zindex, angle]
      });
      return this;
    }
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
    erasePicture(zindex) {
      this.commands.push({
        code: 235,
        indent: this.indent,
        parameters: [zindex]
      });
      return this;
    }
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
    playBGM(name = "", volume = 90, pitch = 100, pan = 0) {
      this.commands.push({
        code: 241,
        indent: this.indent,
        parameters: [{ name, volume, pitch, pan }]
      });
      return this;
    }
    fadeoutBGM(duration) {
      this.commands.push({
        code: 242,
        indent: this.indent,
        parameters: [duration]
      });
      return this;
    }
    saveBGM() {
      this.commands.push({
        code: 243,
        indent: this.indent
      });
      return this;
    }
    resumeBGM() {
      this.commands.push({
        code: 244,
        indent: this.indent
      });
      return this;
    }
    playBGS(name = "", volume = 90, pitch = 100, pan = 0) {
      this.commands.push({
        code: 245,
        indent: this.indent,
        parameters: [{ name, volume, pitch, pan }]
      });
      return this;
    }
    fadeoutBGS(duration) {
      this.commands.push({
        code: 246,
        indent: this.indent,
        parameters: [duration]
      });
      return this;
    }
    playME(name = "", volume = 90, pitch = 100, pan = 0) {
      this.commands.push({
        code: 249,
        indent: this.indent,
        parameters: [{ name, volume, pitch, pan }]
      });
      return this;
    }
    playSE(name = "", volume = 90, pitch = 100, pan = 0) {
      this.commands.push({
        code: 250,
        indent: this.indent,
        parameters: [{ name, volume, pitch, pan }]
      });
      return this;
    }
    stopSE() {
      this.commands.push({
        code: 251,
        indent: this.indent
      });
      return this;
    }
    playMovie(name = "") {
      this.commands.push({
        code: 261,
        indent: this.indent,
        parameters: [name]
      });
      return this;
    }
    changeMapNameDisplay(isMapNameDisplay = $mvs.enumSwitchValue.ON) {
      this.commands.push({
        code: 281,
        indent: this.indent,
        parameters: [isMapNameDisplay]
      });
      return this;
    }
    changeTileset(tilesetId) {
      this.commands.push({
        code: 282,
        indent: this.indent,
        parameters: [tilesetId]
      });
      return this;
    }
    changeBattleBackground(imageWall = "", imageGround = "") {
      this.commands.push({
        code: 283,
        indent: this.indent,
        parameters: [imageWall, imageGround]
      });
      return this;
    }
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
    shopProcessing(isPurchaseOnly = false, getItems = () => [[0, 0, 0, 0]]) {
      const [firstItem, ...restItems] = getItems(new $mvs.ItemListGenerator());
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
    nameInputProcessing(actorId, maxChars = 8) {
      this.commands.push({
        code: 303,
        indent: this.indent,
        parameters: [actorId, maxChars]
      });
      return this;
    }
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
    changeMP(actorId, amount) {
      this.commands.push({
        code: 312,
        indent: this.indent,
        parameters: [0, actorId, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
      });
      return this;
    }
    changeTP(actorId, amount) {
      this.commands.push({
        code: 326,
        indent: this.indent,
        parameters: [0, actorId, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
      });
      return this;
    }
    changeState(actorId, op, stateId) {
      this.commands.push({
        code: 313,
        indent: this.indent,
        parameters: [0, actorId, op, stateId]
      });
      return this;
    }
    recoverAll(actorId) {
      this.commands.push({
        code: 314,
        indent: this.indent,
        parameters: [0, actorId]
      });
      return this;
    }
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
    changeAbility(actorId, type, amount) {
      this.commands.push({
        code: 317,
        indent: this.indent,
        parameters: [0, actorId, type, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
      });
      return this;
    }
    changeSkill(actorId, op, skillId) {
      this.commands.push({
        code: 318,
        indent: this.indent,
        parameters: [0, actorId, op, skillId]
      });
      return this;
    }
    changeEquipment(actorId, equipType, equipId) {
      this.commands.push({
        code: 319,
        indent: this.indent,
        parameters: [actorId, equipType, equipId]
      });
      return this;
    }
    changeName(actorId, name) {
      this.commands.push({
        code: 320,
        indent: this.indent,
        parameters: [actorId, name]
      });
      return this;
    }
    changeClass(actorId, classId, keepLevel = false) {
      this.commands.push({
        code: 321,
        indent: this.indent,
        parameters: [actorId, classId, keepLevel]
      });
      return this;
    }
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
    changeVehicleImage(vehicleId, imageName, imageIndex) {
      this.commands.push({
        code: 323,
        indent: this.indent,
        parameters: [vehicleId, imageName, imageIndex]
      });
      return this;
    }
    changeClass(actorId, nickName) {
      this.commands.push({
        code: 324,
        indent: this.indent,
        parameters: [actorId, nickName]
      });
      return this;
    }
    changeProfile(actorId, profile) {
      this.commands.push({
        code: 325,
        indent: this.indent,
        parameters: [actorId, profile]
      });
      return this;
    }
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
    changeEnemyMP(enemyIndex, amount) {
      this.commands.push({
        code: 332,
        indent: this.indent,
        parameters: [enemyIndex, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
      });
      return this;
    }
    changeEnemyTP(enemyIndex, amount) {
      this.commands.push({
        code: 342,
        indent: this.indent,
        parameters: [enemyIndex, amount >= 0 ? 0 : 1, 0, Math.abs(amount)]
      });
      return this;
    }
    changeEnemyState(enemyIndex, op, stateId) {
      this.commands.push({
        code: 333,
        indent: this.indent,
        parameters: [enemyIndex, op, stateId]
      });
      return this;
    }
    enemyRecoverAll(enemyIndex) {
      this.commands.push({
        code: 334,
        indent: this.indent,
        parameters: [enemyIndex]
      });
      return this;
    }
    enemyAppear(enemyIndex) {
      this.commands.push({
        code: 335,
        indent: this.indent,
        parameters: [enemyIndex]
      });
      return this;
    }
    enemyTransform(enemyIndex, enemyId) {
      this.commands.push({
        code: 336,
        indent: this.indent,
        parameters: [enemyIndex, enemyId]
      });
      return this;
    }
    showBattleAnimation(enemyIndex, animationId) {
      this.commands.push({
        code: 337,
        indent: this.indent,
        parameters: [enemyIndex, animationId, enemyIndex === -1]
      });
      return this;
    }
    actorForceBattleAction(actorId, actionId, targetIndex) {
      this.commands.push({
        code: 339,
        indent: this.indent,
        parameters: [1, actorId, actionId, targetIndex]
      });
      return this;
    }
    enemyForceBattleAction(enemyIndex, actionId, targetIndex) {
      this.commands.push({
        code: 339,
        indent: this.indent,
        parameters: [1, enemyIndex, actionId, targetIndex]
      });
      return this;
    }
    abortBattle() {
      this.commands.push({ code: 340, indent: this.indent });
      return this;
    }
    openMenuScreen() {
      this.commands.push({ code: 351, indent: this.indent });
      return this;
    }
    openSaveScreen() {
      this.commands.push({ code: 352, indent: this.indent });
      return this;
    }
    gameOver() {
      this.commands.push({ code: 353, indent: this.indent });
      return this;
    }
    returnTitleScreen() {
      this.commands.push({ code: 354, indent: this.indent });
      return this;
    }
    callJsFunction(jsFunction) {
      this.commands.push({
        code: 355,
        indent: this.indent,
        parameters: [jsFunction]
      });
      return this;
    }
    callPluginCommand(command) {
      this.commands.push({
        code: 356,
        indent: this.indent,
        parameters: [command]
      });
      return this;
    }

  }
  $mvs.ItemListGenerator = class {
    constructor() {
      this.itemList = [];
    }
    addItem(id, price = null) {
      itemList.push(0, id, price === null ? 0 : 1, price);
    }
    addWeapon(id, price = null) {
      itemList.push(1, id, price === null ? 0 : 1, price);
    }
    addArmor(id, price = null) {
      itemList.push(2, id, price === null ? 0 : 1, price);
    }
    done() {
      return itemList;
    }
  };
  $mvs.RouteCommandGenerator = class {
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
      this.list.push({
        code: 44,
        parameters: [{ name, volume, pitch, pan }]
      });
      return this;
    }
    callJsFunction(jsFunction) {
      this.list.push({ code: 45, parameters: [jsFunction] });
      return this;
    }
  };
  $mvs.enumBackground = {
    Window: 0,
    Dim: 1,
    Transparent: 2
  };
  $mvs.enumPositionV = {
    Top: 0,
    Middle: 1,
    Botton: 2
  };
  $mvs.enumPositionH = {
    Left: 0,
    Middle: 1,
    Right: 2
  };
  $mvs.enumItemType = {
    RegularItem: 1,
    KeyItem: 2,
    HiddenItemA: 3,
    HiddenItemB: 4
  };
  $mvs.enumSwitchValue = {
    ON: 0,
    OFF: 1
  };
  $mvs.enumSelfSwitchName = {
    A: "A",
    B: "B",
    C: "C",
    D: "D"
  };
  $mvs.enumVehicleType = {
    Boat: 0,
    Ship: 1,
    Airship: 2
  };
  $mvs.enumDirection = {
    Retain: 0,
    Down: 1,
    Left: 2,
    Right: 3,
    Top: 4
  };
  $mvs.enumFadeType = {
    Black: 0,
    White: 1,
    None: 2
  };
  $mvs.enumSpeed = {
    x8Slower: 1,
    x4Slower: 2,
    x2Slower: 3,
    Normal: 4,
    x2Faster: 5,
    x4Faster: 6
  };
  $mvs.enumBlendMode = {
    Normal: 0,
    Additive: 1,
    Multiply: 2,
    Screen: 3
  };
  $mvs.enumWeather = {
    None: "none",
    Rain: "rain",
    Storm: "storm",
    Snow: "snow"
  };
  $mvs.enumAbility = {
    MaxHP: 0,
    MaxMP: 1,
    Attack: 2,
    Defense: 3,
    MAttack: 4,
    MDefense: 5,
    Agility: 6,
    Luck: 7
  };
  window.$mvs = $mvs;
})();
