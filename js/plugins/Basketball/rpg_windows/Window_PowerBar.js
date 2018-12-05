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
    this.val = 0;
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
  drawGaugeBar(title,min,max, x, y, width,hasGauge=false,hasText=true,color1,color2) {
    width = width || 186;
    if(hasGauge) {
      this.drawGauge(x, y, width, (max === 0 ? 0 : min / max), color1, color2);
    }
    this.changeTextColor(this.systemColor());
    this.drawText(title, x, y, 56);
    if(hasText) {
      this.drawCurrentAndMax(min, max, x, y, width,
        this.normalColor(), this.normalColor());
    }
  };
  refresh() {
    const w = this.contents.width;
    this.contents.clear();
    this.drawGaugeBar('', this.val , this.max,0,0,w,true,true, this.tpGaugeColor1(),this.tpGaugeColor2());
  }
}