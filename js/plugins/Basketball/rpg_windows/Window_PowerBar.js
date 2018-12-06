const { Window_Base } = window;
export default class Window_PowerBar extends Window_Base {
  constructor() {
    super();
    this.initialize(arguments);
  }
  initialize() {
    const [w, h] = [this.windowWidth(), this.windowHeight()];
    const x = (Graphics.boxWidth - w) / 2;
    super.initialize(x, 0, w, h)
    this.max = 100;
    this.val = 20;
    this.refresh();
  }
  windowWidth() {
    return 400;
  }
  windowHeight() {
    return this.fittingHeight(1);
  }
  setValue(val, max = this.max) {
    this.val = val;
    this.max = max;
  }
  drawRangeBar(width) {
    var gaugeY = 0 + this.lineHeight() - 40;
    this.contents.gradientFillRect(Math.floor(width * 0), gaugeY, Math.floor(width * 0.25), 20, '#19A15F', '#FFCD42');
    this.contents.gradientFillRect(Math.floor(width * 0.25), gaugeY, Math.floor(width * 0.2), 20, '#FFCD42', '#DD5246');
    this.contents.gradientFillRect(Math.floor(width * 0.45), gaugeY, Math.floor(width * 0.1), 20, '#DD5246', '#DD5246');
    this.contents.gradientFillRect(Math.floor(width * 0.55)-1, gaugeY, Math.floor(width * 0.2)+1, 20, '#DD5246', '#FFCD42');
    this.contents.gradientFillRect(Math.floor(width * 0.75)-1, gaugeY, Math.floor(width * 0.25)+1, 20, '#FFCD42', '#19A15F');
  }
  drawPowerBar(width, rate, color1, color2) {
    var gaugeY = 0 + this.lineHeight() - 20;
    this.contents.fillRect(0, gaugeY, width, 20, this.gaugeBackColor());

    var fillW = Math.floor(width * rate);
    this.contents.gradientFillRect(fillW - 2, gaugeY, 4, 20, color1, color2);
  };
  refresh() {
    const w = this.contents.width;
    this.contents.clear();
    this.drawRangeBar(w);
    this.drawPowerBar(w, (this.max === 0 ? 0 : this.val / this.max), '#fff', '#fff');
  }
}
