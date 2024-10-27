import { requestGravatar } from '@/utils/query-system/query-actions/userProfileActions.js'

export const QueryPaths = {
  // Возвращает базовый api URL-адрес
  baseApi: () => `${import.meta.env.VITE_API_BASE_URL}`,

  //// User
  ////////////////////////
  // Возвращает URL-адрес для регистрации пользователя
  register: () => "/auth/register",

  // Возвращает URL-адрес для входа в систему
  login: () => "/auth/login",

  refreshToken: () => "/auth/refresh-token",
  ////////////////////////
  //// User


  //// UserProfile
  ////////////////////////
  crudUserProfile: (id = null) =>  {
    if(id) {
      return `/protected/user-profile/${id}`;
    }
    else {
      return '/protected/user-profile';
    }
  },

  requestGravatar: () => "/upload/protected/profile/gravatar",
  ////////////////////////
  //// UserProfile


  //// Release
  ////////////////////////
  crudRelease: (id = null) =>  {
    if(id) {
      return `/protected/release/${id}`;
    }
    else {
      return '/protected/release';
    }
  },

  // Возвращает URL-адрес для запроса идентификатора файла для загрузки файлов
  requestFileId: () => "/upload/protected/audio/request-file-id",

  // Возвращает URL-адрес для запросов по загрузке файлов в формате чанков
  uploadChunkedFile: () => "/upload/protected/audio/file",

  // Возвращает URL-адрес для запроса по загрузке обложки релиза или плейлиста
  uploadCover: (id) => `/upload/protected/collection/${id}/cover`,

  songConvert: (songId, fileId) => `/upload/protected/test/audio/${songId}/${fileId}/convert`,
  ////////////////////////
  //// Release


  //// Song
  ////////////////////////
  crudSong: (id = null) =>  {
    if(id) {
      return `/protected/song/${id}`;
    }
    else {
      return '/protected/song';
    }
  },
  ////////////////////////
  //// Song


  //// Genre
  ////////////////////////
  crudGenre: (id = null) =>  {
    if(id) {
      return `/public/genre/${id}`;
    }
    else {
      return '/public/genre';
    }
  },
  ////////////////////////
  //// Genre


  // Возвращает URL-адрес для запроса для стриминга песни
  streamAudio: (songId, quality) => `/stream/protected/playlist/${songId}/${quality}`,
};