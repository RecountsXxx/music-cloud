export function showHidePassword(elem) {
    console.log(elem.type)
    if (elem.type === 'password') {
        elem.type = 'text';
        return false;
    } else {
        elem.type = 'password';
        return true;
    }
}