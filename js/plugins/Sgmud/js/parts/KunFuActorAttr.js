(() => {
  Game_BattlerBase.prototype.setup = function(actorId) {
    const { meta } = $dataActors[actorId];
    this._kfName = meta.姓名 || '无名氏';
    this._kfSex = meta.性别 || '？';
    this._kfAge = 1 * meta.年龄 || 14;
    this._kfAtk = 1 * meta.攻击 || 0;
    this._kfDef = 1 * meta.防御 || 0;
    this._kfHit = 1 * meta.命中 || 0;
    this._kfEva = 1 * meta.回避 || 0;
    this._kfStr = 1 * meta.膂力 || 0;
    this._kfDex = 1 * meta.敏捷 || 0;
    this._kfInt = 1 * meta.悟性 || 0;
    this._kfVit = 1 * meta.根骨 || 0;
    this._kfLuk = 1 * meta.福缘 || 0;
    this._hp = 1 * meta.hp || 0;
    this._mhp = 1 * meta.mhp || 0;
    this._kfFhp = 1 * meta.fhp || 0;
    this._mp = 1 * meta.mp || 0;
    this._mmp = 1 * meta.mmp || 0;
  }
  Object.defineProperties(Game_BattlerBase.prototype, {
    sex: { get: function(){ return this._kfSex ||'？';}, configurable: true },
    age: { get: function() { return this._kfAge * 1; }, configurable: true },
    hp: { 
      get: function() { return this._hp || this._mhp; }, 
      set: function(v) { this._hp = v; }, 
      configurable: true 
    },
    mhp: { get: function() { return this._mhp; }, configurable: true },
    mp: { get: function() { return this._mp; }, configurable: true },
    mmp: { get: function() { return this._mmp; }, configurable: true },
    //先天膂力
    str: { get: function(){ return this._kfStr; }, configurable: true },
    //先天敏捷
    dex: { get: function(){ return this._kfDex; }, configurable: true },
    //先天悟性
    int: { get: function(){ return this._kfInt; }, configurable: true },
    //先天根骨
    vit: { get: function(){ return this._kfVit; }, configurable: true },
    luk: { get: function(){ return this._kfLuk; }, configurable: false },
    hit: { get: function() { return this._kfHit; }, configurable: true },
    eva: { get: function() { return this._kfEva; }, configurable: true },
    atk: { get: function() { return this._kfAtk; }, configurable: true },
    def: { get: function() { return this._kfDef; }, configurable: true },
  });
})();
(() => {
  PluginManager.regHook('Game_Actor.prototype.setup', oFunc => function(actorId) {
    Game_BattlerBase.prototype.setup(actorId);
    oFunc();
  });
  PluginManager.regHook('Game_Actor.prototype.initMembers', oFunc => function() {
    oFunc();
    this._skilllevels = [];
    this._skillexps = [];
    this._equipskills = [];//装备的技能
  });
  function getAttrNow(val, skillId) {
    return (val || 0) + parseInt(this.skilllevel(skillId) / 10);
  }
  Object.defineProperties(Game_Actor.prototype, {
    //最大HP上限
    mhp: {
      get: function() {
        let mhp = 100 + this.mmp / 4;
        mhp += (Math.min(this.age, 25) - 14) * 20;
        let hllv = this.skilllevel(16); //红莲教义等级；
        if (hllv > 80) {
          mhp += (hllv * this.vit) / 80;
        }
        return parseInt(mhp);
      },
      configurable: false
    },
    mmp: {
      get: function() {
        return this._mmp || 0;
      },
      set: function(v) {
        this._mmp = v;
      },
      configurable: true
    },
    fhp: {
      get: function() {
        return this._fhp || this.mhp;
      },
      set: function(v) {
        this._fhp = v;
      },
      configurable: true
    },
    mp: {
      get: function() {
        return this._mp || this.mmp;
      },
      set: function(v) {
        this._mp = v;
      },
      configurable: true
    },
    str_now: { //后天膂力
      get: function() {
        return getAttrNow.bind(this)(this._kfStr, 6); //基本拳脚
      },
      configurable: false
    },
    dex_now: { //后天敏捷
      get: function() {
        return getAttrNow.bind(this)(this._kfDex, 12); //基本轻功
      },
      configurable: false
    },
    int_now: { //后天悟性
      get: function() {
        return getAttrNow.bind(this)(this._kfInt, 14); //读书识字
      },
      configurable: false
    },
    vit_now: { //后天根骨
      get: function() {
        return getAttrNow.bind(this)(this._kfVit, 11); //基本内功
      },
      configurable: false
    },
    exp: { // 经验
      get: function() {
        return this._kfExp || 0;
      },
      set: function(v) {
        this._kfExp = v;
      },
      configurable: false
    },
    pot: { // 潜能
      get: function() {
        return this._kfPot || 100;
      },
      set: function(v) {
        this._kfPot = v;
      },
      configurable: false
    },
    mfood: { //最大食物
      get: function() {
        return 75 + this.str_now * 15;
      },
      configurable: true
    },
    mwater: { //最大饮水
      get: function() {
        return 60 + this.str_now * 15;
      },
      configurable: true
    },
    food: {
      get: function() {
        return this._kfFood || 100;
      },
      set: function(v) {
        this._kfFood = v;
      },
      configurable: true
    },
    water: {
      get: function() {
        return this._kfWater || 100;
      },
      set: function(v) {
        this._kfWater = v;
      },
      configurable: true
    }
  });
	Game_Actor.prototype.point = function() {
    return this._point;
  };
  Game_Actor.prototype.skilllevels = function() {
      return this._skilllevels;
  };
  Game_Actor.prototype.skilllevel = function(id) {
      return this._skilllevels[id] || 0;
  };
  Game_Actor.prototype.skillexps = function() {
      return this._skillexps;
  };
  Game_Actor.prototype.skillexp = function(id) {
      return this._skillexps[id] || 0;
  };
  Game_Actor.prototype.getequipskill = function(type) {
      return this._equipskills[type];
  }
  Game_Actor.prototype.setequipskill = function(type,skill) {
      this._equipskills[type]=skill;
  }
})();