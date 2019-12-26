  
(() => {
  const maxCharCountPerLine = 16;
  String.prototype.gblen = function() {
    let len = 0;
    for (let i=0; i<this.length; i++) {
      if (this.charCodeAt(i)>127 || this.charCodeAt(i)===94) {
        len += 2;
      } else {
        len ++;
      }
    }
    return len;
  };
  class Window_Text extends Window_Base {
    constructor() {
      super(...arguments);
    }
    initialize(text = '', x = 0, y = 0) {
      this._text = text;
      Window_Base.prototype.initialize.call(this, x, y,140,60);
      this.move(0,0,0,0);
      this._arrowSprite = new Sprite(new Bitmap(20, 10));
      this._arrowSprite.visible = false;
      const skin = this._windowskin;
      this._arrowSprite.bitmap.blt(skin,135,61,20,10,0,0);

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
        .reduce((pv, cv) => pv + (pv.gblen() == maxCharCountPerLine ? '\n' : '') + cv);
      this._text = rtxt;
      this.refresh();

      const {x,y} = this;
      const width = this.textWidth(rtxt.split('\n')[0]) + this.standardPadding() * 2;
      const height = this.fittingHeight(rtxt.includes('\n')?2:1);
      this.move(x,y,width,height);
      this._arrowSprite.visible = true;
      this._arrowSprite.x = this.width / 4;
      this._arrowSprite.y = this.height - 1;

      this.open();
      console.log('开始'+new Date());
      setTimeout(() => {
        this.close();
        this._arrowSprite.visible = false;
        this._text = undefined;
        this.parent._character._balloonText = undefined;
        console.log('结束'+new Date());
      }, 2500);
    }
    refresh() {
      this.contents.clear();
      this.drawTextEx(this._text, 0, 0);
    }
  }
  window.Window_Text = Window_Text;
  //Scene_Map.prototype.createAllWindows
  // const Scene_Title_createCommandWindow = Scene_Title.prototype.createCommandWindow;
  // Scene_Title.prototype.createCommandWindow = function() {
  //   Scene_Title_createCommandWindow.call(this);
  //   this._textWindow = new Window_Text();
  //   this.addWindow(this._textWindow);
  //   this._textWindow.setText('你好啊你好啊你好啊你好啊你好啊你好啊你好啊');
  // };
  const Sprite_Character_initMembers = Sprite_Character.prototype.initMembers;
  Sprite_Character.prototype.initMembers = function() {
    Sprite_Character_initMembers.call(this);
    this._textWindow = new Window_Text();
    this.addChild(this._textWindow);
  };
  const Sprite_Character_setupBalloon = Sprite_Character.prototype.setupBalloon;
  Sprite_Character.prototype.setupBalloon = function() {
    Sprite_Character_setupBalloon.call(this);
    if (this._character._balloonText !== undefined) {
      this._textWindow.setText(this._character._balloonText);
    }
  };
  Game_CharacterBase.prototype.setupTextBalloon = function(text) {
    this._balloonText = text;
  };
})();
