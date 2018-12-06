import Window_PowerBar from '../rpg_windows/Window_PowerBar.js';
const { Sprite, Input, TouchInput, SceneManager, SoundManager, Scene_Base, Window_Command } = window;
export default class Scene_Basketball extends Scene_Base {
  constructor() {
    super();
    this.initialize(arguments);
  }
  initialize() {
    super.initialize();
  }
  create() {
    super.create();
    this.createBackground();
    this.createWindowLayer();
    this.createSceneWindow();
  }
  createBackground() {
    this._backgroundSprite = new Sprite();
    this._backgroundSprite.bitmap = SceneManager.backgroundBitmapNoBlur();
    this.addChild(this._backgroundSprite);
  }
  createSceneWindow() {
    this._powerWindow = new Window_PowerBar();
    this.addWindow(this._powerWindow);

    this._configWindow = new Window_Command(0,0);
    this._configWindow.makeCommandList = function () {
      this.addCommand('是','yes',true);
      this.addCommand('否','no',true);
    }
    this._configWindow.initialize();
    this._configWindow.activate();
    this.addWindow(this._configWindow);
  }
  update() {
    if(Input.isTriggered('escape') || TouchInput.isCancelled()) {
      this._powerWindow.close();
      SoundManager.playCancel();
      SceneManager.pop();
    }
  }
}