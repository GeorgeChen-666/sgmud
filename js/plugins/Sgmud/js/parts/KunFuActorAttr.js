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
      /**
   *000 
   *005 010 015 020 025 030 035 040 045 50 
   *055 060 065 070 075 080 085 090 095 100 
   *105 110 115 120 125 130 135 140 145 150 
   *155 160 165 170 175 180 185 190 195 200 
   *205 210 215 220 225 230 235 240 245 255
   */
  Game_BattlerBase.prototype.getLevelDesc = function(level) {
    const SkillLevelDesc = [
      '不堪一击',
      '毫不足虑','不足挂齿','初学乍练','勉勉强强','初窥门径','初出茅庐','略知一二','普普通通','平平常常','平淡无奇',
      '粗懂皮毛','半生不熟','登堂入室','略有小成','已有小成','鹤立鸡群','驾轻就熟','青出於蓝','融会贯通','心领神会',
      '炉火纯青','了然於胸','略有大成','已有大成','豁然贯通','非比寻常','出类拔萃','罕有敌手','技冠群雄','神乎其技',
      '出神入化','傲视群雄','登峰造极','无与伦比','所向披靡','一代宗师','精深奥妙','神功盖世','举世无双','惊世骇俗',
      '撼天动地','震古铄今','超凡入圣','威镇寰宇','空前绝后','天人合一','深藏不露','深不可测','返璞归真','极轻很轻',
    ];
    return SkillLevelDesc[parseInt((Math.min((level || 0),255)) / 5)];
  }
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