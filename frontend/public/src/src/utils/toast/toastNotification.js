import {useToast} from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-sugar.css';

const $toast = useToast();

export const toastError = (message, duration = 5000) => {
  $toast.error(`Message: ${message}`, {
    position: "top-right",
    duration: duration
  });
};

export const toastSuccess = (message, duration = 5000) => {
  $toast.success(`Message: ${message}`, {
    position: "top-right",
    duration: duration
  });
};

export const toastInfo = (message, duration = 5000) => {
  $toast.info(`Message: ${message}`, {
    position: "top-right",
    duration: duration
  });
};