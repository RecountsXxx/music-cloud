export const QueryPaths = {
  // Возвращает базовый api URL-адрес
  baseApi: () => `${import.meta.env.VITE_API_BASE_URL}`,

  // Возвращает URL-адрес для регистрации пользователя
  register: () => "/auth/register",

  // Возвращает URL-адрес для входа в систему
  login: () => "/auth/login",

  refreshToken: () => "/auth/refresh-token",

  // Возвращает URL-адрес для запроса идентификатора файла для загрузки файлов
  requestFileId: () => "/upload/protected/audio/request-file-id",

  // Возвращает URL-адрес для запросов по загрузке файлов в формате чанков
  uploadChunkedFile: () => "/upload/protected/audio/file",

  // Возвращает URL-адрес для запроса по загрузке обложки релиза или плейлиста
  uploadCover: (id) => `/upload/protected/collection/${id}/cover`,

  songConvert: (songId, fileId) => `/upload/protected/test/audio/${songId}/${fileId}/convert`,

  // Возвращает URL-адрес для запроса для стриминга песни
  streamAudio: (songId, quality) => `/stream/protected/playlist/${songId}/${quality}`,

  // Возвращает URL-адрес для запросов по менеджменту релизами
  crudReleases: (id = null) =>  {
    if(id) {
      return `/php/collection/releases/${id}`;
    }
    else {
      return `/php/collection/releases`;
    }
  },
};