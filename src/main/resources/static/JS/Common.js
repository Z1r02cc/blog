function checkNull(str) {
    return !(str == null || str === " " || str.length < 1);
}

function zuiMsg(msg){
    new $.zui.Messager(msg, {
        type: 'danger'
    }).show();
    return
}
function checkNotNull(str) {
    if (str == null || str == "" || str.length < 1 || str == undefined) {
        return false;
    }
    return true;
}

