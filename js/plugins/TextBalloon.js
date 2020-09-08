/*:
 * @plugindesc 让NPC显示对话气球.使用方法：在事件编辑器中输入this.setupTextBalloon('你好呀~~！')
 * @author GeorgeChen
 */
/**
 * @requires Hooks.js
 */
(() => {
  const maxCharCountPerLine = 16;
  const gblen = function (text) {
    let len = 0;
    for (let i = 0; i < text.length; i++) {
      if (text.charCodeAt(i) > 127 || text.charCodeAt(i) === 94) {
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
      this._text = "";
      Window_Base.prototype.initialize.call(this, 0, 0, 140, 60);
      this.move(0, 0, 0, 0);
      this.close();
      this._arrowSprite = new Sprite(new Bitmap(20, 10));
      this._arrowSprite.visible = false;
      const skin = this._windowskin;
      this._arrowSprite.bitmap.blt(skin, 135, 61, 20, 10, 0, 0);

      this.on("added", () => {
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
        .slice(0, maxCharCountPerLine * 2)
        .reduce(
          (pv, cv) =>
            pv +
            (gblen(pv) === maxCharCountPerLine ||
            gblen(pv) + 1 === maxCharCountPerLine
              ? "\n"
              : "") +
            cv
        );
      this._text = rtxt;
      this.refresh();
      const width =
        this.textWidth(rtxt.split("\n")[0] + " ") + this.standardPadding() * 2;
      const height = this.fittingHeight(rtxt.includes("\n") ? 2 : 1);
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
      }, 2000);
    }
    refresh() {
      this.contents.clear();
      this.drawTextEx(this._text, 0, 0);
    }
  }

  PluginManager.regHook(
    "Sprite_Character.prototype.initMembers",
    (oFunc) =>
      function () {
        oFunc();
        this._textBalloonWindow = new Window_TextBalloon();
        this.addChild(this._textBalloonWindow);
      }
  );

  PluginManager.regHook(
    "Sprite_Character.prototype.setupBalloon",
    (oFunc) =>
      function () {
        oFunc();
        if (
          this._character._balloonText !== undefined &&
          this._textBalloonWindow.isClosed()
        ) {
          this._textBalloonWindow.setText(this._character._balloonText);
        }
      }
  );
  PluginManager.regHook(
    "Game_CharacterBase.prototype.startBalloon",
    (oFunc) =>
      function () {
        oFunc();
        this._balloonText = undefined;
      }
  );
  PluginManager.regHook(
    "Game_CharacterBase.prototype.isBalloonPlaying",
    (oFunc) =>
      function () {
        return oFunc() || this._balloonText !== undefined;
      }
  );
  Game_Interpreter.prototype.setupTextBalloon = function (
    text,
    actorId = 0, //-1 玩家，0 当前事件，其它代表事件编号
    isWait = false //是否等待气球消失再继续下面的事件
  ) {
    this._character = this.character(actorId);
    if (this._character) {
      this.character(actorId)._balloonText = text;
      if (isWait) {
        this.setWaitMode("balloon");
      }
    }
    return true;
  };
})();
