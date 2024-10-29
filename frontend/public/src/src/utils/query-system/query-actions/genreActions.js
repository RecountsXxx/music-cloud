import { PerformQuery } from '@/utils/query-system/performQuery.js'
import { QueryMethods } from '@/utils/query-system/queryMethods.js'
import { QueryPaths } from '@/utils/query-system/queryPaths.js'
import { QueryContentTypes } from '@/utils/query-system/queryContentTypes.js'
import { toastError } from '@/utils/toast/toastNotification.js'

export async function getGenres() {
  try {
    return await PerformQuery(QueryMethods.GET, QueryPaths.crudGenre(), null, QueryContentTypes.applicationJson);
  }
  catch (error) {
    console.log(error);
    toastError(`Failed to get genres`);
  }
}