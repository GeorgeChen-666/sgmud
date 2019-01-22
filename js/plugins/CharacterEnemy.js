(function () {
  // Game_Actors
  {
    Game_Actors.prototype.actor = function(actorId, isRenew = false) {
      if ($dataActors[actorId]) {
        if (isRenew || !this._data[actorId]) {
          this._data[actorId] = new Game_Actor(actorId);
        }
        return this._data[actorId];
      }
      return null;
    };
  }
  // Game_Troop
  {
    let gt_troop = Game_Troop.prototype.troop;
    Game_Troop.prototype.troop = function () {
      let tempTroop = gt_troop.call(this);
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

    let gt_setup = Game_Troop.prototype.setup;
    Game_Troop.prototype.setup = function(troopId) {
      gt_setup.call(this,troopId);
      let troop = this.troop();
      this._actors =[];
      troop.actor_members.forEach(function(act_member) {
        let actorId = act_member.actorId;
        if($dataActors[actorId]) {
          let actor = $gameActors.actor(actorId, true);
          if (!$gameParty.members().contains(actor)) {// 同一个人不能同时出现在两边
            actor.setEnemy();
            if (act_member.hidden) {
              actor.hide();
            }
            this._actors.push(actor);
          }
        }
      }.bind(this));
    };

    Game_Troop.prototype.members = function () {
      return [...this._actors, ...this._enemies];
    };
    Game_Troop.prototype.ac_members = function () {
      return this._actors;
    };

    let gt_clear = Game_Troop.prototype.clear;
    Game_Troop.prototype.clear = function () {
      gt_clear.call(this);
      this._actors =[];
    };
  }
  // Game_Actor
  {
    Game_Actor.prototype.setEnemy = function (isEnemy = true) {
      this._isEnemy = isEnemy;
    };

    let ga_index = Game_Actor.prototype.index;
    Game_Actor.prototype.index =function () {
      let index = ga_index.call(this);
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

    let ga_isAutoBattle = Game_Actor.prototype.isAutoBattle;
    Game_Actor.prototype.isAutoBattle = function () {
      if(this._isEnemy) {
        return true
      } else {
        return ga_isAutoBattle.call(this);
      }
    };

    Game_Actor.prototype.isBattleMember = function() {
      return $gameParty.battleMembers().contains(this) || $gameTroop.members().contains(this);
    };

    Game_Actor.prototype.friendsUnit = function() {
      if(this._isEnemy) {
        return $gameTroop;
      } else {
        return $gameParty;
      }
    };

    Game_Actor.prototype.opponentsUnit = function() {
      if(this._isEnemy) {
        return $gameParty;
      } else {
        return $gameTroop;
      }
    };

    Game_Actor.prototype.gold = function() {
      return parseInt(this.actor().meta.gold, 10) || 100;
    };

    Game_Actor.prototype.exp = function() {
      return 0;
    };

    Game_Actor.prototype.makeDropItems = function() {
      return [];
    };
    //Game_Actor.prototype.makeAutoBattleActions
  }
  // Sprite_Battler
  {
    let sb_initialize = Sprite_Battler.prototype.initialize;
    Sprite_Battler.prototype.initialize = function(battler,isMirror = false) {
      this._isMirror = isMirror;
      sb_initialize.call(this, battler);
    };
  }
  // Sprite_Actor
  {
    Sprite_Actor.prototype.initialize = function(battler, isMirror) {
      Sprite_Battler.prototype.initialize.call(this, battler, isMirror);
      this.scale.x = isMirror ? -1 : 1;
      if(!isMirror) {
        this.moveToStartPosition();
      }
    };

    Sprite_Actor.prototype.setActorHome = function (index) {
      if(this._isMirror) {
        this.setHome(136 - index * 32, 328 + index * 48);
      } else {
        this.setHome(664 + index * 32, 328 + index * 48);
      }
    };

    Sprite_Actor.prototype.stepForward = function() {
      if(this._isMirror) {
        this.startMove(48, 0, 12);
      } else {
        this.startMove(-48, 0, 12);
      }
    };
  }
  // Spriteset_Battle
  {
    Spriteset_Battle.prototype.createEnemies = function () {
      let sprites = [];
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
  }
})();