import axios from 'axios'
import { ApiError } from '@/errors/apiError.js'

export async function PerformQuery(method, path, data = null, contentType = 'application/json', token = null) {
  const url = `${import.meta.env.VITE_API_BASE_URL}${path}`;

  try {
    const headers = {
      'Content-Type': `${contentType}`,
      Authorization: token ? `Bearer ${token}` : ''
    }

    const config = {
      method: method,
      url: url,
      headers: headers,
      data: data,
    };

    const response = await axios.request(config);

    return response.data;
  } catch (error) {
    if (error.response) {
      throw new ApiError(`API error: ${error.response.data}`, error.response);
    } else {
      throw new Error(`Network error: ${error.message}`);
    }
  }
}