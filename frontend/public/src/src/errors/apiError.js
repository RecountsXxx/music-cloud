export class ApiError extends Error {
  constructor(message, response) {
    super(message);
    this.response = response;
  }
}