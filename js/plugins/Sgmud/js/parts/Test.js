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
    fhp: { get: function() { return this._kfFhp; }, configurable: true },
    mp: { get: function() { return this._mp; }, configurable: true },
    fmp: { get: function() { return this._mmp; }, configurable: true },
    //最大HP上限
    //mfhp:{ get: function(){ return this.data.meta.mhp * 1;}, configurable: false},
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
  PluginManager.regHook('Game_BattlerBase.prototype.initMembers', oFunc => function(actorId) {
    this._kfName = '无名氏';
    this._kfSex = '？';
    this._kfAge = 14;
    this._kfAtk = 0;
    this._kfDef = 0;
    this._kfHit = 0;
    this._kfEva = 0;
    this._kfStr = 0;
    this._kfDex = 0;
    this._kfInt = 0;
    this._kfVit = 0;
    this._kfLuk = 0;
    this._kfHp = 0;
    this._kfFhp = 0;
    this._kfMhp = 0;
    this._kfMp = 0;
    this._kfFmp = 0;
    oFunc();
  });


  
  PluginManager.regHook('Game_Actor.prototype.setup', oFunc => function(actorId) {
    Game_BattlerBase.prototype.setup(actorId);
    oFunc();
  });

})();
