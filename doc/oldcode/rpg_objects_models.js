
{
    //Game_BattlerBase
    (function () {
        Game_BattlerBase.prototype.attackSkillId = function () {
            return this.AttackMoveId || 1;
        };
        Game_BattlerBase.prototype.setHp = function (hp) {
            this.hp = hp;
            this.refresh();
        };
        Object.defineProperties(Game_BattlerBase.prototype, {
            // Actor原始数据
            data: {
                get: function () {
                    try {
                        return $dataActors[this.actorId()];
                    } catch (e) {
                        return {meta: {}};
                    }

                }, configurable: true
            },
            //性别
            sex: {
                get: function () {
                    return this.data.meta.性别 || '？';
                }, configurable: true
            },
            //年龄
            age: {
                get: function () {
                    return this.data.meta.年龄 * 1;
                }, configurable: true
            },
            hp: {
                get: function () {
                    return (this.data.meta.hp || this.mfhp) * 1;
                },
                set: function (v) {
                    this.data.meta.hp = v;
                },
                configurable: true
            },
            // Maximum Hit Points
            fhp: {
                get: function () {
                    return this.data.meta.fhp * 1;
                }, configurable: true
            },
            mp: {
                get: function () {
                    return this.data.meta.mp * 1;
                }, configurable: true
            },
            // Maximum Magic Points
            fmp: {
                get: function () {
                    return this.data.meta.fmp * 1;
                }, configurable: true
            },
            //最大HP上限
            mfhp: {
                get: function () {
                    return this.data.meta.mhp * 1;
                }, configurable: false
            },
            //先天膂力
            str: {
                get: function () {
                    return (this.data.meta.膂力 || '0') * 1
                }, configurable: true
            },
            //先天敏捷
            dex: {
                get: function () {
                    return (this.data.meta.敏捷 || '0') * 1
                }, configurable: true
            },
            //先天悟性
            int: {
                get: function () {
                    return (this.data.meta.悟性 || '0') * 1
                }, configurable: true
            },
            //先天根骨
            vit: {
                get: function () {
                    return (this.data.meta.根骨 || '0') * 1
                }, configurable: true
            },
            //福缘
            luk: {
                get: function () {
                    return (this.data.meta.福缘 || '0') * 1
                }, configurable: false
            }
        });

    })();

    //Game_Actor
    (function (){
        Object.defineProperties(Game_Actor.prototype, {
            //最大HP上限
            mfhp: {
                get: function () {
                    var mfhp = 100 + this.fmp / 4
                    mfhp += (Math.min(this.age, 25) - 14) * 20;
                    var hllv = this.skilllevel(16);//红莲教义等级；
                    if (hllv > 80) {
                        mfhp += (hllv * this.vit) / 80;
                    }
                    return parseInt(mfhp) * 1;
                },
                configurable: false
            },
            // Maximum Magic Points
            fmp: {
                get: function () {
                    return (this.data.meta.fmp || 0) * 1;
                },
                set: function (v) {
                    this.data.meta.fmp = v;
                },
                configurable: true
            },
            // Maximum Hit Points
            fhp: {
                get: function () {
                    return (this.data.meta.fhp || this.mfhp) * 1;
                },
                set: function (v) {
                    this.data.meta.fhp = v;
                },
                configurable: true
            },
            mp: {
                get: function () {
                    return (this.data.meta.mp || this.fmp) * 1;
                },
                set: function (v) {
                    this.data.meta.mp = v;
                },
                configurable: true
            },
            //后天膂力
            str_now: {
                get: function () {
                    return (this.data.meta.膂力 || '0') * 1 + parseInt(this.skilllevel(6) / 10);//基本拳脚
                }, configurable: false
            },
            //后天敏捷
            dex_now: {
                get: function () {
                    return (this.data.meta.敏捷 || '0') * 1 + parseInt(this.skilllevel(12) / 10);//基本轻功
                }, configurable: false
            },
            //后天悟性
            int_now: {
                get: function () {
                    return (this.data.meta.悟性 || '0') * 1 + parseInt(this.skilllevel(14) / 10);//读书识字
                }, configurable: false
            },
            //后天根骨
            vit_now: {
                get: function () {
                    return (this.data.meta.根骨 || '0') * 1 + parseInt(this.skilllevel(11) / 10);//基本内功
                }, configurable: false
            },
            //经验
            exp: {
                get: function () {
                    return (this.data.meta.经验 || '0') * 1
                },
                set: function (v) {
                    this.data.meta.经验 = v;
                },
                configurable: false
            },
            //潜能
            pot: {
                get: function () {
                    return (this.data.meta.潜能 || '0') * 1
                },
                set: function (v) {
                    this.data.meta.潜能 = v;
                },
                configurable: false
            },
            //最大食物
            ffood: {
                get: function () {
                    return 75 + this.str * 15
                }, configurable: true
            },
            //最大饮水
            fwater: {
                get: function () {
                    return 60 + this.str * 15
                }, configurable: true
            },
            //食物
            food: {
                get: function () {
                    return this.data.meta.食物 || 100
                },
                set: function (v) {
                    this.data.meta.食物 = v
                },
                configurable: true
            },
            //饮水
            water: {
                get: function () {
                    return this.data.meta.饮水 || 100
                },
                set: function (v) {
                    this.data.meta.饮水 = v
                },
                configurable: true
            },

        });
        Game_Actor.prototype.initMembers = function () {
            Game_Battler.prototype.initMembers.call(this);
            this._actorId = 0;
            this._name = '';
            this._nickname = '';
            this._classId = 0;
            this._level = 0;
            this._characterName = '';
            this._characterIndex = 0;
            this._faceName = '';
            this._faceIndex = 0;
            this._battlerName = '';
            this._exp = {};
            this._point = 100;//潜能
            this._skills = [];
            this._skilllevels = [];
            this._skillexps = [];
            this._equipskills = [];//装备的技能
            this._equips = [];
            this._actionInputIndex = 0;
            this._lastMenuSkill = new Game_Item();
            this._lastBattleSkill = new Game_Item();
            this._lastCommandSymbol = '';
        };
        Game_Actor.prototype.point = function () {
            return this._point;
        };
        Game_Actor.prototype.skilllevels = function () {
            return this._skilllevels;
        };
        Game_Actor.prototype.skilllevel = function (id) {
            return this._skilllevels[id] || 0;
        };
        Game_Actor.prototype.skillexps = function () {
            return this._skillexps;
        };
        Game_Actor.prototype.skillexp = function (id) {
            return this._skillexps[id] || 0;
        };
        Game_Actor.prototype.getequipskill = function (type) {
            return this._equipskills[type];
        };
        Game_Actor.prototype.setequipskill = function (type, skill) {
            this._equipskills[type] = skill;
        };
        //获取进攻招式
        Game_Actor.prototype.makeAttackMove = function () {
            var saMoves = []
            var weapons = this.weapons()[0];
            var eSkill = weapons ?
                this.getequipskill(2) || [, , $dataSkills[7], $dataSkills[8], $dataSkills[9], $dataSkills[10]][weapons.wtypeId] :
                this.getequipskill(1) || $dataSkills[6];//获取已经装备的拳脚类型的技能或基本拳脚
            var eSkillLevel = this.skilllevel(eSkill.id);
            var saMoves = $dataSkills.filter(function (skill) {
                return skill && skill.meta.所属武功 == eSkill.id && skill.meta.领悟等级 <= eSkillLevel;
            }, this);
            var saRandMove = saMoves[Math.randomInt(saMoves.length)]
            this.AttackMoveId = saRandMove ? saRandMove.id : 1;
        }
    })();

    //Game_Enemy
    (function (){
        // Game_Enemy.prototype.makeAttackMove = function(){alert('')
        //     return 1;
        // }
        //增加_enemy字段，缓存一份，便于支持$dataEnemies的修改，避免联动变值。
        Game_Enemy.prototype.initMembers = function () {
            Game_Battler.prototype.initMembers.call(this);
            this._enemyId = 0;
            this._enemy = null;
            this._letter = '';
            this._plural = false;
            this._screenX = 0;
            this._screenY = 0;
        };
        Game_Enemy.prototype.initialize = function (enemyId, x, y, actorId) {
            Game_Battler.prototype.initialize.call(this);
            this.setup(enemyId, x, y, actorId);
        };
        Game_Enemy.prototype.setup = function (enemyId, x, y, actorId) {
            this._enemyId = enemyId;
            this._actorId = actorId;
            this._enemy = JsonEx.makeDeepCopy($dataEnemies[this._enemyId]);
            if (actorId) {
                this._actor = JsonEx.makeDeepCopy($dataActors[this._actorId]);

            }
            this.initEnemySkill();
            //此处还要照抄Game_Actor初始化装备等地方的功能，战斗要用的。
            this._screenX = x;
            this._screenY = y;
            this.recoverAll();
        };
        Game_Enemy.prototype.skilllevel = function (skillid) {
            for (var i = 1; i <= 13; i++) {
                if (this.actor().meta['武功' + i] == skillid) {
                    return this.actor().meta['等级' + i] || 0;
                }
            }
            return 0;
        };
        Game_Enemy.prototype.initEnemySkill = function () {
            if (this.actorId) {
                this.enemy().actions = [];
                var saMoves = []
                var weapons = this.weapons()[0];
                var eSkill = $dataSkills[weapons ? this.actor().meta.使用兵刃 || [, , 7, 8, 9, 10][weapons.wtypeId] : this.actor().meta.使用拳脚 || 6]//获取已经装备的拳脚类型的技能或基本拳脚
                var eSkillLevel = this.skilllevel(eSkill.id);
                var saMoves = $dataSkills.filter(function (skill) {
                    return skill && skill.meta.所属武功 == eSkill.id && skill.meta.领悟等级 <= eSkillLevel;
                }, this);
                var saActions = this.enemy().actions;
                saMoves.forEach(function (move) {
                    saActions.push({
                        "conditionParam1": 0,
                        "conditionParam2": 0,
                        "conditionType": 0,
                        "rating": 5,
                        "skillId": move.id
                    });
                });

            }
        };
        Game_Enemy.prototype.enemy = function () {
            return this._enemy;
        };
        Game_Enemy.prototype.actor = function () {
            return this._actor;
        };
        //让enemy一直嘚瑟
        Game_Enemy.prototype.makeActions = function () {
            Game_Battler.prototype.makeActions.call(this);
            if (this.numActions() > 0) {
                var actionList = this.enemy().actions.filter(function (a) {
                    return this.isActionValid(a);
                }, this);
                if (actionList.length > 0) {
                    this.selectAllActions(actionList);
                }
            }
            //this.setActionState('waiting');
        };
        Game_Enemy.prototype.equips = function () {
            return [$dataItems[this.actor().meta.掉落1]];
        };
        Game_Enemy.prototype.weapons = function () {
            return this.equips().filter(function (item) {
                return item && DataManager.isWeapon(item);
            });
        };
        //返回对应的actor
        Game_Enemy.prototype.actorId = function () {
            return this._actorId;
        };
        Game_Enemy.prototype.performDamage = function () {
            Game_Battler.prototype.performDamage.call(this);
            if (this.actorId) {
                this.requestMotion('damage');
            }
            else {
                this.requestEffect('blink');
            }
            SoundManager.playEnemyDamage();
        };
        Game_Enemy.prototype.performAttack = function () {
            var weapons = this.weapons();
            var wtypeId = weapons[0] ? weapons[0].wtypeId : 0;
            var attackMotion = $dataSystem.attackMotions[wtypeId];
            if (attackMotion) {
                if (attackMotion.type === 0) {
                    this.requestMotion('thrust');
                } else if (attackMotion.type === 1) {
                    this.requestMotion('swing');
                } else if (attackMotion.type === 2) {
                    this.requestMotion('missile');
                }
                this.startWeaponAnimation(attackMotion.weaponImageId);
            }
        };
        Game_Enemy.prototype.performAction = function (action) {
            Game_Battler.prototype.performAction.call(this, action);
            if (action.isAttack()) {
                this.performAttack();
            } else if (action.isGuard()) {
                this.requestMotion('guard');
            } else if (action.isMagicSkill()) {
                this.requestMotion('spell');
            } else if (action.isSkill()) {
                this.requestMotion('skill');
            } else if (action.isItem()) {
                this.requestMotion('item');
            }
        };
    })();

}