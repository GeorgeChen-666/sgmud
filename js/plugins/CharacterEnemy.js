(function () {
  var gt_troop = Game_Troop.prototype.troop;
  Game_Troop.prototype.troop = function () {
    var tempTroop = gt_troop.call(this);
    if(this._troopId === 1) {// 如果_troopId为1那么根据name，决定character
      tempTroop.actor_members = tempTroop.name.split(',').map(e => {
        return {
          actorId: parseInt(e),
          hidden: false
        }
      });
    }
    return tempTroop;
  };

  var gt_setup = Game_Troop.prototype.setup;
  Game_Troop.prototype.setup = function(troopId) {
    gt_setup.call(this,troopId);
    var troop = this.troop();
    this._actors =[];
    troop.actor_members.forEach(function(act_member) {
      if($dataActors[act_member.actorId]) {
        var actorId = act_member.actorId;
        var actor = new Game_Actor(actorId);
        if (act_member.hidden) {
          actor.hide();
        }
        this._actors.push(actor);
      }
    }.bind(this));
  };

  Game_Troop.prototype.members = function () {
    return [...this._actors, ...this._enemies];
  };
  Game_Troop.prototype.ac_members = function () {
    return this._actors;
  };

  var gt_clear = Game_Troop.prototype.clear;
  Game_Troop.prototype.clear = function () {
    gt_clear.call(this);
    this._actors =[];
  };

  var ga_initialize = Game_Actor.prototype.initialize;
  Game_Actor.prototype.initialize = function (actorId,isEnemy = false) {
    ga_initialize.call(this, actorId);
    this._isEnemy = isEnemy;
  };

  var ga_index = Game_Actor.prototype.index;
  Game_Actor.prototype.index =function () {
    var index = ga_index.call(this);
    if(index > -1) {
      return index - ($gameParty.members().length - 1) * 0.5;
    } else {
      index = $gameTroop.ac_members().indexOf(this);
      return index - ($gameTroop.ac_members().length - 1) * 0.5;
    }
  };
  Game_Actor.prototype.originalName = function () {
    return this.actor().name;
  };
  var ga_isAutoBattle = Game_Actor.prototype.isAutoBattle;
  Game_Actor.prototype.isAutoBattle = function () {
    if(this._isEnemy) {
      return true
    } else {
      return ga_isAutoBattle.call(this);
    }
  }
  Game_Actor.prototype.gold = function() {
    return parseInt(this.actor().meta.gold, 10) || 100;
  }
  Game_Actor.prototype.exp = function() {
    return 0;
  }
  Game_Actor.prototype.makeDropItems = function() {
    return [];
  }


  var sb_initialize = Sprite_Battler.prototype.initialize;
  Sprite_Battler.prototype.initialize = function(battler,isMirror = false) {
    sb_initialize.call(this, battler);
    this.scale.x = isMirror ? -1 : 1;
  };

  Sprite_Actor.prototype.initialize = function(battler, isMirror) {
    Sprite_Battler.prototype.initialize.call(this, battler, isMirror);
    if(!isMirror) {
      this.moveToStartPosition();
    }
  };

  Sprite_Actor.prototype.setActorHome = function (index) {
    if(this.scale.x > 0) {
      this.setHome(664 + index * 32, 328 + index * 48);
    } else {
      this.setHome(136 - index * 32, 328 + index * 48);
    }
  };

  Spriteset_Battle.prototype.createEnemies = function () {
    var sprites = [];
    $gameTroop.members().forEach(e => {
      if(e instanceof Game_Enemy) {
        sprites.push(new Sprite_Enemy(e))
      } else {
        sprites.push(new Sprite_Actor(e, true))
      }
    });
    sprites.sort(this.compareEnemySprite.bind(this));
    sprites.forEach(sprite =>
      this._battleField.addChild(sprite)
    );
    this._enemySprites = sprites;
  };

})();