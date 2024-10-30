export function showHidePassword(elem) {
    if (elem.type === 'password') {
        elem.type = 'text';
        return true;
    } else {
        elem.type = 'password';
        return false;
    }
}