Game_Troop = class extends Game_Troop {
  troop() {
    let tempTroop = $dataTroops[this._troopId];
    if(this._troopId === 1) {// 如果_troopId为1那么根据name，决定character
      tempTroop.actor_members = tempTroop.name.split(',').map(e => {
        return {
          actorId: parseInt(e),
          hidden: false
        }
      });
    }
    return tempTroop;
  }
  setup(troopId) {
    this.clear();
    this._troopId = troopId;
    let troop = this.troop();

    this._actors =[];
    troop.actor_members.forEach(act_member => {
      if($dataActors[act_member.actorId]) {
        let actorId = act_member.actorId;
        let actor = new Game_Actor(actorId);
        if (act_member.hidden) {
          actor.hide();
        }
        this._actors.push(actor);
      }
    });

    this._enemies = [];
    troop.members.forEach(member => {
      if ($dataEnemies[member.enemyId]) {
        let enemyId = member.enemyId;
        let x = member.x;
        let y = member.y;
        let enemy = new Game_Enemy(enemyId, x, y);
        if (member.hidden) {
          enemy.hide();
        }
        this._enemies.push(enemy);
      }
    });
    this.makeUniqueNames();
  }
  makeUniqueNames() {
    let table = this.letterTable();
    let enemys = this.members().filter(e => (e instanceof Game_Enemy))
    enemys.forEach((enemy) => {
      if (enemy.isAlive() && enemy.isLetterEmpty()) {
        let name = enemy.originalName();
        let n = this._namesCount[name] || 0;
        enemy.setLetter(table[n % table.length]);
        this._namesCount[name] = n + 1;
      }
    });
    enemys.forEach((enemy) => {
      let name = enemy.originalName();
      if (this._namesCount[name] >= 2) {
        enemy.setPlural(true);
      }
    });
  }
  members() {
    return [...this._actors, ...this._enemies];
  }

  ac_members() {
    return this._actors;
  }
  clear () {
    this._interpreter.clear();
    this._troopId = 0;
    this._eventFlags = {};
    this._enemies = [];
    this._actors =[];
    this._turnCount = 0;
    this._namesCount = {};
  }
}
Game_Actor = class extends Game_Actor {
  initialize(actorId,isEnemy = false) {
    Game_Battler.prototype.initialize.call(this);
    this.setup(actorId);
    this._isEnemy = isEnemy;
  }
  index() {
    let index = $gameParty.members().indexOf(this);
    if(index > -1) {
      return index - ($gameParty.members().length - 1) * 0.5;
    } else {
      index = $gameTroop.ac_members().indexOf(this);
      return index - ($gameTroop.ac_members().length - 1) * 0.5;
    }
  }
  originalName() {
    return this.actor().name;
  }
  isAutoBattle() {
    if(this._isEnemy) {
      return true
    } else {
      return this.specialFlag(Game_BattlerBase.FLAG_ID_AUTO_BATTLE);
    }
  }
  gold() {
    return parseInt(this.actor().meta.gold, 10) || 100;
  }
  exp() {
    return 0;
  }
  makeDropItems() {
    return [];
  }
}

Sprite_Battler.prototype.initialize = function(battler,isMirror = false) {
  Sprite_Base.prototype.initialize.call(this);
  this.initMembers();
  this.scale.x = isMirror ? -1 : 1;
  this.setBattler(battler);
};

Sprite_Actor.prototype.initialize = function(battler, isMirror) {
  Sprite_Battler.prototype.initialize.call(this, battler, isMirror);
  if(!isMirror) {
    this.moveToStartPosition();
  }
};
Sprite_Actor = class extends Sprite_Actor {
  setActorHome(index) {
    if(this.scale.x > 0) {
      this.setHome(664 + index * 32, 328 + index * 48);
    } else {
      this.setHome(136 - index * 32, 328 + index * 48);
    }
  }
}
Spriteset_Battle = class extends Spriteset_Battle {
  createEnemies() {
    let sprites = [];
    $gameTroop.members().forEach(e => {
      if(e instanceof Game_Enemy) {
        sprites.push(new Sprite_Enemy(e))
      } else {
        sprites.push(new Sprite_Actor(e, true))
      }
    })
    sprites.sort(this.compareEnemySprite.bind(this));
    sprites.forEach(sprite =>
      this._battleField.addChild(sprite)
    )
    this._enemySprites = sprites;
  }
}