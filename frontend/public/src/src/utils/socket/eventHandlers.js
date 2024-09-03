import { useSocketStore } from '@/stores/socketStore.js';
import { toastInfo } from '@/utils/toast/toastNotification.js'

const socketStore = useSocketStore();

export const subscribeToCoverUpload = () => {
  const eventName = 'cover.ready';

  const onCoverUploaded = (data) => {
    console.log('Cover uploaded:', data);

    toastInfo(data.message);

    socketStore.unsubscribe(eventName, onCoverUploaded);
  };

  socketStore.subscribe(eventName, onCoverUploaded);
};