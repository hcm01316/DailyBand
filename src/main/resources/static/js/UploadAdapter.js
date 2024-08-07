 class UploadAdapter {
    constructor(loader) {
    this.loader = loader;
}

    upload() {
    return this.loader.file.then(file => new Promise((resolve, reject) => {
    this._initRequest();
    this._initListeners(resolve, reject, file);
    this._sendRequest(file);
}));
}

    _initRequest() {

    var url  = location.host
    const xhr = this.xhr = new XMLHttpRequest();
    xhr.open('POST', `http://${url}/dailyband/rboard/upload`, true);
    xhr.responseType = 'json';

    // CSRF 토큰을 헤더에 추가
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    xhr.setRequestHeader(csrfHeader, csrfToken);
}

    _initListeners(resolve, reject, file) {
    const xhr = this.xhr;
    const loader = this.loader;
    const genericErrorText = '파일을 업로드 할 수 없습니다.';

    xhr.addEventListener('error', () => reject(genericErrorText));
    xhr.addEventListener('abort', () => reject());
    xhr.addEventListener('load', () => {
    const response = xhr.response;
    if (!response || response.error) {
    return reject(response && response.error ? response.error.message : genericErrorText);
}

    resolve({
    default: response.url // 업로드된 파일 주소
});
});
}

    _sendRequest(file) {
    const data = new FormData();
    data.append('upload', file);
    this.xhr.send(data);
}
}

    function MyCustomUploadAdapterPlugin(editor) {
    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
        return new UploadAdapter(loader);
    };
}