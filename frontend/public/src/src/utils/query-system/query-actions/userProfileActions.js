import { PerformQuery } from '@/utils/query-system/performQuery.js'
import { QueryMethods } from '@/utils/query-system/queryMethods.js'
import { QueryPaths } from '@/utils/query-system/queryPaths.js'
import { QueryContentTypes } from '@/utils/query-system/queryContentTypes.js'
import { toastError, toastInfo } from '@/utils/toast/toastNotification.js'
import { useAuthStore } from '@/stores/authStore.js'

export async function createUserProfile(data) {
  try {
    const authStore = useAuthStore();

    const responseData = await PerformQuery(QueryMethods.POST, QueryPaths.crudUserProfile(), data, QueryContentTypes.applicationJson, authStore.getJWT);
    return responseData.userProfile;
  }
  catch (error) {
    console.log(error);
    toastError(`Failed to create user profile`);
  }
}

export async function requestGravatar() {
  try {
    const authStore = useAuthStore();

    const responseData = await PerformQuery(QueryMethods.POST, QueryPaths.requestGravatar(), null, QueryContentTypes.applicationJson, authStore.getJWT);
    toastInfo(responseData.message);
  }
  catch (error) {
    console.log(error);
    toastError(`Failed to request gravatar`);
  }
}