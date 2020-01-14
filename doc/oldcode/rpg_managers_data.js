//增加一个可以读取mod数据文件的方法
/**
 * 从json读取数据文件
 */
DataManager.loadPluginDataFile = function(name, src) {
    var xhr = new XMLHttpRequest();
    var base_path = 'KnightErrantBox/';
    var url = base_path + 'data/' + src;
    xhr.open('GET', url);
    xhr.overrideMimeType('application/json');
    xhr.onload = function() {
        if (xhr.status < 400) {
            window[name] = JSON.parse(xhr.responseText);
            DataManager.onLoad(window[name]);
        }
    };
    xhr.onerror = function() {
        DataManager._errorUrl = DataManager._errorUrl || url;
    };
    window[name] = null;
    xhr.send();
};