(()=>{
  const { 
    DataManager, 
    Scene_Map,
    _gbb:{ Window_PowerBar, Window_Text } 
  } = window;
  DataManager.loadMapData(1001,false,'../js/plugins/Basketball/data/Map.json');
  let onMapLoaded,onSceneMapUpdate,isMenuEnabled;
  const _this = new StateMachine({
    init: 'load',
    transitions: [
      { name: 'showMenu', from: '*',  to: 'menu' },
      { name: 'startGame',   from: '*', to: 'power'},
      { name: 'throwBall', from: '*', to: 'ball'},
      { name: 'showScore', from: '*',    to: 'score' },
      { name: 'showConfirm', from: '*', to: 'confirm'},
      { name: 'endGame', from: '*', to: 'unload'},
    ],
    data: {
    },
    methods: {
      enter() {
        const {
          $gamePlayer: {_x: x, _y: y, _direction: direction},
          $gameMap:{
            _mapId: mapId,
            _interpreter:itpr,
          }
        } = window;
    
        _this._CustomWindows = (_this._CustomWindows||[]);
        _this._actorIndex = 0;
        _this._touchCount = 0;
        _this._score = 0;
        _this._totalScore = 0;
        //记录之前位置
        _this._locHistory = { x, y, direction, mapId,};
        //插入自定元素
        _this._powerWindow = new Window_PowerBar();
        _this._CustomWindows.push(_this._powerWindow);
        _this._textWindow = new Window_Text();
        _this._CustomWindows.push(_this._textWindow);
        _this._hackSceneMap();
    
        itpr.insertCommands([
          {"code":201,"indent":0,"parameters":[0,1001,-1,-1,0,0]},
          {"code":355,"indent":0,"parameters":[()=>{
            _this.hotTurn('showMenu');
          }]},
          {"code":0,"indent":0,"parameters":[]}
        ]);
      },
      onMenu() { 
        const {
          $gameMap:{
            _interpreter:itpr,
          }
        } = window;
        const evchar = $gameMap.event(3);
        const char = $gameParty.allMembers()[_this._actorIndex];
        evchar._characterName = char._characterName;
        evchar._characterIndex = char._characterIndex;
        evchar._direction = 6;
    
        _this._powerWindow.close();
        _this._textWindow.close();
        itpr.insertCommands([
          {"code":101,"indent":0,"parameters":["",0,1,0]},
          {"code":401,"indent":0,"parameters":[""]},
          {"code":401,"indent":0,"parameters":[""]},
          {"code":401,"indent":0,"parameters":["                      ==投篮游戏=="]},
          {"code":102,"indent":0,"parameters":[["开始游戏","游戏说明","退出游戏"],2,0,1,0]},
          {"code":402,"indent":0,"parameters":[0,"开始游戏"]},
          {"code":355,"indent":1,"parameters":[()=>{
            _this.hotTurn('startGame');
          }]},
          {"code":402,"indent":0,"parameters":[1,"游戏说明"]},
          {"code":105,"indent":1,"parameters":[2,false]},
          {"code":405,"indent":1,"parameters":["这是一段很长很长的说明啊啊啊啊啊啊啊啊啊"]},
          {"code":355,"indent":1,"parameters":[()=>{
            _this.hotTurn('onMenu');
          }]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":402,"indent":0,"parameters":[2,"退出游戏"]},
          {"code":355,"indent":1,"parameters":[()=>{
            //this.showMenu();
          }]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":404,"indent":0,"parameters":[]},
        ]);
      },
      onPower() {
        _this._powerWindow.open();
      },
      onBall() {
        const {
          $gameMap:{
            _interpreter:itpr,
          }
        } = window;
        let [x, y] = [12, 0];
        _this._powerWindow.refresh();
        _this._score = _this._powerWindow.getScore();
        _this._totalScore += _this._score;
        if(_this._score === 0) {
          y = 1;
          x = x - parseInt(Math.random() * 3) - 1;
        }
        itpr.insertCommands([
          {"code":205,"indent":0,"parameters":[2,{"list":[{"code":14,"parameters":[0,0],"indent":null},{"code":0}],"repeat":false,"skippable":false,"wait":false}]},
          {"code":505,"indent":0,"parameters":[{"code":14,"parameters":[0,0],"indent":null}]},
          {"code":205,"indent":0,"parameters":[3,{"list":[{"code":14,"parameters":[0,0],"indent":null},{"code":0}],"repeat":false,"skippable":false,"wait":true}]},
          {"code":505,"indent":0,"parameters":[{"code":14,"parameters":[0,0],"indent":null}]},
          {"code":111,"indent":0,"parameters":[12,()=>{
            return (_this._score == 2);
          }]},
          {"code":212,"indent":1,"parameters":[2,2,false]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":412,"indent":0,"parameters":[]},
          {"code":205,"indent":0,"parameters":[2,{"list":[{"code":14,"parameters":[x,y],"indent":null},{"code":0}],"repeat":false,"skippable":false,"wait":true}]},
          {"code":505,"indent":0,"parameters":[{"code":14,"parameters":[x,y],"indent":null}]},
          {"code":111,"indent":0,"parameters":[12,()=>{
            return (_this._score > 0);
          }]},
          {"code":250,"indent":1,"parameters":[{"name":"Wind7","volume":90,"pitch":130,"pan":0}]},
          {"code":412,"indent":0,"parameters":[]},
          {"code":205,"indent":0,"parameters":[3,{"list":[{"code":0}],"repeat":false,"skippable":false,"wait":true}]},
          {"code":203,"indent":0,"parameters":[2,0,2,10,0]},
          {"code":355,"indent":0,"parameters":[()=>{
            _this.hotTurn('showScore');
          }]},
          {"code":230,"indent":0,"parameters":[60]},
          {"code":0,"indent":0,"parameters":[]}
        ]);
      },
      onScore() {
        if(_this._score > 0) {
          _this._textWindow.open();
          _this._textWindow.setText(`得分：${_this._score}`)
        }
        _this.hotTurn('startGame');
      },
      onConfirm() {
        const {
          $gameMap:{
            _interpreter:itpr,
          }
        } = window;
        itpr.insertCommands([
          {"code":101,"indent":0,"parameters":["",0,0,2]},
          {"code":401,"indent":0,"parameters":[`共得分${_this._totalScore}，是否结束？`]},
          {"code":102,"indent":0,"parameters":[["是","否"],1,0,2,0]},
          {"code":402,"indent":0,"parameters":[0,"是"]},
          {"code":355,"indent":1,"parameters":[()=>{
            _this.hotTurn('endGame');
          }]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":402,"indent":0,"parameters":[1,"否"]},
          {"code":355,"indent":1,"parameters":[()=>{
            _this.hotTurn('startGame');
          }]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":404,"indent":0,"parameters":[]},
          {"code":0,"indent":0,"parameters":[]}
        ]);
      },
      update(map) {
        if(_this.state === 'power') {
          _this.throwBall._touchCount = _this.throwBall._touchCount || 0 ;
          if (TouchInput.isTriggered() || _this.throwBall._touchCount > 0) {
            if (TouchInput.isPressed()) {
              if (_this.throwBall._touchCount === 0 || _this.throwBall._touchCount >= 15) {
                _this.throwBall();
              }
              _this.throwBall._touchCount++;
            } else {
              _this.throwBall._touchCount = 0;
            }
          }
          if(map.isMenuCalled()) {
            _this.showConfirm();
          }
          _this._powerWindow.nextValue();
        }
      },
      onUnload() {
        const {
          $gameMap:{
            _interpreter:itpr,
          }
        } = window;
        const { x, y, direction, mapId,} = _this._locHistory;
        itpr.insertCommands([{"code":201,"indent":0,"parameters":[0,mapId,x,y,direction,0]}]);
        _this._restoreSceneMap();
      },
      _hackSceneMap() {
        isMenuEnabled = Scene_Map.prototype.isMenuEnabled;
        Scene_Map.prototype.isMenuEnabled = function () {
          return false;
        }
        //劫持onMapLoaded显示自定义窗口
        onMapLoaded = Scene_Map.prototype.onMapLoaded;
        Scene_Map.prototype.onMapLoaded = function() {
          onMapLoaded.call(this);
          _this._CustomWindows.forEach(child => {
            child.close();
            this.addWindow(child);
          });
        };
        //劫持update
        onSceneMapUpdate = Scene_Map.prototype.update;
        Scene_Map.prototype.update = function () {
          onSceneMapUpdate.call(this);
          _this.update(this);
        };
      },
      _restoreSceneMap() {
        // 恢复之前的劫持
        Scene_Map.prototype.onMapLoaded = onMapLoaded;
        Scene_Map.prototype.update = onSceneMapUpdate;
        Scene_Map.prototype.isMenuEnabled = isMenuEnabled;
      },
      hotTurn(fn) {
        setTimeout(()=>{
          _this[fn]()
        },0);
      },
    }
  });
  window.BasketballManager = _this;
})()
