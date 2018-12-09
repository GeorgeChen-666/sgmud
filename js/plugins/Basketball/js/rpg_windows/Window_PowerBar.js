const { Window_Base } = window;
export default class Window_PowerBar extends Window_Base {
  constructor() {
    super(...arguments);
  }
  initialize() {
    const [w, h] = [this.windowWidth(), this.windowHeight()];
    const x = (Graphics.boxWidth - w) / 2;
    const y = this.lineHeight() * 3;
    super.initialize(x, y, w, h);
    this._max = 100;
    this._val = 0;
    this._score = 0;
    this._offset = 1;
    this._offsetTime = 1;
    this.refresh();
    this.drawRangeBar(this.contents.width);
  }
  windowWidth() {
    return 400;
  }
  windowHeight() {
    return this.fittingHeight(1);
  }
  getScore() {
    return this._score;
  }
  nextValue() {
    this._val += (this._offset * this._offsetTime);
    this.refresh();
    if(this._val <= 0 || this._val >= this._max) {
      this._offset *= -1;
    }
    if(this._val <= 20 || this._val >= 80) {
      this._offsetTime = 3;
    } else if(this._val <= 35 || this._val >= 65) {
      this._offsetTime = 2.5;
    } else if(this._val <= 45 || this._val >= 55) {
      this._offsetTime = 1;
    } else {
      this._offsetTime = 0.5;
    }
  }
  setValue(val) {
    this._val = val;
    this.refresh();
  }

  drawRangeBar(width) {
    const gaugeY = 0 + this.lineHeight() - 40;
    this.contents.gradientFillRect(Math.floor(width * 0), gaugeY, Math.floor(width * 0.45), 20, '#19A15F', '#19A15F');
    this.contents.gradientFillRect(Math.floor(width * 0.45), gaugeY, Math.floor(width * 0.025), 20, '#19A15F', '#FFCD42');
    this.contents.gradientFillRect(Math.floor(width * 0.475), gaugeY, Math.floor(width * 0.025)+1, 20, '#FFCD42', '#DD5246');
    // this.contents.gradientFillRect(Math.floor(width * 0.475), gaugeY, Math.floor(width * 0.05), 20, '#DD5246', '#DD5246');
    this.contents.gradientFillRect(Math.floor(width * 0.5), gaugeY, Math.floor(width * 0.025), 20, '#DD5246', '#FFCD42');
    this.contents.gradientFillRect(Math.floor(width * 0.525), gaugeY, Math.floor(width * 0.025), 20, '#FFCD42', '#19A15F');
    this.contents.gradientFillRect(Math.floor(width * 0.55), gaugeY, Math.floor(width * 0.45)+1, 20, '#19A15F', '#19A15F');

    // this.contents.gradientFillRect(Math.floor(width * 0.4625)-1, gaugeY, Math.floor(width * (0.5375 - 0.4625)+1, 20, '#FFCD42', '#FFCD42');
    // this.contents.gradientFillRect(Math.floor(width * 0.4625)-1, gaugeY, Math.floor(width * 0.5375)+1, 20, '#FFCD42', '#FFCD42');
  }
  drawPowerBar(width, rate) {
    const gaugeY = 0 + this.lineHeight() - 20;
    this.contents.fillRect(0, gaugeY, width, 20, this.gaugeBackColor());

    const fillW = Math.floor(width * rate);
    this.contents.gradientFillRect(fillW - 1, gaugeY, 2, 20, '#fff', '#fff');
  };
  refresh() {
    const w = this.contents.width;
    // this.contents.clear();
    // this.drawRangeBar(w);
    const v = (this._val / this._max);
    if(v > 0.4875 && v < 0.5125) {
      this._score = 2;
    } else if(v > 0.4625 && v < 0.5375) {
      this._score = 1;
    } else {
      this._score = 0;
    }
    this.drawPowerBar(w, (this._max === 0 ? 0 : v));
  }
}
