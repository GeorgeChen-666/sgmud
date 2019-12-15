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
  /**
   * 条件支持js代码
   */
  const _command111 = Game_Interpreter.prototype.command111;
  Game_Interpreter.prototype.command111 = function() {
    if(typeof this._params[0] === 'function') {
      const result = !!this._params[0]();
      this._branch[this._indent] = result;
      if (this._branch[this._indent] === false) {
          this.skipBranch();
      }
      return true;
    } else {
      _command111.call(this);
    }
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
   * @param basepath 路径
   */
  DataManager.loadMapData = function(mapId, isRefresh = false, nameTempl = 'Map%1.json', basepath = 'data/') {
    DataManager._mapCache = (DataManager._mapCache || {});
    if (mapId > 0) {
      var cachedMap = DataManager._mapCache[mapId];
      if(cachedMap && !isRefresh) {
        $dataMap = cachedMap;
      } else {
        var filename = nameTempl.format(mapId.padZero(3));
        this._mapLoader = ResourceHandler.createLoader(basepath + filename, this.loadDataFile.bind(this, '$dataMap', filename));
        this.loadDataFile('$dataMap', filename, basepath)
        .then(()=> {
          DataManager.setMapCache(mapId, $dataMap);
        });
      }
    } else {
        this.makeEmptyMap();
    }
  };
  DataManager.loadDataFile = function(name, src, basepath = 'data/') {
    return new Promise((resolve, reject) => {
      var xhr = new XMLHttpRequest();
      var url = basepath + src;
      xhr.open('GET', url);
      xhr.overrideMimeType('application/json');
      xhr.onload = function() {
        if (xhr.status < 400) {
          window[name] = JSON.parse(xhr.responseText);
          DataManager.onLoad(window[name]);
          resolve(window[name]);
        }
      };
      xhr.onerror = this._mapLoader || function() {
        DataManager._errorUrl = DataManager._errorUrl || url;
        reject(DataManager._errorUrl);
      };
      window[name] = null;
      xhr.send();
    });
  };
  DataManager.setMapCache = function (mapId, data) {
    DataManager._mapCache[mapId] = data;
  };
})();