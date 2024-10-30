export function checkRememberMe() {
    const remember = document.getElementById('remember-me');
    return !!remember.checked;
}