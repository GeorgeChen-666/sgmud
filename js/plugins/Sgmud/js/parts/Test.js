(() => {
  /**
   * 进度条窗口
   */
  class Window_Progress extends Window_Base {
    constructor() {
      super()
      this.initialize.apply(this, arguments);
    }
    initialize() {
      var width = this.windowWidth();
      var height = this.windowHeight();
      var x = (Graphics.boxWidth - width) / 2;
      const y = height;
      Window_Base.prototype.initialize.call(this, 9999, y, width, height);
      this.close();
      //this.refresh();
    }
    open() {
      this.move(x, y, width, height);
      super.open();
    }
    windowWidth() {
      return 400;
    }
    windowHeight() {
      return this.fittingHeight(1);
    }
    refresh() {
      var x = this.textPadding();
      var width = this.contents.width;
      this.contents.clear();
      var max = $gameMessage.progressMax;
      var val = $gameMessage.progressVal;
      const title = $gameMessage.progressTitle;
      this.drawGaugeBar(title, val, max, 0, 0, width, true, true, this.tpGaugeColor1(), this.tpGaugeColor2());
    }
  }
  PluginManager.regHook(
    'Game_Message.prototype.clear',
    oFunc =>
      function() {
        oFunc();
        this.progressMax = 10;
        this.progressVal = 0;
        this.progressTitle = '';
      }
  );
  Game_Message.prototype.isProgress = function() {
    return this.progressTitle !== '';
  };
  PluginManager.regHook(
    'Window_Message.prototype.startInput',
    oFunc =>
      function() {
        if(this.isProgress()) {
          this._mapProgressWindow.open();
          return true;
        } else {
          oFunc();
        }
      }
  );
  PluginManager.regHook(
    'Window_Message.prototype.createSubWindows',
    oFunc =>
      function() {
        oFunc();
        this._mapProgressWindow = new Window_Progress(this);
      }
  );
  PluginManager.regHook(
    'Window_Message.prototype.subWindows',
    oFunc =>
      function() {
        return [...oFunc(),this._mapProgressWindow];
      }
  );
  PluginManager.regHook(
    'Window_Message.prototype.isAnySubWindowActive',
    oFunc =>
      function() {
        return oFunc() || this._mapProgressWindow.active;
      }
  );
})();
