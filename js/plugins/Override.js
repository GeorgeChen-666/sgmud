/**
 * @requires Hooks.js
 */
(function() {
  /**
   * 默认场景的背景是模糊
   */
  Scene_Base.prototype.createBackground = function() {
    this._backgroundSprite = new Sprite();
    this._backgroundSprite.bitmap = SceneManager.backgroundBitmap();
    this.addChild(this._backgroundSprite);
  };
  /**
   * 简易的窗体贴图功能，不用写乱糟糟的等资源代码。
   */
  Window_Base.prototype.reserveImageTodo = function(image, callBack) {
    const unReg = PluginManager.regHook(
      "Window_Base.prototype.update",
      oFunc =>
        function() {
          oFunc();
          if (image.isReady()) {
            callBack();
            unReg();
          }
        }
    );
  };
  Window_Base.prototype.drawHorzLine = function(y) {
    const line_y = y + this.lineHeight() / 2 - 1;
    this.contents.fillRect(
      0,
      line_y,
      this.contentsWidth(),
      2,
      this.normalColor()
    );
  };
  Window_Base.prototype.drawGaugeBar = function(
    title,
    min,
    max,
    x,
    y,
    width,
    hasGauge = false,
    hasText = true,
    color1,
    color2
  ) {
    width = width || 186;
    if (hasGauge) {
      this.drawGauge(x, y, width, max == 0 ? 0 : min / max, color1, color2);
    }
    this.changeTextColor(this.systemColor());
    this.drawText(title, x, y, 56);
    if (hasText) {
      this.drawCurrentAndMax(
        min,
        max,
        x,
        y,
        width,
        this.normalColor(),
        this.normalColor()
      );
    }
  };
  /**
   * 直接加载base64资源
   */
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
    } else if (!bitmap.isReady()) {
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
  DataManager.loadMapData = function(
    mapId,
    isRefresh = false,
    nameTempl = "Map%1.json",
    basepath = "data/"
  ) {
    DataManager._mapCache = DataManager._mapCache || {};
    if (mapId > 0) {
      var cachedMap = DataManager._mapCache[mapId];
      if (cachedMap && !isRefresh) {
        $dataMap = cachedMap;
      } else {
        var filename = nameTempl.format(mapId.padZero(3));
        this._mapLoader = ResourceHandler.createLoader(
          basepath + filename,
          this.loadDataFile.bind(this, "$dataMap", filename)
        );
        this.loadDataFile("$dataMap", filename, basepath).then(() => {
          DataManager.setMapCache(mapId, $dataMap);
        });
      }
    } else {
      this.makeEmptyMap();
    }
  };
  DataManager.loadDataFile = function(name, src, basepath = "data/") {
    return new Promise((resolve, reject) => {
      var xhr = new XMLHttpRequest();
      var url = basepath + src;
      xhr.open("GET", url);
      xhr.overrideMimeType("application/json");
      xhr.onload = function() {
        if (xhr.status < 400) {
          window[name] = JSON.parse(xhr.responseText);
          DataManager.onLoad(window[name]);
          resolve(window[name]);
        }
      };
      xhr.onerror =
        this._mapLoader ||
        function() {
          DataManager._errorUrl = DataManager._errorUrl || url;
          reject(DataManager._errorUrl);
        };
      window[name] = null;
      xhr.send();
    });
  };
  DataManager.setMapCache = function(mapId, data) {
    DataManager._mapCache[mapId] = data;
  };
})();
