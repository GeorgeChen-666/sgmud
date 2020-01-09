PluginManager.regHook('Window_MenuStatus.prototype.maxItems', () => function() {
  return 1;
});
PluginManager.regHook('Window_MenuStatus.prototype.windowHeight', () => function() {
  return Graphics.boxHeight;
});
PluginManager.regHook('Window_MenuStatus.prototype.maxCols', () => function() {
  return 1;
});
PluginManager.regHook('Window_MenuStatus.prototype.numVisibleRows', () => function() {
  return 1;
});
PluginManager.regHook('Window_MenuStatus.prototype.drawItemImage', () => function(index) {
  const actor = $gameParty.members()[index];
  const rect = this.itemRectForText(index);
  const w = Math.min(rect.width, 144);
  const h = Math.min(rect.height, 144);
  const lineHeight = this.lineHeight();
  this.changePaintOpacity(actor.isBattleMember());
  this.drawActorFace(actor, rect.x, rect.y + lineHeight * 2, w, h);
  this.changePaintOpacity(true);
});
Window_MenuStatus.prototype.drawActorHp = function(actor, x, y, width,withnum=true) {
  this.drawGaugeBar(TextManager.hpA,actor.hp, actor.mhp,x, y, width,
      true,withnum,this.hpGaugeColor1(),this.hpGaugeColor1());
};
Window_MenuStatus.prototype.drawActorMp = function(actor, x, y, width,withnum=true) {
  this.drawGaugeBar(TextManager.mpA,actor.mp, actor.mmp,x, y, width,
      true,withnum,this.mpGaugeColor1(),this.mpGaugeColor1());
};
PluginManager.regHook('Window_MenuStatus.prototype.drawItemStatus', () => function(index) {
  var actor = $gameParty.members()[index];
  var rect = this.itemRectForText(index);
  var x = rect.x;
  var y = rect.y;
  var width = rect.width;
  this.drawActorSimpleStatus(actor, x, y, width);
});
PluginManager.regHook('Window_MenuStatus.prototype.drawActorSimpleStatus', () => function(me, x, y, width) {
  const lineHeight = this.lineHeight();
  //门派
  this.drawActorClass(me, x, y + lineHeight * 0, width);
  //角色名
  this.drawActorName(me, lineHeight * 6, y + lineHeight * 0, width);
  //角色图标
  this.drawActorIcons(me, lineHeight * 13, y + lineHeight * 0, width);
  this.drawHorzLine(lineHeight * 1);
  this.drawGaugeBar('食物',me.food,me.mfood,x + 160, y + lineHeight * 2, width - 240,
      true,true,this.hpGaugeColor1(),this.hpGaugeColor2());
  this.drawGaugeBar('饮水',me.water,me.mwater,x + 160, y + lineHeight * 3, width - 240,
      true,true,this.mpGaugeColor1(),this.mpGaugeColor1());
  this.drawActorHp(me,x + 160, y + lineHeight * 4,width - 240);
  this.drawText(parseInt((me.mhp/me.mfhp)*100)+'%', x + width - 63 , y + lineHeight * 4, 70);
  this.drawActorMp(me,x + 160, y + lineHeight * 5,width - 240);
  this.drawText('+255', x + width - 63 , y + lineHeight * 5, 70);
  this.drawHorzLine(lineHeight * 6);
  this.drawGaugeBar('膂力',me.str_now,me.str,x , y + lineHeight * 7, 200);
  this.drawGaugeBar('敏捷',me.dex_now,me.dex,x + 287 , y + lineHeight * 7, 200);
  this.drawGaugeBar('悟性',me.int_now,me.int,x , y + lineHeight * 8, 200);
  this.drawGaugeBar('根骨',me.vit_now,me.vit,x + 287 , y + lineHeight * 8, 200);
  this.drawHorzLine(lineHeight * 9);
  this.resetTextColor();
  this.drawText('你是一名' + me.age + '岁的' + me.sex + '生', x, lineHeight * 10, width);
  this.drawText('你一脸稚气', x, lineHeight * 11, width);
  this.drawText('武艺看起来不堪一击', x, lineHeight * 12, width);
  this.drawText('出手似乎很轻', x, lineHeight * 13, width);
  this.drawText('你还是光棍一条', x, lineHeight * 14, width);
});

(() => {
  Window_Gold.prototype.windowHeight = function() {
    return this.fittingHeight(3);
  };
  Window_Gold.prototype.refresh = function() {
      var x = this.textPadding();
      var width = this.contents.width - this.textPadding() * 2;
      var actor = $gameParty.members()[0];
      this.contents.clear();
      this.drawCurrencyValue(actor.currentExp(), '经验', x, 0, width);
      this.drawCurrencyValue($gameParty.gold(), this.currencyUnit(), x, this.lineHeight() * 2, width);
      this.drawCurrencyValue(actor.point(), '潜能', x, this.lineHeight() * 1, width);
  };
})();