import { useAuthStore } from '@/stores/authStore.js'
import { PerformQuery } from '@/utils/query-system/performQuery.js'
import { QueryMethods } from '@/utils/query-system/queryMethods.js'
import { QueryPaths } from '@/utils/query-system/queryPaths.js'
import { QueryContentTypes } from '@/utils/query-system/queryContentTypes.js'
import { toastError, toastInfo } from '@/utils/toast/toastNotification.js'

const authStore = useAuthStore();

export async function createSong(data) {
  try {
    const responseData = await PerformQuery(QueryMethods.POST, QueryPaths.crudSong(), data, QueryContentTypes.applicationJson, authStore.getJWT);
    toastInfo(`Song "${data.name}" was successfully created and added to release`);
    return responseData.song;
  }
  catch (error) {
    console.log(error);
    toastError(`Failed to create song "${data.name}"`);
  }
}