import { defineStore } from 'pinia'
import {
  loadObjectFromLocalStorage,
  removeObjectFromLocalStorage,
  saveObjectToLocalStorage
} from '@/utils/localStorage.js'


const localStorageKey = "userProfileStore";
const storeName = "useUserProfileStore";

export const useUserProfileStore = defineStore(storeName, {
  state: () => ({
    userProfile: null
  }),

  actions: {
    initialize() {
      const object = loadObjectFromLocalStorage(localStorageKey);
      if (object) {
        this.userProfile = object;
      }
    },

    setObject(object) {
      if (object) {
        this.userProfile = object;
        saveObjectToLocalStorage(localStorageKey, object);
      }
    },

    clearObject() {
      this.userProfile = null;
      removeObjectFromLocalStorage(localStorageKey);
    }
  }
});