import Window_PowerBar from '../rpg_windows/Window_PowerBar.js';
const { Sprite, Input, TouchInput, SceneManager, SoundManager, Scene_Base } = window;
export class Scene_Basketball extends Scene_Base {
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
    this._backgroundSprite.bitmap = SceneManager.backgroundBitmap();
    this.addChild(this._backgroundSprite);
  }
  createSceneWindow() {
    this._powerWindow = new Window_PowerBar();
    this.addWindow(this._powerWindow);
  }
  update() {
    if(Input.isTriggered('escape') || TouchInput.isCancelled()) {
      this._powerWindow.close();
      SoundManager.playCancel();
      SceneManager.pop();
    }
  }
}