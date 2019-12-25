(() => {
  const { Window_Base, setTimeout } = window;
  class Window_Text extends Window_Base {
    constructor() {
      super(...arguments);
    }
    initialize(text = '', x = 0, y = 0, width = null, rows = 1) {
      this._text = text;
      width = width || 140;
      var height = this.fittingHeight(rows) * 2;
      Window_Base.prototype.initialize.call(this, x, y, width, height);
      this.hide();
      this.refresh();
    }
    lineHeight() {
      return 18;
    }
    standardFontSize() {
      return 16;
    }
    standardPadding() {
      return 6;
    }
    setText(text) {
      this._text = text;
      setTimeout(() => {
        //this.close();
      }, 2500);
      this.show();
      this.open();
      this.refresh();
    }
    refresh() {
      this.contents.clear();
      this.drawTextEx(this._text, 0, 0);
      // const skin = this._windowskin;
      // this._windowFrameSprite.bitmap.blt(skin,0,0,50,50,30,30);
    }
  }
  window.Window_Text = Window_Text;
  //Scene_Map.prototype.createAllWindows
  const temp = Scene_Title.prototype.createCommandWindow;
  Scene_Title.prototype.createCommandWindow = function() {
    temp.call(this);
    const txt = '好新鲜的草莓啊！快来买哦！';
    const rtxt = [...txt]
      .slice(0, 18)
      .reduce((pv, cv) => pv + (pv.gblen() == 16 ? '\n' : '') + cv);
    this._textWindow = new Window_Text();
    this._textWindow.setText(rtxt);
    this.addWindow(this._textWindow);
    this._arrowSprite = new Sprite(new Bitmap(50, 50));
    const skin = this._textWindow._windowskin;
    this._arrowSprite.bitmap.blt(skin,0,0,50,50,0,0);
    this.addChild(this._arrowSprite)
  };
})();
