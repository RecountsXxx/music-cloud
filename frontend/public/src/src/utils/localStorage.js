export function saveObjectToLocalStorage(localStorageKey, object) {
  localStorage.setItem(localStorageKey, JSON.stringify(object));
}

export function loadObjectFromLocalStorage(localStorageKey) {
  return JSON.parse(localStorage.getItem(localStorageKey));
}

export function removeObjectFromLocalStorage(localStorageKey) {
  return JSON.parse(localStorage.getItem(localStorageKey));
}