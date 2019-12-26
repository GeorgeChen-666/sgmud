(() => {
  const maxCharCountPerLine = 16;
  String.prototype.gblen = function() {
    let len = 0;
    for (let i = 0; i < this.length; i++) {
      if (this.charCodeAt(i) > 127 || this.charCodeAt(i) === 94) {
        len += 2;
      } else {
        len++;
      }
    }
    return len;
  };
  class Window_TextBalloon extends Window_Base {
    constructor() {
      super(...arguments);
    }
    initialize() {
      this._text = '';
      Window_Base.prototype.initialize.call(this, 0, 0, 140, 60);
      this.move(0, 0, 0, 0);
      this.close();
      this._arrowSprite = new Sprite(new Bitmap(20, 10));
      this._arrowSprite.visible = false;
      const skin = this._windowskin;
      this._arrowSprite.bitmap.blt(skin, 135, 61, 20, 10, 0, 0);

      this.on('added', () => {
        this.parent.addChild(this._arrowSprite);
      });
    }
    lineHeight() {
      return 22;
    }
    standardFontSize() {
      return 16;
    }
    standardPadding() {
      return 6;
    }
    setText(text) {
      const rtxt = [...text]
        .slice(0, 18)
        .reduce(
          (pv, cv) => pv + (pv.gblen() == maxCharCountPerLine ? '\n' : '') + cv
        );
      this._text = rtxt;
      this.refresh();

      const { x, y } = this;
      const width =
        this.textWidth(rtxt.split('\n')[0]) + this.standardPadding() * 2;
      const height = this.fittingHeight(rtxt.includes('\n') ? 2 : 1);
      const offSetX = -24;
      const offSetY = -height - 52;
      this.move(offSetX, offSetY, width, height);
      this._arrowSprite.x = 0;
      this._arrowSprite.y = this.height - 1 + offSetY;
      setTimeout(() => {
        this._arrowSprite.visible = true;
      }, 200);

      this.open();
      setTimeout(() => {
        this.close();
        this._arrowSprite.visible = false;
        this._text = undefined;
        this.parent._character._balloonText = undefined;
      }, 3000);
    }
    refresh() {
      this.contents.clear();
      this.drawTextEx(this._text, 0, 0);
    }
  }

  PluginManager.regHook('Sprite_Character.prototype.initMembers', function() {
    this._textWindow = new Window_TextBalloon();
    this.addChild(this._textWindow);
  });

  PluginManager.regHook('Sprite_Character.prototype.setupBalloon', function() {
    if (
      this._character._balloonText !== undefined &&
      this._textWindow.isClosed()
    ) {
      this._textWindow.setText(this._character._balloonText);
    }
  });
  Game_Interpreter.prototype.setupTextBalloon = function(text) {
    this.character()._balloonText = text;
  };
})();
