import {Upload} from "tus-js-client";
import {PerformQuery} from "@/utils/query-system/performQuery.js";
import {QueryMethods} from "@/utils/query-system/queryMethods.js";
import {QueryPaths} from "@/utils/query-system/queryPaths.js";
import {QueryContentTypes} from "@/utils/query-system/queryContentTypes.js";
import {useAuthStore} from "@/stores/authStore.js";
import {toastError, toastInfo, toastSuccess} from "@/utils/toast/toastNotification.js";

const authStore = useAuthStore();

export async function requestFileId() {
  const responseData = await PerformQuery(QueryMethods.POST, QueryPaths.requestFileId(), null, QueryContentTypes.applicationJson, authStore.getJWT);
  return responseData.fileId;
}

export async function startUpload(track, onProgress) {
  const upload = new Upload(track.file, {
    endpoint: `${QueryPaths.baseApi()}${QueryPaths.uploadChunkedFile()}`,
    retryDelays: [0, 3000, 5000, 10000, 20000],
    chunkSize: import.meta.env.VITE_CHUNK_SIZE,
    metadata: {
      filename: track.name,
      filetype: track.file.type,
      fileId: track.id,
    },
    headers: {
      Authorization: `Bearer ${authStore.getJWT}`
    },
    onError: (error) => {
      console.error('Upload failed:', error);
      toastError(`File "${track.name}" uploading failed.`);
    },
    onProgress: (bytesUploaded, bytesTotal) => {
      const percentage = ((bytesUploaded / bytesTotal) * 100).toFixed(2);
      onProgress(percentage);
    },
    onSuccess: () => {
      console.log("The file was successfully uploaded.");
      toastSuccess(`The file "${track.name}" was successfully uploaded.`);
    },
  });

  upload.findPreviousUploads().then((previousUploads) => {
    if (previousUploads.length) {
      toastInfo(`Resuming file "${track.name}" upload.`);
      upload.resumeFromPreviousUpload(previousUploads[0]);
    } else {
      toastInfo(`Starting file "${track.name}" upload.`);
      upload.start();
    }
  });
}

export async function uploadCover(collectionId, data) {
  try {
    const responseData = await PerformQuery(QueryMethods.POST, QueryPaths.uploadCover(collectionId), data, QueryContentTypes.multipartFormData, authStore.getJWT);
    return responseData.message;
  }
  catch (error) {
    if(error.response.status === 413) {
      toastError(`File is too large, crop it more or choose another one.`);
    }
  }
}

export async function songConvert(songId, fileId) {
  try {
    const responseData = await PerformQuery(QueryMethods.POST, QueryPaths.songConvert(songId, fileId), null, QueryContentTypes.applicationJson, authStore.getJWT);
    return responseData.message;
  }
  catch (error) {
    toastError(`Failed to send message for converting song. ${error.message}`);
  }
}

export async function createRelease(data) {
  try {
    const responseData = await PerformQuery(QueryMethods.POST, QueryPaths.crudReleases(), data, QueryContentTypes.applicationJson, authStore.getJWT);
    return responseData.collectionId;
  }
  catch (error) {
    toastError(`Failed to create release. ${error.message}`);
  }
}