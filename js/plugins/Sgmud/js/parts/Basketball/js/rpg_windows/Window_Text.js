(() => {
  const { Window_Base, setTimeout } = window;
  class Window_Text extends Window_Base {
    constructor() {
      super(...arguments);
    }
    initialize(text = '', x = 0, y = 0, width = null, rows = 1) {
      this._text = text;
      width = width || Graphics.boxWidth;
      var height = this.fittingHeight(rows);
      Window_Base.prototype.initialize.call(this, x, y, width, height);
      this.refresh();
    }
    setText(text) {
      this._text = text;
      setTimeout(() => {
        this.close();
      }, 1500);
      this.refresh();
    }
    refresh() {
      this.contents.clear();
      this.drawTextEx(this._text, 0, 0);
    }
  }
  _gbb.Window_Text = Window_Text;
})();
