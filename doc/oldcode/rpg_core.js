
//增加可以禁用触摸的功能（后期把true换成移动中的判断）
/**
 * Updates the touch data.
 *
 * @static
 * @method update
 */
TouchInput.update = function() {
    if(true)
    {
        this._triggered = this._events.triggered;
        this._cancelled = this._events.cancelled;
        this._moved = this._events.moved;
        this._released = this._events.released;
        this._wheelX = this._events.wheelX;
        this._wheelY = this._events.wheelY;
        this._events.triggered = false;
        this._events.cancelled = false;
        this._events.moved = false;
        this._events.released = false;
        this._events.wheelX = 0;
        this._events.wheelY = 0;
        if (this.isPressed()) {
            this._pressedTime++;
        }
    }
};
//增加可以禁用输入的功能（后期把true换成移动中的判断）
/**
 * Updates the input data.
 *
 * @static
 * @method update
 */
Input.update = function() {
    if(true)
    {
        this._pollGamepads();
        if (this._currentState[this._latestButton]) {
            this._pressedTime++;
        } else {
            this._latestButton = null;
        }
        for (var name in this._currentState) {
            if (this._currentState[name] && !this._previousState[name]) {
                this._latestButton = name;
                this._pressedTime = 0;
                this._date = Date.now();
            }
            this._previousState[name] = this._currentState[name];
        }
        this._updateDirection();
    }
};