export const QueryPaths = {
  // Возвращает базовый api URL-адрес
  baseApi: () => `${import.meta.env.VITE_API_BASE_URL}`,

  // Возвращает URL-адрес для регистрации пользователя
  register: () => "/auth/register",

  // Возвращает URL-адрес для входа в систему
  login: () => "/auth/login",

  // Возвращает URL-адрес для запроса идентификатора файла для загрузки файлов
  requestFileId: () => "/java/protected/upload/request-file-id",

  // Возвращает URL-адрес для запросов по загрузке файлов в формате чанков
  uploadChunkedFile: () => "/java/protected/upload/file",

  // Возвращает URL-адрес для запроса по загрузке обложки релиза или плейлиста
  uploadCover: (id) => `/java/protected/collection/${id}/cover`,

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