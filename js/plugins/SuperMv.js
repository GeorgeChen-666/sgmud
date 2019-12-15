(function () {
  PluginManager.loadScript = function(name, path='', basepath = this._path) {
    var url = basepath + path + name;
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;
    script.async = false;
    script.onerror = this.onError.bind(this);
    script._url = url;
    document.body.appendChild(script);
  };
  // 加载ES6模块
  PluginManager.loadModuleScript = function(name) {
    var url = this._path + name;
    var script = document.createElement('script');
    script.type = 'module';
    script.src = url;
    script.async = false;
    script.onerror = this.onError.bind(this);
    script._url = url;
    document.body.appendChild(script);
  };
  /**
   * 扩展解释器，在解析器当前执行的命令后面插入命令
   */
  Game_Interpreter.prototype.insertCommands = function(jsonCmds) {
    // this._list = (this._list || []);
    if(!this._list) {// 没有此处判断仅能在地图事件中调用insertCommands插入命令，有此判断后可以随时随地插入命令。
      this.clear();
      this.setup(jsonCmds);
    } else {
      var listCmds = this._list;// 命令列表
      var listCount = listCmds.length;// 这个事件队列中一共多少条命令
      var cuCmdIndex = this._index;// 当前执行的是第几条命令
      var cuCmdIndent = this._indent;// 当前代码的缩进级别
      jsonCmds.forEach(function(e) {// 将参数命令的indent拼接到当前的indent级别下，参数的indent一定要从0开始。
        e.indent += cuCmdIndent;
      });
      this._list = listCmds.slice(0,cuCmdIndex + 1)// 在当前执行的事件后面插入
        .concat(jsonCmds)
        .concat(listCmds.slice(cuCmdIndex + 1,listCount));
    }
  };


  Game_Interpreter.prototype.command111 = function() {
    var result = false;
    switch (this._params[0]) {
      case 0:  // Switch
        result = ($gameSwitches.value(this._params[1]) === (this._params[2] === 0));
        break;
      case 1:  // Variable
        var value1 = $gameVariables.value(this._params[1]);
        var value2;
        if (this._params[2] === 0) {
          value2 = this._params[3];
        } else {
          value2 = $gameVariables.value(this._params[3]);
        }
        switch (this._params[4]) {
          case 0:  // Equal to
            result = (value1 === value2);
            break;
          case 1:  // Greater than or Equal to
            result = (value1 >= value2);
            break;
          case 2:  // Less than or Equal to
            result = (value1 <= value2);
            break;
          case 3:  // Greater than
            result = (value1 > value2);
            break;
          case 4:  // Less than
            result = (value1 < value2);
            break;
          case 5:  // Not Equal to
            result = (value1 !== value2);
            break;
        }
        break;
      case 2:  // Self Switch
        if (this._eventId > 0) {
          var key = [this._mapId, this._eventId, this._params[1]];
          result = ($gameSelfSwitches.value(key) === (this._params[2] === 0));
        }
        break;
      case 3:  // Timer
        if ($gameTimer.isWorking()) {
          if (this._params[2] === 0) {
            result = ($gameTimer.seconds() >= this._params[1]);
          } else {
            result = ($gameTimer.seconds() <= this._params[1]);
          }
        }
        break;
      case 4:  // Actor
        var actor = $gameActors.actor(this._params[1]);
        if (actor) {
          var n = this._params[3];
          switch (this._params[2]) {
            case 0:  // In the Party
              result = $gameParty.members().contains(actor);
              break;
            case 1:  // Name
              result = (actor.name() === n);
              break;
            case 2:  // Class
              result = actor.isClass($dataClasses[n]);
              break;
            case 3:  // Skill
              result = actor.hasSkill(n);
              break;
            case 4:  // Weapon
              result = actor.hasWeapon($dataWeapons[n]);
              break;
            case 5:  // Armor
              result = actor.hasArmor($dataArmors[n]);
              break;
            case 6:  // State
              result = actor.isStateAffected(n);
              break;
          }
        }
        break;
      case 5:  // Enemy
        var enemy = $gameTroop.members()[this._params[1]];
        if (enemy) {
          switch (this._params[2]) {
            case 0:  // Appeared
              result = enemy.isAlive();
              break;
            case 1:  // State
              result = enemy.isStateAffected(this._params[3]);
              break;
          }
        }
        break;
      case 6:  // Character
        var character = this.character(this._params[1]);
        if (character) {
          result = (character.direction() === this._params[2]);
        }
        break;
      case 7:  // Gold
        switch (this._params[2]) {
          case 0:  // Greater than or equal to
            result = ($gameParty.gold() >= this._params[1]);
            break;
          case 1:  // Less than or equal to
            result = ($gameParty.gold() <= this._params[1]);
            break;
          case 2:  // Less than
            result = ($gameParty.gold() < this._params[1]);
            break;
        }
        break;
      case 8:  // Item
        result = $gameParty.hasItem($dataItems[this._params[1]]);
        break;
      case 9:  // Weapon
        result = $gameParty.hasItem($dataWeapons[this._params[1]], this._params[2]);
        break;
      case 10:  // Armor
        result = $gameParty.hasItem($dataArmors[this._params[1]], this._params[2]);
        break;
      case 11:  // Button
        result = Input.isPressed(this._params[1]);
        break;
      case 12:  // Script
        if(typeof this._params[1] === 'function')
        {
          result = !!this._params[1]();
        } else {
          result = !!eval(this._params[1]);
        }
        break;
      case 13:  // Vehicle
        result = ($gamePlayer.vehicle() === $gameMap.vehicle(this._params[1]));
        break;
    }
    this._branch[this._indent] = result;
    if (this._branch[this._indent] === false) {
      this.skipBranch();
    }
    return true;
  };
  Game_Interpreter.prototype.command355 = function() {
    if(typeof this._params[0] === 'function')
    {
      this._params[0]();
      return true;
    } else {
      var script = this.currentCommand().parameters[0] + '\n';
      while (this.nextEventCode() === 655) {
        this._index++;
        script += this.currentCommand().parameters[0] + '\n';
      }
      eval(script);
      return true;
    }
  };
  ImageManager.loadBase64Bitmap = function(path, data, hue) {
    var key = this._generateCacheKey(path, hue);
    var bitmap = this._imageCache.get(key);
    bitmap = undefined;
    if (!bitmap) {
      bitmap = Object.create(Bitmap.prototype);
      bitmap._defer = true;
      bitmap.initialize();
      bitmap._decodeAfterRequest = true;
      bitmap._image = new Image();
      bitmap._image.src = data;
      bitmap.addLoadListener(function() {
        bitmap.rotateHue(hue);
      });
      this._imageCache.add(key, bitmap);
    }else if(!bitmap.isReady()){
      bitmap.decode();
    }
    return bitmap;
  };

  /**
   * 更强大的地图加载器，支持缓存。
   * @param mapId 地图ID
   * @param isRefresh 是否刷新缓存
   * @param nameTempl 地图文件名模版（支持通过../读取其它文件夹下的地图数据，base：data/）
   */
  DataManager.loadMapData = function(mapId, isRefresh, nameTempl) {
    isRefresh = isRefresh || false;
    nameTempl = nameTempl || 'Map%1.json';
    DataManager._mapCache = (DataManager._mapCache || {});
    if (mapId > 0) {
      var cachedMap = DataManager._mapCache[mapId];
      if(cachedMap && !isRefresh) {
        $dataMap = cachedMap;
      } else {
        var filename = nameTempl.format(mapId.padZero(3));
        var _loadDataFile = function (name, src) {
          var xhr = new XMLHttpRequest();
          var url = 'data/' + src;
          xhr.open('GET', url);
          xhr.overrideMimeType('application/json');
          xhr.onload = function() {
            if (xhr.status < 400) {
              window[name] = JSON.parse(xhr.responseText);
              DataManager.onLoad(window[name]);
              DataManager.setMapCache(mapId, $dataMap);
            }
          };
          xhr.onerror = this._mapLoader || function() {
            DataManager._errorUrl = DataManager._errorUrl || url;
          };
          window[name] = null;
          xhr.send();
        };
        this._mapLoader = ResourceHandler.createLoader('data/' + filename, _loadDataFile.bind(this, '$dataMap', filename));
        _loadDataFile.call(this, '$dataMap', filename);
      }
    } else {
      this.makeEmptyMap();
    }
  };
  DataManager.setMapCache = function (mapId, data) {
    DataManager._mapCache[mapId] = data;
  };

})();