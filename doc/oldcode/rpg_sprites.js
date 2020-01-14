
//增加Actor的Enemy
Spriteset_Battle.prototype.createEnemies = function() {
    var enemies = $gameTroop.members();
    var sprites = [];
    for (var i = 0; i < enemies.length; i++) {
        if(enemies[i]._enemyId==1) {
            sprites[i] = new Sprite_Actor_Enemy(enemies[i]);
        }
        else {
            sprites[i] = new Sprite_Enemy(enemies[i]);
        }
        
    }
    sprites.sort(this.compareEnemySprite.bind(this));
    for (var j = 0; j < sprites.length; j++) {
        this._battleField.addChild(sprites[j]);
    }
    this._enemySprites = sprites;
};
Sprite_Actor.prototype.initialize = function(battler) {
    Sprite_Battler.prototype.initialize.call(this, battler);
    this.scale.x *= -1;
    this.moveToStartPosition();
};
Sprite_Actor.prototype.moveToStartPosition = function() {
    this.startMove(-300, 0, 0);
};
Sprite_Actor.prototype.retreat = function() {
    this.startMove(-300, 0, 30);
};

Sprite_Actor.prototype.stepForward = function() {
    //this.startMove(-48, 0, 12);
};

Sprite_Actor.prototype.stepBack = function() {
    //this.startMove(0, 0, 12);
};


//调整下出场位置
Sprite_Actor.prototype.setActorHome = function(index) {
    this.setHome(200 - index * 32, 328 + index * 48);
};

//扩展Sprite_Actor给敌人用
function Sprite_Actor_Enemy() {
    this.initialize.apply(this, arguments);
}
Sprite_Actor_Enemy.prototype = Object.create(Sprite_Actor.prototype);
Sprite_Actor_Enemy.prototype.constructor = Sprite_Actor_Enemy;
Sprite_Actor_Enemy.prototype.initialize = function(battler) {
    Sprite_Actor.prototype.initialize.call(this, battler);
    this.scale.x *= -1;
};

//调整出场位置
Sprite_Actor_Enemy.prototype.setActorHome = function(index) {
    this.setHome(600 + index * 32, 328 + index * 48);
};
//敌人就不要做出场动作了，跟主角一起移动会出瞬移的问题。
Sprite_Actor_Enemy.prototype.moveToStartPosition = function() {
    //window.fuck = this;
    //console.log(this);
    //this.startMove(0, 0, 0);
};


// function Spriteset_Test() {
//     this.initialize.apply(this, arguments);
// }

// Spriteset_Test.prototype = Object.create(Spriteset_Base.prototype);
// Spriteset_Test.prototype.constructor = Spriteset_Test;

// Spriteset_Test.prototype.initialize = function() {
//     Spriteset_Base.prototype.initialize.call(this);
//     this._battlebackLocated = false;
// };
// Spriteset_Test.prototype.createLowerLayer = function() {
//     Spriteset_Base.prototype.createLowerLayer.call(this);
// };
